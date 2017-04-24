package com.wm.entity.networkDevelop;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员开拓网点记录表
 * @author wuyong
 * @date 2016-04-13 11:41:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_network_development", schema = "")
@SuppressWarnings("serial")
public class NetworkDevelopmentEntity implements java.io.Serializable {
	/**主键id*/
	private java.lang.Integer id;
	/**社区名称*/
	private java.lang.String communityName;
	/**住户数量*/
	private java.lang.Integer household;
	/**2房1厅租金*/
	private java.lang.Double rent;
	/**房价*/
	private java.lang.Double housePrice;
	/**经度*/
	private BigDecimal longitude;
	/**纬度*/
	private BigDecimal latitude;
	/**门店数量*/
	private java.lang.Integer shopAmount;
	/**店铺租金*/
	private java.lang.Double shopRent;
	/**饿了么进驻数量*/
	private java.lang.Integer elmAmount;
	/**美团外卖进驻数量*/
	private java.lang.Integer meituanAmount;
	/**百度外卖进驻数量*/
	private java.lang.Integer baiduAmount;
	/**口碑外卖进驻数量*/
	private java.lang.Integer koubeiAmount;
	/**其他外卖进驻数量*/
	private java.lang.Integer otherAmount;
	/**出单量/单天*/
	private java.lang.Integer orderAmount;
	/**创建时间*/
	private java.util.Date createDate;
	/**更改时间*/
	private java.util.Date doneDate;
	/**审核状态1审核中2审核通过3审核不通过*/
	private java.lang.Integer state;
	/**业务员id*/
	private java.lang.Integer courierId;
	/**审核人*/
	private java.lang.String auditor;
	/**拒绝理由*/
	private java.lang.String refuseReason;
	/**社区地址*/
	private java.lang.String communityAddress;
	/**完成进度*/
	private java.lang.String completeStatus;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  社区名称
	 */
	@Column(name ="COMMUNITY_NAME",nullable=true,length=32)
	public java.lang.String getCommunityName(){
		return this.communityName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  社区名称
	 */
	public void setCommunityName(java.lang.String communityName){
		this.communityName = communityName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  住户数量
	 */
	@Column(name ="HOUSEHOLD",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHousehold(){
		return this.household;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  住户数量
	 */
	public void setHousehold(java.lang.Integer household){
		this.household = household;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  2房1厅租金
	 */
	@Column(name ="RENT",nullable=true,precision=11,scale=2)
	public java.lang.Double getRent(){
		return this.rent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  2房1厅租金
	 */
	public void setRent(java.lang.Double rent){
		this.rent = rent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  房价
	 */
	@Column(name ="HOUSE_PRICE",nullable=true,precision=11,scale=2)
	public java.lang.Double getHousePrice(){
		return this.housePrice;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  房价
	 */
	public void setHousePrice(java.lang.Double housePrice){
		this.housePrice = housePrice;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  经度
	 */
	@Column(name ="LONGITUDE",nullable=true,precision=15,scale=0)
	public BigDecimal getLongitude(){
		return this.longitude;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  经度
	 */
	public void setLongitude(BigDecimal longitude){
		this.longitude = longitude;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  纬度
	 */
	@Column(name ="LATITUDE",nullable=true,precision=15,scale=0)
	public BigDecimal getLatitude(){
		return this.latitude;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  纬度
	 */
	public void setLatitude(BigDecimal latitude){
		this.latitude = latitude;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  门店数量
	 */
	@Column(name ="SHOP_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getShopAmount(){
		return this.shopAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  门店数量
	 */
	public void setShopAmount(java.lang.Integer shopAmount){
		this.shopAmount = shopAmount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店铺租金
	 */
	@Column(name ="SHOP_RENT",nullable=true,precision=11,scale=2)
	public java.lang.Double getShopRent(){
		return this.shopRent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店铺租金
	 */
	public void setShopRent(java.lang.Double shopRent){
		this.shopRent = shopRent;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  饿了么进驻数量
	 */
	@Column(name ="ELM_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getElmAmount(){
		return this.elmAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  饿了么进驻数量
	 */
	public void setElmAmount(java.lang.Integer elmAmount){
		this.elmAmount = elmAmount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  美团外卖进驻数量
	 */
	@Column(name ="MEITUAN_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMeituanAmount(){
		return this.meituanAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  美团外卖进驻数量
	 */
	public void setMeituanAmount(java.lang.Integer meituanAmount){
		this.meituanAmount = meituanAmount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  百度外卖进驻数量
	 */
	@Column(name ="BAIDU_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getBaiduAmount(){
		return this.baiduAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  百度外卖进驻数量
	 */
	public void setBaiduAmount(java.lang.Integer baiduAmount){
		this.baiduAmount = baiduAmount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  口碑外卖进驻数量
	 */
	@Column(name ="KOUBEI_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getKoubeiAmount(){
		return this.koubeiAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  口碑外卖进驻数量
	 */
	public void setKoubeiAmount(java.lang.Integer koubeiAmount){
		this.koubeiAmount = koubeiAmount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  其他外卖进驻数量
	 */
	@Column(name ="OTHER_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getOtherAmount(){
		return this.otherAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  其他外卖进驻数量
	 */
	public void setOtherAmount(java.lang.Integer otherAmount){
		this.otherAmount = otherAmount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  出单量/单天
	 */
	@Column(name ="ORDER_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getOrderAmount(){
		return this.orderAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  出单量/单天
	 */
	public void setOrderAmount(java.lang.Integer orderAmount){
		this.orderAmount = orderAmount;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更改时间
	 */
	@Column(name ="DONE_DATE",nullable=true)
	public java.util.Date getDoneDate(){
		return this.doneDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更改时间
	 */
	public void setDoneDate(java.util.Date doneDate){
		this.doneDate = doneDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态1审核中2审核通过3审核不通过
	 */
	@Column(name ="STATE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态1审核中2审核通过3审核不通过
	 */
	public void setState(java.lang.Integer state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务员id
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务员id
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审核人
	 */
	@Column(name ="AUDITOR",nullable=true,length=20)
	public java.lang.String getAuditor(){
		return this.auditor;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审核人
	 */
	public void setAuditor(java.lang.String auditor){
		this.auditor = auditor;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拒绝理由
	 */
	@Column(name ="REFUSE_REASON",nullable=true,length=128)
	public java.lang.String getRefuseReason(){
		return this.refuseReason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拒绝理由
	 */
	public void setRefuseReason(java.lang.String refuseReason){
		this.refuseReason = refuseReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  社区地址
	 */
	@Column(name ="COMMUNITY_ADDRESS",nullable=true,length=128)
	public java.lang.String getCommunityAddress(){
		return this.communityAddress;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  社区地址
	 */
	public void setCommunityAddress(java.lang.String communityAddress){
		this.communityAddress = communityAddress;
	}

	@Column(name ="COMPLETE_STATUS",nullable=true,length=25)
	public java.lang.String getCompleteStatus() {
		return completeStatus;
	}

	public void setCompleteStatus(java.lang.String completeStatus) {
		this.completeStatus = completeStatus;
	}
	
}
