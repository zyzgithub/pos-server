package com.dianba.pos.extended.vo;

import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.extended.util.FlowCharge19EApi;
import com.dianba.pos.extended.util.FlowCharge19EUtil;


/**
 * Created by Administrator on 2017/5/6 0006.
 * 获取产品信息
 */
public class Product {


    /*****************************协议参数***********************************/

    /****签名**/
    private String sign;

    /****签名方式*****/
    private String signType = "MD5";

    /******访问时间戳yyyyMMddHHmmss****/
    private String timestamp = DateUtil.getCurrDate("yyyyMMddHHHmmss");

    /****数据格式类型 KEYVALUE**/
    private String dataType = "JSON";

    /**
     * 参数编码字符串
     **/
    private String inputCharset = "UTF-8";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**********************************业务请求参数***************************************/


    private String merchantId;

    private String mobile;

    public static void main(String[] args) {

        Product pd = new Product();
        pd.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
        pd.setMobile("13249196272");
        String result = FlowCharge19EApi.queryProduct(FlowCharge19EUtil.QUERY_PRODUCT, pd);
        System.out.println(result);
    }

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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Product{" +
                "sign='" + sign + '\'' +
                ", signType='" + signType + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", dataType='" + dataType + '\'' +
                ", inputCharset='" + inputCharset + '\'' +
                ", version='" + version + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public String sign() {

        return

                "dataType=" + dataType + '&' +
                        "inputCharset=" + inputCharset + '&' +
                        "merchantId=" + merchantId + '&' +
                        "mobile=" + mobile + "&" +
                        "signType=" + signType + '&' +
                        "timestamp=" + timestamp + '&' +
                        "version=" + version;

    }

    public String params(String signp) {

        return
                "dataType=" + dataType + '&' +
                        "inputCharset=" + inputCharset + '&' +
                        "merchantId=" + merchantId + '&' +
                        "mobile=" + mobile + "&" +
                        "signType=" + signType + '&' +
                        "timestamp=" + timestamp + '&' +
                        "version=" + version + "&" + "sign=" + signp;


    }
}
