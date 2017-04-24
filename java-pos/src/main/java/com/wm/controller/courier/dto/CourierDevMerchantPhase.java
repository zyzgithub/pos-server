package com.wm.controller.courier.dto;

import java.util.List;

public class CourierDevMerchantPhase {
	private Integer devId;
	private Integer courierId;
	private String merchantName;
	private String merchantHolder;
	private String merchantMobile;
	private Integer curComletePhaseId;
	private Integer curCompleteSubTaskId;
	
	private Integer curComletePhaseOrderNo;
	private Integer curCompleteSubTaskOrderNo;
	private String checkedSubTasks;
	private String remark;
	
	private List<DevMerchantPhase> devMerchantPhases;
	public Integer getDevId() {
		return devId;
	}

	public void setDevId(Integer devId) {
		this.devId = devId;
	}

	public Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantHolder() {
		return merchantHolder;
	}

	public void setMerchantHolder(String merchantHolder) {
		this.merchantHolder = merchantHolder;
	}

	public String getMerchantMobile() {
		return merchantMobile;
	}

	public void setMerchantMobile(String merchantMobile) {
		this.merchantMobile = merchantMobile;
	}

	public List<DevMerchantPhase> getDevMerchantPhases() {
		return devMerchantPhases;
	}

	public void setDevMerchantPhases(List<DevMerchantPhase> devMerchantPhases) {
		this.devMerchantPhases = devMerchantPhases;
	}

	public Integer getCurComletePhaseId() {
		return curComletePhaseId;
	}

	public void setCurComletePhaseId(Integer curComletePhaseId) {
		this.curComletePhaseId = curComletePhaseId;
	}

	public Integer getCurCompleteSubTaskId() {
		return curCompleteSubTaskId;
	}

	public void setCurCompleteSubTaskId(Integer curCompleteSubTaskId) {
		this.curCompleteSubTaskId = curCompleteSubTaskId;
	}

	public Integer getCurComletePhaseOrderNo() {
		return curComletePhaseOrderNo;
	}

	public void setCurComletePhaseOrderNo(Integer curComletePhaseOrderNo) {
		this.curComletePhaseOrderNo = curComletePhaseOrderNo;
	}

	public Integer getCurCompleteSubTaskOrderNo() {
		return curCompleteSubTaskOrderNo;
	}

	public void setCurCompleteSubTaskOrderNo(Integer curCompleteSubTaskOrderNo) {
		this.curCompleteSubTaskOrderNo = curCompleteSubTaskOrderNo;
	}

	public String getCheckedSubTasks() {
		return checkedSubTasks;
	}

	public void setCheckedSubTasks(String checkedSubTasks) {
		this.checkedSubTasks = checkedSubTasks;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
