package com.dianba.pos.common.util;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class Md5Util {
    public static String HEXAndMd5(String plainText) {

        String result = "";
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("utf-8"));

            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            System.out.println("MD5(" + plainText + ",32) = " + result);
            System.out.println("MD5(" + plainText + ",16) = " + buf.toString().substring(8, 24));
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
}
