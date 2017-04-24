package com.wm.entity.menu;

public class MenuPackVo {
	private Integer id;
	private String name;
	private Double price;
	private String image;
	private Integer typeId;
	private Integer merchantId;
	private String intro;
	private Integer repertory;
	private Integer todayRepertory;
	private Integer menuSort;
	private Double  priceOnline;
	
	private Integer merchantSource;
	
	public Integer getMerchantSource() {
		return merchantSource;
	}

	public void setMerchantSource(Integer merchantSource) {
		this.merchantSource = merchantSource;
	}

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
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public Integer getTypeId() {
		return typeId;
	}
	
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	public Integer getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	public String getIntro() {
		return intro;
	}
	
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public Integer getRepertory() {
		return repertory;
	}
	
	public void setRepertory(Integer repertory) {
		this.repertory = repertory;
	}
	
	public Integer getTodayRepertory() {
		return todayRepertory;
	}
	
	public void setTodayRepertory(Integer todayRepertory) {
		this.todayRepertory = todayRepertory;
	}
	
	public Integer getMenuSort() {
		return menuSort;
	}
	
	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}
	
	public Double getPriceOnline() {
		return priceOnline;
	}
	
	public void setPriceOnline(Double priceOnline) {
		this.priceOnline = priceOnline;
	}
}
