package com.wm.entity.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author wuyong
 * @version V1.0
 * @Title: Entity
 * @Description: menu
 * @date 2015-01-07 09:58:45
 */
@Entity
@Table(name = "menu", schema = "")
@SuppressWarnings("serial")
public class MenuEntity implements java.io.Serializable {

	/**id*/
	private java.lang.Integer id;
	/**菜名*/
	private java.lang.String name;
	/**价格*/
	private java.lang.Double price;
	/**图片路径*/
	private java.lang.String image;
	/**条形码*/
	private java.lang.String barcode;
	/**是否下架: Y: 用户端展示, N 用户端不展示*/
	private java.lang.String display;
	/**菜单类型id*/
	//private MenutypeEntity menutype;
	private MenutypeEntity menuType;   
	/**商家id*/
	//private MerchantEntity merchant;
	private java.lang.Integer merchantId;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**销量*/
	private java.lang.Integer buyCount;
	
    private java.lang.Integer printType;
    /**
     * 库存
     */
    private java.lang.Integer repertory;
    /**
     * 当天库存
     */
    private java.lang.Integer todayRepertory;
    /**
     * 抢购开始时间
     */
    private java.lang.Integer beginTime;
    /**
     * 抢购结束时间
     */
    private java.lang.Integer endTime;
    /**
     * 每天限量抢购数量
     */
    private java.lang.Integer limitToday;
    /**
     * 预警库存.
     */
    private java.lang.Integer warnInventory;
    /**
     * 标准库存
     */
    private java.lang.Integer standardInventory;
    /**
     * 是否支持线上，1：只支持线上，2：只支持线下，3：同时支持线上线下
     */
    private java.lang.Integer isonline;  /**
     * 是否支持线上，1：只支持线上，2：只支持线下，3：同时支持线上线下
     */
    private java.lang.String isDelete;
	private String unit;
	private Integer syncTime;
	private Integer updateTime = (int) System.currentTimeMillis()/1000;
	private Integer unitId;
	private Integer isSync;
	private Double priceOnline;
	private Double originalPrice;
	/**生产日期*/
	private java.lang.Integer productionDate;
	/**保质期-天*/
	private java.lang.Integer shelfLife;

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 19, scale = 0)
    public java.lang.Integer getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  菜名
     */
    @Column(name = "NAME", nullable = false, length = 50)
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  菜名
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.Double
     *
     * @return: java.lang.Double  价格
     */
    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    public java.lang.Double getPrice() {
        return this.price;
    }

    /**
     * 方法: 设置java.lang.Double
     *
     * @param: java.lang.Double  价格
     */
    public void setPrice(java.lang.Double price) {
        this.price = price;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  图片路径
     */
    @Column(name = "IMAGE", nullable = true, length = 100)
    public java.lang.String getImage() {
        return this.image;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  图片路径
     */
    public void setImage(java.lang.String image) {
        this.image = image;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID")
    @NotFound(action=NotFoundAction.IGNORE)
    public MenutypeEntity getMenuType() {
        return this.menuType;
    }

    public void setMenuType(MenutypeEntity menuType) {
        this.menuType = menuType;
    }
/*	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID", nullable = true)
	public MenutypeEntity getMenutype() {
		return menutype;
	}

	public void setMenutype(MenutypeEntity menutype) {
		this.menutype = menutype;
	}*/

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  商家id
     */
    @Column(name = "MERCHANT_ID", nullable = false, precision = 19, scale = 0)
    public java.lang.Integer getMerchantId() {
        return this.merchantId;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  商家id
     */
    public void setMerchantId(java.lang.Integer merchantId) {
        this.merchantId = merchantId;
    }

	/*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = true)
	public MerchantEntity getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantEntity merchant) {
		this.merchant = merchant;
	}*/


    public java.lang.String getBarcode() {
        return barcode;
    }

    public void setBarcode(java.lang.String barcode) {
        this.barcode = barcode;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  创建时间
     */
    @Column(name = "CREATE_TIME", nullable = true, precision = 10, scale = 0)
    public java.lang.Integer getCreateTime() {
        return this.createTime;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  创建时间
     */
    public void setCreateTime(java.lang.Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  销量
     */
    @Column(name = "BUY_COUNT", nullable = false, precision = 19, scale = 0)
    public java.lang.Integer getBuyCount() {
        return this.buyCount;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  销量
     */
    public void setBuyCount(java.lang.Integer buyCount) {
        this.buyCount = buyCount;
    }

    @Column(name = "PRINT_TYPE", nullable = false, precision = 19, scale = 0)
    public java.lang.Integer getPrintType() {
        return this.printType;
    }

    public void setPrintType(java.lang.Integer printType) {
        this.printType = printType;
    }

    @Column(name = "REPERTORY", nullable = false)
    public java.lang.Integer getRepertory() {
        return repertory;
    }

    public void setRepertory(java.lang.Integer repertory) {
        this.repertory = repertory;
    }

    @Column(name = "TODAY_REPERTORY", nullable = false)
    public java.lang.Integer getTodayRepertory() {
        return todayRepertory;
    }

    public void setTodayRepertory(java.lang.Integer todayRepertory) {
        this.todayRepertory = todayRepertory;
    }

    @Column(name = "begin_time", nullable = true)
    public java.lang.Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(java.lang.Integer beginTime) {
        this.beginTime = beginTime;
    }

    @Column(name = "end_time", nullable = true)
    public java.lang.Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Integer endTime) {
        this.endTime = endTime;
    }

    @Column(name = "limit_today", nullable = true)
    public java.lang.Integer getLimitToday() {
        return limitToday;
    }

    public void setLimitToday(java.lang.Integer limitToday) {
        this.limitToday = limitToday;
    }

    @Column(name = "DISPLAY", nullable = false)
    public java.lang.String getDisplay() {
        return display;
    }

    public void setDisplay(java.lang.String display) {
        this.display = display;
    }

    @Column(name = "warn_inventory")
    public Integer getWarnInventory() {
        return warnInventory;
    }

    public void setWarnInventory(Integer warnInventory) {
        this.warnInventory = warnInventory;
    }

    @Column(name = "standard_inventory")
    public Integer getStandardInventory() {
        return standardInventory;
    }

    public void setStandardInventory(Integer standardInventory) {
        this.standardInventory = standardInventory;
    }

    @Column(name = "is_delete")
    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

	@Column(name="ISONLINE",nullable=false)
	public java.lang.Integer getIsonline() {
		return isonline;
	}

	public void setIsonline(java.lang.Integer isonline) {
		this.isonline = isonline;
	}

	@Column(name="UNIT",nullable=false)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name="SYNC_TIME",nullable=false)
	public Integer getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Integer syncTime) {
		this.syncTime = syncTime;
	}

	@Column(name="UNIT_ID",nullable=false)
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	@Column(name="IS_SYNC",nullable=false)
	public Integer getIsSync() {
		return isSync;
	}

	public void setIsSync(Integer isSync) {
		this.isSync = isSync;
	}

	@Column(name="PRICE_ONLINE",nullable=false)
	public Double getPriceOnline() {
		return priceOnline;
	}

	public void setPriceOnline(Double priceOnline) {
		this.priceOnline = priceOnline;
	}

	@Column(name = "ORIGINAL_PRICE")
	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

    @Column(name = "update_time")
    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "production_date")
	public java.lang.Integer getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(java.lang.Integer productionDate) {
		this.productionDate = productionDate;
	}
	
	@Column(name = "shelf_life")
	public java.lang.Integer getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(java.lang.Integer shelfLife) {
		this.shelfLife = shelfLife;
	}
    
}
