package com.dianba.pos.menu.vo;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by zhangyong on 2017/5/26.
 */
public class PosItemVo {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //商品名字
    private String itemName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //商品入库码
    private String barcode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //商品类型id
    private Long posTypeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String posTypeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //商品单位id
    private Long itemUnitId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemUnitName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //商品对应模板id
    private Long itemTemplateId;
    //商品商家id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long passportId;
    /**商品图片**/
    private String item_img;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**商品销售量**/
    private Integer buyCount;

    /**商品说明**/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**商品上下架**/
    private String isShelve;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**商品是否删除**/
    private String isDelete;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**库存**/
    private Integer repertory;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**预警库存**/

    private Integer warningRepertory;

    /**商品保质期（天）**/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer shelfLife;

    /**原价**/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double stockPrice;
    /**销售价格**/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double salesPrice;

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

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
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


    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }
}
