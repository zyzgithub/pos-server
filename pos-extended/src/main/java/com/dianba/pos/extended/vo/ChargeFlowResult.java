package com.dianba.pos.extended.vo;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class ChargeFlowResult {

    /***返回结果码**/
    private String resultCode;

    /**
     * 返回结果描述
     **/
    private String resultDesc;

    /****19e订单号**/
    private String orderNo;

    /**
     * 商户订单号
     **/
    private String merOrderNo;

    /*****/
    private String mobile;

    /***结果查询url**/
    private String flowValue;

    /**
     * 该订单扣款金额
     ***/
    private String payPrice;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFlowValue() {
        return flowValue;
    }

    public void setFlowValue(String flowValue) {
        this.flowValue = flowValue;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }
}
