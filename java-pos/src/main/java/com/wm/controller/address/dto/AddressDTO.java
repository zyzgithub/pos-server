package com.wm.controller.address.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.wm.entity.address.AddressEntity;

public class AddressDTO {

	@NotEmpty
	private String name;
	@NotEmpty
	private String tel;
	@NotEmpty
	private String addr;
	@NotEmpty
	private String sex;
	private Integer buildId;
	private String buildName;
	private Integer floor;
	
	public AddressEntity getEntity() {
		AddressEntity entity = new AddressEntity();
		entity.setName(name);
		entity.setMobile(tel);
		entity.setSex(sex);
		entity.setAddressDetail(addr);
		if(buildId!=null && buildId > 0) 
			entity.setBuildingId(buildId);
		if(buildName!=null && !buildName.equals("其他地址"))
			entity.setBuildingName(buildName);
		if(floor != null && floor != 0)
			entity.setBuildingFloor(floor);
		entity.setIsDefault("Y");
		entity.setCreateTime(new Date());
		
		return entity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

}
