package com.dianba.pos.extended.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
@Entity
@Table(name = "charge_19e")
public class Charge19e implements Serializable {
    @Id
    @Column(name = "charge_id")
    @GeneratedValue
    private Integer chargeId;

    @Column(name = "resultCode")
    private String resultCode;

    @Column(name = "resultDesc")
    private String resultDesc;

    @Column(name = "eOrderId")
    private String eOrderId;

    @Column(name = "productName")
    private String productName;

    @Column(name = "createTime")
    private String createTime;

    @Column(name = "chargePhone")
    private String chargePhone;

    @Column(name = "chargeNumber")
    private String chargeNumber;

    @Column(name = "orderStatus")
    private String orderStatus;

    @Column(name = "merchantId")
    private String merchantId;

    @Column(name = "finishTime")
    private String finishTime;

    @Column(name = "merchantOrderId")
    private String merchantOrderId;

    @Column(name = "queryResultUrl")
    private String queryResultUrl;

    @Column(name = "resultTest")
    private String resultText;

    @Column(name = "type")
    private Integer type;


    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

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

    public String geteOrderId() {
        return eOrderId;
    }

    public void seteOrderId(String eOrderId) {
        this.eOrderId = eOrderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChargePhone() {
        return chargePhone;
    }

    public void setChargePhone(String chargePhone) {
        this.chargePhone = chargePhone;
    }

    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getQueryResultUrl() {
        return queryResultUrl;
    }

    public void setQueryResultUrl(String queryResultUrl) {
        this.queryResultUrl = queryResultUrl;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
