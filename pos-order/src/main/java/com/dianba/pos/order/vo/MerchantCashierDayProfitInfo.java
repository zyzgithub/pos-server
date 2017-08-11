package com.dianba.pos.order.vo;

/**
 * Created by zhangyong on 2017/6/21.
 */
public class MerchantCashierDayProfitInfo {

    private Long cashierId;
    private String realName;
    private Long sumTotalPrice;
    private String cashierPhoto;
    private String time;
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

    public Long getSumTotalPrice() {
        return sumTotalPrice;
    }

    public void setSumTotalPrice(Long sumTotalPrice) {
        this.sumTotalPrice = sumTotalPrice;
    }

    public String getCashierPhoto() {
        return cashierPhoto;
    }

    public void setCashierPhoto(String cashierPhoto) {
        this.cashierPhoto = cashierPhoto;
    }

    public String getTime() {
        return time.substring(0,19);
    }

    public void setTime(String time) {
        this.time = time;
    }
}
