package com.dianba.pos.box.util;

import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.payment.util.MD5Util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DoorStatusUtil {

    private static final String DOOR_SECURITY_KEY = "DOOR_SECURITY_CHECK";
    private static final Map<Long, Long> DOOR_STATUS = new ConcurrentHashMap<>();

    public static final Integer DOOR_OPEN = 1;
    public static final Integer DOOR_CLOSE = 2;

    public static Long getDoorStatus(Long passportId) {
        synchronized (DOOR_CLOSE) {
            return DOOR_STATUS.get(passportId);
        }
    }

    public static void writeDoorStatus(Long passportId, Integer commandType) {
        synchronized (DOOR_CLOSE) {
            if (DOOR_CLOSE.longValue() == commandType) {
                if (DOOR_STATUS.containsKey(passportId)) {
                    DOOR_STATUS.remove(passportId);
                }
            } else {
                DOOR_STATUS.put(passportId, System.currentTimeMillis());
            }
        }
    }

    public static String getDoorSecurityKey(Long passportId) {
        String currMinutes = DateUtil.getCurrDate(DateUtil.FORMAT_TWO);
        return MD5Util.md5(passportId + currMinutes + DOOR_SECURITY_KEY);
    }
}
