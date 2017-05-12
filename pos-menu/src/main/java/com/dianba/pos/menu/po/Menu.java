package com.dianba.pos.menu.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MENU")
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 19, scale = 0)
    private Integer id;
    /**
     * 菜名
     */
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
    /**
     * 价格
     */
    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private Double price;
    /**
     * 图片路径
     */
    @Column(name = "IMAGE", nullable = true, length = 100)
    private String image;

    @Column(name = "TYPE_ID")
    private Integer typeId;
    /**
     * 商家id
     */
    //private MerchantEntity merchant;
    @Column(name = "MERCHANT_ID", nullable = false, precision = 19, scale = 0)
    private Integer merchantId;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", nullable = true, precision = 10, scale = 0)
    private Integer createTime;
    /**
     * 销量
     */
    @Column(name = "BUY_COUNT", nullable = false, precision = 19, scale = 0)
    private Integer buyCount;
    /**
     * 介绍
     */
    @Column(name = "INTRO")
    private String intro;

    @Column(name = "PRINT_TYPE", nullable = false, precision = 19, scale = 0)
    private Integer printType;
    /**
     * 是否下架: Y: 用户端展示, N 用户端不展示
     */
    @Column(name = "DISPLAY")
    private String display;
    /**
     * 库存
     */
    @Column(name = "REPERTORY", nullable = false)
    private Integer repertory;
    /**
     * 当天库存
     */
    @Column(name = "TODAY_REPERTORY", nullable = false)
    private Integer todayRepertory;
    /**
     * 抢购开始时间
     */
    @Column(name = "begin_time", nullable = true)
    private Integer beginTime;
    /**
     * 抢购结束时间
     */
    @Column(name = "end_time", nullable = true)
    private Integer endTime;
    /**
     * 每天限量抢购数量
     */
    @Column(name = "limit_today", nullable = true)
    private Integer limitToday;
    /**
     * 是否支持线上，1：只支持线上，2：只支持线下，3：同时支持线上线下
     */
    @Column(name = "is_delete")
    private String isDelete;

    @Column(name = "MENU_SORT")
    private String menuSort;

    @Column(name = "PRICE_ONLINE", nullable = false)
    private Double priceOnline;

    /**
     * 是否支持线上，1：只支持线上，2：只支持线下，3：同时支持线上线下
     */
    @Column(name = "ISONLINE", nullable = false)
    private Integer isonline;

    @Column(name = "UNIT", nullable = false)
    private String unit;
    /**
     * 条形码
     */
    @Column(name = "BARCODE")
    private String barcode;

    @Column(name = "SYNC_TIME", nullable = false)
    private Integer syncTime;

    @Column(name = "UNIT_ID", nullable = false)
    private Integer unitId;

    @Column(name = "IS_SYNC", nullable = false)
    private Integer isSync;

    @Column(name = "is_flash")
    private Integer isFlash;

    @Column(name = "flash_price")
    private Double flashPrice;

    @Column(name = "detail_text")
    private String detailText;

    @Column(name = "agree_count")
    private Integer agreeCount;

    @Column(name = "ORIGINAL_PRICE")
    private Double originalPrice;
    /**
     * 预警库存.
     */
    @Column(name = "warn_inventory")
    private Integer warnInventory;
    /**
     * 标准库存
     */
    @Column(name = "standard_inventory")
    private Integer standardInventory;

    @Column(name = "update_time")
    private Integer updateTime = (int) System.currentTimeMillis() / 1000;
    /**
     * 生产日期
     */
    @Column(name = "production_date")
    private Integer productionDate;

    /**
     * 保质期-天
     */
    @Column(name = "shelf_life")
    private Integer shelfLife;

    @Column(name = "menu_key")
    private Integer menuKey;

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
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Integer getRepertory() {
        if (repertory == null) {
            repertory = 0;
        }
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public Integer getTodayRepertory() {
        if (todayRepertory == null) {
            todayRepertory = 0;
        }
        return todayRepertory;
    }

    public void setTodayRepertory(Integer todayRepertory) {
        this.todayRepertory = todayRepertory;
    }

    public Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Integer beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getLimitToday() {
        return limitToday;
    }

    public void setLimitToday(Integer limitToday) {
        this.limitToday = limitToday;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(String menuSort) {
        this.menuSort = menuSort;
    }

    public Double getPriceOnline() {
        return priceOnline;
    }

    public void setPriceOnline(Double priceOnline) {
        this.priceOnline = priceOnline;
    }

    public Integer getIsonline() {
        return isonline;
    }

    public void setIsonline(Integer isonline) {
        this.isonline = isonline;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Integer syncTime) {
        this.syncTime = syncTime;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getIsSync() {
        return isSync;
    }

    public void setIsSync(Integer isSync) {
        this.isSync = isSync;
    }

    public Integer getIsFlash() {
        return isFlash;
    }

    public void setIsFlash(Integer isFlash) {
        this.isFlash = isFlash;
    }

    public Double getFlashPrice() {
        return flashPrice;
    }

    public void setFlashPrice(Double flashPrice) {
        this.flashPrice = flashPrice;
    }

    public String getDetailText() {
        return detailText;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText;
    }

    public Integer getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(Integer agreeCount) {
        this.agreeCount = agreeCount;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getWarnInventory() {
        return warnInventory;
    }

    public void setWarnInventory(Integer warnInventory) {
        this.warnInventory = warnInventory;
    }

    public Integer getStandardInventory() {
        return standardInventory;
    }

    public void setStandardInventory(Integer standardInventory) {
        this.standardInventory = standardInventory;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Integer productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Integer getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(Integer menuKey) {
        this.menuKey = menuKey;
    }
}
