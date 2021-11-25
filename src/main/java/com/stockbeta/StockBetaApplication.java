package com.stockbeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class StockBetaApplication {
    public static void main(String[] args) {
        log.info("Starting StockBeta App ......");
        //STEP1: start creating beans in DiConfig file -> one of them is stockBetaProvider
        log.info("STEP1");
        SpringApplication.run(StockBetaApplication.class, args);
    }
}
