package com.stockbeta;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface StockBetaProvider {
    void run();
    List<Double> getStockBetas(String ticker, String tickerBaseline, String startDate, String endDate, int betaDurationDays);
    void makeBothReturnListsSameLength(LinkedList<StockReturnRecord> list1, LinkedList<StockReturnRecord> list2);
    StockBetaProviderImpl.ListReturnWrapperObj getListReturnWithinDuration(List<StockReturnRecord> listReturn,
                                                                           Map<String, Integer> mapDate2Index4Ticker,
                                                                           String startDate,
                                                                           int betaDurationDays);
}
