package com.dianba.pos.order.vo;

import java.math.BigDecimal;

/**
 * Created by zhangyong on 2017/6/21.
 */
public class MerchantCashierDayProfitInfo {

    private Long cashierId;
    private String realName;
    private BigDecimal sumTotalPrice;
    private String cashierPhoto;

    private String time;
    private  BigDecimal a=new BigDecimal(100);
    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getSumTotalPrice() {
        return sumTotalPrice.divide(a,2,BigDecimal.ROUND_HALF_UP);
    }

    public void setSumTotalPrice(BigDecimal sumTotalPrice) {
        this.sumTotalPrice = sumTotalPrice;
    }

    public String getCashierPhoto() {
        return cashierPhoto;
    }

    public void setCashierPhoto(String cashierPhoto) {
        this.cashierPhoto = cashierPhoto;
    }

    public String getTime() {
        return time.substring(0,10);
    }

    public void setTime(String time) {
        this.time = time;
    }
}
