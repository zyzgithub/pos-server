package com.dianba.pos.box.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DoorStatusUtil {

    private static final Map<Long, Long> DOOR_STATUS = new ConcurrentHashMap<>();

    public static Long getDoorStatus(Long passportId) {
        if (DOOR_STATUS.containsKey(passportId)) {
            return DOOR_STATUS.get(passportId);
        }
        return null;
    }

    public static void writeDoorStatus(Long passportId) {
        DOOR_STATUS.put(passportId, System.currentTimeMillis());
    }
}
