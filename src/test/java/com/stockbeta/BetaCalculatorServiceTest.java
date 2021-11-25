package com.stockbeta;
import com.util.CsvParser;
import lombok.var;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//https://calculator-online.net/covariance-calculator/
//verify my covariance result using this site
public class BetaCalculatorServiceTest {
    private BetaCalculatorService betaCalculator;
    private List<StockReturnRecord> listReturn1;
    private List<StockReturnRecord> listReturn2;
    private List<StockPriceRecord> listPrice;

    @Before
    public void before() {
        listPrice = new ArrayList<>();
        var stockPriceRecord1 = new StockPriceRecord(); stockPriceRecord1.setTradeDate("3/10/1999");stockPriceRecord1.setClosePrice(1);
        var stockPriceRecord2 = new StockPriceRecord(); stockPriceRecord2.setTradeDate("3/11/1999");stockPriceRecord2.setClosePrice(2);
        var stockPriceRecord3 = new StockPriceRecord(); stockPriceRecord3.setTradeDate("3/12/1999");stockPriceRecord3.setClosePrice(3);

        listPrice.add(stockPriceRecord1);
        listPrice.add(stockPriceRecord2);
        listPrice.add(stockPriceRecord3);

        listReturn1 = new ArrayList<>();
        var StockReturnRecord1 = new StockReturnRecord(1, "3/10/1999", 1);
        var StockReturnRecord2 = new StockReturnRecord(2, "3/11/1999", 2);
        var StockReturnRecord3 = new StockReturnRecord(3, "3/12/1999", 3);
        var StockReturnRecord4 = new StockReturnRecord(4, "3/13/1999", 4);
        var StockReturnRecord5 = new StockReturnRecord(5, "3/14/1999", 5);
        listReturn1.add(StockReturnRecord1);
        listReturn1.add(StockReturnRecord2);
        listReturn1.add(StockReturnRecord3);
        listReturn1.add(StockReturnRecord4);
        listReturn1.add(StockReturnRecord5);

        listReturn2 = new ArrayList<>();
        var StockReturnRecorda1 = new StockReturnRecord(1, "3/10/1999", 10);
        var StockReturnRecorda2 = new StockReturnRecord(2, "3/11/1999", 12);
        var StockReturnRecorda3 = new StockReturnRecord(3, "3/12/1999", 18);
        var StockReturnRecorda4 = new StockReturnRecord(4, "3/13/1999", 25);
        var StockReturnRecorda5 = new StockReturnRecord(5, "3/14/1999", 30);
        listReturn2.add(StockReturnRecorda1);
        listReturn2.add(StockReturnRecorda2);
        listReturn2.add(StockReturnRecorda3);
        listReturn2.add(StockReturnRecorda4);
        listReturn2.add(StockReturnRecorda5);

        betaCalculator = new BetaCalculatorServiceImpl();
    }

    @Test
    public void convertPriceToReturnTest() {
        var pairReturn = betaCalculator.convertPriceToReturn(listPrice);
        assertThat(pairReturn.getValue0().size()).isEqualTo(2);
        assertThat(pairReturn.getValue0().get(0).getTradeDate()).isEqualTo("3/11/1999");
        assertThat(pairReturn.getValue0().get(0).getId()).isEqualTo(1);

        assertThat(pairReturn.getValue1().get("3/11/1999")).isEqualTo(0);
        assertThat(pairReturn.getValue1().get("3/12/1999")).isEqualTo(1);
    }

    @Test
    public void calculateVarianceTest() {
        var variance = betaCalculator.calculateVariance(listReturn2);
        assertThat(variance).isEqualTo(72);
    }

    //listReturn1 and listReturn2 have different length
    @Test
    public void calculateCovarianceTest() {
        var result = betaCalculator.calculateCovariance(listReturn1, listReturn2);
        assertThat(result).isEqualTo(13.25);
    }

    @Test
    public void calculateBetaTest() {
        var result = betaCalculator.calculateBeta(listReturn1, listReturn2);
        assertThat(result).isEqualTo(0.1840277777777778);
    }
}
