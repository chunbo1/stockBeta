package com.stockbeta;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
//can't apply @AllArgsConstructor, otherwise opencsv will fail
public class StockPriceRecord {
    @CsvBindByName(column = "Date")
    public String tradeDate;
    @CsvBindByName(column = "Adj Close")
    public double closePrice;

}
