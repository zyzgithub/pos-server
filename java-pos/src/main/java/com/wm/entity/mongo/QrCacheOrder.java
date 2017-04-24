package com.wm.entity.mongo;

import java.math.BigDecimal;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "qc_cache_order")
public class QrCacheOrder {
    @Id public String id;

    @Indexed public Long order_id;
    
    private String payId;

    private String payType;

    private Integer userId;

    private Long courierId;

    private Long cityId;

    private String cardId;

    private String status;

    private String state;

    private String rstate;

    private String rereason;

    private Integer retime;

    private String realname;

    private String mobile;

    private String address;

    private BigDecimal onlineMoney;

    private BigDecimal origin;

    private BigDecimal credit;

    private BigDecimal card;

    private String remark;

    private Integer createTime;

    private Integer payTime;

    private String commentContent;

    private String commentDisplay;

    private BigDecimal commentTaste;

    private BigDecimal commentSpeed;

    private BigDecimal commentService;

    private BigDecimal commentCourier;

    private Integer commentTime;

    private Integer merchantId;

    private BigDecimal scoreMoney;

    private Integer score;

    private String orderType;

    private Integer accessTime;

    private Integer deliveryTime;

    private Integer completeTime;

    private Integer urgentTime;

    private String title;

    private String ifcourier;

    private Integer deliveryDoneTime;

    private String payState;

    private Integer saleType;

    private String orderNum;

    private String outTraceId;

    private String timeRemark;

    private Integer cookDoneTime;

    private String cookDoneCode;

    private Integer startTime;

    private String commentCourierContent;

    private Integer startSendTime;

    private Integer endSendTime;

    private Integer userAddressId;

    private String invoice;

    private String fromType;

    private BigDecimal deliveryFee;

    private BigDecimal costLunchBox;

    private BigDecimal memberDiscountMoney;

    private BigDecimal merchantMemberDiscountMoney;

    private BigDecimal dineInDiscountMoney;

    private Integer rechargeSrc;//充值来源 0-正常充值 1-快递员推荐充值

    private Long inviteId;//邀请人ID

    private Integer agentId;//代理商ID

    private String isMerchantDelivery;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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
        this.cardId = cardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRstate() {
        return rstate;
    }

    public void setRstate(String rstate) {
        this.rstate = rstate;
    }

    public String getRereason() {
        return rereason;
    }

    public void setRereason(String rereason) {
        this.rereason = rereason;
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
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getOnlineMoney() {
        return onlineMoney;
    }

    public void setOnlineMoney(BigDecimal onlineMoney) {
        this.onlineMoney = onlineMoney;
    }

    public BigDecimal getOrigin() {
        return origin;
    }

    public void setOrigin(BigDecimal origin) {
        this.origin = origin;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getCard() {
        return card;
    }

    public void setCard(BigDecimal card) {
        this.card = card;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentDisplay() {
        return commentDisplay;
    }

    public void setCommentDisplay(String commentDisplay) {
        this.commentDisplay = commentDisplay;
    }

    public BigDecimal getCommentTaste() {
        return commentTaste;
    }

    public void setCommentTaste(BigDecimal commentTaste) {
        this.commentTaste = commentTaste;
    }

    public BigDecimal getCommentSpeed() {
        return commentSpeed;
    }

    public void setCommentSpeed(BigDecimal commentSpeed) {
        this.commentSpeed = commentSpeed;
    }

    public BigDecimal getCommentService() {
        return commentService;
    }

    public void setCommentService(BigDecimal commentService) {
        this.commentService = commentService;
    }

    public BigDecimal getCommentCourier() {
        return commentCourier;
    }

    public void setCommentCourier(BigDecimal commentCourier) {
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

    public BigDecimal getScoreMoney() {
        return scoreMoney;
    }

    public void setScoreMoney(BigDecimal scoreMoney) {
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
        this.orderType = orderType;
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
        this.title = title;
    }

    public String getIfcourier() {
        return ifcourier;
    }

    public void setIfcourier(String ifcourier) {
        this.ifcourier = ifcourier;
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
        this.payState = payState;
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
        this.orderNum = orderNum;
    }

    public String getOutTraceId() {
        return outTraceId;
    }

    public void setOutTraceId(String outTraceId) {
        this.outTraceId = outTraceId;
    }

    public String getTimeRemark() {
        return timeRemark;
    }

    public void setTimeRemark(String timeRemark) {
        this.timeRemark = timeRemark;
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
        this.cookDoneCode = cookDoneCode;
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
        this.commentCourierContent = commentCourierContent;
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
        this.invoice = invoice;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getCostLunchBox() {
        return costLunchBox;
    }

    public void setCostLunchBox(BigDecimal costLunchBox) {
        this.costLunchBox = costLunchBox;
    }

    public BigDecimal getMemberDiscountMoney() {
        return memberDiscountMoney;
    }

    public void setMemberDiscountMoney(BigDecimal memberDiscountMoney) {
        this.memberDiscountMoney = memberDiscountMoney;
    }

    public BigDecimal getMerchantMemberDiscountMoney() {
        return merchantMemberDiscountMoney;
    }

    public void setMerchantMemberDiscountMoney(BigDecimal merchantMemberDiscountMoney) {
        this.merchantMemberDiscountMoney = merchantMemberDiscountMoney;
    }

    public BigDecimal getDineInDiscountMoney() {
        return dineInDiscountMoney;
    }

    public void setDineInDiscountMoney(BigDecimal dineInDiscountMoney) {
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
        this.isMerchantDelivery = isMerchantDelivery;
    }
    
    
}
