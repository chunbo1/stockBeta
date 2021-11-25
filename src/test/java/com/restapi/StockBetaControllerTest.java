package com.restapi;
import com.stockbeta.controller.BetaRequestParameter;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StockBetaControllerTest {

    private RestApiHandlerImpl restApiHandler;
    private final String stockBetaMultipleDayUrl = "http://localhost:8842/calcbeta";
    private BetaRequestParameter betaRequestValidParameter;
    private BetaRequestParameter betaRequestStartDateIsHolidayParameter;
    @Before
    public void before() {
        betaRequestValidParameter = new BetaRequestParameter();
        betaRequestValidParameter.setTicker("AAPL");
        betaRequestValidParameter.setTickerBaseLine("SPY");
        betaRequestValidParameter.setStartDate("1/9/2021");
        betaRequestValidParameter.setEndDate("11/12/2021");
        betaRequestValidParameter.setBetaDurationDays(1000);

        betaRequestStartDateIsHolidayParameter = new BetaRequestParameter();
        betaRequestStartDateIsHolidayParameter.setTicker("AAPL");
        betaRequestStartDateIsHolidayParameter.setTickerBaseLine("SPY");
        betaRequestStartDateIsHolidayParameter.setStartDate("7/4/2021");//7/4/2021 is Sunday, 7/5 is set up as a holiday in yaml config
        betaRequestStartDateIsHolidayParameter.setEndDate("7/4/2021");
        betaRequestStartDateIsHolidayParameter.setBetaDurationDays(1000);

        restApiHandler = new RestApiHandlerImpl();
    }

    @Test
    public void calcBetaStartDateIsHolidayTest() {
        List<Double> betas = restApiHandler.post(ArrayList.class, stockBetaMultipleDayUrl, betaRequestStartDateIsHolidayParameter);
        //7/4/2021 is Sunday, 7/5 is set up as a holiday, Controller automatically adjusts to next business day which is 7/6/2021
        assertThat(betas.get(0)).isEqualTo(1.2288);
    }

    @Test
    public void calcBetaTest() {
        List<Double> betas = restApiHandler.post(ArrayList.class, stockBetaMultipleDayUrl, betaRequestValidParameter);
        assertThat(betas.size()).isEqualTo(214);
    }


}
