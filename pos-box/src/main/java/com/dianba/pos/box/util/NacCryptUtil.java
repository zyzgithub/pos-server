package com.dianba.pos.box.util;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.box.vo.AccessResultVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NacCryptUtil {

    private static final byte[] KEY = {121, 103, 50, 48, 49, 54, 48, 56};

    private static Logger logger = LogManager.getLogger(NacCryptUtil.class);

    public static AccessResultVo decode(HttpServletRequest request) {
        String reqData = request.getParameterMap().keySet().iterator().next().toString();
        return decode(reqData);
    }

    public static AccessResultVo decode(String reqData) {
        logger.debug("原始key:" + reqData);
        byte[] realDataBytes = new byte[reqData.getBytes().length * 2];
        NacCryptUtil.decrypt(reqData.getBytes(), reqData.getBytes().length, realDataBytes, realDataBytes.length);
        String realData = new String(realDataBytes);
        logger.debug("解析key:" + realData);
        return JSONObject.parseObject(realData, AccessResultVo.class);
    }

    public static void encode(HttpServletResponse response, AccessResultVo accessResultVo) {
        String reqData = JSONObject.toJSONString(accessResultVo);
        logger.debug("发送原始key:" + reqData);
        byte[] realDataBytes = new byte[reqData.getBytes().length * 2];
        NacCryptUtil.encrypt(reqData.getBytes(), reqData.getBytes().length, realDataBytes, realDataBytes.length);
        String realData = new String(realDataBytes);
        logger.debug("发送加密key:" + realData);
        try {
            response.setContentType("application/json");
            response.getWriter().write(realData);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2) {
        int i = paramInt1 * 2;
        int j = 0;
        if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null)) {
            return 0;
        }
        if (paramArrayOfByte2.length < paramArrayOfByte1.length * 2) {
            return -1;
        }
        int k = 0;
        for (j = 0; j < paramInt1; j = (short) (j + 1)) {
            paramArrayOfByte2[(j * 2)] = ((byte) (paramArrayOfByte1[j] ^ KEY[k]));
            k++;
            if (k >= KEY.length) {
                k = 0;
            }
        }
        for (j = 0; j < paramInt1 * 2; j = (short) (j + 2)) {
            if (((paramArrayOfByte2[j] & 0xF) >= 0) && ((paramArrayOfByte2[j] & 0xF) <= 9)) {
                paramArrayOfByte2[(j + 1)] = ((byte) ((paramArrayOfByte2[j] & 0xF) + 48));
            } else if (((paramArrayOfByte2[j] & 0xF) >= 10) && ((paramArrayOfByte2[j] & 0xF) <= 15)) {
                paramArrayOfByte2[(j + 1)] = ((byte) ((paramArrayOfByte2[j] & 0xF) - 10 + 65));
            }
            paramArrayOfByte2[j] = ((byte) ((paramArrayOfByte2[j] & 0xF0) >> 4));
            if (((paramArrayOfByte2[j] & 0xF) >= 0) && ((paramArrayOfByte2[j] & 0xF) <= 9)) {
                paramArrayOfByte2[j] = ((byte) (paramArrayOfByte2[j] + 48));
            } else if (((paramArrayOfByte2[j] & 0xF) >= 10) && ((paramArrayOfByte2[j] & 0xF) <= 15)) {
                paramArrayOfByte2[j] = ((byte) (paramArrayOfByte2[j] - 10 + 65));
            }
        }
        return i;
    }

    private static int decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2) {
        int j = 0;
        int k = 0;
        int m = 0;
        int n = 0;
        if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null)) {
            return 0;
        }
        if (paramArrayOfByte2.length < paramArrayOfByte1.length / 2) {
            return -1;
        }
        for (j = 0; j < paramInt1; j += 2) {
            if ((paramArrayOfByte1[j] >= 48) && (paramArrayOfByte1[j] <= 57)) {
                k = (byte) (paramArrayOfByte1[j] - 48);
            } else if ((paramArrayOfByte1[j] >= 65) && (paramArrayOfByte1[j] <= 70)) {
                k = (byte) (paramArrayOfByte1[j] - 65 + 10);
            }
            k = (byte) (k << 4 & 0xF0);
            if ((paramArrayOfByte1[(j + 1)] >= 48) && (paramArrayOfByte1[(j + 1)] <= 57)) {
                k = (byte) (k | paramArrayOfByte1[(j + 1)] - 48);
            } else if ((paramArrayOfByte1[(j + 1)] >= 65) && (paramArrayOfByte1[(j + 1)] <= 70)) {
                k = (byte) (k | paramArrayOfByte1[(j + 1)] - 65 + 10);
            }
            paramArrayOfByte2[n] = ((byte) (k ^ KEY[m]));
            n++;
            m++;
            if (m >= KEY.length) {
                m = 0;
            }
        }
        return paramInt1 / 2;
    }
}
