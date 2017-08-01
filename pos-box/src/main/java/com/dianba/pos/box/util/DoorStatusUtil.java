package com.dianba.pos.box.util;

import com.dianba.pos.box.constant.BoxRedisKeySet;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.core.context.ApplicationContextHelper;
import com.dianba.pos.core.service.RedisManager;
import com.dianba.pos.payment.util.MD5Util;

public class DoorStatusUtil {

    private static final String DOOR_SECURITY_KEY = "DOOR_SECURITY_CHECK";
    private static RedisManager redisManager = ApplicationContextHelper
            .getApplicationContext().getBean(RedisManager.class);

    public static final Integer DOOR_OPEN = 1;
    public static final Integer DOOR_CLOSE = 2;

    public static Long getDoorStatus(Long passportId) {
        String time = redisManager.get(BoxRedisKeySet.DOOR_OPEN_STATUS + passportId);
        if (time != null) {
            return Long.parseLong(time);
        }
        return null;
    }

    public static void writeDoorStatus(Long passportId, Integer commandType) {
        if (DOOR_CLOSE.longValue() == commandType) {
            String time = redisManager.get(BoxRedisKeySet.DOOR_OPEN_STATUS + passportId);
            if (time != null) {
                redisManager.expire(BoxRedisKeySet.DOOR_OPEN_STATUS + passportId, 0);
            }
        } else {
            redisManager.set(BoxRedisKeySet.DOOR_OPEN_STATUS + passportId, System.currentTimeMillis() + "");
        }
    }

    public static String getDoorSecurityKey(Long passportId) {
        String currMinutes = DateUtil.getCurrDate(DateUtil.FORMAT_TWO);
        return MD5Util.md5(passportId + currMinutes + DOOR_SECURITY_KEY);
    }
}
