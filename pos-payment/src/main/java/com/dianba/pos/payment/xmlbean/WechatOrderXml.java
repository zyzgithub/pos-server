package com.dianba.pos.payment.xmlbean;

import com.dianba.pos.common.util.JaxbUtil;
import com.dianba.pos.payment.util.MD5Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WechatOrderXml {

    private static Logger logger = LogManager.getLogger(WechatOrderXml.class);

    @XmlElement(required = true)
    private String appid;
    @XmlElement(name = "mch_id", required = true)
    private String mchId;
    @XmlElement(name = "device_info")
    private String deviceInfo;
    @XmlElement(name = "nonce_str", required = true)
    private String nonceStr;
    @XmlElement(required = true)
    private String sign;
    @XmlElement(name = "sign_type")
    private String signType;
    @XmlElement(required = true)
    private String body;
    @XmlElement
    private String detail;
    @XmlElement
    private String attach;
    @XmlElement(name = "out_trade_no", required = true)
    private String outTradeNo;
    @XmlElement(name = "fee_type")
    private String feeType;
    @XmlElement(name = "total_fee", required = true)
    private Integer totalFee;
    @XmlElement(name = "spbill_create_ip", required = true)
    private String spbillCreateIp;
    @XmlElement(name = "goods_tag")
    private String goodsTag;
    @XmlElement(name = "notify_url", required = true)
    private String notifyUrl;
    @XmlElement(name = "trade_type", required = true)
    private String tradeType;
    @XmlElement(name = "product_id")
    private String productId;
    @XmlElement(name = "limit_pay")
    private String limitPay;
    @XmlElement(name = "time_start")
    private String timeStart;
    @XmlElement(name = "time_expire")
    private String timeExpire;
    @XmlElement
    private String openid;
    @XmlElement(name = "auth_code")
    private String authCode;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String buildSign(String secrectKey) {
        String xml = JaxbUtil.convertToXml(this);
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element rootElement = doc.getRootElement();
            SortedMap<String, String> sortedMap = new TreeMap<>();
            for (Element element : (List<Element>) rootElement.elements()) {
                sortedMap.put(element.getName(), element.getText());
            }
            StringBuilder sb = new StringBuilder();
            for (String key : sortedMap.keySet()) {
                sb = sb.append(key).append("=").append(sortedMap.get(key)).append("&");
            }
            sb = sb.append("key=").append(secrectKey);
            logger.info("参数验签:" + sb.toString());
            this.sign = MD5Util.md5(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JaxbUtil.convertToXml(this);
    }
}
