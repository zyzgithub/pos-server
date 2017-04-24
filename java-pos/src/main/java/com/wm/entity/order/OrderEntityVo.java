package com.wm.entity.order;

/**
 * @Title: Entity
 * @Description: order
 * @author wuyong
 * @date 2015-01-07 10:00:57
 * @version V1.0
 *
 */
public class OrderEntityVo {
	/** id */
	private java.lang.Integer id;
	/** payId */
	private java.lang.String payId;
	/** payType */
	private java.lang.String payType;
	/** userId */
	private java.lang.Integer userId;
	/** courierId */
	private java.lang.Integer courierId;
	/** 城市ID */
	private java.lang.Integer cityId;
	/** 代金券id */
	private java.lang.String cardId;
	/** state */
	private java.lang.String state;
	/** rstate */
	private java.lang.String rstate;
	/** rereason */
	private java.lang.String rereason;
	/** retime */
	private java.lang.Integer retime;
	/** 名字 */
	private java.lang.String realname;
	/** 手机 */
	private java.lang.String mobile;
	/** 地址 */
	private java.lang.String address;
	/** 在线支付金额 */
	private java.lang.Double onlineMoney;
	/** 订单金额 */
	private java.lang.Double origin;
	/** 余额支付金额 */
	private java.lang.Double credit;
	/** 代金券金额 */
	private java.lang.Double card;
	/** remark */
	private java.lang.String remark;
	/** createTime */
	private java.lang.Integer createTime;
	/** payTime */
	private java.lang.Integer payTime;
	/** commentContent */
	private java.lang.String commentContent;
	/** commentDisplay */
	private java.lang.String commentDisplay;
	/** 评价-口味 */
	private java.lang.Float commentTaste;
	/** 评价速度 */
	private java.lang.Float commentSpeed;
	/** 评价-服务 */
	private java.lang.Float commentService;
	/** commentTime */
	private java.lang.Integer commentTime;
	/** 商家id */
	private java.lang.Integer merchantId;
	/** 使用积分抵用金额 */
	private java.lang.Double scoreMoney;
	/** 抵用积分 */
	private java.lang.Integer score;
	/** 是否电话订单 */
	private java.lang.String orderType;
	/** 商家接收订单时间 */
	private java.lang.Integer accessTime;
	/** 开始配送时间 */
	private java.lang.Integer deliveryTime;
	/** 订单完成时间 */
	private java.lang.Integer completeTime;

	private java.lang.Integer urgentTime;
	/** 订单描述 */
	private java.lang.String title;

	private java.lang.String ifCourier;

	private java.lang.String status;

	/** 销售方式,1:外卖，2堂食 */
	private java.lang.Integer saleType;

	private java.lang.Integer deliveryDoneTime;

	private java.lang.String payState;
	// 配送时间备注
	private java.lang.String timeRemark;
	// 厨房制作完成时间
	private java.lang.Integer cookDoneTime;
	// 订单制作状态
	private java.lang.String cookDoneCode;
	// 厨房开始制作时间
	private java.lang.Integer startTime;

	// 下单时间 useless
	private String createDate;
	// 支付时间 useless
	private String payDate;

	// 排号:每天清空
	private String orderNum;
	// 第三方交易流水号:支付宝等
	private String outTraceId;
	// 快递员评价
	private java.lang.Double commenTcourier;

	private Integer addrId;

	private String invoice;

	private String fromType;
	
	private String completeStartTime;
	
	private String completeEndTime;
	

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getPayId() {
		return payId;
	}

	public void setPayId(java.lang.String payId) {
		this.payId = payId;
	}

	public java.lang.String getPayType() {
		return payType;
	}

	public void setPayType(java.lang.String payType) {
		this.payType = payType;
	}

	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	public java.lang.Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(java.lang.Integer courierId) {
		this.courierId = courierId;
	}

	public java.lang.Integer getCityId() {
		return cityId;
	}

	public void setCityId(java.lang.Integer cityId) {
		this.cityId = cityId;
	}

	public java.lang.String getCardId() {
		return cardId;
	}

	public void setCardId(java.lang.String cardId) {
		this.cardId = cardId;
	}

	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	public java.lang.String getRstate() {
		return rstate;
	}

	public void setRstate(java.lang.String rstate) {
		this.rstate = rstate;
	}

	public java.lang.String getRereason() {
		return rereason;
	}

	public void setRereason(java.lang.String rereason) {
		this.rereason = rereason;
	}

	public java.lang.Integer getRetime() {
		return retime;
	}

	public void setRetime(java.lang.Integer retime) {
		this.retime = retime;
	}

	public java.lang.String getRealname() {
		return realname;
	}

