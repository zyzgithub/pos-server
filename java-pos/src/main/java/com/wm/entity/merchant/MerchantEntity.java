package com.wm.entity.merchant;

import java.util.Calendar;
import java.util.Date;

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

import org.apache.commons.lang3.time.DateUtils;

import com.wm.entity.category.CategoryEntity;
import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: merchant
 * @author wuyong
 * @date 2015-01-07 09:59:43
 * @version V1.0   
 *
 */
@Entity
@Table(name = "merchant", schema = "")
@SuppressWarnings("serial")
public class MerchantEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**title*/
	private java.lang.String title;
	/**groupId*/
	private CategoryEntity category;
	/**cityId*/
	private java.lang.Integer cityId;
	/**bankName*/
	private java.lang.String bankName;
	/**bankNo*/
	private java.lang.String bankNo;
	/**bankUser*/
	private java.lang.String bankUser;
	/**address*/
	private java.lang.String address;
	/**contact*/
	private java.lang.String contact;
	/**phone*/
	private java.lang.String phone;
	/**经度*/
	private java.lang.Double lng;
	/**纬度*/
	private java.lang.Double lat;
	/**mobile*/
	private java.lang.String mobile;
	
	private Date mobileUpdateTime;
	
	private Date deliveryBeginUpdateTime;
	
	/**createTime*/
	private java.lang.Integer createTime;
	/**display*/
	private java.lang.String display;
	/**notice*/
	private java.lang.String notice;
	/**公告时间*/
	private Date noticeTime;
	/**开始营业时间*/
	private java.lang.Integer startTime;
	/**结束营业时间*/
	private java.lang.Integer endTime;
	/**营业执照图片*/
	private java.lang.String businessLicense;
	/**经营许可证*/
	private java.lang.String operatingPermit;
	/**打印机编号*/
	private java.lang.String printCode;
	/**cardMoney*/
	private java.lang.Double cardMoney;
	/**代金券活动标志*/
	private java.lang.String cardActivity;
	/**竞价金额*/
	private java.lang.Double biddingMoney;
	/**logo地址*/
	private java.lang.String logoUrl;
	/**type**/
	private java.lang.String type;
	/**配送费用**/
	private java.lang.Double costDelivery;
	
	private java.lang.Double deduction;
	/**排号（每天清零） **/
	private java.lang.Integer orderNum;
	/**预收入到账天数 **/
	private java.lang.Integer incomeDate;
	
	/**起送金额*/
	private java.lang.Double deliveryBegin;
	
	/**开始配送时间**/
	private Integer deliveryTime;
	
	/**
	 * 是否打印堂食订单
	 */
	private java.lang.String dineOrderPrint;
	
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  userId
	 */
	/*@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	/*public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}*/

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  title
	 */
	@Column(name ="TITLE",nullable=true,length=128)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  title
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  groupId
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="GROUP_ID")
	public CategoryEntity getCategory(){
		return this.category;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  groupId
	 */
	public void setCategory(CategoryEntity category){
		this.category = category;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  cityId
	 */
	@Column(name ="CITY_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getCityId(){
		return this.cityId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  cityId
	 */
	public void setCityId(java.lang.Integer cityId){
		this.cityId = cityId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankName
	 */
	@Column(name ="BANK_NAME",nullable=true,length=128)
	public java.lang.String getBankName(){
		return this.bankName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankName
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankNo
	 */
	@Column(name ="BANK_NO",nullable=true,length=128)
	public java.lang.String getBankNo(){
		return this.bankNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankNo
	 */
	public void setBankNo(java.lang.String bankNo){
		this.bankNo = bankNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankUser
	 */
	@Column(name ="BANK_USER",nullable=true,length=128)
	public java.lang.String getBankUser(){
		return this.bankUser;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankUser
	 */
	public void setBankUser(java.lang.String bankUser){
		this.bankUser = bankUser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  address
	 */
	@Column(name ="ADDRESS",nullable=false,length=300)
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  address
	 */
	public void setAddress(java.lang.String address){
		this.address = address;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  contact
	 */
	@Column(name ="CONTACT",nullable=true,length=32)
	public java.lang.String getContact(){
		return this.contact;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  contact
	 */
	public void setContact(java.lang.String contact){
		this.contact = contact;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  phone
	 */
	@Column(name ="PHONE",nullable=true,length=18)
	public java.lang.String getPhone(){
		return this.phone;
	}
	
	@Column(name="longitude")
	public java.lang.Double getLng() {
		return lng;
	}
	
	public void setLng(java.lang.Double lng) {
		this.lng = lng;
	}
	
	@Column(name="latitude")
	public java.lang.Double getLat() {
		return lat;
	}
	
	public void setLat(java.lang.Double lat) {
		this.lat = lat;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  phone
	 */
	public void setPhone(java.lang.String phone){
		this.phone = phone;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  mobile
	 */
	@Column(name ="MOBILE",nullable=true,length=12)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  mobile
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	
	@Column(name ="mobile_update_time",nullable=true)
	public Date getMobileUpdateTime() {
		return mobileUpdateTime;
	}

	public void setMobileUpdateTime(Date mobileUpdateTime) {
		this.mobileUpdateTime = mobileUpdateTime;
	}

	@Column(name ="delivery_begin_update_time",nullable=true)
	public Date getDeliveryBeginUpdateTime() {
		return deliveryBeginUpdateTime;
	}

	public void setDeliveryBeginUpdateTime(Date deliveryBeginUpdateTime) {
		this.deliveryBeginUpdateTime = deliveryBeginUpdateTime;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  display
	 */
	@Column(name ="DISPLAY",nullable=false,length=1)
	public java.lang.String getDisplay(){
		return this.display;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  display
	 */
	public void setDisplay(java.lang.String display){
		this.display = display;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  notice
	 */
	@Column(name ="NOTICE",nullable=true,length=255)
	public java.lang.String getNotice(){
		return this.notice;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  notice
	 */
	public void setNotice(java.lang.String notice){
		this.notice = notice;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  开始营业时间
	 */
	@Column(name ="START_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getStartTime(){
		return this.startTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  开始营业时间
	 */
	public void setStartTime(java.lang.Integer startTime){
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  结束营业时间
	 */
	@Column(name ="END_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  结束营业时间
	 */
	public void setEndTime(java.lang.Integer endTime){
		this.endTime = endTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  营业执照图片
	 */
	@Column(name ="BUSINESS_LICENSE",nullable=true,length=255)
	public java.lang.String getBusinessLicense(){
		return this.businessLicense;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  营业执照图片
	 */
	public void setBusinessLicense(java.lang.String businessLicense){
		this.businessLicense = businessLicense;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  经营许可证
	 */
	@Column(name ="OPERATING_PERMIT",nullable=true,length=255)
	public java.lang.String getOperatingPermit(){
		return this.operatingPermit;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  经营许可证
	 */
	public void setOperatingPermit(java.lang.String operatingPermit){
		this.operatingPermit = operatingPermit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印机编号
	 */
	@Column(name ="PRINT_CODE",nullable=true,length=100)
	public java.lang.String getPrintCode(){
		return this.printCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印机编号
	 */
	public void setPrintCode(java.lang.String printCode){
		this.printCode = printCode;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  cardMoney
	 */
	@Column(name ="CARD_MONEY",nullable=true,precision=10,scale=2)
	public java.lang.Double getCardMoney(){
		return this.cardMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  cardMoney
	 */
	public void setCardMoney(java.lang.Double cardMoney){
		this.cardMoney = cardMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  代金券活动标志
	 */
	@Column(name ="CARD_ACTIVITY",nullable=true,length=1)
	public java.lang.String getCardActivity(){
		return this.cardActivity;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  代金券活动标志
	 */
	public void setCardActivity(java.lang.String cardActivity){
		this.cardActivity = cardActivity;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  竞价金额
	 */
	@Column(name ="BIDDING_MONEY",nullable=true,precision=10,scale=2)
	public java.lang.Double getBiddingMoney(){
		return this.biddingMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  竞价金额
	 */
	public void setBiddingMoney(java.lang.Double biddingMoney){
		this.biddingMoney = biddingMoney;
	}
	
	@Column(name ="logo_url")
	public java.lang.String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(java.lang.String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	@Column(name ="type")
	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	
	@Column(name ="DEDUCTION",nullable=true,precision=10,scale=2)
	public java.lang.Double getDeduction(){
		return this.deduction;
	}

	public void setDeduction(java.lang.Double deduction){
		this.deduction = deduction;
	}
	
	@Column(name ="ORDER_NUM",nullable=true,precision=10,scale=0)
	public java.lang.Integer getOrderNum(){
		return this.orderNum;
	}
	
	public void setOrderNum(java.lang.Integer orderNum){
		this.orderNum = orderNum;
	}

	@Column(name ="INCOME_DATE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getIncomeDate(){
		return this.incomeDate;
	}
	
	public void setIncomeDate(java.lang.Integer incomeDate){
		this.incomeDate = incomeDate;
	}
	
	@Column(name ="COST_DELIVERY",nullable=true,precision=12,scale=2)
	public java.lang.Double getCostDelivery(){
		return this.costDelivery;
	}

	public void setCostDelivery(java.lang.Double costDelivery){
		this.costDelivery = costDelivery;
	}

	@Column(name="delivery_begin",nullable=true)
	public java.lang.Double getDeliveryBegin() {
		return deliveryBegin;
	}

	public void setDeliveryBegin(java.lang.Double deliveryBegin) {
		this.deliveryBegin = deliveryBegin;
	}

	@Column(name ="delivery_time")
	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	@Column(name="dine_order_print")
	public java.lang.String getDineOrderPrint() {
		return dineOrderPrint;
	}

	public void setDineOrderPrint(java.lang.String dineOrderPrint) {
		this.dineOrderPrint = dineOrderPrint;
	}
	
	@Column(name="notice_time",nullable=true)
	public Date getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}
	
	
	@Transient
	public boolean isOpening() {
		
		if("Y".equals(getDisplay())) {
			double start = startTime/3600.0;
			double end = endTime/3600.0;
			
			// 当前几点钟
			Calendar c = DateUtils.toCalendar(new Date());
			double now = c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)/60.0;
			// 如果开始时间大于结束时间，为12:00 到 明天凌晨 3:00的情况
			if (start >= end) {
				if(now > start || now <end)
					return true;
				
			} else if (now >= start && now <= end)
				return true;
		}

		return false;
	}
	
	@Transient
	public String getOpenTime() {
		int startHour = startTime / 3600;
		int startMinus = (startTime - startHour*3600) / 60;
		
		int endHour = endTime / 3600;
		int endMinute = (endTime - endHour*3600) / 60;
		if (startHour >= endHour) {
			
			return startHour+":"+ (startMinus > 9 ? startMinus : "0"+startMinus) 
					+ " - 次日" + endHour+":"+ (endMinute > 9 ? endMinute : "0"+endMinute);
		} else {
			
			return startHour+":"+ (startMinus > 9 ? startMinus : "0"+startMinus) 
					+ " - " + endHour+":"+ (endMinute > 9 ? endMinute : "0"+endMinute);
		}
	}

}
