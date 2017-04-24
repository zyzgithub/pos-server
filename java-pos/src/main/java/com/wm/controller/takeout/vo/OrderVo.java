package com.wm.controller.takeout.vo;

import java.math.BigInteger;
import java.util.List;

import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;

public class OrderVo {

	private Integer orderId;
	private Integer merchantId;
	private Integer courierId;
	private Integer hasComment;
	private String merchantName;
	private String state;
	private String restate;
	private double origin;
	private double deliveryFee;
	private double onlineMoney;
	private double creditMoney;
	private double scoreMoney;
	private double couponsMoney;
	private int totalCount;

	private String name;
	private String mobile;
	private String address;
	private String invoice;
	private String payType;
	private String orderType;
	private int saleType;
	private String payId;
	private String orderNum;
	private String timeRemark;
	
	private List<OrderMenuVo> menus;
	
	private String courierName;
	private String courierIcon;
	private String couriermobile;
	private double score;
	private long deliveryCount;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId.intValue();
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(BigInteger merchantId) {
		this.merchantId = merchantId.intValue();
	}

	public Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(BigInteger courierId) {
		this.courierId = courierId.intValue();
	}
	
	public Integer getHasComment() {
		return hasComment;
	}
	
	public void setHasComment(BigInteger hasComment) {
		this.hasComment = hasComment.intValue();
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getStateCn() {
		return OrderStateEnum.getCnNameByEnName(getState());
	}

	public double getOrigin() {
		return origin;
	}

	public void setOrigin(double origin) {
		this.origin = origin;
	}
	
	public double getOnlineMoney() {
		return onlineMoney;
	}

	public void setOnlineMoney(double onlineMoney) {
		this.onlineMoney = onlineMoney;
	}

	public double getCreditMoney() {
		return creditMoney;
	}

	public void setCreditMoney(double creditMoney) {
		this.creditMoney = creditMoney;
	}

	public double getScoreMoney() {
		return scoreMoney;
	}

	public void setScoreMoney(double scoreMoney) {
		this.scoreMoney = scoreMoney;
	}
	
	public double getCouponsMoney() {
		return couponsMoney;
	}
	
	public void setCouponsMoney(double couponsMoney) {
		this.couponsMoney = couponsMoney;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		if (getSaleType() == 1) {
			return address;
		}
		return "";
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getInvoice() {
		return invoice;
	}
	
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public String getPayTypeCn() {
		if(OrderStateEnum.UNPAY.getOrderStateEn().equals(getState()))
			return "暂无";
		else if (OrderStateEnum.CANCEL.getOrderStateEn().equals(getState())) 
			return "暂无";
			
		return PayEnum.getCn(getPayType());
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public int getSaleType() {
		return saleType;
	}

	public void setSaleType(int saleType) {
		this.saleType = saleType;
	}
	
	public String getSaleTypeCn() {
		if (getSaleType() == 1) {
			return "外卖";
		}
		return "堂食";
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getTimeRemark() {
		if(getSaleType() == 1) {
			return timeRemark;
		}
		return "";
	}

	public void setTimeRemark(String timeRemark) {
		this.timeRemark = timeRemark;
	}

	public List<OrderMenuVo> getMenus() {
		return menus;
	}

	public void setMenus(List<OrderMenuVo> menus) {
		this.menus = menus;
	}
	
	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierIcon() {
		return courierIcon;
	}

	public void setCourierIcon(String courierIcon) {
		this.courierIcon = courierIcon;
	}
	
	public String getCouriermobile() {
		return couriermobile;
	}
	
	public void setCouriermobile(String couriermobile) {
		this.couriermobile = couriermobile;
	}

	public double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public long getDeliveryCount() {
		return deliveryCount;
	}

	public void setDeliveryCount(long deliveryCount) {
		this.deliveryCount = deliveryCount;
	}

	public String getRestate() {
		return restate;
	}
	
	public void setRestate(String restate) {
		this.restate = restate;
	}
	
	public int getTotalQuantity() {
		int count = 0;
		for (OrderMenuVo m : menus) {
			count += m.getQuantity();
		}
		return count;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}
	
	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	
	public Double getOughtPayMoney() {
		return (Math.rint(origin*100) + Math.rint(deliveryFee*100) - Math.rint(scoreMoney*100) - Math.rint(couponsMoney*100))/100;
	}
}
