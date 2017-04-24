package com.wm.service.provider.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class ProductEntity implements Serializable {
	private String name; // :'奶茶1号', // 名
	private Double price;// :'10.00',// 价
	private String unit;// :'份'. // 单位
	private String image;// :'http://apptest.0085.com/', // 图片地址
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id.longValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return BigDecimal.valueOf(this.price == null ? 0 : this.price).setScale(2, RoundingMode.HALF_UP).toString();
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(Character unit) {
		this.unit = unit + "";
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "ProductEntity [name=" + name + ", price=" + price + ", unit=" + unit + ", image=" + image + "]";
	}

}
