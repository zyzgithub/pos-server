package com.dianba.pos.payment.util;

import com.dianba.pos.payment.config.AlipayConfig;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AlipayResultUtil {

    public static final String ALIPAY_SUCCESS = "TRADE_SUCCESS";

    public static boolean isSuccess(HttpServletRequest request) {
        String tradeStatus = request.getParameter("trade_status");
        return StringUtils.equals(tradeStatus, ALIPAY_SUCCESS);
    }

    public static void writeResult(HttpServletResponse response, String txt) {
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("text/plain");
            response.setCharacterEncoding(AlipayConfig.CHARSET);
            writer = response.getWriter();
            writer.write(txt);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }

        }
    }
}
