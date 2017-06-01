package com.dianba.pos.item.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangyong on 2017/5/24.
 */

@Entity
@Table(name = "life_item.item_template")
public class LifeItemTemplate implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "define_code")
    private String defineCode;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "barcode")
    private String barcode;
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "unit_id")
    private Long unitId;

    /**商品成本价**/
    @Column(name = "cost_price")
    private Long costPrice;

    /**商品零售价**/
    @Column(name ="default_price")
    private Long defaultPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "introduction_page")
    private String introductionPage;

    @Column(name = "status")
    private Integer status;

    @Column(name = "description")
    private String description;

    @Column(name = "uploaders_passport_id")
    private Long uploadersPassportId;

    @Column(name = "upload_time")
    private Date uploadTime;

    @Column(name = "ascription_type")
    private Integer ascriptionType;
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
        this.name = name;
    }

    public String getDefineCode() {
        return defineCode;
    }

    public void setDefineCode(String defineCode) {
        this.defineCode = defineCode;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long costPrice) {
        this.costPrice = costPrice;
    }

    public Long getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Long defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getIntroductionPage() {
        return introductionPage;
    }

    public void setIntroductionPage(String introductionPage) {
        this.introductionPage = introductionPage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUploadersPassportId() {
        return uploadersPassportId;
    }

    public void setUploadersPassportId(Long uploadersPassportId) {
        this.uploadersPassportId = uploadersPassportId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getAscriptionType() {
        return ascriptionType;
    }

    public void setAscriptionType(Integer ascriptionType) {
        this.ascriptionType = ascriptionType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
