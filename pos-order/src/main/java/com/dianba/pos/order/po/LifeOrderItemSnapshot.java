package com.dianba.pos.order.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "life_order.order_item_snapshot")
public class LifeOrderItemSnapshot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "user_mark")
    private String userMark;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "item_template_id")
    private Long itemTemplateId;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_type_id")
    private Long itemTypeId;
    @Column(name = "item_type_name")
    private String itemTypeName;
    @Column(name = "item_unit_id")
    private Long itemUnitId;
    @Column(name = "item_unit_name")
    private String itemUnitName;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "item_code")
    private String itemCode;
    @Column(name = "item_batches")
    private Integer itemBatches;
    @Column(name = "introduction_page")
    private String introductionPage;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "normal_quantity")
    private Integer normalQuantity = Integer.valueOf(0);
    @Column(name = "normal_price")
    private Long normalPrice = Long.valueOf(0L);
    @Column(name = "cost_price")
    private Long costPrice = Long.valueOf(0L);
    @Column(name = "market_price")
    private Long marketPrice = Long.valueOf(0L);
    @Column(name = "total_price")
    private Long totalPrice = Long.valueOf(0L);
    @Column(name = "return_price")
    private Long returnPrice = Long.valueOf(0L);

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

    public String getUserMark() {
        return userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
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

    public Long getItemUnitId() {
        return itemUnitId;
    }

    public void setItemUnitId(Long itemUnitId) {
        this.itemUnitId = itemUnitId;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getItemBatches() {
        return itemBatches;
    }

    public void setItemBatches(Integer itemBatches) {
        this.itemBatches = itemBatches;
    }

    public String getIntroductionPage() {
        return introductionPage;
    }

    public void setIntroductionPage(String introductionPage) {
        this.introductionPage = introductionPage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNormalQuantity() {
        return normalQuantity;
    }

    public void setNormalQuantity(Integer normalQuantity) {
        this.normalQuantity = normalQuantity;
    }

    public Long getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(Long normalPrice) {
        this.normalPrice = normalPrice;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long costPrice) {
        this.costPrice = costPrice;
    }

    public Long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(Long returnPrice) {
        this.returnPrice = returnPrice;
    }
}
