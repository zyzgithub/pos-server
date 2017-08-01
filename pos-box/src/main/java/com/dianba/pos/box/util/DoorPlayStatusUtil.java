package com.dianba.pos.box.util;

import com.dianba.pos.box.constant.BoxRedisKeySet;
import com.dianba.pos.core.context.ApplicationContextHelper;
import com.dianba.pos.core.service.RedisManager;

public class DoorPlayStatusUtil {

    private static RedisManager redisManager = ApplicationContextHelper
            .getApplicationContext().getBean(RedisManager.class);

    public static Long getDoorPlayStatus(Long passportId) {
        String time = redisManager.get(BoxRedisKeySet.DOOR_PLAY_STATUS + passportId);
        if (time != null) {
            return Long.parseLong(time);
        }
        return null;
    }

    public static void writeDoorPlayStatus(Long passportId) {
        redisManager.set(BoxRedisKeySet.DOOR_PLAY_STATUS + passportId, System.currentTimeMillis() + "");
    }
}
