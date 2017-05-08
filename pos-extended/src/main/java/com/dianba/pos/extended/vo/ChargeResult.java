package com.dianba.pos.extended.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/4 0004.
 * 话费充值返回结果
 */
public class ChargeResult implements Serializable{

    /***返回结果码**/
    private String resultCode;

    /**返回结果描述**/
    private String resultDesc;

    /**商户订单Id**/
    private String merchantOrderId;

    /***19e 话费订单号**/
    private String ehfOrderId;

    /***结果查询url**/
    private String queryResultUrl;

    /**该订单扣款金额***/
    private String payMoney;

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

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getEhfOrderId() {
        return ehfOrderId;
    }

    public void setEhfOrderId(String ehfOrderId) {
        this.ehfOrderId = ehfOrderId;
    }

    public String getQueryResultUrl() {
        return queryResultUrl;
    }

    public void setQueryResultUrl(String queryResultUrl) {
        this.queryResultUrl = queryResultUrl;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}
