package com.wm.dto.org;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProvinceAndCityDto {
	
	private Integer orgId;
	private String orgName;
	
	private List<Map<String, Object>> citys = Collections.emptyList();

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<Map<String, Object>> getCitys() {
		return citys;
	}

	public void setCitys(List<Map<String, Object>> citys) {
		this.citys = citys;
	}
	
	
}
