package com.wm.controller.takeout.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MerchantSimpleVo {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	private int merchantId;
	private String name;
	private String logo;
	private Date start;
	private Date end;
	private Date delivery;
	private Date nowTime;
	private String type;
	private double deliveryPrice;
	private int buyCount;
	private int distance;
	private double score = 5;
	private int sale;
	private int promotion;
	private double lng;
	private double lat;

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(BigInteger merchantId) {
		this.merchantId = merchantId.intValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getDelivery() {
		return delivery;
	}
	
	public Date getNowTime() {
		return nowTime;
	}
	
	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}

	public void setDelivery(Date delivery) {
		this.delivery = delivery;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getSale() {
		return sale;
	}

	public void setSale(int sale) {
		this.sale = sale;
	}

	public int getPromotion() {
		return promotion;
	}

	public void setPromotion(Character promotion) {
		if ("Y".equals(promotion.toString()))
			this.promotion = 1;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat.doubleValue();
	}

	public double getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng.doubleValue();
	}
	
	public double getDeliveryPrice() {
		return deliveryPrice;
	}
	
	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public String getOpen() {
		if(isOpening()) 
			return "营业中";
		else
			return "已打烊";
	}
	
	public boolean isOpening() {
		// 如果开始时间大于结束时间，为12:00 到 明天凌晨 3:00的情况
		if (start.before(end)) {
			if (nowTime.after(start) && nowTime.before(end))
				return true;
		} else if (nowTime.after(start) || nowTime.before(end) )
			return true;
		
		return false;
	}

	public String getSend() {
		return sdf.format(delivery);
	}
}
