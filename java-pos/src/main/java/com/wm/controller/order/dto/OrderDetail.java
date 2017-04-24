package com.wm.controller.order.dto;

public class OrderDetail {
	private Integer id;  //货物ID
	private String name; //货物名称
	private Integer quantity;  //货物数量
	private Double price; 	//货物金额
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
