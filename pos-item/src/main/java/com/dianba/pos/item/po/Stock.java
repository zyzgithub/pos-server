package com.dianba.pos.item.po;

import java.util.Date;

public class Stock {
    private Integer id;

    private Long menuId;

    private Date createTime;

    private Integer creator;

    private Double stockPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }
}
