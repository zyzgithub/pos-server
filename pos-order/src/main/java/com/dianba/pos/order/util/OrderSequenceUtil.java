package com.dianba.pos.order.util;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;

public class OrderSequenceUtil {

    public static String generateOrderSequence() {
        StringBuilder primaryKey = new StringBuilder("908");
        primaryKey.append(CommonUtils.defineDateFormat(System.currentTimeMillis(), "yyyyMMddHHmmssSSS"));
        primaryKey.append(DefineRandom.randomNumber(6));
        return primaryKey.toString();
    }
}