	public void setRealname(java.lang.String realname) {
		this.realname = realname;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.Double getOnlineMoney() {
		return onlineMoney;
	}

	public void setOnlineMoney(java.lang.Double onlineMoney) {
		this.onlineMoney = onlineMoney;
	}

	public java.lang.Double getOrigin() {
		return origin;
	}

	public void setOrigin(java.lang.Double origin) {
		this.origin = origin;
	}

	public java.lang.Double getCredit() {
		return credit;
	}

	public void setCredit(java.lang.Double credit) {
		this.credit = credit;
	}

	public java.lang.Double getCard() {
		return card;
	}

	public void setCard(java.lang.Double card) {
		this.card = card;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public java.lang.Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}

	public java.lang.Integer getPayTime() {
		return payTime;
	}

	public void setPayTime(java.lang.Integer payTime) {
		this.payTime = payTime;
	}

	public java.lang.String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(java.lang.String commentContent) {
		this.commentContent = commentContent;
	}

	public java.lang.String getCommentDisplay() {
		return commentDisplay;
	}

	public void setCommentDisplay(java.lang.String commentDisplay) {
		this.commentDisplay = commentDisplay;
	}

	public java.lang.Float getCommentTaste() {
		return commentTaste;
	}

	public void setCommentTaste(java.lang.Float commentTaste) {
		this.commentTaste = commentTaste;
	}

	public java.lang.Float getCommentSpeed() {
		return commentSpeed;
	}

	public void setCommentSpeed(java.lang.Float commentSpeed) {
		this.commentSpeed = commentSpeed;
	}

	public java.lang.Float getCommentService() {
		return commentService;
	}

	public void setCommentService(java.lang.Float commentService) {
		this.commentService = commentService;
	}

	public java.lang.Integer getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(java.lang.Integer commentTime) {
		this.commentTime = commentTime;
	}

	public java.lang.Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(java.lang.Integer merchantId) {
		this.merchantId = merchantId;
	}

	public java.lang.Double getScoreMoney() {
		return scoreMoney;
	}

	public void setScoreMoney(java.lang.Double scoreMoney) {
		this.scoreMoney = scoreMoney;
	}

	public java.lang.Integer getScore() {
		return score;
	}

	public void setScore(java.lang.Integer score) {
		this.score = score;
	}

	public java.lang.String getOrderType() {
		return orderType;
	}

	public void setOrderType(java.lang.String orderType) {
		this.orderType = orderType;
	}

	public java.lang.Integer getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(java.lang.Integer accessTime) {
		this.accessTime = accessTime;
	}

	public java.lang.Integer getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(java.lang.Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public java.lang.Integer getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(java.lang.Integer completeTime) {
		this.completeTime = completeTime;
	}

	public java.lang.Integer getUrgentTime() {
		return urgentTime;
	}

	public void setUrgentTime(java.lang.Integer urgentTime) {
		this.urgentTime = urgentTime;
	}

	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getIfCourier() {
		return ifCourier;
	}

	public void setIfCourier(java.lang.String ifCourier) {
		this.ifCourier = ifCourier;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public java.lang.Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(java.lang.Integer saleType) {
		this.saleType = saleType;
	}

	public java.lang.Integer getDeliveryDoneTime() {
		return deliveryDoneTime;
	}

	public void setDeliveryDoneTime(java.lang.Integer deliveryDoneTime) {
		this.deliveryDoneTime = deliveryDoneTime;
	}

	public java.lang.String getPayState() {
		return payState;
	}

	public void setPayState(java.lang.String payState) {
		this.payState = payState;
	}

	public java.lang.String getTimeRemark() {
		return timeRemark;
	}

	public void setTimeRemark(java.lang.String timeRemark) {
		this.timeRemark = timeRemark;
	}

	public java.lang.Integer getCookDoneTime() {
		return cookDoneTime;
	}

	public void setCookDoneTime(java.lang.Integer cookDoneTime) {
		this.cookDoneTime = cookDoneTime;
	}

	public java.lang.String getCookDoneCode() {
		return cookDoneCode;
	}

	public void setCookDoneCode(java.lang.String cookDoneCode) {
		this.cookDoneCode = cookDoneCode;
	}

	public java.lang.Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(java.lang.Integer startTime) {
		this.startTime = startTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
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

	public java.lang.Double getCommenTcourier() {
		return commenTcourier;
	}

	public void setCommenTcourier(java.lang.Double commenTcourier) {
		this.commenTcourier = commenTcourier;
	}

	public Integer getAddrId() {
		return addrId;
	}

	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
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

	public String getCompleteStartTime() {
		return completeStartTime;
	}

	public void setCompleteStartTime(String completeStartTime) {
		this.completeStartTime = completeStartTime;
	}

	public String getCompleteEndTime() {
		return completeEndTime;
	}

	public void setCompleteEndTime(String completeEndTime) {
		this.completeEndTime = completeEndTime;
	}
}
