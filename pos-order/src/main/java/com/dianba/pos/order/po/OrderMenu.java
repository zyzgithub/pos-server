package com.dianba.pos.order.po;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "ORDER_MENU")
public class OrderMenu implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer menuId;  //商品id
    private String name; //商品名称
    private Integer count;//数量
    private double price;//单价
    private double promotionPrice;//促销价格
    private double discountMoney;//优惠金额
    private double total;//总价
    private String salesPromotion;//是否促销
    private String unit = "";//单位
    private String errMsg = "";

    @Column(name = "ORDER_ID")
    private Long orderId;

    private Integer quantity;

    private Double totalPrice;

    private String state;

    private Double promotionMoney;

    private Integer menuPromotionId;

    private Double originalPrice;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getPromotionMoney() {
        return promotionMoney;
    }

    public void setPromotionMoney(Double promotionMoney) {
        this.promotionMoney = promotionMoney;
    }

    public Integer getMenuPromotionId() {
        return menuPromotionId;
    }

    public void setMenuPromotionId(Integer menuPromotionId) {
        this.menuPromotionId = menuPromotionId;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getSalesPromotion() {
        return salesPromotion;
    }

    public void setSalesPromotion(String salesPromotion) {
        this.salesPromotion = salesPromotion;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}