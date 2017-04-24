package com.wm.entity.order;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 供应链订单信息表
 * @author wuyong
 * @date 2016-05-07 17:22:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "supply_chain_order_info", schema = "")
@SuppressWarnings("serial")
public class SupplyChainOrderInfoEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**下单时间*/
	private java.lang.Long createTime;
	/**order表中id*/
	private java.lang.Integer orderId;
	/**供应链订单id*/
	private java.lang.Integer supplyChainOrderId;
	/**订单类型 1 商家订单 2 子仓订单*/
	private java.lang.Integer orderType;
	/**总金额*/
	private java.lang.Double totalMoney;
	/**货物总数量*/
	private java.lang.Integer tomtalQuantity;
	/**收货方ID*/
	private java.lang.Integer destId;
	/**收货方名称*/
	private java.lang.String destName;
	/**收货地址*/
	private java.lang.String destAddress;
	/**收货人姓名*/
	private java.lang.String destUserName;
	/**收货人电话*/
	private java.lang.String destMobile;
	/**收货人经度*/
	private java.lang.Double destLon;
	/**收货人纬度*/
	private java.lang.Double destLat;
	/**发货方ID*/
	private java.lang.Integer srcId;
	/**发货方名称*/
	private java.lang.String srcName;
	/**srcAddress*/
	private java.lang.String srcAddress;
	/**srcUsername*/
	private java.lang.String srcUserName;
	/**发货人电话*/
	private java.lang.String srcMobile;
	/**收货人经度*/
	private java.lang.Double srcLon;
	/**收货人纬度*/
	private java.lang.Double srcLat;
	/**订单明细*/
	private java.lang.String orderDetails;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  下单时间
	 */
	@Column(name ="CREATETIME",nullable=false,precision=10,scale=0)
	public java.lang.Long getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  下单时间
	 */
	public void setCreateTime(java.lang.Long createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  order表中id
	 */
	@Column(name ="ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getOrderId(){
		return this.orderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  order表中id
	 */
	public void setOrderId(java.lang.Integer orderId){
		this.orderId = orderId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  供应链订单id
	 */
	@Column(name ="SUPPLY_CHAIN_ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getSupplyChainOrderId(){
		return this.supplyChainOrderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  供应链订单id
	 */
	public void setSupplyChainOrderId(java.lang.Integer supplyChainOrderId){
		this.supplyChainOrderId = supplyChainOrderId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  订单类型 1 商家订单 2 子仓订单
	 */
	@Column(name ="ORDER_TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getOrderType(){
		return this.orderType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  订单类型 1 商家订单 2 子仓订单
	 */
	public void setOrderType(java.lang.Integer orderType){
		this.orderType = orderType;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  总金额
	 */
	@Column(name ="TOTAL_MONEY",nullable=false,precision=10,scale=2)
	public java.lang.Double getTotalMoney(){
		return this.totalMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  总金额
	 */
	public void setTotalMoney(java.lang.Double totalMoney){
		this.totalMoney = totalMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  货物总数量
	 */
	@Column(name ="TOMTAL_QUANTITY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTomtalQuantity(){
		return this.tomtalQuantity;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  货物总数量
	 */
	public void setTomtalQuantity(java.lang.Integer tomtalQuantity){
		this.tomtalQuantity = tomtalQuantity;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收货方ID
	 */
	@Column(name ="DEST_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getDestId(){
		return this.destId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收货方ID
	 */
	public void setDestId(java.lang.Integer destId){
		this.destId = destId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货方名称
	 */
	@Column(name ="DEST_NAME",nullable=false,length=50)
	public java.lang.String getDestName(){
		return this.destName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货方名称
	 */
	public void setDestName(java.lang.String destName){
		this.destName = destName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货地址
	 */
	@Column(name ="DEST_ADDRESS",nullable=false,length=255)
	public java.lang.String getDestAddress(){
		return this.destAddress;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货地址
	 */
	public void setDestAddress(java.lang.String destAddress){
		this.destAddress = destAddress;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货人姓名
	 */
	@Column(name ="DEST_USERNAME",nullable=false,length=30)
	public java.lang.String getDestUserName(){
		return this.destUserName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人姓名
	 */
	public void setDestUserName(java.lang.String destUserName){
		this.destUserName = destUserName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货人电话
	 */
	@Column(name ="DEST_MOBILE",nullable=false,length=15)
	public java.lang.String getDestMobile(){
		return this.destMobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人电话
	 */
	public void setDestMobile(java.lang.String destMobile){
		this.destMobile = destMobile;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  收货人经度
	 */
	@Column(name ="DEST_LON",nullable=false,precision=15,scale=6)
	public java.lang.Double getDestLon(){
		return this.destLon;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  收货人经度
	 */
	public void setDestLon(java.lang.Double destLon){
		this.destLon = destLon;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  收货人纬度
	 */
	@Column(name ="DEST_LAT",nullable=false,precision=15,scale=6)
	public java.lang.Double getDestLat(){
		return this.destLat;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  收货人纬度
	 */
	public void setDestLat(java.lang.Double destLat){
		this.destLat = destLat;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  发货方ID
	 */
	@Column(name ="SRC_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getSrcId(){
		return this.srcId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  发货方ID
	 */
	public void setSrcId(java.lang.Integer srcId){
		this.srcId = srcId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发货方名称
	 */
	@Column(name ="SRC_NAME",nullable=false,length=50)
	public java.lang.String getSrcName(){
		return this.srcName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发货方名称
	 */
	public void setSrcName(java.lang.String srcName){
		this.srcName = srcName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  srcAddress
	 */
	@Column(name ="SRC_ADDRESS",nullable=false,length=255)
	public java.lang.String getSrcAddress(){
		return this.srcAddress;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  srcAddress
	 */
	public void setSrcAddress(java.lang.String srcAddress){
		this.srcAddress = srcAddress;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  srcUsername
	 */
	@Column(name ="SRC_USERNAME",nullable=false,length=30)
	public java.lang.String getSrcUserName(){
		return this.srcUserName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  srcUsername
	 */
	public void setSrcUserName(java.lang.String srcUserName){
		this.srcUserName = srcUserName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发货人电话
	 */
	@Column(name ="SRC_MOBILE",nullable=false,length=15)
	public java.lang.String getSrcMobile(){
		return this.srcMobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发货人电话
	 */
	public void setSrcMobile(java.lang.String srcMobile){
		this.srcMobile = srcMobile;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  收货人经度
	 */
	@Column(name ="SRC_LON",nullable=false,precision=15,scale=6)
	public java.lang.Double getSrcLon(){
		return this.srcLon;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  收货人经度
	 */
	public void setSrcLon(java.lang.Double srcLon){
		this.srcLon = srcLon;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  收货人纬度
	 */
	@Column(name ="SRC_LAT",nullable=false,precision=15,scale=6)
	public java.lang.Double getSrcLat(){
		return this.srcLat;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  收货人纬度
	 */
	public void setSrcLat(java.lang.Double srcLat){
		this.srcLat = srcLat;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  订单明细
	 */
	@Column(name ="ORDER_DETAILS",nullable=false,length=2000)
	public java.lang.String getOrderDetails(){
		return this.orderDetails;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单明细
	 */
	public void setOrderDetails(java.lang.String orderDetails){
		this.orderDetails = orderDetails;
	}
}
