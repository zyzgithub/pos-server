package com.dianba.pos.common.exception.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    public static final String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
