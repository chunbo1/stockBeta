package com.stockbeta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockReturnRecord {
    private int id;
    private String tradeDate;
    private double dailyReturn;
}
