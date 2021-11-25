package com.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {
    public static boolean isNumeric(String strNum) {
        if (strNum == null) return false;
        try{
            double d = Double.parseDouble(strNum);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}