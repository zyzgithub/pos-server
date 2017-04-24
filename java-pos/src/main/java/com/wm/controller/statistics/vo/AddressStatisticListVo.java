package com.wm.controller.statistics.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AddressStatisticListVo {

	private long total;
	private long totalNum;
	private double sumOrigin;
	private double sumOnline;
	private double sumCredit;
	private double sumScore;
	private double sumDelivery;
	private double sumMoblie;
	private Integer buildingId;
	private String buildingName;

	public long getTotal() {
		return total;
	}

	public void setTotal(BigInteger total) {
		if(null != total)
			this.total = total.longValue();
	}

	public long getTotalNum() {
		return totalNum;
	}
	
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}
	
	public double getSumOrigin() {
		return sumOrigin;
	}

	public void setSumOrigin(double sumOrigin) {
		this.sumOrigin = sumOrigin;
	}

	public double getSumOnline() {
		return sumOnline;
	}

	public void setSumOnline(double sumOnline) {
		this.sumOnline = sumOnline;
	}

	public double getSumCredit() {
		return sumCredit;
	}

	public void setSumCredit(double sumCredit) {
		this.sumCredit = sumCredit;
	}

	public double getSumScore() {
		return sumScore;
	}

	public void setSumScore(double sumScore) {
		this.sumScore = sumScore;
	}

	public double getSumDelivery() {
		return sumDelivery;
	}

	public void setSumDelivery(double sumDelivery) {
		this.sumDelivery = sumDelivery;
	}
	
	public double getSumMoblie() {
		sumMoblie = Math.rint(sumOrigin*100)+Math.rint(sumDelivery*100)-Math.rint(sumOnline*100)-Math.rint(sumCredit*100)-Math.rint(sumScore*100);
		return new BigDecimal(sumMoblie/100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	public void setSumMoblie(double sumMoblie) {
		this.sumMoblie = sumMoblie;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

}
