package com.stockbeta;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import com.util.NumberUtil;

@Slf4j
public class StockBetaProviderImpl implements StockBetaProvider {
    @Data
    @AllArgsConstructor
    class ListReturnWrapperObj {
        LinkedList<StockReturnRecord> listReturnInDuration;
        int startIndex;
        int endIndex;
    }

    private Map<String, StockReturnEntry> stockReturnMap;
    private final BetaCalculatorService betaCalculatorService;
    private final FileMonitor fileMonitor;
    private final KafkaMonitor kafkaMonitor;
    public StockBetaProviderImpl(BetaCalculatorService betaCalculatorService,
                                 FileMonitor fileMonitor,
                                 KafkaMonitor kafkaMonitor) {
        this.betaCalculatorService = betaCalculatorService;
        this.fileMonitor = fileMonitor;
        this.kafkaMonitor = kafkaMonitor;
    }

    @SneakyThrows
    public List<Double> getStockBetas(String ticker, String tickerBaseLine, String startDate, String endDate, int betaDurationDays) {
        StockReturnEntry stockReturnEntry4Ticker = stockReturnMap.get(ticker);
        Map<String, Integer>  mapDate2Index4Ticker = stockReturnEntry4Ticker.getMapDate2Index();
        if (!mapDate2Index4Ticker.containsKey(startDate) || !mapDate2Index4Ticker.containsKey(endDate))
            throw new Exception("StartDate/EndDate  can't be found: " + " for " + ticker);
        int startIndex = mapDate2Index4Ticker.get(startDate);
        int endIndex = mapDate2Index4Ticker.get(endDate);

        return getStockBetaForNDays(ticker, tickerBaseLine, startDate, endIndex-startIndex+1, betaDurationDays);
    }

    private void trimList(LinkedList<StockReturnRecord> list, int minSize) {
        int extraCount;
        extraCount = list.size() - minSize;
        for (int i=0; i<extraCount; i++)
            list.removeFirst();
    }

    public void makeBothReturnListsSameLength(LinkedList<StockReturnRecord> list1, LinkedList<StockReturnRecord> list2) {
        if (list1.size() != list2.size()) {
            int minSize = Math.min(list1.size(), list2.size());
            if (list1.size() != minSize) {
                trimList(list1, minSize);
            }
            else
                trimList(list2, minSize);
        }
    }

    @SneakyThrows
    public List<Double> getStockBetaForNDays(String ticker, String tickerBaseline, String startDate, int numOfDays, int betaDurationDays) {
        List<Double> betaList = new ArrayList<>();
        StockReturnEntry stockReturnEntry4Ticker = stockReturnMap.get(ticker);
        StockReturnEntry stockReturnEntry4TickerBaseLine = stockReturnMap.get(tickerBaseline);

        //1 Prepare listReturn for ticker
        List<StockReturnRecord> listReturn4Ticker = stockReturnEntry4Ticker.getRecords();
        Map<String, Integer>  mapDate2Index4Ticker = stockReturnEntry4Ticker.getMapDate2Index();
        ListReturnWrapperObj wrapperObj4Ticker = getListReturnWithinDuration(listReturn4Ticker, mapDate2Index4Ticker, startDate, betaDurationDays);
        LinkedList<StockReturnRecord> listReturn4TickerInDuration = wrapperObj4Ticker.getListReturnInDuration();
        if (listReturn4TickerInDuration == null)
            throw new Exception("StartDate  can't not found: " + startDate + " for " + ticker);

        //2 Prepare listReturn for tickerBaseline
        List<StockReturnRecord> listReturn4TickerBaseLine = stockReturnEntry4TickerBaseLine.getRecords();
        Map<String, Integer>  mapDate2Index4TickerBaseLine = stockReturnEntry4TickerBaseLine.getMapDate2Index();
        ListReturnWrapperObj wrapperObj4BaseLine = getListReturnWithinDuration(listReturn4TickerBaseLine, mapDate2Index4TickerBaseLine, startDate, betaDurationDays);
        LinkedList<StockReturnRecord> listReturn4TickerBaseLineInDuration = wrapperObj4BaseLine.getListReturnInDuration();
        if (listReturn4TickerBaseLineInDuration == null)
            throw new Exception("StartDate  can't not found: " + startDate + " for " + tickerBaseline);

        makeBothReturnListsSameLength(listReturn4TickerInDuration, listReturn4TickerBaseLineInDuration);
        var beta = betaCalculatorService.calculateBeta(listReturn4TickerInDuration, listReturn4TickerBaseLineInDuration);
        betaList.add(NumberUtil.round(beta,4));

        //Sliding window to shift list forward and recalculate beta for next business day
        for (int i = 1; i < numOfDays; i++) {
            listReturn4TickerInDuration.removeFirst();
            listReturn4TickerInDuration.addLast(listReturn4Ticker.get(wrapperObj4Ticker.getEndIndex()+i));
            listReturn4TickerBaseLineInDuration.removeFirst();
            listReturn4TickerBaseLineInDuration.addLast(listReturn4TickerBaseLine.get(wrapperObj4BaseLine.getEndIndex()+i));

            beta = betaCalculatorService.calculateBeta(listReturn4TickerInDuration, listReturn4TickerBaseLineInDuration);
            betaList.add(NumberUtil.round(beta,4));
        }
        return betaList;
    };

    @Override
    @SneakyThrows
    public ListReturnWrapperObj getListReturnWithinDuration(List<StockReturnRecord> listReturn,
                                                     Map<String, Integer>  mapDate2Index4Ticker,
                                                     String startDate,
                                                     int betaDurationDays) {
        LinkedList<StockReturnRecord> listReturnInDuration =  new LinkedList<>();
        if (!mapDate2Index4Ticker.containsKey(startDate)) //startDate not found
            return null;

        int startIndex = mapDate2Index4Ticker.get(startDate) - betaDurationDays + 1;
        if (startIndex < 0) startIndex = 0; //startIndex must >=0
        int endIndex = mapDate2Index4Ticker.get(startDate);
        listReturnInDuration.addAll(listReturn.subList(startIndex, endIndex+1));

        return new ListReturnWrapperObj(listReturnInDuration, startIndex, endIndex);
    }

    //STEP5:
    @Override
    public void run() {
        log.info("STEP5: at startup, parse existing price files at a file folder specified in yaml file");
        stockReturnMap = fileMonitor.parsePriceFiles();

        var futureList = new ArrayList<CompletableFuture<Void>>();
        //1
        CompletableFuture<Void> fileFolderMonitorFuture = CompletableFuture.runAsync(() -> {
            log.info("Monitor file folder using a CompletableFuture");
            try {
                fileMonitor.run();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor()).handleAsync((value, throwable) -> {
            if (throwable != null) {
                throw Lombok.sneakyThrow(throwable);
            }
            return null;
        });
        futureList.add(fileFolderMonitorFuture);
        //2
        CompletableFuture<Void> kafkaTopicMonitorFuture = CompletableFuture.runAsync(() -> {
            log.info("Monitor Kafka Topic using a CompletableFuture");
            try {
                kafkaMonitor.run();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor()).handleAsync((value, throwable) -> {
            if (throwable != null) {
                throw Lombok.sneakyThrow(throwable);
            }
            return null;
        });
        futureList.add(kafkaTopicMonitorFuture);

        CompletableFuture<Void>[] futureArray = new CompletableFuture[futureList.size()];
        futureArray = futureList.toArray(futureArray);
        try {
            CompletableFuture.allOf(futureArray).join();
        } catch(Exception ex) {
            throw ex;
        }

        return;
    }



}
