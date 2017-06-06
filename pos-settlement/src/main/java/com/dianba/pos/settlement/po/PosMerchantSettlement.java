package com.dianba.pos.settlement.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "life_pos.pos_merchant_settlement")
public class PosMerchantSettlement {

    @Id
    @Column(name = "merchant_passport_id")
    private Integer merchantPassportId;
    @Column(name = "settlement_paid")
    private String settlementPaid;
    @Column(name = "create_time")
    private Date createTime;

    public Integer getMerchantPassportId() {
        return merchantPassportId;
    }

    public void setMerchantPassportId(Integer merchantPassportId) {
        this.merchantPassportId = merchantPassportId;
    }

    public String getSettlementPaid() {
        return settlementPaid;
    }

    public void setSettlementPaid(String settlementPaid) {
        this.settlementPaid = settlementPaid == null ? null : settlementPaid.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}