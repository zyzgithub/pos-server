package com.dianba.pos.passport.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "life_achieve.achieve")
public class LifeAchieve implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "global_id")
    private Long globalId;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "modify_date")
    private Date modifyDate;
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "achieve_plat_form_id")
    private Long achievePlatFormId;
    @Column(name = "show_name")
    private String showName;
    @Column(name = "phone_number")
    private Long phoneNumber;
    @Column(name = "province")
    private Integer province;
    @Column(name = "city")
    private Integer city;
    @Column(name = "address")
    private String address;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "contract_num")
    private String contractNum;
    @Column(name = "permit_no")
    private String permitNo;
    @Column(name = "permit_img")
    private String permitImg;
    @Column(name = "businesslicense_no")
    private String businesslicenseNo;
    @Column(name = "businesslicense_img")
    private String businesslicenseImg;
    @Column(name = "auditor_type")
    private Integer auditorType;
    @Column(name = "auditor_name")
    private String auditorName;
    @Column(name = "auditor_id")
    private Long auditorId;
    @Column(name = "audit_opinion")
    private String auditOpinion;
    @Column(name = "achieve_type")
    private Integer achieveType;
    @Column(name = "achieve_type_id")
    private Long achieveTypeId;
    @Column(name = "item_type_parent_id")
    private String itemTypeParentId;
    @Column(name = "item_type_id")
    private String itemTypeId;
    @Column(name = "operation_id")
    private Long operationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Long getAchievePlatFormId() {
        return achievePlatFormId;
    }

    public void setAchievePlatFormId(Long achievePlatFormId) {
        this.achievePlatFormId = achievePlatFormId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getPermitNo() {
        return permitNo;
    }

    public void setPermitNo(String permitNo) {
        this.permitNo = permitNo;
    }

    public String getPermitImg() {
        return permitImg;
    }

    public void setPermitImg(String permitImg) {
        this.permitImg = permitImg;
    }

    public String getBusinesslicenseNo() {
        return businesslicenseNo;
    }

    public void setBusinesslicenseNo(String businesslicenseNo) {
        this.businesslicenseNo = businesslicenseNo;
    }

    public String getBusinesslicenseImg() {
        return businesslicenseImg;
    }

    public void setBusinesslicenseImg(String businesslicenseImg) {
        this.businesslicenseImg = businesslicenseImg;
    }

    public Integer getAuditorType() {
        return auditorType;
    }

    public void setAuditorType(Integer auditorType) {
        this.auditorType = auditorType;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public Integer getAchieveType() {
        return achieveType;
    }

    public void setAchieveType(Integer achieveType) {
        this.achieveType = achieveType;
    }

    public Long getAchieveTypeId() {
        return achieveTypeId;
    }

    public void setAchieveTypeId(Long achieveTypeId) {
        this.achieveTypeId = achieveTypeId;
    }

    public String getItemTypeParentId() {
        return itemTypeParentId;
    }

    public void setItemTypeParentId(String itemTypeParentId) {
        this.itemTypeParentId = itemTypeParentId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
}
