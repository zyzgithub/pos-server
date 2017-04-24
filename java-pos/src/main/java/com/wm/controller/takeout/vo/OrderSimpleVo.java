package com.wm.controller.takeout.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

import com.base.enums.OrderStateEnum;
import com.team.wechat.util.MapUtil;
import com.wm.util.BaiduMap;

public class OrderSimpleVo {

	private int orderId;
	private long accessTime;
	private long deliveryTime;
	private String orderNum;
	private String orderState;
	private String merchantName;
	private String merchantLogo;
	private int saleType;
	private double lng;
	private double lat;
	private String merchantMobile;
	private String courierMobile;
	private String courierIcon;
	private String courier;
	private int dis;
	private int addressId;
	private AddressDetailVo address;
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId.intValue();
	}

	public int getSaleType() {
		return saleType;
	}
	
	public void setSaleType(int saleType) {
		this.saleType = saleType;
	}
	
	public void setAccessTime(int accessTime) {
		this.accessTime = accessTime;
	}

	public void setDeliveryTime(int deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getOrderState() {
		if(getSaleType() == 1) {
			if(OrderStateEnum.ACCEPT.getOrderStateEn().equals(orderState))
				return "接单成功,一嫂正在制作中";
			else
				return "制作完成,一哥正为你火速配送中";
		} else {
			return "正等待你的光临";
		}
	}

	public String getTime() {
		if (getSaleType() == 1) {
			long time = 1;
			
			if(OrderStateEnum.ACCEPT.getOrderStateEn().equals(orderState)) {
				time = (System.currentTimeMillis()/1000 - accessTime)/60;
			} else {
				time = (System.currentTimeMillis()/1000 - deliveryTime)/60;
				time = 30 - time % 30;
			}
			return time + "分钟";
		}
		return "";
	}
	
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getMerchantName() {
		if(OrderStateEnum.ACCEPT.getOrderStateEn().equals(orderState))
			return merchantName;
		else
			return courier;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantLogo() {
		if(OrderStateEnum.ACCEPT.getOrderStateEn().equals(orderState))
			return merchantLogo;
		else
			return courierIcon;
	}

	public void setMerchantLogo(String merchantLogo) {
		this.merchantLogo = merchantLogo;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng.doubleValue();
	}

	public double getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat.doubleValue();
	}

	public void setMerchantMobile(String merchantMobile) {
		this.merchantMobile = merchantMobile;
	}

	public void setCourierMobile(String courierMobile) {
		this.courierMobile = courierMobile;
	}
	
	public String getMobile() {
		if(OrderStateEnum.ACCEPT.getOrderStateEn().equals(orderState))
			return merchantMobile;
		else
			return courierMobile;
	}
	
	public String getDis() {
		if(getSaleType() == 2)
			return "堂食订单";
		
		//判断是否是选择地址
		if(null != address.getBuildId()) {
			dis = (int)MapUtil.GetShortDistance(lng, lat, address.getLng(), address.getLat());
			return dis+"米";
			
		} else if(!StringUtils.isEmpty(address.getBuildName())){
			try {
				BaiduMap map = MapUtil.getBaiduMap(address.getBuildName());
				dis = (int)MapUtil.GetShortDistance(lng, lat, map.getResult().getLocation().getLng(), map.getResult().getLocation().getLat());
				return dis+"米";
			} catch (Exception e) {
				e.printStackTrace();
				return 200+"米";
			}
		} else {
			return 200+"米";
		}
	}
	
	public String getOrderNum() {
		if(!StringUtils.isEmpty(orderNum))
			return orderNum.substring(8);
		return StringUtils.EMPTY;
	}
	
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	public int getAddressId() {
		return addressId;
	}
	
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	
	public void setAddress(AddressDetailVo address) {
		this.address = address;
	}
	
	public void setCourier(String courier) {
		this.courier = courier;
	}
	
	public void setCourierIcon(String courierIcon) {
		this.courierIcon = courierIcon;
	}
}
