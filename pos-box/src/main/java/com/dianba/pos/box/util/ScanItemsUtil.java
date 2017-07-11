package com.dianba.pos.box.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScanItemsUtil {

    private static final Map<Long, String> SCAN_ITEMS = new ConcurrentHashMap<>();

    public static String getRFIDItems(Long passportId) {
        if (SCAN_ITEMS.containsKey(passportId)) {
            return SCAN_ITEMS.get(passportId);
        }
        return "";
    }

    public static void writeScanItems(Long passportId, String rfids) {
        SCAN_ITEMS.put(passportId, rfids);
    }
}
