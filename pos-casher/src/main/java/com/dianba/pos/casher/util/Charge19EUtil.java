package com.dianba.pos.casher.util;

import com.dianba.pos.casher.vo.Charge_19E;

import java.util.List;

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
     public   static final String KEY="1OxQjlashjdlsc1OZd0CQYohforvnlfpsdj";

     public  static final String MERCHANT_ID="AA0b0192015072415092542712";

     public static String toJson(String result){

         String [] list = result.split("&");
         String myStr = "{";
         for(int i=0;i<list.length;i++)
         {
             try{
                 String [] keys = list[i].split("=");
                 String key = keys[0];
                 String value= keys[1];
                 if(i>0)
                 {
                     myStr += ",";
                 }
                 myStr += "\""+key+"\":\""+value+"\"";
             }catch(Exception e)
             {
                 continue;
             }
         }
         myStr += "}";
         return myStr;

     }
}
