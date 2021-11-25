package com.stockbeta;
import com.util.CsvParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FileMonitorImpl implements FileMonitor {
    private CsvParser<StockPriceRecord> csvParser;
    private final StockBetaSettings stockBetaSettings;
    private final BetaCalculatorService betaCalculatorService;

    public FileMonitorImpl(CsvParser<StockPriceRecord> csvParser,
                           StockBetaSettings stockBetaSettings,
                           BetaCalculatorService betaCalculatorService) {
        this.csvParser = csvParser;
        this.stockBetaSettings = stockBetaSettings;
        this.betaCalculatorService = betaCalculatorService;
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("STEP7: simulate a long-running process such as monitoring a folder");
        while (true) {
            //Check if a new file comes in and process it into stockReturnMap
            Thread.sleep(1000);


        }
    }

    @SneakyThrows
    public Map<String, StockReturnEntry> parsePriceFiles() {
        Map<String, StockReturnEntry> stockReturnMap;
        log.info("STEP6: Go through all the files in data folder specified in yaml, save price data to stockReturnMap");
        stockReturnMap = new ConcurrentHashMap<>();
        List<StockPriceRecord> listPrice;

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        File[] files = new File(stockBetaSettings.getFilePath()).listFiles(obj -> obj.isFile() && obj.getName().endsWith(".csv"));
        for (File aFile : files) {
            String ticker = aFile.getName().replace(".csv", "");
            log.info(aFile.getAbsolutePath());
            listPrice = csvParser.parse(aFile.getAbsolutePath());

            var pair = betaCalculatorService.convertPriceToReturn(listPrice);
            var listReturn = pair.getValue0();
            var mapDate2Index = pair.getValue1();
            var beginDate = formatter.parse(listReturn.get(0).getTradeDate());
            var endDate = formatter.parse(listReturn.get(listReturn.size()-1).getTradeDate());
            StockReturnEntry stockReturnEntry = new StockReturnEntry(ticker, LocalDateTime.now(), beginDate, endDate, listReturn.size(), mapDate2Index, listReturn);
            stockReturnMap.put(ticker, stockReturnEntry);
            log.info("{} has {} return records, begin date {}, end date {}", ticker, stockReturnEntry.getRecords().size(), stockReturnEntry.getBeginDate(), stockReturnEntry.getEndDate());
        }

        return stockReturnMap;
    }

}
