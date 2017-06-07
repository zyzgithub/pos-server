package com.dianba.pos.passport.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "life_pos.pos_merchant_rate")
public class PosMerchantRate {

    public static final BigDecimal COMMISSION_RATE = BigDecimal.valueOf(0.0038);

    @Id
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }
}
