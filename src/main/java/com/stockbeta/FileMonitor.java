package com.stockbeta;

import java.util.Map;

public interface FileMonitor {
    void run();
    Map<String, StockReturnEntry> parsePriceFiles();
}
