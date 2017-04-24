package com.wm.controller.takeout.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

public class OrderMerchantVo implements Serializable {

	private static final long serialVersionUID = -7379967767996502778L;

	private int orderId;//
	private String orderNum;
	private Date orderCreateTime;//创建时间
	private Date orderPayTime;//支付时间
	private Date orderCommentTime;//评论时间
	private Date orderRefundTime;//订单退款时间
	private Date deliveryDoneTime;//订单配送完成时间
	private String orderState;
	private String orderStateCn;
	private String orderType;
	private Double orderOrigin;//订单金额
	private int merchantId;//商家id
	private String merchantTitle;//商家名
	private String merchantLogoUrl;//商家图片地址（全路径）
	private String refundState; //默认为normal，normal 正常状态，askrefund 申请退款，norefund 退款
	private int courierId; //快递员ID
	private Double deliveryFee;//配送费
	private Double scoreMoney;//积分抵扣
	private Double couponsMoney;
	/**
	 * 当订单状态为confirm 时，这个状态才有效; true为已评价; false为待评价；
	 */
	private boolean isEvaluated = false; 
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	@JSONField(format = "yyyy-MM-dd HH:mm")
	public Date getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Date orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm")
	public Date getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(Date orderPayTime) {
		this.orderPayTime = orderPayTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm")
	public Date getOrderCommentTime() {
		return orderCommentTime;
	}

	public void setOrderCommentTime(Date orderCommentTime) {
		this.orderCommentTime = orderCommentTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm")
	public Date getOrderRefundTime() {
		return orderRefundTime;
	}

	public void setOrderRefundTime(Date orderRefundTime) {
		this.orderRefundTime = orderRefundTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm")
	public Date getDeliveryDoneTime() {
		return deliveryDoneTime;
	}

	public void setDeliveryDoneTime(Date deliveryDoneTime) {
		this.deliveryDoneTime = deliveryDoneTime;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public Double getOrderOrigin() {
		return orderOrigin;
	}

	public void setOrderOrigin(Double orderOrigin) {
		this.orderOrigin = orderOrigin;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantTitle() {
		return merchantTitle;
	}

	public void setMerchantTitle(String merchantTitle) {
		this.merchantTitle = merchantTitle;
	}

	public String getMerchantLogoUrl() {
		return merchantLogoUrl;
	}

	public void setMerchantLogoUrl(String merchantLogoUrl) {
		this.merchantLogoUrl = merchantLogoUrl;
	}

	public String getOrderStateCn() {
		return orderStateCn;
	}

	public void setOrderStateCn(String orderStateCn) {
		this.orderStateCn = orderStateCn;
	}

	public String getRefundState() {
		return refundState;
	}

	public void setRefundState(String refundState) {
		this.refundState = refundState;
	}

	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

	public boolean isEvaluated() {
		return isEvaluated;
	}

	public void setEvaluated(boolean isEvaluated) {
		this.isEvaluated = isEvaluated;
	}

	public String getOrderType() {
		return orderType;
	}
	
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderNum() {
		if(!StringUtils.isEmpty(orderNum)){
			if(orderNum.length() > 8){
				return orderNum.substring(8);
			} else {
				return orderNum;
			}
		}
		return StringUtils.EMPTY;
	}
	
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	public Double getDeliveryFee() {
		return deliveryFee;
	}
	
	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	
	public Double getScoreMoney() {
		return scoreMoney;
	}
	
	public void setScoreMoney(Double scoreMoney) {
		this.scoreMoney = scoreMoney;
	}
	
	public Double getCouponsMoney() {
		return couponsMoney;
	}
	
	public void setCouponsMoney(Double couponsMoney) {
		this.couponsMoney = couponsMoney;
	}
	
	public Double getOughtPayMoney() {
		return (Math.rint(orderOrigin*100) + Math.rint(deliveryFee*100) - Math.rint(scoreMoney*100) - Math.rint(couponsMoney*100))/100;
	}
}
