package com.dianba.pos.payment.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "life_pos.pos_merchant_rate")
public class PosMerchantRate {

    public static final Double COMMISSION_RATE = 0.0038;

    @Id
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "commission_rate")
    private Double commissionRate;

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }
}
