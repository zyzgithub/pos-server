package com.dianba.pos.extended.vo;


import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.extended.util.FlowCharge19EApi;
import com.dianba.pos.extended.util.FlowCharge19EUtil;


/**
 * Created by Administrator on 2017/5/6 0006.
 * 流量充值类
 */
public class ChargeFlow {

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

    /**
     * 产品id
     */
    private String productId;

    /***手机号码**/
    private String mobile;

    /***商户订单号**/
    private String merOrderNo;

    /***商户id**/
    private String merchantId;

    /**
     * 备注---可不填
     */
    private String remark;


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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Charge_Flow{" +
                "sign='" + sign + '\'' +
                ", signType='" + signType + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", dataType='" + dataType + '\'' +
                ", inputCharset='" + inputCharset + '\'' +
                ", version='" + version + '\'' +
                ", productId='" + productId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", merOrderNo='" + merOrderNo + '\'' +
                ", merchantId='" + merchantId + '\'' +

                '}';
    }

    /**
     * 签名拼接字符串
     */
    public String sign() {

        return
                "dataType=" + dataType + '&' +
                        "inputCharset=" + inputCharset + '&' +
                        "mobile=" + mobile + '&' +
                        "merOrderNo=" + merOrderNo + '&' +
                        "merchantId=" + merchantId + '&' +
                        "signType=" + signType + '&' +
                        "timestamp=" + timestamp + '&' +
                        "version=" + version + "&" +
                        "remark=" + remark;
    }

    public String params(String signp) {

        return
                "sign=" + signp + "&" +
                        "signType=" + signType + '&' +
                        "timestamp=" + timestamp + '&' +
                        "dataType=" + dataType + '&' +
                        "inputCharset=" + inputCharset + '&' +
                        "version=" + version + "&" +
                        "mobile=" + mobile + '&' +
                        "merOrderNo=" + merOrderNo + '&' +
                        "merchantId=" + merchantId + '&' +
                        "remark=" + remark;
    }

    public static void main(String[] args) {


//     Product pd=new Product();
//     pd.setMobile("15001000000");
//     pd.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
//     String bb=  FlowCharge19EApi.queryProduct(FlowCharge19EUtil.QUERY_PRODUCT,pd);
//     System.out.println(bb);
        ChargeFlow cf = new ChargeFlow();
        cf.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
        cf.setMerOrderNo("2222222");
        cf.setProductId("RF0116111310421001");

        cf.setMobile("15001000000");
        cf.setRemark("流量充值!");
       // ChargeFlow chargeFlow= FlowCharge19EApi.flowCharge(FlowCharge19EUtil.FLOW_CHARGE_URL, cf);
      //  System.out.println(chargeFlow.toString());
    }
}
