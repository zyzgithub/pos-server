package com.wm.controller.takeout.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wm.util.JsonMapper;
import com.wm.util.StringUtil;

public class ShopcartDTO {

	@NotEmpty
	private String shopcartJson;
	@NotNull
	private Integer merchantId;
	private Integer saleType=1;
	private Integer payType=1;
	private String orderType="normal";
	private String remark="";
	private String timeRemark="";
	private String invoice="";
	private boolean credit=false;
	private boolean coupons=false;
	private Integer couponsId;
	
	private Integer userId;
	
	private String userName;
	private String userMobile;
	private String userAddress;

	public void setJson(String json) {
		this.shopcartJson = json;
	}

	public String getJson() {
		return shopcartJson;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	public Integer getSaleType() {
		return saleType;
	}
	
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	
	public Integer getPayType() {
		return payType;
	}
	
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	
	public String getOrderType() {
		return orderType;
	}
	
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getTimeRemark() {
		return timeRemark;
	}
	
	public void setTimeRemark(String timeRemark) {
		this.timeRemark = timeRemark;
	}
	
	public String getInvoice() {
		return invoice;
	}
	
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
	public boolean isCredit() {
		return credit;
	}
	
	public void setCredit(boolean credit) {
		this.credit = credit;
	}
	
	public boolean isCoupons() {
		return coupons;
	}
	
	public void setCoupons(boolean coupons) {
		this.coupons = coupons;
	}
	
	public Integer getCouponsId() {
		return couponsId;
	}
	
	public void setCouponsId(Integer couponsId) {
		this.couponsId = couponsId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	public List<Shopcart> getShopcarts() {
		try {
			shopcartJson = StringUtil.replaceNTSRElement(shopcartJson);
			List<Shopcart> carts = JsonMapper.MAPPER.readValue(shopcartJson, new TypeReference<List<Shopcart>>() {});
			
			//判断购物车数量
			Iterator<Shopcart> it = carts.iterator();
			while (it.hasNext()) {
				Shopcart cart = it.next();
				if(cart.getCount() <= 0) {
					it.remove();
					continue;
				}
				cart.setMerchantId(merchantId);
			}
			return carts;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<Shopcart>(0);
		}
	}

	public String getUserMobile() {
		return userMobile;
	}
	
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserAddress() {
		return userAddress;
	}
	
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
}
