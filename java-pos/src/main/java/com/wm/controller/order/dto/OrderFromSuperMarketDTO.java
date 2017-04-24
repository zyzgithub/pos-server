package com.wm.controller.order.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class OrderFromSuperMarketDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer orderId; //订单id
	private String  payId; //pay_id
	private double totalOrigin; //订单原价总金额
	private double totalPrice; //订单总金额
	private String totalDiscount; //订单总金额
	private Integer totalCount; //商品总数量
	private String payType;//支付类型
	private String remark;//备注
	private String payTypeName; //支付类型名称
	private String completeTime; //完成时间
	private String merchantName; //商店名称
	private Double change = 0.00; //找零
	private double actuallyPaid = 0.00;	//实收
	private List<SuperMarketOrderMenuVo> menuList; //商品明细
	private double memberDiscountMoney = 0.00;//会员优惠
	private double deliveryFee = 0.00;//配送费
	private double costLunchBox = 0.00;//餐盒费
	private double minusDiscountMoney = 0.00;//立减优惠
	private double dineInDiscountMoney = 0.00;//立减优惠

	private String createTime;
	/**
	 * 订单类型,
	 */
	private String orderType;
	/**
	 * 抵金卷金额
	 */
	private String card;
	/**
	 * 积分抵扣金额
	 */
	private String scoreMoney;
	/**
	 * 订单类型: '1.为外卖订单，2为堂食订单',
	 *
	 */
	private String saleType;
	/**
	 * 订单排号
	 */
	private String orderNum;

	private String merchantAddress;
	private String merchantMobile;
	private String platform = "一号生活";
	private String guestPhone = "4008-945-917";





	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public List<SuperMarketOrderMenuVo> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<SuperMarketOrderMenuVo> menuList) {
		this.menuList = menuList;
	}
	public double getTotalOrigin() {
		return totalOrigin;
	}
	public void setTotalOrigin(double totalOrigin) {
		this.totalOrigin = totalOrigin;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Double getChange() {
		return change;
	}
	public void setChange(Double change) {
		this.change = change;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public double getActuallyPaid() {
		return actuallyPaid;
	}
	public void setActuallyPaid(double actuallyPaid) {
		this.actuallyPaid = actuallyPaid;
	}
	public double getMemberDiscountMoney() {
		return memberDiscountMoney;
	}
	public void setMemberDiscountMoney(double memberDiscountMoney) {
		this.memberDiscountMoney = memberDiscountMoney;
	}
	public double getMinusDiscountMoney() {
		return minusDiscountMoney;
	}
	public void setMinusDiscountMoney(double minusDiscountMoney) {
		this.minusDiscountMoney = minusDiscountMoney;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getScoreMoney() {
		return scoreMoney;
	}

	public void setScoreMoney(String scoreMoney) {
		this.scoreMoney = scoreMoney;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getGuestPhone() {
		return guestPhone;
	}

	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getMerchantMobile() {
		return merchantMobile;
	}

	public void setMerchantMobile(String merchantMobile) {
		this.merchantMobile = merchantMobile;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(String totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public double getDineInDiscountMoney() {
		return dineInDiscountMoney;
	}

	public void setDineInDiscountMoney(double dineInDiscountMoney) {
		this.dineInDiscountMoney = dineInDiscountMoney;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public double getCostLunchBox() {
		return costLunchBox;
	}

	public void setCostLunchBox(double costLunchBox) {
		this.costLunchBox = costLunchBox;
	}

	@Override
	public String toString() {
		return "OrderFromSuperMarketDTO{" +
				"orderId=" + orderId +
				", payId='" + payId + '\'' +
				", totalPrice=" + totalPrice +
				", totalCount=" + totalCount +
				", payType='" + payType + '\'' +
				", payTypeName='" + payTypeName + '\'' +
				", completeTime='" + completeTime + '\'' +
				", merchantName='" + merchantName + '\'' +
				", change=" + change +
				", actuallyPaid=" + actuallyPaid +
				", menuList=" + menuList +
				", memberDiscountMoney=" + memberDiscountMoney +
				", minusDiscountMoney=" + minusDiscountMoney +
				", createTime='" + createTime + '\'' +
				", orderType='" + orderType + '\'' +
				", card='" + card + '\'' +
				", scoreMoney='" + scoreMoney + '\'' +
				", saleType='" + saleType + '\'' +
				", orderNum='" + orderNum + '\'' +
				", merchantAddress='" + merchantAddress + '\'' +
				", merchantMobile='" + merchantMobile + '\'' +
				", platform='" + platform + '\'' +
				", guestPhone='" + guestPhone + '\'' +
				'}';
	}
}
