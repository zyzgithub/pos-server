package com.dianba.pos.extended.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
@Entity
@Table(name = "life_pos.pos_charge_19e_order")
public class PosCharge19eOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "result_code")
    private String resultCode;
    @Column(name = "result_desc")
    private String resultDesc;
    @Column(name = "eOrder_id")
    private String eOrderId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "charge_phone")
    private String chargePhone;
    @Column(name = "charge_number")
    private String chargeNumber;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "merchant_id")
    private String merchantId;
    @Column(name = "finish_time")
    private String finishTime;
    @Column(name = "merchant_order_id")
    private String merchantOrderId;
    @Column(name = "query_result_url")
    private String queryResultUrl;
    @Column(name = "result_text")
    private String resultText;
    @Column(name = "type")
    private Integer type;



    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
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
