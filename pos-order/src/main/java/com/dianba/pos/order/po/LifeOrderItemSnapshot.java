package com.dianba.pos.order.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "life_order.order_item_snapshot")
@DynamicInsert
@DynamicUpdate
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
    private Integer normalQuantity = 0;
    @Column(name = "normal_price")
    private BigDecimal normalPrice = BigDecimal.ZERO;
    @Column(name = "cost_price")
    private BigDecimal costPrice = BigDecimal.ZERO;
    @Column(name = "market_price")
    private BigDecimal marketPrice = BigDecimal.ZERO;
    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;
    @Column(name = "return_price")
    private BigDecimal returnPrice = BigDecimal.ZERO;

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

    public BigDecimal getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(BigDecimal normalPrice) {
        this.normalPrice = normalPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getReturnPrice() {
        if (returnPrice == null) {
            returnPrice = BigDecimal.ZERO;
        }
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }
}
