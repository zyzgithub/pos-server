package com.dianba.pos.item.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Entity
@Table(name = "pos_item", schema = "life_pos")
public class PosItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_type_id")
    private Long itemTypeId;

    @Column(name = "pos_type_id")
    private Long posTypeId;

    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "item_template_id")
    private Long itemTemplateId;

    @Column(name = "item_img_url")
    private String itemImgUrl;
    @Column(name = "create_time")
    private String createTime;

    @Column(name = "generated_date")
    private Long generatedDate;
    /**
     * 商品销售量
     **/
    @Column(name = "buy_count")
    private Integer buyCount;

    /**
     * 商品说明
     **/
    @Column(name = "description")
    private String description;

    /**
     * 商品上下架
     **/
    @Column(name = "is_shelve")
    private String isShelve;

    /**
     * 商品是否删除
     **/
    @Column(name = "is_delete")
    private String isDelete;

    /**
     * 库存
     **/
    @Column(name = "repertory")
    private Integer repertory;

    /**
     * 预警库存
     **/
    @Column(name = "warning_repertory")
    private Integer warningRepertory;

    /**
     * 商品保质期（天）
     **/
    @Column(name = "shelf_life")
    private Integer shelfLife;
    /**
     * 原价
     **/
    @Column(name = "stock_price")
    private Long stockPrice;
    /**
     * 销售价格
     **/
    @Column(name = "sales_price")
    private Long salesPrice;

    @Column(name = "barcode")
    private String barcode;
    @Column(name = "code_id")
    private Integer codeId;

    @Column(name = "menu_key")
    private String menuKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getPosTypeId() {
        return posTypeId;
    }

    public void setPosTypeId(Long posTypeId) {
        this.posTypeId = posTypeId;
    }

    public Long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(Long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }


    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsShelve() {
        return isShelve;
    }

    public void setIsShelve(String isShelve) {
        this.isShelve = isShelve;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }


    public Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Integer getWarningRepertory() {
        return warningRepertory;
    }

    public void setWarningRepertory(Integer warningRepertory) {
        this.warningRepertory = warningRepertory;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getStockPrice() {
        if (stockPrice <= 0) {
            stockPrice = getSalesPrice();
        }
        return stockPrice;
    }

    public void setStockPrice(Long stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Long getSalesPrice() {
        if (salesPrice <= 0) {
            salesPrice = 1L;
        }
        return salesPrice;
    }

    public void setSalesPrice(Long salesPrice) {
        this.salesPrice = salesPrice;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Long generatedDate) {
        this.generatedDate = generatedDate;
    }
}

