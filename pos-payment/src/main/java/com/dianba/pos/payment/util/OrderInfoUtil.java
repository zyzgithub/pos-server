package com.dianba.pos.payment.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class OrderInfoUtil {

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        key = key + System.currentTimeMillis();
        key = key.substring(0, 15);
        return key;
    }

}
