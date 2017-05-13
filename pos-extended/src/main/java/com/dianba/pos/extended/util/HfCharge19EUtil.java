package com.dianba.pos.extended.util;


import com.dianba.pos.extended.config.ExtendedUrlConstant;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/5/3 0003.
 */
public class HfCharge19EUtil

{
    /**
     * 19e话费充值测试平台
     **/
    public static final String HF_CHARGE_IP_PORT = "http://114.247.40.65:8093/";
//    public static final String HF_CHARGE_IP_PORT = "http://edx.19ego.cn:8080";

    /**
     * 充值url
     **/
    public static final String HF_CHARGE_19E_URL = HF_CHARGE_IP_PORT +
            "hfCharge/requestCharge3In1KvDealer/deal";

    public static final String HT_ORDER_INFO_QUERY = HF_CHARGE_IP_PORT + "hfCharge/orderInfoQueryKvDealer/deal";


    /**
     * 签名秘钥测试
     */
    public static final String KEY = "1OxQjlashjdlsc1OZd0CQYohforvnlfpsdj";

    public static final String MERCHANT_ID = "AA0b0192015072415092542712";

//    public static final String KEY = "CXRJSnB9fFqKxXEpQ7H0nub7f0c1EakZ9CBH0zcGcWbRHgttGc";

//   public static final String MERCHANT_ID = "AA0ae02017050816303689733";

    public static final String POS_TEST_URL = "http://apptest.0085.com/pos/";
//    public static final String POS_TEST_URL = "http://no1.0085.com/pos/";

    public static final String NOTIFY_URL = POS_TEST_URL + ExtendedUrlConstant.CHARGE_19E_INFO + "hfChargeBack";

    public static String toJson(String result) {

        String[] list = result.split("&");
        String myStr = "{";
        for (int i = 0; i < list.length; i++) {
            try {
                String[] keys = list[i].split("=");
                String key = keys[0];
                String value = keys[1];
                if (i > 0) {
                    myStr += ",";
                }
                myStr += "\"" + key + "\":\"" + value + "\"";
            } catch (Exception e) {
                continue;
            }
        }
        myStr += "}";
        return myStr;

    }


}
