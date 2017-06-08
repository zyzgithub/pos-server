package com.dianba.pos.order.vo;

import com.dianba.pos.order.po.LifeOrder;

public class LifeOrderVo extends LifeOrder {

    private String transType;

    @Override
    public String getTransType() {
        return transType;
    }

    @Override
    public void setTransType(String transType) {
        this.transType = transType;
    }
}
