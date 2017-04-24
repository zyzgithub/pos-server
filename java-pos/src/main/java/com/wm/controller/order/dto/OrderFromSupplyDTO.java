package com.wm.controller.order.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OrderFromSupplyDTO implements Serializable{
	private Integer orderId; 		// 订单Id
	private Long createTime; 		// 下单时间
	private Double totalMoney; 		// 订单总金额
	private Integer tomtalQuantity; // 货物总数量

	private Integer destId; 		// 收货方ID(如果是从子仓库到商家传商家Id, 如果是从总仓到子仓传子仓ID)
	private String destName; 		// 收货方名称
	private String destAddress; 	// 收货地址（如果是从子仓库到商家传商家地址, 如果是从总仓到子仓传子仓地址）
	private String destUserName; 	// 收货人姓名
	private String destMobile; 		// 收货人电话
	private Double destLon; 		// 收货人经度
	private Double destLat; 		// 收货人纬度

	private Integer srcId; 			// 源ID, 发货方ID
	private String srcName; 		// 发货方名称
	private String srcAddress; 		// 源地址， 发货方地址
	private String srcUserName; 	// 发货人姓名
	private String srcMobile; 		// 发货人电话
	private Double srcLon; 			// 收货人经度
	private Double srcLat; 			// 收货人纬度

	private String orderDetails; 	// 订单明细

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getTomtalQuantity() {
		return tomtalQuantity;
	}

	public void setTomtalQuantity(Integer tomtalQuantity) {
		this.tomtalQuantity = tomtalQuantity;
	}

	public Integer getDestId() {
		return destId;
	}

	public void setDestId(Integer destId) {
		this.destId = destId;
	}

	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	public String getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}

	public String getDestUserName() {
		return destUserName;
	}

	public void setDestUserName(String destUserName) {
		this.destUserName = destUserName;
	}

	public String getDestMobile() {
		return destMobile;
	}

	public void setDestMobile(String destMobile) {
		this.destMobile = destMobile;
	}

	public Double getDestLon() {
		return destLon;
	}

	public void setDestLon(Double destLon) {
		this.destLon = destLon;
	}

	public Double getDestLat() {
		return destLat;
	}

	public void setDestLat(Double destLat) {
		this.destLat = destLat;
	}

	public Integer getSrcId() {
		return srcId;
	}

	public void setSrcId(Integer srcId) {
		this.srcId = srcId;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getSrcAddress() {
		return srcAddress;
	}

	public void setSrcAddress(String srcAddress) {
		this.srcAddress = srcAddress;
	}

	public String getSrcUserName() {
		return srcUserName;
	}

	public void setSrcUserName(String srcUserName) {
		this.srcUserName = srcUserName;
	}

	public String getSrcMobile() {
		return srcMobile;
	}

	public void setSrcMobile(String srcMobile) {
		this.srcMobile = srcMobile;
	}

	public Double getSrcLon() {
		return srcLon;
	}

	public void setSrcLon(Double srcLon) {
		this.srcLon = srcLon;
	}

	public Double getSrcLat() {
		return srcLat;
	}

	public void setSrcLat(Double srcLat) {
		this.srcLat = srcLat;
	}

	public String getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}
}