package com.stockbeta;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "stock-beta")  //this line need to be paired with StockBetaDiConfig..@EnableConfigurationProperties({StockBetaSettings.class})
public class StockBetaSettings {
    String dbRecoryTable;
    String filePath;
    Map<String, String> bankHolidays;
}