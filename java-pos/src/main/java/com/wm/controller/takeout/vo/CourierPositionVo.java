package com.wm.controller.takeout.vo;

public class CourierPositionVo {
	private int courierId;
	private int positionId;
	private String positionName;
	private int positionSortOrder;
	private int groupId;
	private String groupName;
	private String groupDesc;
	public int getCourierId() {
		return courierId;
	}
	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public int getPositionSortOrder() {
		return positionSortOrder;
	}
	public void setPositionSortOrder(int positionSortOrder) {
		this.positionSortOrder = positionSortOrder;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}	
}
