package com.dianba.pos.order.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ORDER")
public class Order implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer orderId; //订单id

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "CITY_ID")
    private Long cityId;

    private Long courierId;

    private String cardId;

    private String status;

    private String state;

    private String rstate;

    private Integer retime;

    private String realname;

    private String mobile;

    private String address;

    private Double onlineMoney;

    private Double origin;

    private Double credit;

    private Integer payTime;

    private String commentDisplay;

    private Float commentTaste;

    private Float commentSpeed;

    private Float commentService;

    private Float commentCourier;

    private Integer commentTime;

    private Integer merchantId;

    private Integer score;

    private Integer accessTime;

    private Integer deliveryTime;

    private Integer urgentTime;

    private String title;

    private String ifcourier;

    private Integer deliveryDoneTime;

    private String payState;

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

    private Integer rechargeSrc;

    private Long inviteId;

    private Integer agentId;

    private String isMerchantDelivery;

    private String lwkId;

    private String lwkMark;

    private String lwkFlag;

    private Long flashOrderId;



    @Transient
    private String payId; //pay_id
    @Transient
    private double totalOrigin; //订单原价总金额
    @Transient
    private double totalPrice; //订单总金额
    @Transient
    private String totalDiscount; //订单总金额
    @Transient
    private Integer totalCount; //商品总数量
    @Transient
    private String payType;//支付类型
    @Transient
    private String remark;//备注
    @Transient
    private String payTypeName; //支付类型名称
    @Transient
    private String completeTime; //完成时间
    @Transient
    private String merchantName; //商店名称
    @Transient
    private Double change = 0.00; //找零
    @Transient
    private double actuallyPaid = 0.00;    //实收
    @Transient
    private List<OrderMenu> menuList; //商品明细
    @Transient
    private double memberDiscountMoney = 0.00;//会员优惠
    @Transient
    private double deliveryFee = 0.00;//配送费
    @Transient
    private double costLunchBox = 0.00;//餐盒费
    @Transient
    private double minusDiscountMoney = 0.00;//立减优惠
    @Transient
    private double dineInDiscountMoney = 0.00;//立减优惠
    @Transient
    private String createTime;
    /**
     * 订单类型,
     */
    @Transient
    private String orderType;
    /**
     * 抵金卷金额
     */
    @Transient
    private String card;
    /**
     * 积分抵扣金额
     */
    @Transient
    private String scoreMoney;
    /**
     * 订单类型: '1.为外卖订单，2为堂食订单',
     */
    @Transient
    private String saleType;
    /**
     * 订单排号
     */
    @Transient
    private String orderNum;

    @Transient
    private String merchantAddress;
    @Transient
    private String merchantMobile;
    @Transient
    private String platform = "一号生活";
    @Transient
    private String guestPhone = "4008-945-917";


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public List<OrderMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<OrderMenu> menuList) {
        this.menuList = menuList;
    }

    public double getTotalOrigin() {
        return totalOrigin;
    }

    public void setTotalOrigin(double totalOrigin) {
        this.totalOrigin = totalOrigin;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public double getActuallyPaid() {
        return actuallyPaid;
    }

    public void setActuallyPaid(double actuallyPaid) {
        this.actuallyPaid = actuallyPaid;
    }

    public double getMemberDiscountMoney() {
        return memberDiscountMoney;
    }

    public void setMemberDiscountMoney(double memberDiscountMoney) {
        this.memberDiscountMoney = memberDiscountMoney;
    }

    public double getMinusDiscountMoney() {
        return minusDiscountMoney;
    }

    public void setMinusDiscountMoney(double minusDiscountMoney) {
        this.minusDiscountMoney = minusDiscountMoney;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getScoreMoney() {
        return scoreMoney;
    }

    public void setScoreMoney(String scoreMoney) {
        this.scoreMoney = scoreMoney;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getMerchantMobile() {
        return merchantMobile;
    }

    public void setMerchantMobile(String merchantMobile) {
        this.merchantMobile = merchantMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getDineInDiscountMoney() {
        return dineInDiscountMoney;
    }

    public void setDineInDiscountMoney(double dineInDiscountMoney) {
        this.dineInDiscountMoney = dineInDiscountMoney;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getCostLunchBox() {
        return costLunchBox;
    }

    public void setCostLunchBox(double costLunchBox) {
        this.costLunchBox = costLunchBox;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
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
        this.commentDisplay = commentDisplay;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public String getLwkId() {
        return lwkId;
    }

    public void setLwkId(String lwkId) {
        this.lwkId = lwkId;
    }

    public String getLwkMark() {
        return lwkMark;
    }

    public void setLwkMark(String lwkMark) {
        this.lwkMark = lwkMark;
    }

    public String getLwkFlag() {
        return lwkFlag;
    }

    public void setLwkFlag(String lwkFlag) {
        this.lwkFlag = lwkFlag;
    }

    public Long getFlashOrderId() {
        return flashOrderId;
    }

    public void setFlashOrderId(Long flashOrderId) {
        this.flashOrderId = flashOrderId;
    }

    @Override
    public String toString() {
        return "OrderFromSuperMarketDTO{" +
                "orderId=" + orderId +
                ", payId='" + payId + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalCount=" + totalCount +
                ", payType='" + payType + '\'' +
                ", payTypeName='" + payTypeName + '\'' +
                ", completeTime='" + completeTime + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", change=" + change +
                ", actuallyPaid=" + actuallyPaid +
                ", menuList=" + menuList +
                ", memberDiscountMoney=" + memberDiscountMoney +
                ", minusDiscountMoney=" + minusDiscountMoney +
                ", createTime='" + createTime + '\'' +
                ", orderType='" + orderType + '\'' +
                ", card='" + card + '\'' +
                ", scoreMoney='" + scoreMoney + '\'' +
                ", saleType='" + saleType + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", merchantAddress='" + merchantAddress + '\'' +
                ", merchantMobile='" + merchantMobile + '\'' +
                ", platform='" + platform + '\'' +
                ", guestPhone='" + guestPhone + '\'' +
                '}';
    }
}