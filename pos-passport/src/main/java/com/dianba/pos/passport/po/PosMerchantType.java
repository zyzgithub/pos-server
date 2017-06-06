package com.dianba.pos.passport.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "life_pos.pos_merchant_type")
public class PosMerchantType {

    @Id
    @Column(name = "merchant_passport_id")
    private Long merchantPassportId;
    @Column(name = "merchant_type")
    private String merchantType;
    @Column(name = "create_time")
    private Date createTime;

    public Long getMerchantPassportId() {
        return merchantPassportId;
    }

    public void setMerchantPassportId(Long merchantPassportId) {
        this.merchantPassportId = merchantPassportId;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
