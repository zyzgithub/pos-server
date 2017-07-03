package com.alipay.demo.trade.utils;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class Utils {
    public static String toAmount(long amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100)).toString();
    }

    public static boolean isEmpty(Object object) {
        if ((object instanceof String)) {
            return StringUtils.isEmpty((String) object);
        }
        return object == null;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static <T> boolean isListNotEmpty(List<T> list) {
        return (list != null) && (list.size() > 0);
    }

    public static <T> boolean isListEmpty(List<T> list) {
        return !isListNotEmpty(list);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
