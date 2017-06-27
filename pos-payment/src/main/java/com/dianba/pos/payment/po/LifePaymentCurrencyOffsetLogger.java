package com.dianba.pos.payment.po;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "life_payment.payment_currency_offset_logger")
public class LifePaymentCurrencyOffsetLogger {

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
    @Column(name = "before_amount")
    private BigDecimal beforeAmount;
    @Column(name = "offset_amount")
    private BigDecimal offsetAmount;
    @Column(name = "after_amount")
    private BigDecimal afterAmount;
    @Column(name = "trans_title")
    private String transTitle;
    @Column(name = "trans_type")
    private String transType;
    @Column(name = "relation_trans_type")
    private Integer relationTransType;
    @Column(name = "relation_trans_sequence")
    private String relationTransSequence;
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

    public BigDecimal getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(BigDecimal beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public BigDecimal getOffsetAmount() {
        return offsetAmount;
    }

    public void setOffsetAmount(BigDecimal offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    public BigDecimal getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(BigDecimal afterAmount) {
        this.afterAmount = afterAmount;
    }

    public String getTransTitle() {
        return transTitle;
    }

    public void setTransTitle(String transTitle) {
        this.transTitle = transTitle == null ? null : transTitle.trim();
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }

    public Integer getRelationTransType() {
        return relationTransType;
    }

    public void setRelationTransType(Integer relationTransType) {
        this.relationTransType = relationTransType;
    }

    public String getRelationTransSequence() {
        return relationTransSequence;
    }

    public void setRelationTransSequence(String relationTransSequence) {
        this.relationTransSequence = relationTransSequence == null ? null : relationTransSequence.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
