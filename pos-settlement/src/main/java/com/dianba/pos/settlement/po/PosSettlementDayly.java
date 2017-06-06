package com.dianba.pos.settlement.po;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "life_pos.pos_settlement_dayly")
public class PosSettlementDayly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "merchant_passport_id")
    private Long merchantPassportId;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "count")
    private Integer count;
    @Column(name = "is_paid")
    private String isPaid = "0";
    @Column(name = "create_time")
    private Date createTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Long getMerchantPassportId() {
        return merchantPassportId;
    }

    public void setMerchantPassportId(Long merchantPassportId) {
        this.merchantPassportId = merchantPassportId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
