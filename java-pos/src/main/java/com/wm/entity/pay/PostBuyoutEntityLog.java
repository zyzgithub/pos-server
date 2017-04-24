package com.wm.entity.pay;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by mjorcen on 16/8/8.
 */
@Entity
@Table(name = "post_buyout_log", schema = "")
@SuppressWarnings("serial")
@DynamicUpdate(true)
@DynamicInsert(true)
public class PostBuyoutEntityLog {
    /**
     * id
     */
    private Long id;
    private Long userId;
    private Long postBuyoutId;
    /**
     * 1.未支付
     * 2.支付完成
     */
    private Integer payState;
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
     * 需要付款的时间
     */
    private Long needPayTime;
    /**
     * 增加的使用时间,单位 月
     */
    private Integer validityMonth;
    /*
    *实际支付时间
    */
    private Long realPayTime;

    private String payIndex;

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
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_id", nullable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "post_buyout_id", nullable = false)
    public Long getPostBuyoutId() {
        return postBuyoutId;
    }

    public void setPostBuyoutId(Long postBuyoutId) {
        this.postBuyoutId = postBuyoutId;
    }

    @Column(name = "pay_state", nullable = false)
    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
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

    @Column(name = "need_pay_time", nullable = false)
    public Long getNeedPayTime() {
        return needPayTime;
    }

    public void setNeedPayTime(Long needPayTime) {
        this.needPayTime = needPayTime;
    }

    @Column(name = "real_pay_time", nullable = false)
    public Long getRealPayTime() {
        return realPayTime;
    }

    public void setRealPayTime(Long realPayTime) {
        this.realPayTime = realPayTime;
    }


    @Column(name = "pay_index", nullable = false)

    public String getPayIndex() {
        return payIndex;
    }

    public void setPayIndex(String payIndex) {
        this.payIndex = payIndex;
    }

    @Column(name = "validity_month", nullable = false)
    public Integer getValidityMonth() {
        return validityMonth;
    }

    public void setValidityMonth(Integer validityMonth) {
        this.validityMonth = validityMonth;
    }
}
