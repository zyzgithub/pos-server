package com.dianba.pos.report.pojo;

import java.math.BigDecimal;

public class TopMerchant {

    //商家名称
    private String merchantName;
    //商家排名
    private Integer ranking;
    //商家营业额
    private BigDecimal turnover;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }
}
