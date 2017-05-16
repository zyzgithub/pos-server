package com.dianba.pos.extended.vo;


import com.dianba.pos.extended.util.HfCharge19EApi;
import com.dianba.pos.extended.util.HfCharge19EUtil;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
public class HfOrderQuery {

    /*****************************协议参数***********************************/

    /****签名**/
    private String sign;

    /****签名方式*****/
    private String signType = "MD5";

    /******访问时间戳yyyyMMddHHmmss****/
    private String timestamp = "20160406175304";

    /****数据格式类型 KEYVALUE**/
    private String dataType = "KEYVALUE";

    /**
     * 参数编码字符串
     **/
    private String inputCharset = "UTF-8";

    /**
     * 版本号
     */
    private String version = "1.0";
    private String ehfOrderId;

    private String merchantId;

    private String merchantOrderId;


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEhfOrderId() {
        return ehfOrderId;
    }

    public void setEhfOrderId(String ehfOrderId) {
        this.ehfOrderId = ehfOrderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    /**
     * 签名拼接字符串
     */
    public String sign() {

        return
                "ehfOrderId=" + ehfOrderId + '&' + "merchantId=" + merchantId + "&" + "timestamp=" + timestamp + '&'
                        + "key=" + HfCharge19EUtil.KEY;


    }

    public String params(String signp) {

        return
                "sign=" + signp + "&"
                        + "signType=" + signType + '&'
                        + "timestamp=" + timestamp + '&'
                        + "dataType=" + dataType + '&'
                        + "inputCharset=" + inputCharset + '&'
                        + "version=" + version + "&"
                        + "ehfOrderId=" + ehfOrderId + '&'
                        + "merchantId=" + merchantId + "&"
                        + "merchantOrderId" + merchantOrderId;


    }

    public static void main(String[] args) {

        HfOrderQuery ho = new HfOrderQuery();
        ho.setMerchantId("AA0b0192015072415092542712");
        ho.setEhfOrderId("ESSOD02141011705081347128037");
        ho.setMerchantOrderId("4234234324");
        String aaa = HfCharge19EApi.hfOrderQuery(HfCharge19EUtil.HT_ORDER_INFO_QUERY, ho);
        System.out.println(aaa);


    }
}
