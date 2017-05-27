package com.dianba.pos.menu.vo;


/**
 * Created by zhangyong on 2017/5/26.
 */
public class PosItemVo {

    private Long id;

    private String itemName;

    private String barcode;
    private Long posTypeId;

    private Long itemTemplateId;

    private String createDate;

    /**商品销售量**/
    private Integer buyCount;

    /**商品说明**/
    private String description;

    /**商品上下架**/
    private String isShelve;

    /**商品是否删除**/
    private String isDelete;

    /**库存**/
    private Integer repertory;

    /**预警库存**/

    private Integer warningRepertory;

    /**商品保质期（天）**/
    private Integer shelfLife;
    /**原价**/

    private Long stockPrice;
    /**销售价格**/

    private Long salesPrice;

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

    public Long getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Long stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Long getSalesPrice() {
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
