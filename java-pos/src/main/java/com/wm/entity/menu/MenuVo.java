package com.wm.entity.menu;

import java.io.Serializable;


public class MenuVo implements Serializable {
	
	private static final long serialVersionUID = 1609569759345947307L;
	
	/** id */
	private java.lang.Integer id;
	/** 菜名 */
	private java.lang.String name;
	/** 价格 */
	private java.lang.Double price;
	/** 促销价格 */
	private java.lang.Double promotionPrice;
	/** 图片路径 */
	private java.lang.String image;
	/** 菜单类型id */
	private java.lang.Integer typeId;
	/** 商家id */
	private java.lang.Integer merchantId;
	/** 创建时间 */
	private java.lang.Integer createTime;
	/** 销量 */
	private java.lang.Integer buyCount;
	/** 更新时间 */
	private java.lang.Integer updateTime;
	/** 介绍 */
	private java.lang.String intro;
	/** 未用 */
	private java.lang.Integer printType;
	/** 是否下架 */
	private String display;
	/** 库存 */
	private java.lang.Integer repertory;
	/** 当天库存 */
	private java.lang.Integer todayRepertory;
	/**抢购开始时间*/
	private java.lang.Integer beginTime;
	/** 抢购结束时间*/
	private java.lang.Integer endTime;
	/**每天限量抢购数量*/
	private java.lang.Integer limitToday;
	/** 是否删除 */
	private java.lang.String isDelete;
	/** 排序 */
	private java.lang.Integer menuSort;
	/** 线上价格 */
	private java.lang.Double priceOnline;
	/** 是否支持线上，1：只支持线上，2：只支持线下，3：同时支持线上线下 */
	private java.lang.String isonline;
	
	/** 条形码 **/
	private String barcode;
	
	/**拼音**/
	private String pinyin = ""; 
	/**首字母**/
	private String firstLetter = "";

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Double getPrice() {
		return price;
	}

	public void setPrice(java.lang.Double price) {
		this.price = price;
	}

	public java.lang.Double getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(java.lang.Double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public java.lang.String getImage() {
		return image;
	}

	public void setImage(java.lang.String image) {
		this.image = image;
	}

	public java.lang.Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(java.lang.Integer typeId) {
		this.typeId = typeId;
	}

	public java.lang.Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(java.lang.Integer merchantId) {
		this.merchantId = merchantId;
	}

	public java.lang.Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}

	public java.lang.Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(java.lang.Integer buyCount) {
		this.buyCount = buyCount;
	}

	public java.lang.Integer getPrintType() {
		return printType;
	}

	public void setPrintType(java.lang.Integer printType) {
		this.printType = printType;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public java.lang.Integer getRepertory() {
		return repertory;
	}

	public void setRepertory(java.lang.Integer repertory) {
		this.repertory = repertory;
	}

	public java.lang.Integer getTodayRepertory() {
		return todayRepertory;
	}

	public void setTodayRepertory(java.lang.Integer todayRepertory) {
		this.todayRepertory = todayRepertory;
	}

	public java.lang.Integer getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(java.lang.Integer beginTime) {
		this.beginTime = beginTime;
	}

	public java.lang.Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(java.lang.Integer endTime) {
		this.endTime = endTime;
	}

	public java.lang.Integer getLimitToday() {
		return limitToday;
	}

	public void setLimitToday(java.lang.Integer limitToday) {
		this.limitToday = limitToday;
	}

	public java.lang.String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(java.lang.String isDelete) {
		this.isDelete = isDelete;
	}

	public java.lang.Integer getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(java.lang.Integer menuSort) {
		this.menuSort = menuSort;
	}

	public java.lang.Double getPriceOnline() {
		return priceOnline;
	}

	public void setPriceOnline(java.lang.Double priceOnline) {
		this.priceOnline = priceOnline;
	}

	public java.lang.String getIsonline() {
		return isonline;
	}

	public void setIsonline(java.lang.String isonline) {
		this.isonline = isonline;
	}

	public java.lang.String getIntro() {
		return intro;
	}

	public void setIntro(java.lang.String intro) {
		this.intro = intro;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}
}
