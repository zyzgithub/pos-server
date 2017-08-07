package com.dianba.pos.box.util;

import com.dianba.pos.box.constant.BoxRedisKeySet;
import com.dianba.pos.core.context.ApplicationContextHelper;
import com.dianba.pos.core.service.RedisManager;

public class ScanItemsUtil {

    private static RedisManager redisManager = ApplicationContextHelper
            .getApplicationContext().getBean(RedisManager.class);

    public static String getRFIDItems(Long passportId) {
        String rfids = redisManager.get(BoxRedisKeySet.USER_BUY_RFID + passportId);
        if (rfids != null) {
            return rfids;
        }
        return null;
    }

    public static void writeScanItems(Long passportId, String rfids) {
        redisManager.set(BoxRedisKeySet.USER_BUY_RFID, rfids);
    }
}
