package com.courier_mana.statistics.vo;

public class WarehouseReportDetailVo {
	/**
	 * 排名
	 */
	private int rank;
	/**
	 * 商家ID
	 */
	private long merchantId;
	/**
	 * 商家名称
	 */
	private String merchantName;
	/**
	 * 商家用户名
	 */
	private String merchantUserName;
	/**
	 * 商家联系电话
	 */
	private String phone;
	/**
	 * 拨打次数
	 */
	private int dials;
	/**
	 * APP订单数
	 */
	private int appOrders;
	/**
	 * H5订单数
	 */
	private int h5Orders;
	/**
	 * 总订单数
	 */
	private int totalOrders;
	/**
	 * 总金额
	 */
	private double totalIncome;
	/**
	 * 客单价(总额/用户数)
	 */
	private String avgPrice;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getDials() {
		return dials;
	}
	public void setDials(int dials) {
		this.dials = dials;
	}
	public int getAppOrders() {
		return appOrders;
	}
	public void setAppOrders(int appOrders) {
		this.appOrders = appOrders;
	}
	public int getH5Orders() {
		return h5Orders;
	}
	public void setH5Orders(int h5Orders) {
		this.h5Orders = h5Orders;
	}
	public int getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}
	public double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public String getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(String avgPrice) {
		this.avgPrice = avgPrice;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantUserName() {
		return merchantUserName;
	}
	public void setMerchantUserName(String merchantUserName) {
		this.merchantUserName = merchantUserName;
	}
}
