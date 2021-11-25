package com.util;

import com.stockbeta.StockBetaDiConfig;
import com.object.factory.ObjectFactory;
import com.stockbeta.StockBetaDiConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({StockBetaDiConfig.class})
public class UtilsDiConfig {


}
