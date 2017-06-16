package com.dianba.pos.item.vo;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
public class MenuDto {

    private String menuId;

    private String menuName;

    private BigDecimal price;

    private BigDecimal stockPrice;


    private BigDecimal a = new BigDecimal(100);
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public BigDecimal getPrice() {

        return price.divide(a,2,BigDecimal.ROUND_HALF_DOWN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStockPrice() {
        return stockPrice.divide(a,2,BigDecimal.ROUND_HALF_DOWN);
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }
}
