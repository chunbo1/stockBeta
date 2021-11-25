package com.stockbeta.controller;
import com.stockbeta.StockBetaProvider;
import com.stockbeta.StockBetaSettings;
import com.util.DateTimeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@Slf4j
public class StockBetaController {

    private StockBetaProvider stockBetaProvider;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
    private StockBetaSettings stockBetaSettings;//we can also use Autowire to do field injection

    public StockBetaController(StockBetaProvider stockBetaProvider, StockBetaSettings stockBetaSettings) {
        this.stockBetaProvider = stockBetaProvider;
        this.stockBetaSettings = stockBetaSettings;
    }

    @ExceptionHandler({ Exception.class })
    public void handleException(Exception e) {
        log.error("Exception occured inside StockBetaController" + e.getMessage());
    }

    @SneakyThrows
    @RequestMapping(value = "/calcbeta", method = RequestMethod.POST)
    public List<Double> calcbeta(@RequestBody BetaRequestParameter requestParameter) {
        String ticker = requestParameter.getTicker();
        String tickerBaseLine = requestParameter.getTickerBaseLine();
        String startDate = requestParameter.getStartDate();
        startDate = validateAdjustBusinessDate(startDate);
        String endDate = requestParameter.getEndDate();
        endDate = validateAdjustBusinessDate(endDate);
        int betaDurationDays = requestParameter.getBetaDurationDays();
        if (betaDurationDays<=1)
            throw new Exception("betaDurationDays must > 1");
        return stockBetaProvider.getStockBetas(ticker, tickerBaseLine, startDate, endDate, betaDurationDays);
    }

    @SneakyThrows
    private String validateAdjustBusinessDate(String inputDate) {
        String outputDate = inputDate;
        boolean isBankHoliday = DateTimeUtil.isBankHoliday(dateFormat.parse(inputDate), stockBetaSettings.getBankHolidays());
        if (isBankHoliday)
            outputDate = dateFormat.format(DateTimeUtil.getNextWorkingDay(dateFormat.parse(inputDate),  stockBetaSettings.getBankHolidays()));
        return outputDate;
    }
}
