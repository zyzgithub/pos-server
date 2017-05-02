package com.dianba.supplychain.po;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "supply_chain_warehouse_goods")
public class WarehouseGoods implements Serializable{

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer goodsId;
    private Integer warehouseId;

    private Integer inventory;
    private Integer inventoryAlarm;
    private Integer waitForDelivery = 0;
    private Integer safetyThreshold;
    private Integer oversoldNumber = 0;

    private Timestamp createTime;

    private Byte status;
    private Byte isDelete;

    private Double costPrice;
    private Double marketPrice;
    private Double retailPrice;
    private Double discountPrice;

    private Integer discountLimit;
    private Integer normalLimit;
    private Integer deliveryDelay;

    private Integer sales;
    /**
     * 最低采购量
     */
    private Integer minSales;

    /**
     * 检查实体除id以及不能被随意修改的成员以外的成员是否都为null
     *
     * @return
     */
    public boolean isEmpty() {
        return inventory == null && inventoryAlarm == null && status == null && isDelete == null && marketPrice == null
                && costPrice == null && retailPrice == null && discountPrice == null && discountLimit == null
                && normalLimit == null && deliveryDelay == null && sales == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getwarehouseId() {
        return warehouseId;
    }

    public void setwarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getInventory() {
        return (inventory == null ? 0 : inventory) + (oversoldNumber == null ? 0 : oversoldNumber)
                - ((waitForDelivery == null || waitForDelivery < 0) ? 0 : waitForDelivery);
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory == null ? 0 : inventory;
    }

    public int getTotalInventory() {
        // int totalInventory = (inventory == null ? 0 : inventory.intValue()) +
        // (oversoldNumber == null ? 0 : oversoldNumber.intValue());
        // return totalInventory;
        return inventory;
    }

    public Integer getInventoryAlarm() {
        return inventoryAlarm;
    }

    public void setInventoryAlarm(Integer inventoryAlarm) {
        this.inventoryAlarm = inventoryAlarm;
    }

    public Integer getSafetyThreshold() {
        return safetyThreshold;
    }

    public void setSafetyThreshold(Integer safetyThreshold) {
        this.safetyThreshold = safetyThreshold;
    }

    public Integer getOversoldNumber() {
        return oversoldNumber;
    }

    public void setOversoldNumber(Integer oversoldNumber) {
        this.oversoldNumber = oversoldNumber == null ? 0 : oversoldNumber;
    }

    public Integer getWaitForDelivery() {
        return (waitForDelivery == null || waitForDelivery < 0) ? 0 : waitForDelivery;
    }

    public void setWaitForDelivery(Integer waitForDelivery) {
        this.waitForDelivery = waitForDelivery == null ? 0 : waitForDelivery;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountLimit() {
        return discountLimit;
    }

    public void setDiscountLimit(Integer discountLimit) {
        this.discountLimit = discountLimit;
    }

    public Integer getNormalLimit() {
        return normalLimit;
    }

    public void setNormalLimit(Integer normalLimit) {
        this.normalLimit = normalLimit;
    }

    public Integer getDeliveryDelay() {
        return deliveryDelay;
    }

    public void setDeliveryDelay(Integer deliveryDelay) {
        this.deliveryDelay = deliveryDelay;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getMinSales() {
        return minSales == null ? 0 : minSales;
    }

    public void setMinSales(Integer minSales) {
        this.minSales = minSales;
    }
}