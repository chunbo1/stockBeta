package com.stockbeta;

import lombok.var;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StockBetaProviderTest {
    private LinkedList<StockReturnRecord> listReturn1;
    private LinkedList<StockReturnRecord> listReturn2;
    private Map<String, Integer> mapDate2Index4Ticker;
    private StockBetaProvider stockBetaProvider;
    @Autowired
    private BetaCalculatorService betaCalculatorService;
    @Autowired
    private FileMonitor fileMonitor;
    @Autowired
    private KafkaMonitor kafkaMonitor;

    @Before
    public void before() {
        mapDate2Index4Ticker = new HashMap<>();
        mapDate2Index4Ticker.put("3/9/1999", 0);
        mapDate2Index4Ticker.put("3/10/1999", 1);
        mapDate2Index4Ticker.put("3/11/1999", 2);
        mapDate2Index4Ticker.put("3/12/1999", 3);
        mapDate2Index4Ticker.put("3/13/1999", 4);
        mapDate2Index4Ticker.put("3/14/1999", 5);

        listReturn1 = new LinkedList<>();
        var StockReturnRecord0 = new StockReturnRecord(0, "3/9/1999", 0.5);
        var StockReturnRecord1 = new StockReturnRecord(1, "3/10/1999", 1);
        var StockReturnRecord2 = new StockReturnRecord(2, "3/11/1999", 2);
        var StockReturnRecord3 = new StockReturnRecord(3, "3/12/1999", 3);
        var StockReturnRecord4 = new StockReturnRecord(4, "3/13/1999", 4);
        var StockReturnRecord5 = new StockReturnRecord(5, "3/14/1999", 5);
        listReturn1.add(StockReturnRecord0);
        listReturn1.add(StockReturnRecord1);
        listReturn1.add(StockReturnRecord2);
        listReturn1.add(StockReturnRecord3);
        listReturn1.add(StockReturnRecord4);
        listReturn1.add(StockReturnRecord5);

        listReturn2 = new LinkedList<>();
        var StockReturnRecorda1 = new StockReturnRecord(1, "3/11/1999", 10);
        var StockReturnRecorda2 = new StockReturnRecord(2, "3/12/1999", 12);
        var StockReturnRecorda3 = new StockReturnRecord(3, "3/13/1999", 18);
        var StockReturnRecorda4 = new StockReturnRecord(4, "3/14/1999", 25);
        listReturn2.add(StockReturnRecorda1);
        listReturn2.add(StockReturnRecorda2);
        listReturn2.add(StockReturnRecorda3);
        listReturn2.add(StockReturnRecorda4);

        stockBetaProvider = new StockBetaProviderImpl(betaCalculatorService, fileMonitor, kafkaMonitor);
    }

    @Test
    public void makeBothReturnListsSameLengthTest() {
        assertThat(listReturn1.size()).isNotEqualTo(listReturn2.size());
        stockBetaProvider.makeBothReturnListsSameLength(listReturn1, listReturn2);
        assertThat(listReturn1.size()).isEqualTo(listReturn2.size());

    }

    @Test
    public void getListReturnWithinDurationTest() {
        assertThat(listReturn1.size()).isNotEqualTo(listReturn2.size());
        StockBetaProviderImpl.ListReturnWrapperObj wrapperObj = stockBetaProvider.getListReturnWithinDuration(
                listReturn1, mapDate2Index4Ticker, "3/13/1999", 3);

        assertThat(wrapperObj.listReturnInDuration.size()).isEqualTo(3);
        assertThat(wrapperObj.listReturnInDuration.get(0).getTradeDate()).isEqualTo("3/11/1999");
        assertThat(wrapperObj.listReturnInDuration.get(1).getTradeDate()).isEqualTo("3/12/1999");
        assertThat(wrapperObj.listReturnInDuration.get(2).getTradeDate()).isEqualTo("3/13/1999");
        assertThat(wrapperObj.startIndex).isEqualTo(2);
        assertThat(wrapperObj.endIndex).isEqualTo(4);
    }

}
