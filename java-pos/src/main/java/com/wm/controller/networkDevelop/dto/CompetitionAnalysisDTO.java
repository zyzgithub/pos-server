package com.wm.controller.networkDevelop.dto;

import javax.validation.constraints.NotNull;


public class CompetitionAnalysisDTO {
	
	@NotNull
	private Integer courierId;
	@NotNull
	private int id;
	/**饿了么进驻数量*/
	@NotNull
	private int elmAmount;
	/**美团外卖进驻数量*/
	@NotNull
	private int meituanAmount;
	/**百度外卖进驻数量*/
	@NotNull
	private int baiduAmount;
	/**口碑外卖进驻数量*/
	@NotNull
	private int koubeiAmount;
	/**其他外卖进驻数量*/
	@NotNull
	private int otherAmount;
	/**出单量/单天*/
	@NotNull
	private int orderAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getElmAmount() {
		return elmAmount;
	}
	public void setElmAmount(int elmAmount) {
		this.elmAmount = elmAmount;
	}
	public int getMeituanAmount() {
		return meituanAmount;
	}
	public void setMeituanAmount(int meituanAmount) {
		this.meituanAmount = meituanAmount;
	}
	public int getBaiduAmount() {
		return baiduAmount;
	}
	public void setBaiduAmount(int baiduAmount) {
		this.baiduAmount = baiduAmount;
	}
	public int getKoubeiAmount() {
		return koubeiAmount;
	}
	public void setKoubeiAmount(int koubeiAmount) {
		this.koubeiAmount = koubeiAmount;
	}
	public int getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(int otherAmount) {
		this.otherAmount = otherAmount;
	}
	public int getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	
}
