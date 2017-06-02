package com.dianba.pos.supplychain.po;


import com.xlibao.common.GlobalAppointmentOptEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "life_saas_supplychain.supplychain_item")
public class LifeSupplyChainItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "item_template_id")
    private Long itemTemplateId;
    @Column(name = "warehouse_id")
    private Long warehouseId;
    @Column(name = "stock")
    private Integer stock = 0;
    @Column(name = "batches_code")
    private String batchesCode;
    @Column(name = "minimum_sell_count")
    private Integer minimumSellCount = 1;
    @Column(name = "status")
    private Byte status = GlobalAppointmentOptEnum.LOGIC_FALSE.getKey();
    @Column(name = "sell_price")
    private Long sellPrice = 0L;
    @Column(name = "market_price")
    private Long marketPrice = 0L;
    @Column(name = "restriction_quantity")
    private Integer restrictionQuantity;

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

    public Integer getStock() {
        if (stock == null) {
            stock = 0;
        }
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getBatchesCode() {
        return batchesCode;
    }

    public void setBatchesCode(String batchesCode) {
        this.batchesCode = batchesCode;
    }

    public Integer getMinimumSellCount() {
        return minimumSellCount;
    }

    public void setMinimumSellCount(Integer minimumSellCount) {
        this.minimumSellCount = minimumSellCount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getSellPrice() {
        if (sellPrice == null) {
            sellPrice = 0L;
        }
        return sellPrice;
    }

    public void setSellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getMarketPrice() {
        if (marketPrice == null) {
            marketPrice = 0L;
        }
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getRestrictionQuantity() {
        return restrictionQuantity;
    }

    public void setRestrictionQuantity(Integer restrictionQuantity) {
        this.restrictionQuantity = restrictionQuantity;
    }
}
