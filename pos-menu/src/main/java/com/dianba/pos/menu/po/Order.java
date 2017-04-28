package com.dianba.pos.menu.po;

import javax.persistence.*;

@Entity
@Table(name="order")
public class Order {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="pay_id")
    private String payId;

    @Column(name="pay_type")
    private String payType;

    @Column(name="user_id")
    private Long userId;

    @Column(name="courier_id")
    private Long courierId;


    @Column(name = "city_id")
    private Long cityId;


    @Column(name = "card_id")
    private String cardId;

    @Column(name="status")
    private String status;

    @Column(name="state")
    private String state;

    @Column(name = "retate")
    private String rstate;

    @Column(name = "retime")
    private Integer retime;

    @Column(name="realname")
    private String realname;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address")
    private String address;

    @Column(name = "onlineMoney")
    private Double onlineMoney;

    @Column(name = "origin")
    private Double origin;

    @Column(name = "credit")
    private Double credit;

    @Column(name = "card")
    private Double card;

    @Column(name = "createTime")
    private Integer createTime;

    @Column(name = "payTime")
    private Integer payTime;

    @Column(name = "payTime")
    private String commentDisplay;

    @Column(name = "commentTaste")
    private Float commentTaste;

    @Column(name = "commentSpeed")
    private Float commentSpeed;

    @Column(name = "commentService")
    private Float commentService;

    @Column(name = "commentCourier")
    private Float commentCourier;

    @Column(name = "commentTime")
    private Integer commentTime;

    @Column(name = "merchantId")
    private Integer merchantId;

    @Column(name = "scoreMoney")
    private Double scoreMoney;

    @Column(name = "score")
    private Integer score;

    @Column(name = "orderType")
    private String orderType;

    @Column(name = "accessTime")
    private Integer accessTime;

    @Column(name = "deliveryTime")
    private Integer deliveryTime;

    @Column(name = "completeTime")
    private Integer completeTime;

    @Column(name = "urgentTime")
    private Integer urgentTime;

    @Column(name = "title")
    private String title;

    @Column(name = "ifcourier")
    private String ifcourier;

    @Column(name = "deliveryDoneTime")
    private Integer deliveryDoneTime;

    @Column(name = "payState")
    private String payState;

    @Column(name = "saleType")
    private Integer saleType;

    @Column(name = "orderNum")
    private String orderNum;

    @Column(name = "outTraceId")
    private String outTraceId;

    @Column(name = "timeRemark")
    private String timeRemark;

    @Column(name = "cookDoneTime")
    private Integer cookDoneTime;

    @Column(name = "cookDoneCode")
    private String cookDoneCode;

    @Column(name = "startTime")
    private Integer startTime;

    @Column(name = "commentCourierContent")
    private String commentCourierContent;

    @Column(name = "startSendTime")
    private Integer startSendTime;

    @Column(name = "endSendTime")
    private Integer endSendTime;

    @Column(name = "userAddressId")
    private Integer userAddressId;

    @Column(name = "invoice")
    private String invoice;

    @Column(name = "fromType")
    private String fromType;

    @Column(name = "deliveryFee")
    private Double deliveryFee;

    @Column(name = "costLunchBox")
    private Double costLunchBox;

    @Column(name = "memberDiscountMoney")
    private Double memberDiscountMoney;

    @Column(name = "merchantMemberDiscountMoney")
    private Double merchantMemberDiscountMoney;

    @Column(name = "dineInDiscountMoney")
    private Double dineInDiscountMoney;

    @Column(name = "rechargeSrc")
    private Integer rechargeSrc;

    @Column(name = "inviteId")
    private Long inviteId;

    @Column(name = "agentId")
    private Integer agentId;

    @Column(name = "isMerchantDelivery")
    private String isMerchantDelivery;

    @Column(name = "lwkId")
    private String lwkId;

    @Column(name = "lwkMark")
    private String lwkMark;

    @Column(name = "lwkFlag")
    private String lwkFlag;

    @Column(name = "flashOrderId")
    private Long flashOrderId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId == null ? null : payId.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getRstate() {
        return rstate;
    }

    public void setRstate(String rstate) {
        this.rstate = rstate == null ? null : rstate.trim();
    }

    public Integer getRetime() {
        return retime;
    }

    public void setRetime(Integer retime) {
        this.retime = retime;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Double getOnlineMoney() {
        return onlineMoney;
    }

    public void setOnlineMoney(Double onlineMoney) {
        this.onlineMoney = onlineMoney;
    }

    public Double getOrigin() {
        return origin;
    }

    public void setOrigin(Double origin) {
        this.origin = origin;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getCard() {
        return card;
    }

    public void setCard(Double card) {
        this.card = card;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getPayTime() {
        return payTime;
    }

    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    public String getCommentDisplay() {
        return commentDisplay;
    }

    public void setCommentDisplay(String commentDisplay) {
        this.commentDisplay = commentDisplay == null ? null : commentDisplay.trim();
    }

    public Float getCommentTaste() {
        return commentTaste;
    }

    public void setCommentTaste(Float commentTaste) {
        this.commentTaste = commentTaste;
    }

    public Float getCommentSpeed() {
        return commentSpeed;
    }

    public void setCommentSpeed(Float commentSpeed) {
        this.commentSpeed = commentSpeed;
    }

    public Float getCommentService() {
        return commentService;
    }

    public void setCommentService(Float commentService) {
        this.commentService = commentService;
    }

    public Float getCommentCourier() {
        return commentCourier;
    }

    public void setCommentCourier(Float commentCourier) {
        this.commentCourier = commentCourier;
    }

    public Integer getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Integer commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Double getScoreMoney() {
        return scoreMoney;
    }

    public void setScoreMoney(Double scoreMoney) {
        this.scoreMoney = scoreMoney;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public Integer getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Integer accessTime) {
        this.accessTime = accessTime;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Integer completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getUrgentTime() {
        return urgentTime;
    }

    public void setUrgentTime(Integer urgentTime) {
        this.urgentTime = urgentTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getIfcourier() {
        return ifcourier;
    }

    public void setIfcourier(String ifcourier) {
        this.ifcourier = ifcourier == null ? null : ifcourier.trim();
    }

    public Integer getDeliveryDoneTime() {
        return deliveryDoneTime;
    }

    public void setDeliveryDoneTime(Integer deliveryDoneTime) {
        this.deliveryDoneTime = deliveryDoneTime;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState == null ? null : payState.trim();
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public String getOutTraceId() {
        return outTraceId;
    }

    public void setOutTraceId(String outTraceId) {
        this.outTraceId = outTraceId == null ? null : outTraceId.trim();
    }

    public String getTimeRemark() {
        return timeRemark;
    }

    public void setTimeRemark(String timeRemark) {
        this.timeRemark = timeRemark == null ? null : timeRemark.trim();
    }

    public Integer getCookDoneTime() {
        return cookDoneTime;
    }

    public void setCookDoneTime(Integer cookDoneTime) {
        this.cookDoneTime = cookDoneTime;
    }

    public String getCookDoneCode() {
        return cookDoneCode;
    }

    public void setCookDoneCode(String cookDoneCode) {
        this.cookDoneCode = cookDoneCode == null ? null : cookDoneCode.trim();
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public String getCommentCourierContent() {
        return commentCourierContent;
    }

    public void setCommentCourierContent(String commentCourierContent) {
        this.commentCourierContent = commentCourierContent == null ? null : commentCourierContent.trim();
    }

    public Integer getStartSendTime() {
        return startSendTime;
    }

    public void setStartSendTime(Integer startSendTime) {
        this.startSendTime = startSendTime;
    }

    public Integer getEndSendTime() {
        return endSendTime;
    }

    public void setEndSendTime(Integer endSendTime) {
        this.endSendTime = endSendTime;
    }

    public Integer getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Integer userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice == null ? null : invoice.trim();
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType == null ? null : fromType.trim();
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getCostLunchBox() {
        return costLunchBox;
    }

    public void setCostLunchBox(Double costLunchBox) {
        this.costLunchBox = costLunchBox;
    }

    public Double getMemberDiscountMoney() {
        return memberDiscountMoney;
    }

    public void setMemberDiscountMoney(Double memberDiscountMoney) {
        this.memberDiscountMoney = memberDiscountMoney;
    }

    public Double getMerchantMemberDiscountMoney() {
        return merchantMemberDiscountMoney;
    }

    public void setMerchantMemberDiscountMoney(Double merchantMemberDiscountMoney) {
        this.merchantMemberDiscountMoney = merchantMemberDiscountMoney;
    }

    public Double getDineInDiscountMoney() {
        return dineInDiscountMoney;
    }

    public void setDineInDiscountMoney(Double dineInDiscountMoney) {
        this.dineInDiscountMoney = dineInDiscountMoney;
    }

    public Integer getRechargeSrc() {
        return rechargeSrc;
    }

    public void setRechargeSrc(Integer rechargeSrc) {
        this.rechargeSrc = rechargeSrc;
    }

    public Long getInviteId() {
        return inviteId;
    }

    public void setInviteId(Long inviteId) {
        this.inviteId = inviteId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getIsMerchantDelivery() {
        return isMerchantDelivery;
    }

    public void setIsMerchantDelivery(String isMerchantDelivery) {
        this.isMerchantDelivery = isMerchantDelivery == null ? null : isMerchantDelivery.trim();
    }

    public String getLwkId() {
        return lwkId;
    }

    public void setLwkId(String lwkId) {
        this.lwkId = lwkId == null ? null : lwkId.trim();
    }

    public String getLwkMark() {
        return lwkMark;
    }

    public void setLwkMark(String lwkMark) {
        this.lwkMark = lwkMark == null ? null : lwkMark.trim();
    }

    public String getLwkFlag() {
        return lwkFlag;
    }

    public void setLwkFlag(String lwkFlag) {
        this.lwkFlag = lwkFlag == null ? null : lwkFlag.trim();
    }

    public Long getFlashOrderId() {
        return flashOrderId;
    }

    public void setFlashOrderId(Long flashOrderId) {
        this.flashOrderId = flashOrderId;
    }
}