package com.dianba.pos.supplychain.po;


import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "supply_chain_goods")
public class Goods implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "market_price")
    private Double marketPrice;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "img")
    private String img;

    @Column(name = "description")
    private String description;

    @Column(name = "goods_banner")
    private String goodsBanner;

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
        this.name = name == null ? null : name.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getGoodsBanner() {
        return goodsBanner;
    }

    public void setGoodsBanner(String goodsBanner) {
        this.goodsBanner = goodsBanner;
    }

    public String getFormatName(WarehouseGoods item) {
        String formatName = name;
        if (item.getDeliveryDelay() == 1 && !name.contains("次日达")) {
            formatName = "(次日达)" + formatName;
        } else if (item.getDeliveryDelay() > 1 && !name.contains("日达")) {
            formatName = "(" + item.getDeliveryDelay() + "日达)" + formatName;
        }
        if (item.getDiscountLimit() == -2) {
            formatName = "(特价)" + formatName;
        }
        if (item.getDiscountLimit() != 0 && item.getDiscountLimit() != -2) {
            formatName = "(促销)" + formatName;
        }
        if ((item.getCreateTime().getTime() + TimeUnit.DAYS.toMillis(7)) > System.currentTimeMillis()) {
            formatName = "【新】" + formatName;
        }
        return formatName;
    }

    @Override
    public String toString() {
        return "SupplyChainGoods [id=" + id + ", name=" + name + ", productId=" + productId + ", barcode=" + barcode
                + ", typeId=" + typeId + ", typeName=" + typeName + ", unitId=" + unitId + ", marketPrice="
                + marketPrice + ", costPrice=" + costPrice + ", img=" + img + ", description=" + description + "]";
    }
}
