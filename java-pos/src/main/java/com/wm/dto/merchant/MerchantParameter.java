package com.wm.dto.merchant;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;

/**
 * 商家查询相关的参数
 */
public class MerchantParameter {

	private MerchantEntity merchantEntity;

	private MerchantInfoEntity merchantInfoEntity;

	private Long orgId;

	public MerchantEntity getMerchantEntity() {
		return merchantEntity;
	}

	public void setMerchantEntity(MerchantEntity merchantEntity) {
		this.merchantEntity = merchantEntity;
	}

	public MerchantInfoEntity getMerchantInfoEntity() {
		return merchantInfoEntity;
	}

	public void setMerchantInfoEntity(MerchantInfoEntity merchantInfoEntity) {
		this.merchantInfoEntity = merchantInfoEntity;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
