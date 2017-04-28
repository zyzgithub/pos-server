package com.dianba.pos.menu.po;

public class OrderMenu {
    private Long id;

    private Long orderId;

    private Long menuId;

    private Integer quantity;

    private Double price;

    private Double totalPrice;

    private String state;

    private Double promotionMoney;

    private String salesPromotion;

    private Integer menuPromotionId;

    private Double originalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        this.state = state == null ? null : state.trim();
    }

    public Double getPromotionMoney() {
        return promotionMoney;
    }

    public void setPromotionMoney(Double promotionMoney) {
        this.promotionMoney = promotionMoney;
    }

    public String getSalesPromotion() {
        return salesPromotion;
    }

    public void setSalesPromotion(String salesPromotion) {
        this.salesPromotion = salesPromotion == null ? null : salesPromotion.trim();
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
}