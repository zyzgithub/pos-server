package com.dianba.pos.menu.po;

import java.math.BigDecimal;
import java.util.Date;

//商家注册信息表
public class Merchant {
    private Long id;

    private Long userId;

    private String title;

    private Long groupId;

    private Long cityId;

    private String bankName;

    private String bankNo;

    private String bankUser;

    private String address;

    private String contact;

    private String phone;

    private String location;

    private String mobile;

    private Integer createTime;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String display;

    private String notice;

    private Integer startTime;

    private Integer endTime;

    private Integer deliveryTime;

    private String businessLicense;

    private String operatingPermit;

    private String printCode;

    private Double cardMoney;

    private String cardActivity;

    private Double biddingMoney;

    private String logoUrl;

    private String promotion;

    private Double costDelivery;

    private String type;

    private Double deliveryBegin;

    private Double deduction;

    private Integer orderNum;

    private Integer incomeDate;

    private String dineOrderPrint;

    private Date noticeTime;

    private Byte isDelete;

    private Date mobileUpdateTime;

    private Date deliveryBeginUpdateTime;

    private Integer alipayLimit;

    private Integer wechatLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getBankUser() {
        return bankUser;
    }

    public void setBankUser(String bankUser) {
        this.bankUser = bankUser == null ? null : bankUser.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display == null ? null : display.trim();
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense == null ? null : businessLicense.trim();
    }

    public String getOperatingPermit() {
        return operatingPermit;
    }

    public void setOperatingPermit(String operatingPermit) {
        this.operatingPermit = operatingPermit == null ? null : operatingPermit.trim();
    }

    public String getPrintCode() {
        return printCode;
    }

    public void setPrintCode(String printCode) {
        this.printCode = printCode == null ? null : printCode.trim();
    }

    public Double getCardMoney() {
        return cardMoney;
    }

    public void setCardMoney(Double cardMoney) {
        this.cardMoney = cardMoney;
    }

    public String getCardActivity() {
        return cardActivity;
    }

    public void setCardActivity(String cardActivity) {
        this.cardActivity = cardActivity == null ? null : cardActivity.trim();
    }

    public Double getBiddingMoney() {
        return biddingMoney;
    }

    public void setBiddingMoney(Double biddingMoney) {
        this.biddingMoney = biddingMoney;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion == null ? null : promotion.trim();
    }

    public Double getCostDelivery() {
        return costDelivery;
    }

    public void setCostDelivery(Double costDelivery) {
        this.costDelivery = costDelivery;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Double getDeliveryBegin() {
        return deliveryBegin;
    }

    public void setDeliveryBegin(Double deliveryBegin) {
        this.deliveryBegin = deliveryBegin;
    }

    public Double getDeduction() {
        return deduction;
    }

    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(Integer incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getDineOrderPrint() {
        return dineOrderPrint;
    }

    public void setDineOrderPrint(String dineOrderPrint) {
        this.dineOrderPrint = dineOrderPrint == null ? null : dineOrderPrint.trim();
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getMobileUpdateTime() {
        return mobileUpdateTime;
    }

    public void setMobileUpdateTime(Date mobileUpdateTime) {
        this.mobileUpdateTime = mobileUpdateTime;
    }

    public Date getDeliveryBeginUpdateTime() {
        return deliveryBeginUpdateTime;
    }

    public void setDeliveryBeginUpdateTime(Date deliveryBeginUpdateTime) {
        this.deliveryBeginUpdateTime = deliveryBeginUpdateTime;
    }

    public Integer getAlipayLimit() {
        return alipayLimit;
    }

    public void setAlipayLimit(Integer alipayLimit) {
        this.alipayLimit = alipayLimit;
    }

    public Integer getWechatLimit() {
        return wechatLimit;
    }

    public void setWechatLimit(Integer wechatLimit) {
        this.wechatLimit = wechatLimit;
    }
}