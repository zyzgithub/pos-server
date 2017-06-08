package com.dianba.pos.order.vo;

import com.dianba.pos.order.po.LifeOrder;

import java.math.BigDecimal;

public class LifeOrderVo extends LifeOrder {

    private BigDecimal actualPrice;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private BigDecimal distributionFee;

}
