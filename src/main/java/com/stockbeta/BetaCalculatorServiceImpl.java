package com.stockbeta;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.javatuples.Pair;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
public class BetaCalculatorServiceImpl implements BetaCalculatorService {
    @SneakyThrows
    @Override
    public Pair<List<StockReturnRecord>, Map<String, Integer>> convertPriceToReturn(List<StockPriceRecord> listPrice) {
        List<StockReturnRecord> listReturn = new ArrayList<>();
        Map<String, Integer> mapDate2Index = new HashMap<>();
        for (int i = 1; i < listPrice.size(); i++) {
            double p1 = listPrice.get(i).getClosePrice();
            double p0 = listPrice.get(i-1).getClosePrice();
            var tem = 1 + (p1-p0)/p0;
            var stockReturn = Math.log(tem);
            var returnRecord = new StockReturnRecord(i, listPrice.get(i).getTradeDate(), stockReturn);
            listReturn.add(returnRecord);
            mapDate2Index.put(listPrice.get(i).getTradeDate(), i-1);
        }
        return new Pair<List<StockReturnRecord>, Map<String, Integer>>(listReturn, mapDate2Index);
    }

    @Override
    public double calculateVariance(List<StockReturnRecord> listReturnRecord) {
        var listReturnInDouble = listReturnRecord.stream().map(e -> e.getDailyReturn()).collect(Collectors.toList());
        double mean = mean(listReturnInDouble);
        double sumSquare = listReturnInDouble.stream().map(e -> (e-mean)*(e-mean)).reduce(0d, Double::sum);
        double variance = sumSquare /(listReturnInDouble.size()-1);
        return variance;
    }

    @SneakyThrows
    @Override
    public double calculateCovariance(List<StockReturnRecord> listReturnRecord, List<StockReturnRecord> listReturnBaselineRecord) {
        var listReturnInDoubleAbc = listReturnRecord.stream().map(e -> e.getDailyReturn()).collect(Collectors.toList());
        double meanAbc = mean(listReturnInDoubleAbc);
        var listReturnInDoubleXyz = listReturnBaselineRecord.stream().map(e -> e.getDailyReturn()).collect(Collectors.toList());
        double meanXyz = mean(listReturnInDoubleXyz);

        List<Pair<Double, Double>> listReturnPair = new ArrayList<Pair<Double, Double>>();
        Iterator<Double> iAbc = listReturnInDoubleAbc.iterator();
        Iterator<Double> iXyz = listReturnInDoubleXyz.iterator();
        while (iAbc.hasNext() && iXyz.hasNext()) {
            Pair<Double, Double> pairOfReturn = Pair.with(iAbc.next(), iXyz.next());
            listReturnPair.add(pairOfReturn);
        }

        double sumSquare = listReturnPair.stream().map(e -> (e.getValue0()-meanAbc)*(e.getValue1()-meanXyz)).reduce(0d, Double::sum);
        double covariance = sumSquare /(listReturnPair.size()-1);
        return covariance;
    }

    @SneakyThrows
    @Override
    public double calculateBeta(List<StockReturnRecord> listReturn, List<StockReturnRecord> listReturnBaseline) {
        var variance = calculateVariance(listReturnBaseline);
        var covariance = calculateCovariance(listReturn, listReturnBaseline);
        if (variance == 0) throw new Exception("variance can't be 0");
        return covariance/variance;
    }

    private static double mean(final List<Double> array) {
        return array.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);
    }

}
