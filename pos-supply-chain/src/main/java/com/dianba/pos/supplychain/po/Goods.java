package com.dianba.pos.supplychain.po;


import com.xlibao.common.GlobalAppointmentOptEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "life_saas_supplychain.supplychain_item")
public class Goods implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "item_template_id")
    private Long itemTemplateId;
    @Column(name = "warehouse_id")
    private Long warehouseId;
    private Integer productBatches = 0;
    private String batchesCode = "";
    private Integer stock = 0;
    private Integer pendingQuantity = 0;
    private Integer warningQuantity = 0;
    private Integer keepQuantity = 0;
    private Integer oversoldQuantity = 0;
    @Column(name = "minimum_sell_count")
    private Integer minimumSellCount = 1;
    private Integer allocationQuantity = 0;
    private Integer purchaseQuantity = 0;
    private Byte status = GlobalAppointmentOptEnum.LOGIC_FALSE.getKey();
    @Column(name = "cost_price")
    private Long costPrice = 0L;
    @Column(name = "sell_price")
    private Long sellPrice = 0L;
    @Column(name = "market_price")
    private Long marketPrice = 0L;
    @Column(name = "discount_price")
    private Long discountPrice = 0L;
    @Column(name = "discount_type")
    private Byte deliveryDelay = (byte) 0;
    private Integer initialSales = 0;
    private Integer actualSales = 0;
    private Long totalStorage = 0L;
    private Long totalOutStorage = 0L;
    private String description;

    private long relationItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(Long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getProductBatches() {
        return productBatches;
    }

    public void setProductBatches(Integer productBatches) {
        this.productBatches = productBatches;
    }

    public String getBatchesCode() {
        return batchesCode;
    }

    public void setBatchesCode(String batchesCode) {
        this.batchesCode = batchesCode;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(Integer pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public Integer getWarningQuantity() {
        return warningQuantity;
    }

    public void setWarningQuantity(Integer warningQuantity) {
        this.warningQuantity = warningQuantity;
    }

    public Integer getKeepQuantity() {
        return keepQuantity;
    }

    public void setKeepQuantity(Integer keepQuantity) {
        this.keepQuantity = keepQuantity;
    }

    public Integer getOversoldQuantity() {
        return oversoldQuantity;
    }

    public void setOversoldQuantity(Integer oversoldQuantity) {
        this.oversoldQuantity = oversoldQuantity;
    }

    public Integer getMinimumSellCount() {
        return minimumSellCount;
    }

    public void setMinimumSellCount(Integer minimumSellCount) {
        this.minimumSellCount = minimumSellCount;
    }

    public Integer getAllocationQuantity() {
        return allocationQuantity;
    }

    public void setAllocationQuantity(Integer allocationQuantity) {
        this.allocationQuantity = allocationQuantity;
    }

    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long costPrice) {
        this.costPrice = costPrice;
    }

    public Long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Byte getDeliveryDelay() {
        return deliveryDelay;
    }

    public void setDeliveryDelay(Byte deliveryDelay) {
        this.deliveryDelay = deliveryDelay;
    }

    public Integer getInitialSales() {
        return initialSales;
    }

    public void setInitialSales(Integer initialSales) {
        this.initialSales = initialSales;
    }

    public Integer getActualSales() {
        return actualSales;
    }

    public void setActualSales(Integer actualSales) {
        this.actualSales = actualSales;
    }

    public Long getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(Long totalStorage) {
        this.totalStorage = totalStorage;
    }

    public Long getTotalOutStorage() {
        return totalOutStorage;
    }

    public void setTotalOutStorage(Long totalOutStorage) {
        this.totalOutStorage = totalOutStorage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRelationItemId() {
        return relationItemId;
    }

    public void setRelationItemId(long relationItemId) {
        this.relationItemId = relationItemId;
    }
}
