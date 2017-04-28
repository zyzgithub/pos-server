package com.dianba.pos.menu.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MENU")
public class Menu implements Serializable{

    @Id

    @Column(name = "ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="price")
    private Double price;

    @Column(name = "image")
    private String image;

    private Long typeId;

    private Long merchantId;

    private Integer createTime;

    private Long buyCount;

    private String intro;

    private Long printType;

    private String display;

    private Integer repertory;

    private Integer todayRepertory;

    private Integer beginTime;

    private Integer endTime;

    private Integer limitToday;

    private String isDelete;

    private Integer menuSort;

    private Double priceOnline;

    private Byte isonline;

    private String unit;

    private String barcode;

    private Integer syncTime;

    private Integer unitId;

    private Byte isSync;

    private Byte isFlash;

    private Double flashPrice;

    private Integer agreeCount;

    private Double originalPrice;

    private Integer warnInventory;

    private Integer standardInventory;

    private Integer updateTime;

    private Integer productionDate;

    private Integer shelfLife;

    private String detailText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
        this.image = image == null ? null : image.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Long buyCount) {
        this.buyCount = buyCount;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    public Long getPrintType() {
        return printType;
    }

    public void setPrintType(Long printType) {
        this.printType = printType;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display == null ? null : display.trim();
    }

    public Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(Integer repertory) {
        this.repertory = repertory;
    }

    public Integer getTodayRepertory() {
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
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public Double getPriceOnline() {
        return priceOnline;
    }

    public void setPriceOnline(Double priceOnline) {
        this.priceOnline = priceOnline;
    }

    public Byte getIsonline() {
        return isonline;
    }

    public void setIsonline(Byte isonline) {
        this.isonline = isonline;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
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

    public Byte getIsSync() {
        return isSync;
    }

    public void setIsSync(Byte isSync) {
        this.isSync = isSync;
    }

    public Byte getIsFlash() {
        return isFlash;
    }

    public void setIsFlash(Byte isFlash) {
        this.isFlash = isFlash;
    }

    public Double getFlashPrice() {
        return flashPrice;
    }

    public void setFlashPrice(Double flashPrice) {
        this.flashPrice = flashPrice;
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

    public String getDetailText() {
        return detailText;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText == null ? null : detailText.trim();
    }
}