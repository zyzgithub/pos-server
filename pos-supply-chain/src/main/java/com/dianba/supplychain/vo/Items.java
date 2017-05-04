package com.dianba.supplychain.vo;

import java.math.BigDecimal;

/**
 * 勿改属性，json转发
 */
public class Items {

    private Integer id;
    private String name;
    private String image;
    private Integer stock;
    private BigDecimal price;
    private String unit;
    private Integer standard;
    private Integer defaultPurchase;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    public Integer getDefaultPurchase() {
        return defaultPurchase;
    }

    public void setDefaultPurchase(Integer defaultPurchase) {
        this.defaultPurchase = defaultPurchase;
    }
}
