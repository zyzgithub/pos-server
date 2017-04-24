package com.wm.entity.pay;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by mjorcen on 16/8/8.
 */
@Entity
@Table(name = "post_buyout", schema = "")
@SuppressWarnings("serial")
@DynamicUpdate(true)
@DynamicInsert(true)
public class PostBuyoutEntity {
    /**
     * id
     */
    private Long id;
    private Long userId;
    private Long merchantId;
    /**
     * 付款次数
     * 已经分期付款的次数
     */
    private Integer payNum;
    /**
     * 1:一次性付清
     * 12:分12期
     * 24:24期
     * 36:36期
     */
    private Integer payType;
//    /**
//     * 1.正常使用
//     * 2.欠费
//     */
//    private Integer state;

    /**
     * 1.软件
     * 2.硬件
     */
    private Integer item;

    /**
     * 总金额
     */
    private BigDecimal totalmoney;
    /**
     * 支付金额
     */
    private BigDecimal paymoney;
    /**
     * 描述
     */
    private String description;

    private Long createTime;
    /**
     * 当前有效期
     */
    private Long validity;
    /**
     * 有效期开始时间
     */
    private Long startTime;
    /**
     * 最终有效期
     */
    private Long endTime;
//    /**
//     * 完成支付的时间
//     * 如果是分期
//     */
//    private Long finishTime;

    @Column(name = "start_time", nullable = false)
    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time", nullable = false)
    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    @Column(name = "user_id", nullable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "merchant_id", nullable = false)

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    @Column(name = "pay_num", nullable = false)
    public Integer getPayNum() {
        return payNum;
    }

    public void setPayNum(Integer payNum) {
        this.payNum = payNum;
    }

    @Column(name = "pay_type", nullable = false)

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

//    @Column(name = "pay_state", nullable = false)
//
//    public Integer getPayState() {
//        return payState;
//    }
//
//    public void setPayState(Integer payState) {
//        this.payState = payState;
//    }

    @Column(name = "totalmoney", nullable = false)

    public BigDecimal getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(BigDecimal totalmoney) {
        this.totalmoney = totalmoney;
    }

    @Column(name = "paymoney", nullable = false)

    public BigDecimal getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(BigDecimal paymoney) {
        this.paymoney = paymoney;
    }

    @Column(name = "description", nullable = false)

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "createtime", nullable = false)

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "item", nullable = false)
    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    @Column(name = "validity", nullable = false)
    public Long getValidity() {
        return validity;
    }

    public void setValidity(Long validity) {
        this.validity = validity;
    }
}
