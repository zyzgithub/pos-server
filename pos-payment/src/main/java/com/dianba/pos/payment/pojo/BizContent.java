package com.dianba.pos.payment.pojo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 业务参数封装
 *
 * @author Javen
 *         https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.mViJGu&treeId=193&articleId=105465&docType=1
 */
public class BizContent {

    private String body;//对一笔交易的具体描述信息
    private String subject;//商品的标题
    @JSONField(name = "out_trade_no")
    private String outTradeNo;//商户网站唯一订单号
    @JSONField(name = "total_amount")
    private String totalAmount;//订单总金额，单位为元，精确到小数点后两位
    @JSONField(name = "product_code")
    private String productCode;//固定制QUICK_MSECURITY_PAY
    @JSONField(name = "passback_params")
    private String passbackParams;//回传参数

    public BizContent() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPassbackParams() {
        return passbackParams;
    }

    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
    }
}
