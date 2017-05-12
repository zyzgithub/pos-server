package com.dianba.pos.order.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_state", schema = "")
public class OrderState implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * orderId
     */
    private Integer orderId;
    /**
     * 处理时间
     */
    private Integer dealTime;
    /**
     * 状态：未支付，已付款，已接单，申请取消，已取消，配送中，已收货，已确认，已评价
     */
    private String state;
    /**
     * detail
     */
    private String detail;

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  id
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 20, scale = 0)
    public Integer getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  orderId
     */
    @Column(name = "ORDER_ID", nullable = false, precision = 19, scale = 0)
    public Integer getOrderId() {
        return this.orderId;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  orderId
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  处理时间
     */
    @Column(name = "DEAL_TIME", nullable = true, precision = 10, scale = 0)
    public Integer getDealTime() {
        return this.dealTime;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  处理时间
     */
    public void setDealTime(Integer dealTime) {
        this.dealTime = dealTime;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  状态：未支付，已付款，已接单，申请取消，已取消，配送中，已收货，已确认，已评价
     */
    @Column(name = "STATE", nullable = true, length = 10)
    public String getState() {
        return this.state;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  状态：未支付，已付款，已接单，申请取消，已取消，配送中，已收货，已确认，已评价
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  detail
     */
    @Column(name = "DETAIL", nullable = true, length = 100)
    public String getDetail() {
        return this.detail;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
