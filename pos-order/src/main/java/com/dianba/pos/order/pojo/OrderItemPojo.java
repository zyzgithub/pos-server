package com.dianba.pos.order.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemPojo implements Serializable{

    //商品ID
    private long itemId;
    //商品模板ID
    private Long itemTemplateId;
    //商品名称
    private String itemName;
    //商品类型ID
    private Long itemTypeId;
    //商品类型名称
    private String itemTypeName;
    //商品单位ID
    private Long itemTypeUnitId;
    //商品单位名称
    private String itemTypeUnitName;
    //商品条码
    private String itemBarcode;
    //商品成本价
    private BigDecimal costPrice;
    //商品售价
    private BigDecimal totalPrice;
    //商品购买数量
    private Integer normalQuantity;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(Long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public Long getItemTypeUnitId() {
        return itemTypeUnitId;
    }

    public void setItemTypeUnitId(Long itemTypeUnitId) {
        this.itemTypeUnitId = itemTypeUnitId;
    }

    public String getItemTypeUnitName() {
        return itemTypeUnitName;
    }

    public void setItemTypeUnitName(String itemTypeUnitName) {
        this.itemTypeUnitName = itemTypeUnitName;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getNormalQuantity() {
        return normalQuantity;
    }

    public void setNormalQuantity(Integer normalQuantity) {
        this.normalQuantity = normalQuantity;
    }
}
