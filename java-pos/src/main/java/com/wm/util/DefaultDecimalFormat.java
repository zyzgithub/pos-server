package com.wm.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * Created by mjorcen on 16/8/25.
 */
public class DefaultDecimalFormat {
    public final static String formatHalfUp(Number number) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
        return decimalFormat.format(number);
    }

    public final static String formatFloor(Number number) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setMinimumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(number);
    }


    public static void main(String[] number) {
        BigDecimal bigDecimal = new BigDecimal(123.888);
        System.out.println(formatFloor(bigDecimal));
        System.out.println(formatHalfUp(bigDecimal));
    }


}
