package com.stockbeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class StockReturnEntry {
    private String ticker;
    private LocalDateTime lastUpdateTime;
    private Date BeginDate;
    private Date EndDate;
    private int count;
    private Map<String, Integer> mapDate2Index;//given a date, it tells its index in records list
    private List<StockReturnRecord> records;
}
