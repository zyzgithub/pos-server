package com.dianba.pos.payment.po;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "life_payment.payment_currency_account")
@DynamicUpdate
public class LifePaymentCurrencyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "channel_id")
    private Long channelId;
    @Column(name = "currency_type")
    private Integer currencyType;
    @Column(name = "name")
    private String name;
    @Column(name = "current_amount")
    private BigDecimal currentAmount = BigDecimal.ZERO;
    @Column(name = "freeze_amount")
    private BigDecimal freezeAmount = BigDecimal.ZERO;
    @Column(name = "total_into_amount")
    private BigDecimal totalIntoAmount = BigDecimal.ZERO;
    @Column(name = "total_output_amount")
    private BigDecimal totalOutputAmount = BigDecimal.ZERO;
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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getTotalIntoAmount() {
        return totalIntoAmount;
    }

    public void setTotalIntoAmount(BigDecimal totalIntoAmount) {
        this.totalIntoAmount = totalIntoAmount;
    }

    public BigDecimal getTotalOutputAmount() {
        return totalOutputAmount;
    }

    public void setTotalOutputAmount(BigDecimal totalOutputAmount) {
        this.totalOutputAmount = totalOutputAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
