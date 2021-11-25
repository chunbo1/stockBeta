package com.stockbeta.controller;

import lombok.Data;

@Data
public class BetaRequestParameter {
    private String ticker;
    private String tickerBaseLine;
    private String startDate;
    private String endDate;
    private int betaDurationDays;
}

