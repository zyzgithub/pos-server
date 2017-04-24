package com.wm.entity.menu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_promotion", schema = "")
@SuppressWarnings("serial")
public class MenuPromotionEntity implements Serializable {
	private Integer id;
	/** 开始促销日期 */
	private Integer promotionBeginDate;
	/** 结束促销日期 */
	private Integer promotionOverDate;
	/***
	 * 开始促销时间，时分秒
	 */
	private Integer promotionBeginHour;
	/**
	 * 结束促销时间，时分秒
	 */
	private Integer promotionOverHour;
	/**
	 * 促销金额
	 */
	private Double money;
	/**
	 * 促销销量
	 */
	private Integer salesVolume;
	/**
	 * 促销数量
	 */
	private Integer promotion_quantity;
	/**
	 * 促销菜品ID
	 */
	private Integer menuId;

	/**
	 * 是否开始促销
	 */
	private String promotion = "Y";

	/**
	 * 每人限购数量
	 */
	private Integer limitQuantity;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 19, scale = 0)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "promotion_begin_date", nullable = true)
	public Integer getPromotionBeginDate() {
		return promotionBeginDate;
	}

	public void setPromotionBeginDate(Integer promotionBeginDate) {
		this.promotionBeginDate = promotionBeginDate;
	}

	@Column(name = "promotion_over_date", nullable = true)
	public Integer getPromotionOverDate() {
		return promotionOverDate;
	}

	public void setPromotionOverDate(Integer promotionOverDate) {
		this.promotionOverDate = promotionOverDate;
	}

	@Column(name = "promotion_begin_hour", nullable = true)
	public Integer getPromotionBeginHour() {
		return promotionBeginHour;
	}

	public void setPromotionBeginHour(Integer promotionBeginHour) {
		this.promotionBeginHour = promotionBeginHour;
	}

	@Column(name = "promotion_over_hour", nullable = true)
	public Integer getPromotionOverHour() {
		return promotionOverHour;
	}

	public void setPromotionOverHour(Integer promotionOverHour) {
		this.promotionOverHour = promotionOverHour;
	}

	@Column(name = "money", nullable = true)
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "sales_volume", nullable = true)
	public Integer getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}

	@Column(name = "promotion_quantity", nullable = true)
	public Integer getPromotion_quantity() {
		return promotion_quantity;
	}

	public void setPromotion_quantity(Integer promotion_quantity) {
		this.promotion_quantity = promotion_quantity;
	}

	@Column(name = "menu_id", nullable = true)
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@Column(name = "promotion", nullable = false)
	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	@Column(name = "limit_quantity", nullable = true)
	public Integer getLimitQuantity() {
		return limitQuantity;
	}

	public void setLimitQuantity(Integer limitQuantity) {
		this.limitQuantity = limitQuantity;
	}

}
