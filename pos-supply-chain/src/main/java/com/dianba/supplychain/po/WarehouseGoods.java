package com.dianba.supplychain.po;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "supply_chain_warehouse_goods")
public class WarehouseGoods implements Serializable {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "goods_id")
    private Integer goodsId;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "inventory")
    private Integer inventory;

    @Column(name = "inventory_alarm")
    private Integer inventoryAlarm;

    @Column(name = "wait_for_delivery")
    private Integer waitForDelivery = 0;

    @Column(name = "safety_threshold")
    private Integer safetyThreshold;

    @Column(name = "oversold_number")
    private Integer oversoldNumber = 0;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "status")
    private Byte status;

    @Column(name = "is_delete")
    private Byte isDelete;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "market_price")
    private Double marketPrice;

    @Column(name = "retail_price")
    private Double retailPrice;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "discount_limit")
    private Integer discountLimit;

    @Column(name = "normal_limit")
    private Integer normalLimit;

    @Column(name = "delivery_delay")
    private Integer deliveryDelay;

    @Column(name = "sales")
    private Integer sales;
    /**
     * 最低采购量
     */
    @Column(name = "min_sales")
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
