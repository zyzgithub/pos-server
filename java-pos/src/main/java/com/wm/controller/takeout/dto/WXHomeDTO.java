package com.wm.controller.takeout.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class WXHomeDTO {
	private int start=0;
	private int rows=20;
	@NotNull
	private double lng;
	@NotNull
	private double lat;
	@NotEmpty
	private String city;
	private String address;
	private int group;
	private int sort;
	private int promote;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public Integer getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getPromote() {
		return promote;
	}

	public void setPromote(int promote) {
		this.promote = promote;
	}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
