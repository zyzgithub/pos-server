package com.dianba.pos.merchant.po;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//商家注册信息表
@Entity
@Table(name = "merchant")
public class Merchant implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_no")
    private String bankNo;

    @Column(name = "bank_user")
    private String bankUser;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "phone")
    private String phone;

    @Column(name = "location")
    private String location;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "create_time")
    private Integer createTime;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "display")
    private String display;

    @Column(name = "notice")
    private String notice;

    @Column(name = "start_time")
    private Integer startTime;

    @Column(name = "end_time")
    private Integer endTime;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    @Column(name = "business_license")
    private String businessLicense;

    @Column(name = "operating_permit")
    private String operatingPermit;

    @Column(name = "print_code")
    private String printCode;

    @Column(name = "card_money")
    private Double cardMoney;

    @Column(name = "card_activity")
    private String cardActivity;

    @Column(name = "bidding_money")
    private Double biddingMoney;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "promotion")
    private String promotion;

    @Column(name = "cost_delivery")
    private Double costDelivery;

    @Column(name = "type")
    private String type;

    @Column(name = "delivery_begin")
    private Double deliveryBegin;

    @Column(name = "deduction")
    private Double deduction;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "income_date")
    private Integer incomeDate;

    @Column(name = "dine_order_print")
    private String dineOrderPrint;

    @Column(name = "notice_time")
    private Date noticeTime;

    @Column(name = "is_delete")
    private Byte isDelete;

    @Column(name = "mobile_update_time")
    private Date mobileUpdateTime;

    @Column(name = "delivery_begin_update_time")
    private Date deliveryBeginUpdateTime;

    @Column(name = "alipay_limit")
    private Integer alipayLimit;

    @Column(name = "wechat_limit")
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
