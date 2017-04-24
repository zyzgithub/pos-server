package com.wm.dto.merchant;

import java.math.BigDecimal;

import com.wm.entity.merchant.MerchantCashflowEntity;

public class MerchantCashflowDto {
	
	private Integer mainStoreId;
	private BigDecimal totalMoney;
	private MerchantCashflowEntity[] branchDetail;
	
	public Integer getMainStoreId() {
		return mainStoreId;
	}
	public void setMainStoreId(Integer mainStoreId) {
		this.mainStoreId = mainStoreId;
	}
	public MerchantCashflowEntity[] getBranchDetail() {
		return branchDetail;
	}
	public void setBranchDetail(MerchantCashflowEntity[] branchDetail) {
		this.branchDetail = branchDetail;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	
}
