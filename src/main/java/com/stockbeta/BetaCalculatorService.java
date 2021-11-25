package com.stockbeta;
import org.javatuples.Pair;
import java.util.List;
import java.util.Map;

public interface BetaCalculatorService {
    Pair<List<StockReturnRecord>, Map<String, Integer>> convertPriceToReturn(List<StockPriceRecord> listPrice);
    double calculateVariance(List<StockReturnRecord> listReturn);
    double calculateCovariance(List<StockReturnRecord> listReturn, List<StockReturnRecord> listReturnBaseline);
    double calculateBeta(List<StockReturnRecord> listReturn, List<StockReturnRecord> listReturnBaseline);
}
