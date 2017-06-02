package com.dianba.pos.purchase.pojo;

import com.dianba.pos.item.po.PosItem;

public class OneKeyPurchase extends PosItem {

    private boolean isCanBuy = false;
    private Integer totalSale = 0;
    private Integer daySale = 0;

    public boolean isCanBuy() {
        return isCanBuy;
    }

    public void setCanBuy(boolean canBuy) {
        isCanBuy = canBuy;
    }

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
