package com.wm.entity.menu;

/**   
 * @Title: Entity
 * @Description: 商品变更记录
 * @author zhanxinming
 * @date 2016-08-11 15:04:55
 * @version V1.0   
 *
 */
@SuppressWarnings("serial")
public class MenuVariationLogDTO implements java.io.Serializable {
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
	/**新增库存*/
	private java.lang.Integer addRepertory;
	/**是否支持线上，1:只支持线上，2:只支持线下，3:同时支持线上线下*/
	private java.lang.String isonline;
	/**进价*/
	private java.lang.Double stockPrice;
	/**售价*/
	private java.lang.Double price;
	/**商品类型*/
	private java.lang.Integer typeId;
	/**是否下架 Y：上架  N:下架*/
	private java.lang.String display;
	/**商品介绍*/
	private java.lang.String detail;
	/**生产日期*/
	private java.lang.Integer productionDate;
	/**保质期-天*/
	private java.lang.Integer shelfLife;
	/**单位*/
	private String unit;
	/**单位ID*/
	private Integer unitId;
	
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.Integer getCashierId() {
		return cashierId;
	}
	public void setCashierId(java.lang.Integer cashierId) {
		this.cashierId = cashierId;
	}
	public java.lang.Integer getUserId() {
		return userId;
	}
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}
	public java.lang.Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(java.lang.Integer menuId) {
		this.menuId = menuId;
	}
	public java.lang.Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(java.lang.Integer merchantId) {
		this.merchantId = merchantId;
	}
	public java.lang.String getBarcode() {
		return barcode;
	}
	public void setBarcode(java.lang.String barcode) {
		this.barcode = barcode;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.Integer getAddRepertory() {
		return addRepertory;
	}
	public void setAddRepertory(java.lang.Integer addRepertory) {
		this.addRepertory = addRepertory;
	}
	public java.lang.String getIsonline() {
		return isonline;
	}
	public void setIsonline(java.lang.String isonline) {
		this.isonline = isonline;
	}
	public java.lang.Double getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(java.lang.Double stockPrice) {
		this.stockPrice = stockPrice;
	}
	public java.lang.Double getPrice() {
		return price;
	}
	public void setPrice(java.lang.Double price) {
		this.price = price;
	}
	public java.lang.Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(java.lang.Integer typeId) {
		this.typeId = typeId;
	}
	public java.lang.String getDisplay() {
		return display;
	}
	public void setDisplay(java.lang.String display) {
		this.display = display;
	}
	public java.lang.String getDetail() {
		return detail;
	}
	public void setDetail(java.lang.String detail) {
		this.detail = detail;
	}
	public java.lang.Integer getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(java.lang.Integer productionDate) {
		this.productionDate = productionDate;
	}
	public java.lang.Integer getShelfLife() {
		return shelfLife;
	}
	public void setShelfLife(java.lang.Integer shelfLife) {
		this.shelfLife = shelfLife;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
}
