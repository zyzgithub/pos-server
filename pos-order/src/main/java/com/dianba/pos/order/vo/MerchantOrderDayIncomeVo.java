package com.dianba.pos.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantOrderDayIncomeVo implements Serializable{

    private BigDecimal totalAmount;
    private Integer count;
    private String time;
    private String title;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
