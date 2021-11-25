package com.stockbeta;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaMonitorImpl implements KafkaMonitor {
    @SneakyThrows
    @Override
    public void run() {
        log.info("STEP8: simulate a long-running process such as listen to Kafka topic for new prices");
        while (true) {
            //Check if a new file comes in and process it into stockReturnMap
            Thread.sleep(1000);
        }
    }
}
