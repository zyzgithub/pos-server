package com.dianba.supplychain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

/**
 * 勿改属性，json转发
 */
public class Items {

    private Integer id;
    private String name;
    private String image;
    private Integer stock;
    //采购价格
    private BigDecimal price;
    private String unit;
    private Integer standard;
    private Integer defaultPurchase;

    //进价毛利率
    private String buyRate;
    //售价毛利率
    private String saleRate;

    //市场价
    @JsonIgnore
    private BigDecimal retailPrice;
    //最低采购数量
    @JsonIgnore
    private Integer minSales;

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
        if (stock == null) {
            stock = 0;
        }
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

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
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

    public Integer getMinSales() {
        return minSales == null ? 0 : minSales;
    }

    public void setMinSales(Integer minSales) {
        this.minSales = minSales;
    }

    public String getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(String buyRate) {
        this.buyRate = buyRate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }
}
