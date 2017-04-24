package com.wm.entity.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商品变更记录
 * @author zhanxinming
 * @date 2016-08-18 16:44:56
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_menu_variation_log", schema = "")
@SuppressWarnings("serial")
public class MenuVariationLogEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**pos端录入商品，操作收银员id*/
	private java.lang.Integer cashierId;
	/**用户id,不是在pos端录入商品,录入操作员id*/
	private java.lang.Integer userId;
	/**商品id，新增商品没有id，更新商品时有id*/
	private java.lang.Integer menuId;
	/**商家id*/
	private java.lang.Integer merchantId;
	/**条形码*/
	private java.lang.String barcode;
	/**商品名称*/
	private java.lang.String name;
	/**变更前商品名称*/
	private java.lang.String originalName;
//	/**新增库存*/
//	private java.lang.Integer addRepertory;
	/**是否支持线上，1:只支持线上，2:只支持线下，3:同时支持线上线下*/
	private java.lang.Integer isonline;
	/**进价*/
	private java.lang.Double stockPrice;
	/**变更前进价*/
	private java.lang.Double originalStockPrice;
	/**售价*/
	private java.lang.Double price;
	/**原售价*/
	private java.lang.Double originalPrice;
	/**商品类型*/
	private java.lang.Integer typeId;
	/**是否下架 Y：上架  N:下架*/
	private java.lang.String display;
	/**商品介绍*/
	private java.lang.String detail;
	/**商品图片url*/
	private java.lang.String imageUrl;
	/**来源，1：pos端，2：后台，3：商家app*/
	private java.lang.Integer source;
	/**录入类型，1：入库单，2：综合变更单，3：盘点单*/
	private java.lang.Integer instockType;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**审核者*/
	private java.lang.String auditor;
	/**审核状态  1： 待审核， 2：通过，3：不通过*/
	private java.lang.Integer auditState;
	/**审核时间*/
	private java.lang.Integer auditTime;
	/**变更前库存*/
	private java.lang.Integer repertory;
	/**变更后库存*/
	private java.lang.Integer stocktakeRepertory;
	/**库存差异*/
	private java.lang.Integer differRepertory;
	
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
	 *@return: java.lang.Integer  pos端录入商品，操作收银员id
	 */
	@Column(name ="CASHIER_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getCashierId(){
		return this.cashierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  pos端录入商品，操作收银员id
	 */
	public void setCashierId(java.lang.Integer cashierId){
		this.cashierId = cashierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  用户id,不是在pos端录入商品,录入操作员id
	 */
	@Column(name ="USER_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户id,不是在pos端录入商品,录入操作员id
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商品id，新增商品没有id，更新商品时有id
	 */
	@Column(name ="MENU_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getMenuId(){
		return this.menuId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商品id，新增商品没有id，更新商品时有id
	 */
	public void setMenuId(java.lang.Integer menuId){
		this.menuId = menuId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家id
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  条形码
	 */
	@Column(name ="BARCODE",nullable=true,length=15)
	public java.lang.String getBarcode(){
		return this.barcode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  条形码
	 */
	public void setBarcode(java.lang.String barcode){
		this.barcode = barcode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商品名称
	 */
	@Column(name ="NAME",nullable=true,length=25)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商品名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  变更前商品名称
	 */
	@Column(name ="ORIGINAL_NAME",nullable=true,length=25)
	public java.lang.String getOriginalName(){
		return this.originalName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  变更前商品名称
	 */
	public void setOriginalName(java.lang.String originalName){
		this.originalName = originalName;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否支持线上，1:只支持线上，2:只支持线下，3:同时支持线上线下
	 */
	@Column(name ="ISONLINE",nullable=true,precision=3,scale=0)
	public java.lang.Integer getIsonline(){
		return this.isonline;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否支持线上，1:只支持线上，2:只支持线下，3:同时支持线上线下
	 */
	public void setIsonline(java.lang.Integer isonline){
		this.isonline = isonline;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  进价
	 */
	@Column(name ="STOCK_PRICE",nullable=true,precision=12,scale=2)
	public java.lang.Double getStockPrice(){
		return this.stockPrice;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  进价
	 */
	public void setStockPrice(java.lang.Double stockPrice){
		this.stockPrice = stockPrice;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  变更前进价
	 */
	@Column(name ="ORIGINAL_STOCK_PRICE",nullable=true,precision=12,scale=2)
	public java.lang.Double getOriginalStockPrice(){
		return this.originalStockPrice;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  变更前进价
	 */
	public void setOriginalStockPrice(java.lang.Double originalStockPrice){
		this.originalStockPrice = originalStockPrice;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  售价
	 */
	@Column(name ="PRICE",nullable=true,precision=12,scale=2)
	public java.lang.Double getPrice(){
		return this.price;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  售价
	 */
	public void setPrice(java.lang.Double price){
		this.price = price;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  原售价
	 */
	@Column(name ="ORIGINAL_PRICE",nullable=true,precision=12,scale=2)
	public java.lang.Double getOriginalPrice(){
		return this.originalPrice;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  原售价
	 */
	public void setOriginalPrice(java.lang.Double originalPrice){
		this.originalPrice = originalPrice;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商品类型
	 */
	@Column(name ="TYPE_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getTypeId(){
		return this.typeId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商品类型
	 */
	public void setTypeId(java.lang.Integer typeId){
		this.typeId = typeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否下架 Y：上架  N:下架
	 */
	@Column(name ="DISPLAY",nullable=true,length=25)
	public java.lang.String getDisplay(){
		return this.display;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否下架 Y：上架  N:下架
	 */
	public void setDisplay(java.lang.String display){
		this.display = display;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商品介绍
	 */
	@Column(name ="DETAIL",nullable=true,length=500)
	public java.lang.String getDetail(){
		return this.detail;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商品介绍
	 */
	public void setDetail(java.lang.String detail){
		this.detail = detail;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商品图片url
	 */
	@Column(name ="IMAGE_URL",nullable=true,length=500)
	public java.lang.String getImageUrl(){
		return this.imageUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商品图片url
	 */
	public void setImageUrl(java.lang.String imageUrl){
		this.imageUrl = imageUrl;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  来源，1：pos端，2：后台，3：商家app
	 */
	@Column(name ="SOURCE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getSource(){
		return this.source;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  来源，1：pos端，2：后台，3：商家app
	 */
	public void setSource(java.lang.Integer source){
		this.source = source;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  录入类型，1：入库单，2：综合变更单，3：盘点单
	 */
	@Column(name ="INSTOCK_TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getInstockType(){
		return this.instockType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  录入类型，1：入库单，2：综合变更单，3：盘点单
	 */
	public void setInstockType(java.lang.Integer instockType){
		this.instockType = instockType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审核者
	 */
	@Column(name ="AUDITOR",nullable=true,length=25)
	public java.lang.String getAuditor(){
		return this.auditor;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审核者
	 */
	public void setAuditor(java.lang.String auditor){
		this.auditor = auditor;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态  1： 待审核， 2：通过，3：不通过
	 */
	@Column(name ="AUDIT_STATE",nullable=true,precision=3,scale=0)
	public java.lang.Integer getAuditState(){
		return this.auditState;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态  1： 待审核， 2：通过，3：不通过
	 */
	public void setAuditState(java.lang.Integer auditState){
		this.auditState = auditState;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核时间
	 */
	@Column(name ="AUDIT_TIME",nullable=true,precision=19,scale=0)
	public java.lang.Integer getAuditTime(){
		return this.auditTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核时间
	 */
	public void setAuditTime(java.lang.Integer auditTime){
		this.auditTime = auditTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  变更前库存
	 */
	@Column(name ="REPERTORY",nullable=true,precision=10,scale=0)
	public java.lang.Integer getRepertory(){
		return this.repertory;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  变更前库存
	 */
	public void setRepertory(java.lang.Integer repertory){
		this.repertory = repertory;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  变更后库存
	 */
	@Column(name ="STOCKTAKE_REPERTORY",nullable=true,precision=10,scale=0)
	public java.lang.Integer getStocktakeRepertory(){
		return this.stocktakeRepertory;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  变更后库存
	 */
	public void setStocktakeRepertory(java.lang.Integer stocktakeRepertory){
		this.stocktakeRepertory = stocktakeRepertory;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  库存差异
	 */
	@Column(name ="DIFFER_REPERTORY",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDifferRepertory(){
		return this.differRepertory;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  库存差异
	 */
	public void setDifferRepertory(java.lang.Integer differRepertory){
		this.differRepertory = differRepertory;
	}
}
