package com.dianba.pos.purchase.pojo;

import com.dianba.pos.menu.po.Menu;

public class OneKeyPurchase extends Menu{

    private Integer totalSale;
    private Integer daySale;

    public Integer getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Integer totalSale) {
        this.totalSale = totalSale;
    }

    public Integer getDaySale() {
        return daySale;
    }

    public void setDaySale(Integer daySale) {
        this.daySale = daySale;
    }
}
