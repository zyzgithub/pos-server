package com.wm.controller.courier.dto;

import java.util.List;

public class DevMerchantPhase {
	private Integer id;
	private String name;
	private boolean checked;
	private Integer orderNo;
	
	List<DevMerchantPhase> subTasks;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public List<DevMerchantPhase> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(List<DevMerchantPhase> subTasks) {
		this.subTasks = subTasks;
	}
}
