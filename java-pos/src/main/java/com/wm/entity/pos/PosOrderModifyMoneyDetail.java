package com.wm.entity.pos;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by mjorcen on 16/8/15.
 */
@Entity
@Table(name = "pos_order_modify_money_detail", schema = "")
public class PosOrderModifyMoneyDetail {

    /**
     * id
     */
    private java.lang.Integer id;

    /**
     * 订单 ID,
     */
    private java.lang.Integer orderId;
    /**
     * 修改的折扣,当modifyType = 2时必传, 其余无效
     */
    private java.lang.Integer modifyDiscount;
    /**
     * 折扣类型
     * 1 : 免单
     * 2 : 折扣
     * 3 : 抹零
     * 4 : 修改
     */
    private java.lang.Integer modifyType;

    private java.lang.Integer merchantId;
    private BigDecimal modifyMoney;
    private BigDecimal realPrice;
    private java.lang.Long createTime;


    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 20, scale = 0)
    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Column(name = "merchant_id")
    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }


    @Column(name = "modify_discount")
    public Integer getModifyDiscount() {
        return modifyDiscount;
    }

    public void setModifyDiscount(Integer modifyDiscount) {
        this.modifyDiscount = modifyDiscount;
    }

    @Column(name = "modify_money")
    public BigDecimal getModifyMoney() {
        return modifyMoney;
    }

    public void setModifyMoney(BigDecimal modifyMoney) {
        this.modifyMoney = modifyMoney;
    }

    @Column(name = "modify_type")
    public Integer getModifyType() {
        return modifyType;
    }

    public void setModifyType(Integer modifyType) {
        this.modifyType = modifyType;
    }

    @Column(name = "real_price")
    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
