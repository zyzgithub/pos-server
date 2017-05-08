package com.dianba.pos.extended.config;




import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static final String HT_ORDER_INFO_QUERY =HF_CHARGE_IP_PORT+"hfCharge/orderInfoQueryKvDealer/deal";
    /**流量充值请求地址**/
    public static final String FLOW_CHARGET_IP_PORT="http://114.247.40.65:10000/";

    /***流量充值*/
    public  static final String FLOW_CHARGE_URL=FLOW_CHARGET_IP_PORT+
            "flow_center/createOrder/createOrder.jhtml";

    /******根据手机号等参数获取产品信息***/
    public  static final String QUERY_PRODUCT=FLOW_CHARGET_IP_PORT+
            "flow_center/queryProduct/queryProductInfo.jhtml";

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

    public static String getKeyedDigest(String paramsStr, String key) {

        MessageDigest md5 = null;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(paramsStr.getBytes("UTF-8"));

            byte[] temp;
            temp = md5.digest(key.getBytes("UTF-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
