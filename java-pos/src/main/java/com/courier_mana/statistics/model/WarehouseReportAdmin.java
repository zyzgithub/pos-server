package com.courier_mana.statistics.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="COURIER_MANA_WAREHOUSE_REPORT_ADMIN")
public class WarehouseReportAdmin {
	@Id
	private ObjectId id;
	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 用户实名
	 */
	private String userName;
	/**
	 * 用户电话号码
	 */
	private String phone;
	
	public WarehouseReportAdmin(long userId, String userName, String phone){
		this.userId = userId;
		this.userName = userName;
		this.phone = phone;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
