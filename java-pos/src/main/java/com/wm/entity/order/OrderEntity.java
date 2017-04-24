package com.wm.entity.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jeecgframework.core.util.DateUtils;

import com.base.VO.GoodsVO;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: order
 * @author wuyong
 * @date 2015-01-07 10:00:57
 * @version V1.0   
 *
 */
@Entity
@Table(name = "`order`", schema = "")
@SuppressWarnings("serial")
public class OrderEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**payId*/
	private java.lang.String payId;
	/**payType*/
	private java.lang.String payType;
	/**userId*/
	private WUserEntity wuser;
	/**courierId*/
	private java.lang.Integer courierId;
	/**城市ID*/
	private java.lang.Integer cityId;
	/**代金券id*/
	private java.lang.String cardId;
	/**state*/
	private java.lang.String state = "unpay";
	/**rstate*/
	private java.lang.String rstate = "normal";
	/**rereason*/
	private java.lang.String rereason;
	/**retime*/
	private java.lang.Integer retime = 0;
	/**名字*/
	private java.lang.String realname;
	/**手机*/
	private java.lang.String mobile;
	/**地址*/
	private java.lang.String address;
	/**在线支付金额*/
	private java.lang.Double onlineMoney = 0.0;
	/**订单金额*/
	private java.lang.Double origin = 0.0;
	/**余额支付金额*/
	private java.lang.Double credit = 0.0;
	/**代金券金额*/
	private java.lang.Double card = 0.0;
	/**remark*/
	private java.lang.String remark;
	/**createTime*/
	private java.lang.Integer createTime = DateUtils.getSeconds();
	/**payTime*/
	private java.lang.Integer payTime = 0;
	/**commentContent*/
	private java.lang.String commentContent;
	/**commentDisplay*/
	private java.lang.String commentDisplay = "Y";
	/**评价-口味*/
	private java.lang.Float  commentTaste = 0.0f;
	/**评价速度*/
	private java.lang.Float commentSpeed = 0.0f;
	/**评价-服务*/
	private java.lang.Float commentService = 0.0f;
	/**commentTime*/
	private java.lang.Integer commentTime = 0;
	/**商家id*/
	private MerchantEntity merchant;
	/**使用积分抵用金额*/
	private java.lang.Double scoreMoney = 0.0;
	/**抵用积分*/
	private java.lang.Integer score = 0;
	/**是否电话订单*/
	private java.lang.String orderType = "normal";
	/**商家接收订单时间*/
	private java.lang.Integer accessTime = 0;
	/**开始配送时间*/
	private java.lang.Integer deliveryTime = 0;
	/**订单完成时间*/
	private java.lang.Integer completeTime = 0;
	//加急时间
	private java.lang.Integer urgentTime = 0;
	/**订单描述*/
	private java.lang.String title;
	//是否快递服务
	private java.lang.String ifCourier;
	//预收入状态
	private java.lang.String status = "N";
	
	/**销售方式,1:外卖，2堂食*/
	private java.lang.Integer saleType=1;
	
	private List<GoodsVO> menuList = new ArrayList<GoodsVO>();
	//配送完成时间
	private java.lang.Integer deliveryDoneTime = 0;
	//支付状态
	private java.lang.String payState = "unpay";
	//配送时间备注
	private java.lang.String timeRemark;
	//厨房制作完成时间
	private java.lang.Integer cookDoneTime;
	//订单制作状态
	private java.lang.String cookDoneCode;
	//厨房开始制作时间
	private java.lang.Integer startTime;
	
	//下单时间 useless
	private String createDate = "";
	//支付时间 useless
	private String payDate = "";
	
	//排号:每天清空
	private String orderNum;
	//第三方交易流水号:支付宝等
	private String outTraceId;
	//快递员评价
	private java.lang.Double commenTcourier = 0.0;
	//用户收货地址
	private Integer addrId;
	//
	private String invoice;
	//订单来源
	private String fromType;
	//配送费
	private Double deliveryFee;
	//餐盒费
	private Double costLunchBox;
	//平台会员折扣减去金额
	private Double memberDiscountMoney;
	//商家会员折扣减去金额	
	private Double merchantMemberDiscountMoney;
	//堂食折扣减去金额
	private Double dineInDiscountMoney;
	//充值来源
	private String rechargeSrc;		// 0 普通用户充值 1 快递员推广充值
	private Integer inviteId;		//推广的快递员ID
	
	private String isMerchantDelivery;		//是否商家配送：merchant商家配送  courier快递员配送
	/** flash_order 的ID  */
	private Long flashOrderId;
	/** user的ID  */
	private Long userId;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  payId
	 */
	@Column(name ="PAY_ID",nullable=true,length=32)
	public java.lang.String getPayId(){
		return this.payId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  payId
	 */
	public void setPayId(java.lang.String payId){
		this.payId = payId;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  payType
	 */
	@Column(name ="PAY_TYPE",nullable=false,length=9)
	public java.lang.String getPayType(){
		return this.payType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  payType
	 */
	public void setPayType(java.lang.String payType){
		this.payType = payType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  courierId
	 */
	@Column(name ="COURIER_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  courierId
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  城市ID
	 */
	@Column(name ="CITY_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getCityId(){
		return this.cityId;
	}


	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  城市ID
	 */
	public void setCityId(java.lang.Integer cityId){
		this.cityId = cityId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  代金券id
	 */
	@Column(name ="CARD_ID",nullable=true,length=16)
	public java.lang.String getCardId(){
		return this.cardId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  代金券id
	 */
	public void setCardId(java.lang.String cardId){
		this.cardId = cardId;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  state
	 */
	@Column(name ="STATE",nullable=false,length=9)
	public java.lang.String getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  state
	 */
	public void setState(java.lang.String state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  rstate
	 */
	@Column(name ="RSTATE",nullable=false,length=9)
	public java.lang.String getRstate(){
		return this.rstate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  rstate
	 */
	public void setRstate(java.lang.String rstate){
		this.rstate = rstate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  rereason
	 */
	@Column(name ="REREASON",nullable=true,length=65535)
	public java.lang.String getRereason(){
		return this.rereason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  rereason
	 */
	public void setRereason(java.lang.String rereason){
		this.rereason = rereason;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  retime
	 */
	@Column(name ="RETIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getRetime(){
		return this.retime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  retime
	 */
	public void setRetime(java.lang.Integer retime){
		this.retime = retime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名字
	 */
	@Column(name ="REALNAME",nullable=true,length=32)
	public java.lang.String getRealname(){
		return this.realname;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名字
	 */
	public void setRealname(java.lang.String realname){
		this.realname = realname;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手机
	 */
	@Column(name ="MOBILE",nullable=true,length=128)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手机
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地址
	 */
	@Column(name ="ADDRESS",nullable=true,length=128)
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地址
	 */
	public void setAddress(java.lang.String address){
		this.address = address;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  在线支付金额
	 */
	@Column(name ="ONLINE_MONEY",nullable=false,precision=10,scale=2)
	public java.lang.Double getOnlineMoney(){
		return this.onlineMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  在线支付金额
	 */
	public void setOnlineMoney(java.lang.Double onlineMoney){
		this.onlineMoney = onlineMoney;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  订单金额
	 */
	@Column(name ="ORIGIN",nullable=false,precision=10,scale=2)
	public java.lang.Double getOrigin(){
		return this.origin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  订单金额
	 */
	public void setOrigin(java.lang.Double origin){
		this.origin = origin;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  余额支付金额
	 */
	@Column(name ="CREDIT",nullable=false,precision=10,scale=2)
	public java.lang.Double getCredit(){
		return this.credit;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  余额支付金额
	 */
	public void setCredit(java.lang.Double credit){
		this.credit = credit;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  代金券金额
	 */
	@Column(name ="CARD",nullable=false,precision=10,scale=2)
	public java.lang.Double getCard(){
		return this.card;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  代金券金额
	 */
	public void setCard(java.lang.Double card){
		this.card = card;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  remark
	 */
	@Column(name ="REMARK",nullable=true,length=65535)
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  remark
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  createTime
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  payTime
	 */
	@Column(name ="PAY_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPayTime(){
		return this.payTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  payTime
	 */
	public void setPayTime(java.lang.Integer payTime){
		this.payTime = payTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  commentContent
	 */
	@Column(name ="COMMENT_CONTENT",nullable=true,length=65535)
	public java.lang.String getCommentContent(){
		return this.commentContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  commentContent
	 */
	public void setCommentContent(java.lang.String commentContent){
		this.commentContent = commentContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  commentDisplay
	 */
	@Column(name ="COMMENT_DISPLAY",nullable=false,length=1)
	public java.lang.String getCommentDisplay(){
		return this.commentDisplay;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  commentDisplay
	 */
	public void setCommentDisplay(java.lang.String commentDisplay){
		this.commentDisplay = commentDisplay;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  评价-口味
	 */
	@Column(name ="COMMENT_TASTE",nullable=true,precision=3,scale=1)
	public java.lang.Float getCommentTaste(){
		return this.commentTaste;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  评价-口味
	 */
	public void setCommentTaste(java.lang.Float commentTaste){
		this.commentTaste = commentTaste;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  评价速度
	 */
	@Column(name ="COMMENT_SPEED",nullable=true,precision=3,scale=1)
	public java.lang.Float getCommentSpeed(){
		return this.commentSpeed;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  评价速度
	 */
	public void setCommentSpeed(java.lang.Float commentSpeed){
		this.commentSpeed = commentSpeed;
	}
	/**
	 *方法: 取得java.lang.Float
	 *@return: java.lang.Float  评价-服务
	 */
	@Column(name ="COMMENT_SERVICE",nullable=true,precision=3,scale=1)
	public java.lang.Float getCommentService(){
		return this.commentService;
	}

	/**
	 *方法: 设置java.lang.Float
	 *@param: java.lang.Float  评价-服务
	 */
	public void setCommentService(java.lang.Float commentService){
		this.commentService = commentService;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  commentTime
	 */
	@Column(name ="COMMENT_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCommentTime(){
		return this.commentTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  commentTime
	 */
	public void setCommentTime(java.lang.Integer commentTime){
		this.commentTime = commentTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家id
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MERCHANT_ID", nullable = true)
	public MerchantEntity getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantEntity merchant) {
		this.merchant = merchant;
	}
	
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  使用积分抵用金额
	 */
	@Column(name ="SCORE_MONEY",nullable=true,precision=22)
	public java.lang.Double getScoreMoney(){
		return this.scoreMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  使用积分抵用金额
	 */
	public void setScoreMoney(java.lang.Double scoreMoney){
		this.scoreMoney = scoreMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  抵用积分
	 */
	@Column(name ="SCORE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getScore(){
		return this.score;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  抵用积分
	 */
	public void setScore(java.lang.Integer score){
		this.score = score;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否电话订单
	 */
	@Column(name ="ORDER_TYPE",nullable=true,length=10)
	public java.lang.String getOrderType(){
		return this.orderType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否电话订单
	 */
	public void setOrderType(java.lang.String orderType){
		this.orderType = orderType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家接收订单时间
	 */
	@Column(name ="ACCESS_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getAccessTime(){
		return this.accessTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家接收订单时间
	 */
	public void setAccessTime(java.lang.Integer accessTime){
		this.accessTime = accessTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  开始配送时间
	 */
	@Column(name ="DELIVERY_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDeliveryTime(){
		return this.deliveryTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  开始配送时间
	 */
	public void setDeliveryTime(java.lang.Integer deliveryTime){
		this.deliveryTime = deliveryTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  订单完成时间
	 */
	@Column(name ="COMPLETE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCompleteTime(){
		return this.completeTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  订单完成时间
	 */
	public void setCompleteTime(java.lang.Integer completeTime){
		this.completeTime = completeTime;
	}
	@Column(name ="TITLE",nullable=true,length=100)
	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	@Column(name ="IFCOURIER",nullable=true,length=100)
	public java.lang.String getIfCourier() {
		return ifCourier;
	}

	public void setIfCourier(java.lang.String ifCourier) {
		this.ifCourier = ifCourier;
	}
	
	@Column(name ="URGENT_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUrgentTime(){
		return this.urgentTime;
	}

	public void setUrgentTime(java.lang.Integer urgentTime){
		this.urgentTime = urgentTime;
	}
	
	@Column(name ="STATUS",nullable=true,length=100)
	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	
	@Column(name ="sale_type")
	public java.lang.Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(java.lang.Integer saleType) {
		this.saleType = saleType;
	}
	
	@Transient
	public List<GoodsVO> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<GoodsVO> menuList) {
		this.menuList = menuList;
	}
	
	@Column(name ="DELIVERY_DONE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDeliveryDoneTime(){
		return this.deliveryDoneTime;
	}

	public void setDeliveryDoneTime(java.lang.Integer deliveryDoneTime){
		this.urgentTime = deliveryDoneTime;
	}
	
	@Column(name ="PAY_STATE",nullable=true,length=100)
	public java.lang.String getPayState() {
		return payState;
	}

	public void setPayState(java.lang.String payState) {
		this.payState = payState;
	}
	
	@Transient
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Transient
	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	@Column(name ="ORDER_NUM",nullable=true,length=100)
	public java.lang.String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(java.lang.String orderNum) {
		this.orderNum = orderNum;
	}
	
	@Column(name ="OUT_TRACE_ID",nullable=true,length=100)
	public java.lang.String getOutTraceId() {
		return outTraceId;
	}

	public void setOutTraceId(java.lang.String outTraceId) {
		this.outTraceId = outTraceId;
	}
	
	@Column(name ="TIME_REMARK",nullable=true,length=100)
	public java.lang.String getTimeRemark() {
		return timeRemark;
	}

	public void setTimeRemark(java.lang.String timeRemark) {
		this.timeRemark = timeRemark;
	}

	@Column(name ="comment_courier",nullable=true)
	public java.lang.Double getCommenTcourier() {
		return commenTcourier;
	}

	public void setCommenTcourier(java.lang.Double commenTcourier) {
		this.commenTcourier = commenTcourier;
	}
	@Column(name="cook_done_time",nullable=true)
	public java.lang.Integer getCookDoneTime() {
		return cookDoneTime;
	}

	public void setCookDoneTime(java.lang.Integer cookDoneTime) {
		this.cookDoneTime = cookDoneTime;
	}
	
	@Column(name="cook_done_code",nullable=true)
	public java.lang.String getCookDoneCode() {
		return cookDoneCode;
	}

	public void setCookDoneCode(java.lang.String cookDoneCode) {
		this.cookDoneCode = cookDoneCode;
	}
	
	@Column(name="start_time",nullable=true)
	public java.lang.Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(java.lang.Integer startTime) {
		this.startTime = startTime;
	}

	@Column(name="user_address_id", nullable=false)
	public Integer getAddrId() {
		return addrId;
	}

	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}
	
	@Column(name="invoice", nullable=true)
	public String getInvoice() {
		return invoice;
	}
	
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
	@Column(name="from_type")
	public String getFromType() {
		return fromType;
	}
	
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	
	@Column(name="delivery_fee")
	public Double getDeliveryFee() {
		return deliveryFee;
	}
	
	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	
	@Column(name="cost_lunch_box")
	public Double getCostLunchBox() {
		return costLunchBox;
	}

	public void setCostLunchBox(Double costLunchBox) {
		this.costLunchBox = costLunchBox;
	}

	@Column(name="member_discount_money")
	public Double getMemberDiscountMoney() {
		return memberDiscountMoney;
	}

	public void setMemberDiscountMoney(Double memberDiscountMoney) {
		this.memberDiscountMoney = memberDiscountMoney;
	}

	@Column(name="merchant_member_discount_money")
	public Double getMerchantMemberDiscountMoney() {
		return merchantMemberDiscountMoney;
	}

	public void setMerchantMemberDiscountMoney(Double merchantMemberDiscountMoney) {
		this.merchantMemberDiscountMoney = merchantMemberDiscountMoney;
	}

	@Column(name="dine_in_discount_money")
	public Double getDineInDiscountMoney() {
		return dineInDiscountMoney;
	}

	public void setDineInDiscountMoney(Double dineInDiscountMoney) {
		this.dineInDiscountMoney = dineInDiscountMoney;
	}

	@Column(name="recharge_src")
	public String getRechargeSrc() {
		return rechargeSrc;
	}

	public void setRechargeSrc(String rechargeSrc) {
		this.rechargeSrc = rechargeSrc;
	}

	@Column(name="invite_id")
	public Integer getInviteId() {
		return inviteId;
	}
	
	public void setInviteId(Integer inviteId) {
		this.inviteId = inviteId;
	}

	@Column(name="is_merchant_delivery")
	public String getIsMerchantDelivery() {
		return isMerchantDelivery;
	}

	public void setIsMerchantDelivery(String isMerchantDelivery) {
		this.isMerchantDelivery = isMerchantDelivery;
	}

	@Column(name="flash_order_id",nullable=true)
	public Long getFlashOrderId() {
		return flashOrderId;
	}

	public void setFlashOrderId(Long flashOrderId) {
		this.flashOrderId = flashOrderId;
	}
	
	@Column(name="USER_ID", insertable=false, updatable=false)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}



	/**
	 * 订单类型
	 */
	public static final class OrderType {
		// 电话订单
		public static final String MOBILE = "mobile";
		//
		public static final String DIRECT_PAY = "direct_pay";
		// 充值订单
		public static final String RECHARGE = "recharge";
		// 超市订单
		public static final String SUPERMARKET = "supermarket";
		// 超市现金结算订单
		public static final String SUPERMARKET_SETTLEMENT = "supermarket_settlement";
		// 超市无码收银订单
		public static final String SUPERMARKET_CODEFREE = "supermarket_codefree";
		// 普通订单
		public static final String NORMAL = "normal";
		// 第三方订单
		public static final String THIRD_PART = "third_part";
		// 扫码订单
		public static final String SCAN_ORDER = "scan_order";
		// 支付宝扫码订单
		public static final String ALI_SCAN_ORDER = "ali_scan_order";
		// 堂吃订单，如：扫桌码下单
		public static final String EAT_IN_ORDER = "eat_in_order";
		// 代理商充值订单
		public static final String AGENT_RECHARGE = "agent_recharge";
		// 商家会员充值订单
		public static final String MERCHANT_RECHARGE = "merchant_recharge";
		// 闪购订单
		public static final String FLASHSALES = "flashsales";

	}
	
	/**
	 * 支付方式
	 */
	public static final class PayType {
		/**
		 * 财付通
		 */
		public static final String TENPAY = "tenpay";
		/**
		 * 微信支付
		 */
		public static final String WEIXINPAY = "weixinpay";
		/**
		 * 银联支付
		 */
		public static final String UNIONPAY = "unionpay";
		/**
		 * 支付宝支付
		 */
		public static final String ALIPAY = "alipay";
		/**
		 * 商家会员支付
		 */
		public static final String MERCHANTPAY = "merchantpay";
		/**
		 * 威富通
		 */
		public static final String WFTPAY = "wft_pay";
		/**
		 * 平台会员支付
		 */
		public static final String BALANCE = "balance";
		/**
		 * 超市现金支付
		 */
		public static final String SUPERMARKT_CASH = "supermarkt_cash";
		/**
		 * 铂金动态二维码扫码支付
		 */
		public static final String DPT_PAY = "dpt_pay";
		
	}
	
	/**
	 * 外卖/堂食类型
	 */
	public static final class SaleType {
		/**
		 * 外卖订单
		 */
		public static final Integer TAKEOUT = 1;
		/**
		 * 堂食订单
		 */
		public static final Integer DINE = 2;
		
	}
	
	/**
	 * 订单状态
	 */
	public static final class State {
		
		public static final String UNPAY = "unpay";
		
		public static final String PAY = "pay";
		
		public static final String ACCEPT = "accept";
		
		public static final String UNACCEPT = "unaccept";
		
		public static final String DONE = "done";
		
		public static final String EVALUATED = "evaluated";
		
		public static final String CONFIRM = "confirm";
		
		public static final String REFUND = "refund";
		
		public static final String DELIVERY_DONE = "delivery_done";
		
		public static final String CANCEL = "cancel";
		
		public static final String DELIVERY = "delivery";
		
	}
	
	/**
	 * 订单退款状态
	 */
	public static final class Rstate {
		
		public static final String NORMAL = "normal";
		
		public static final String ASKREFUND = "askrefund";
		
		public static final String BEREFUND = "berefund";
		
		public static final String NOREFUND = "norefund";
		
	}
	
	/**
	 * 订单打印类型
	 */
	public static final class PrintType{
		
		public static final String BEFOREADDMENU = "before_add_menu";//加菜前
		
		public static final String AFTERADDMENU = "after_add_menu";//加菜后
		
	}
	
	/**
	 * 订单来源
	 */
	public static final class FromType{
		public static final String CROWDSOURCING = "crowdsourcing"; //众包
	}
	
}
