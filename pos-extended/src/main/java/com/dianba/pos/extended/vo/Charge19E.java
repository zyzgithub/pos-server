package com.dianba.pos.extended.vo;

import com.dianba.pos.extended.util.HfCharge19EApi;
import com.dianba.pos.extended.util.HfCharge19EUtil;

/**
 * Created by Administrator on 2017/5/4 0004.
 * zyz
 * 19e话费充值平台
 */

public class Charge19E {

    /*************************************19充值请求参数****************************************/
    /**
     * 充值手机号码-必填
     **/
    private String chargeNumber;

    /**
     * 充值金额1-500--必填
     **/
    private String chargeMoney;

    /**
     * 到账类型（0 快充 24-24 慢充 48-48 慢充）--必填
     **/
    private String fillType;

    /**
     * 充值类型（0 手机 1 固话 2 小灵通 3 宽带） --必填
     **/
    private String chargeType;

    /***商户id（为19e平台注册的商户的id 长度40位）--必填**/
    private String merchantId = HfCharge19EUtil.MERCHANT_ID;

    /**
     * 订单id长度50位
     **/
    private String merchantOrderId;

    /***商户充值结果接受地址URL**/
    private String sendNotifyUrl;

    /**
     * 运营商（0 联通 1 移动 2 电信，当为固话小灵通必填）
     ***/
    private String ispId;

    /***手机省份Id**/
    private String provinceId;

    /*****************************协议参数***********************************/
    /****签名方式*****/
    private String signType = "MD5";
    /******访问时间戳yyyyMMddHHmmss****/
    private String timestamp = "20160406175304";
    /****签名**/
    private String sign = sign();
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

    /**********************************19充值返回参数***********************************/

    public static void main(String[] args) {
        Charge19E ch = new Charge19E();
        ch.setChargeNumber("17052912345");
        ch.setChargeMoney("10");
        ch.setChargeType("0");
        ch.setMerchantOrderId("testRollbackOrder11");
        ch.setSendNotifyUrl(HfCharge19EUtil.NOTIFY_URL);
        ch.setIspId("");
        ch.setProvinceId("");
        ch.setFillType("0");
        String result = HfCharge19EApi.hfCharge(HfCharge19EUtil.HF_CHARGE_19E_URL, ch);
        System.out.println(result);
        HfOrderQuery hq = new HfOrderQuery();
        hq.setMerchantOrderId("testRollbackOrder11");
        hq.setMerchantId(HfCharge19EUtil.MERCHANT_ID);
        String resultq = HfCharge19EApi.hfOrderQuery(HfCharge19EUtil.HT_ORDER_INFO_QUERY, hq);
        System.out.println(resultq);

    }

    public String getSign() {
        return sign;
    }

    public void setSing(String sing) {
        this.sign = sing;
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

    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getFillType() {
        return fillType;
    }

    public void setFillType(String fillType) {
        this.fillType = fillType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
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

    public String getSendNotifyUrl() {
        return sendNotifyUrl;
    }

    public void setSendNotifyUrl(String sendNotifyUrl) {
        this.sendNotifyUrl = sendNotifyUrl;
    }

    public String getIspId() {


        return ispId;
    }

    public void setIspId(String ispId) {
        this.ispId = ispId;
    }

    public String getProvinceId() {

        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "Charge_19E{" +
                "chargeNumber='" + chargeNumber + '\'' +
                ", chargeMoney='" + chargeMoney + '\'' +
                ", fileType='" + fillType + '\'' +
                ", chargeType='" + chargeType + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                ", sendNotifyUrl='" + sendNotifyUrl + '\'' +
                ", ispId='" + ispId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                '}';
    }

    public String sign() {

        return
                "chargeNumber=" + chargeNumber + '&' +
                        "chargeMoney=" + chargeMoney + '&' +
                        "fillType=" + fillType + '&' +
                        "chargeType=" + chargeType + '&' +
                        "merchantId=" + merchantId + '&' +
                        "merchantOrderId=" + merchantOrderId + '&' +
                        "sendNotifyUrl=" + sendNotifyUrl + '&' +
                        "ispId=" + ispId + '&' +
                        "provinceId=" + provinceId + '&' +
                        "timestamp=" + timestamp + '&' +
                        "key=" + HfCharge19EUtil.KEY;
    }

    public String params(String signp) {

        return


                "chargeNumber=" + chargeNumber + '&' +
                        "chargeMoney=" + chargeMoney + '&' +
                        "fillType=" + fillType + '&' +
                        "chargeType=" + chargeType + '&' +
                        "merchantId=" + merchantId + '&' +
                        "merchantOrderId=" + merchantOrderId + '&' +
                        "sendNotifyUrl=" + sendNotifyUrl + '&' +
                        "ispId=" + ispId + '&' +
                        "provinceId=" + provinceId + "&" +
                        "sign=" + signp + "&" +
                        "signType=" + signType + '&' +
                        "timestamp=" + timestamp + '&' +
                        "dataType=" + dataType + '&' +
                        "inputCharset=" + inputCharset + '&' +
                        "version=" + version;

    }
}
