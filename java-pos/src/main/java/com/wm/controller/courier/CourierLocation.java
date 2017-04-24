package com.wm.controller.courier;

import java.io.Serializable;

/**
 * 类描述：区域实时物流概况 
 * @date 2016年1月12日
 * @author suyuqiang   
 */
public class CourierLocation implements Serializable{
	private static final long serialVersionUID = 1786025174219858823L;
	
	private Integer type;//快递员状态类型：0配送中 1等待中 2空闲中
	private Double x;//快递员位置：x坐标
	private Double y;//快递员位置：y坐标
	private Integer userId;//快递员ID
	
	
	
	public CourierLocation() {
	}
	
	public CourierLocation(Double x, Double y, Integer userId) {		
		this.x = x;
		this.y = y;
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	
}
