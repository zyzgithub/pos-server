package com.courier_mana.statistics.vo;

/**
 * 仓库管理数据Vo
 * @author hyj
 */
public class WarehouseReportVo {
	/**
	 * 排名
	 */
	private int rank;
	/**
	 * 仓库ID
	 */
	private long warehouseId;
	/**
	 * 仓库名
	 */
	private String warehouseName;
	/**
	 * 仓库负责人
	 */
	private String handlerName;
	/**
	 * APP订单数
	 */
	private int appOrders;
	/**
	 * H5订单数
	 */
	private int h5Orders;
	/**
	 * 新用户订单数
	 */
	private int newUserOrders;
	/**
	 * 总订单数
	 */
	private int totalOrders;
	/**
	 * 用户数
	 */
	private int totalUsers;
	/**
	 * 总金额
	 */
	private double totalIncome;
	/**
	 * 客单价(总额/用户数)
	 */
	private String avgPrice;

	/**
	 * 未支付订总单数
	 */
	private int noTotalOrder;

	/**
	 * 未支付总金额
	 */
	private double noTotalIncome;
	
	public long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
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
	public int getNewUserOrders() {
		return newUserOrders;
	}
	public void setNewUserOrders(int newUserOrders) {
		this.newUserOrders = newUserOrders;
	}
	public int getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}
	public int getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getNoTotalOrder() {
		return noTotalOrder;
	}

	public void setNoTotalOrder(int noTotalOrder) {
		this.noTotalOrder = noTotalOrder;
	}

	public double getNoTotalIncome() {
		return noTotalIncome;
	}

	public void setNoTotalIncome(double noTotalIncome) {
		this.noTotalIncome = noTotalIncome;
	}
}
