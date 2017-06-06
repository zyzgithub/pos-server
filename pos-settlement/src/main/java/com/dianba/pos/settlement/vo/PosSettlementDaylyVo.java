package com.dianba.pos.settlement.vo;

import com.dianba.pos.settlement.po.PosSettlementDayly;

import java.io.Serializable;

public class PosSettlementDaylyVo extends PosSettlementDayly implements Serializable {

    private String paymentTypeTitle;

    public String getPaymentTypeTitle() {
        return paymentTypeTitle;
    }

    public void setPaymentTypeTitle(String paymentTypeTitle) {
        this.paymentTypeTitle = paymentTypeTitle;
    }
}
