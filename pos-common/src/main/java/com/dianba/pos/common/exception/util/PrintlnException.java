package com.dianba.pos.common.exception.util;

public class PrintlnException {
    public static final void println(Throwable e) {
        e.printStackTrace();
    }
}
