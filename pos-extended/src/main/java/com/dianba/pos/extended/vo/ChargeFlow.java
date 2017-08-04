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


    /**
     * 签名拼接字符串
     */
    public String sign() {

        return
                "dataType=" + dataType + '&'
                        + "inputCharset=" + inputCharset + '&'
                        + "mobile=" + mobile + '&'
                        + "merOrderNo=" + merOrderNo + '&'
                        + "merchantId=" + merchantId + '&'
                        + "signType=" + signType + '&'
                        + "timestamp=" + timestamp + '&'
                        + "version=" + version + "&"
                        + "remark=" + remark;
    }

    public String params(String signp) {

        return
                "sign=" + signp + "&"
                        + "signType=" + signType + '&'
                        + "timestamp=" + timestamp + '&'
                        + "dataType=" + dataType + '&'
                        + "inputCharset=" + inputCharset + '&'
                        + "version=" + version + "&"
                        + "mobile=" + mobile + '&'
                        + "merOrderNo=" + merOrderNo + '&'
                        + "merchantId=" + merchantId + '&'
                        + "remark=" + remark;
    }

    public static void main(String[] args) {

//
//     Product pd=new Product();
//     pd.setMobile("13249196270");
//     pd.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
//     String bb=  FlowCharge19EApi.queryProduct(FlowCharge19EUtil.QUERY_PRODUCT,pd);
//     System.out.println(bb);
        ChargeFlow cf = new ChargeFlow();
        cf.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
        cf.setMerOrderNo("201707311140072115000354");
        cf.setProductId("p0116111519061002");
        cf.setTimestamp("201707310114000");
        cf.setMobile("13249196270");
        cf.setRemark("流量充值!");
        ChargeFlowResult chargeFlow= FlowCharge19EApi.flowCharge(FlowCharge19EUtil.FLOW_CHARGE_URL, cf);

    }
}
