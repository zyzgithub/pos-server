package com.dianba.pos.casher.util;

import com.dianba.pos.casher.vo.Charge_19E;

/**
 * Created by Administrator on 2017/5/3 0003.
 */
public class Charge19EUtil

{
    /**19e话费充值测试平台**/
    public static  final String HF_CHARGE_IP_PORT="http://114.247.40.65:8093/";
    /**充值url**/
    public static  final String HF_CHARGE_19E_URL=HF_CHARGE_IP_PORT+
            "hfCharge/requestCharge3In1KvDealer/deal";

    /**签名秘钥测试*/
     public   static final String KEY="1QxQjlashjdlsc1OZdoCQYohforvn1fpsdj";

     public  static final String MERCHANT_ID="AAobo192015072415092542712";
}
