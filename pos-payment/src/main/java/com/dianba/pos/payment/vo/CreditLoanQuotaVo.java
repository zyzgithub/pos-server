package com.dianba.pos.payment.vo;

import java.math.BigDecimal;

public class CreditLoanQuotaVo {

    private BigDecimal nowQuota;
    private BigDecimal surplusQuota;
    private Integer accountPeriodDays;
    private String cardName;
    private String busType;
    private String content;

    public BigDecimal getNowQuota() {
        return nowQuota;
    }

    public void setNowQuota(BigDecimal nowQuota) {
        this.nowQuota = nowQuota;
    }

    public BigDecimal getSurplusQuota() {
        return surplusQuota;
    }

    public void setSurplusQuota(BigDecimal surplusQuota) {
        this.surplusQuota = surplusQuota;
    }

    public Integer getAccountPeriodDays() {
        return accountPeriodDays;
    }

    public void setAccountPeriodDays(Integer accountPeriodDays) {
        this.accountPeriodDays = accountPeriodDays;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
