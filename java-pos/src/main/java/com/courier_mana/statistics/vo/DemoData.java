package com.courier_mana.statistics.vo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CMDemoData")
public class DemoData {
	@Id
	private ObjectId id;
	private String moneyCurrent;
	private String orderNumCurrent;
	private String merchantCount;
	private String totalMerchantCount;
	private String userCount;
	private String newUser;
	private String takeAwayCount;
	private String scanCount;
	private String dineInCount;
	private String courierCount;
	private String totalExpense;
	private String totalLoss;
	private String reorderRate;
	private String price;
	private String time;
	private boolean isConfirm;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getMoneyCurrent() {
		return moneyCurrent;
	}
	public void setMoneyCurrent(String moneyCurrent) {
		this.moneyCurrent = moneyCurrent;
	}
	public String getOrderNumCurrent() {
		return orderNumCurrent;
	}
	public void setOrderNumCurrent(String orderNumCurrent) {
		this.orderNumCurrent = orderNumCurrent;
	}
	public String getMerchantCount() {
		return merchantCount;
	}
	public void setMerchantCount(String merchantCount) {
		this.merchantCount = merchantCount;
	}
	public String getTotalMerchantCount() {
		return totalMerchantCount;
	}
	public void setTotalMerchantCount(String totalMerchantCount) {
		this.totalMerchantCount = totalMerchantCount;
	}
	public String getUserCount() {
		return userCount;
	}
	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}
	public String getNewUser() {
		return newUser;
	}
	public void setNewUser(String newUser) {
		this.newUser = newUser;
	}
	public String getTakeAwayCount() {
		return takeAwayCount;
	}
	public void setTakeAwayCount(String takeAwayCount) {
		this.takeAwayCount = takeAwayCount;
	}
	public String getScanCount() {
		return scanCount;
	}
	public void setScanCount(String scanCount) {
		this.scanCount = scanCount;
	}
	public String getDineInCount() {
		return dineInCount;
	}
	public void setDineInCount(String dineInCount) {
		this.dineInCount = dineInCount;
	}
	public String getCourierCount() {
		return courierCount;
	}
	public void setCourierCount(String courierCount) {
		this.courierCount = courierCount;
	}
	public String getTotalExpense() {
		return totalExpense;
	}
	public void setTotalExpense(String totalExpense) {
		this.totalExpense = totalExpense;
	}
	public String getTotalLoss() {
		return totalLoss;
	}
	public void setTotalLoss(String totalLoss) {
		this.totalLoss = totalLoss;
	}
	public String getReorderRate() {
		return reorderRate;
	}
	public void setReorderRate(String reorderRate) {
		this.reorderRate = reorderRate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isConfirm() {
		return isConfirm;
	}
	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}
}
