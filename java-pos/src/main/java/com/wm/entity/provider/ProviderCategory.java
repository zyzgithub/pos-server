package com.wm.entity.provider;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "0085_provider_category")
public class ProviderCategory implements Serializable
{

	@Id
	@Column(name = "merchant_id")
	private Integer merchantId;
	@Id
	@Column(name = "group_id")
	private Integer groupId;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

}
