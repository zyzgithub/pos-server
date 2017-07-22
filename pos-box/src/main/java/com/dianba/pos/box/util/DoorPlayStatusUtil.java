package com.dianba.pos.box.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DoorPlayStatusUtil {

    private static final Map<Long, Long> DOOR_PLAY_STATUS = new ConcurrentHashMap<>();

    public static Long getDoorPlayStatus(Long passportId) {
        if (DOOR_PLAY_STATUS.containsKey(passportId)) {
            return DOOR_PLAY_STATUS.get(passportId);
        }
        return null;
    }

    public static void writeDoorPlayStatus(Long passportId) {
        DOOR_PLAY_STATUS.put(passportId, System.currentTimeMillis());
    }
}
