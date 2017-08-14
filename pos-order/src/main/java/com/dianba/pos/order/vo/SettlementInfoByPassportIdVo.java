package com.dianba.pos.order.vo;
import java.math.BigDecimal;

/**
 * Created by zhangyong on 2017/8/8.
 */
public class SettlementInfoByPassportIdVo {

    private Long id;

    private Long passportId;

    private Long merchantPassportId;

    private String paymentType;

    private BigDecimal amount;

    private Integer count;
    private Integer isPaid;
    private String createTime;
    private BigDecimal cashAmount;

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

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }



    public String getCreateTime() {
        return createTime.substring(0,19);
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }
}
