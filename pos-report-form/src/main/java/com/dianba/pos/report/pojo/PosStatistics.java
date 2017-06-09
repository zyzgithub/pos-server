package com.dianba.pos.report.pojo;

import java.math.BigDecimal;
import java.util.List;

public class PosStatistics {

    //今日新增商家数
    private Integer addedMerchant;
    //今日使用商家数
    private Integer usedMerchant;
    //今日订单数
    private Integer orderCount;
    //今日总营业额
    private BigDecimal sumTurnover;
    //今日现金营业额
    private BigDecimal cashTurnover;
    //今日支付宝营业额
    private BigDecimal aliTurnover;
    //今日微信营业额
    private BigDecimal weChatTurnover;

    //累计商家数
    private BigDecimal totalMerchant;
    //累计订单数
    private Integer totalOrderCount;
    //累计营业额
    private BigDecimal totalTurnover;

    //今日营业额 TOP 10
    List<TopMerchant> topMerchants;

    public Integer getUsedMerchant() {
        return usedMerchant;
    }

    public void setUsedMerchant(Integer usedMerchant) {
        this.usedMerchant = usedMerchant;
    }

    public Integer getAddedMerchant() {
        return addedMerchant;
    }

    public void setAddedMerchant(Integer addedMerchant) {
        this.addedMerchant = addedMerchant;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getSumTurnover() {
        return sumTurnover;
    }

    public void setSumTurnover(BigDecimal sumTurnover) {
        this.sumTurnover = sumTurnover;
    }

    public BigDecimal getCashTurnover() {
        return cashTurnover;
    }

    public void setCashTurnover(BigDecimal cashTurnover) {
        this.cashTurnover = cashTurnover;
    }

    public BigDecimal getAliTurnover() {
        return aliTurnover;
    }

    public void setAliTurnover(BigDecimal aliTurnover) {
        this.aliTurnover = aliTurnover;
    }

    public BigDecimal getWeChatTurnover() {
        return weChatTurnover;
    }

    public void setWeChatTurnover(BigDecimal weChatTurnover) {
        this.weChatTurnover = weChatTurnover;
    }

    public BigDecimal getTotalMerchant() {
        return totalMerchant;
    }

    public void setTotalMerchant(BigDecimal totalMerchant) {
        this.totalMerchant = totalMerchant;
    }

    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public BigDecimal getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(BigDecimal totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public List<TopMerchant> getTopMerchants() {
        return topMerchants;
    }

    public void setTopMerchants(List<TopMerchant> topMerchants) {
        this.topMerchants = topMerchants;
    }
}
