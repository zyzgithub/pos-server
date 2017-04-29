package com.dianba.pos.common.util;

public class PageUtil {
    public static final int getFristResult(int pageNumber, int maxPageSize) {
        int fristResult = pageNumber * maxPageSize - maxPageSize;
        fristResult = fristResult > 0 ? fristResult : 0;
        return fristResult;
    }
}
