package com.dianba.pos.item.vo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;


/**
 * Created by zhangyong on 2017/5/26.
 */

public class PosItemVo {


    private Long id;

    //商品名字

    private String itemName;


    //商品入库码

    private String barcode;


    //商品类型id
    private Long posTypeId;


    private String posTypeName;

    private Long itemTypeId;

    //商品单位id
    private Long itemUnitId;

    private String itemUnitName;

    //商品对应模板id
    private Long itemTemplateId;
    //商品商家id

    private Long passportId;
    /**
     * 商品图片
     **/
    private String itemImg;

    private String createDate;


    private Long generatedDate;

    /**商品销售量**/
    private Integer buyCount;

    /**
     * 商品说明
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;


    /**商品上下架**/
    private String isShelve;


    /**商品是否删除**/
    private String isDelete;

    /**库存**/
    private Integer repertory;

    /**预警库存**/
    private Integer warningRepertory;

    /**
     * 商品保质期（天）
     **/

    private Integer shelfLife;

    /**
     * 原价
     **/

    private BigDecimal stockPrice;
    /**
     * 销售价格
     **/

    private BigDecimal salesPrice;


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


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }


    public String getDescription() {
        return description==null?"":description;
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


    public Integer getWarningRepertory() {
        return warningRepertory;
    }

    public void setWarningRepertory(Integer warningRepertory) {
        this.warningRepertory = warningRepertory;
    }


    public Integer getShelfLife() {
        return shelfLife==null?0:shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }





    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }


    public String getPosTypeName() {
        return posTypeName;
    }

    public void setPosTypeName(String posTypeName) {
        this.posTypeName = posTypeName;
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

    public Long getPassportId() {
        return passportId;
    }


    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }


    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Long getGeneratedDate() {
        return generatedDate==null?0:generatedDate;
    }

    public void setGeneratedDate(Long generatedDate) {
        this.generatedDate = generatedDate;
    }
}
