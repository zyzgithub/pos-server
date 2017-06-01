package com.dianba.pos.order.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "life_order.order_entry")
public class LifeOrder implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "sequence_number")
    private String sequenceNumber;
    @Column(name = "partner_id")
    private String partnerId;
    @Column(name = "partner_user_id")
    private String partnerUserId;
    @Column(name = "type")
    private Integer type;
    @Column(name = "status")
    private Integer status;
    @Column(name = "deliver_status")
    private Integer deliverStatus = Integer.valueOf(0);
    @Column(name = "refund_status")
    private Integer refundStatus;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "trans_type")
    private String transType;
    @Column(name = "shipping_passport_id")
    private Long shippingPassportId;
    @Column(name = "shipping_nick_name")
    private String shippingNickName;
    @Column(name = "shipping_phone")
    private String shippingPhone;
    @Column(name = "receipt_user_id")
    private String receiptUserId;
    @Column(name = "receipt_phone")
    private String receiptPhone;
    @Column(name = "receipt_location")
    private String receiptLocation;
    @Column(name = "remark")
    private String remark;
    @Column(name = "actual_price")
    private Long actualPrice;
    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "discount_price")
    private Long discountPrice;
    @Column(name = "distribution_fee")
    private Long distributionFee;
    @Column(name = "price_logger")
    private String priceLogger;
    @Column(name = "cancel_logger")
    private String cancelLogger;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "payment_time")
    private Date paymentTime = new Date(0L);

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<LifeOrderItemSnapshot> itemSnapshots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Integer deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Long getShippingPassportId() {
        return shippingPassportId;
    }

    public void setShippingPassportId(Long shippingPassportId) {
        this.shippingPassportId = shippingPassportId;
    }

    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getReceiptUserId() {
        return receiptUserId;
    }

    public void setReceiptUserId(String receiptUserId) {
        this.receiptUserId = receiptUserId;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getReceiptLocation() {
        return receiptLocation;
    }

    public void setReceiptLocation(String receiptLocation) {
        this.receiptLocation = receiptLocation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Long actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getDistributionFee() {
        return distributionFee;
    }

    public void setDistributionFee(Long distributionFee) {
        this.distributionFee = distributionFee;
    }

    public String getPriceLogger() {
        return priceLogger;
    }

    public void setPriceLogger(String priceLogger) {
        this.priceLogger = priceLogger;
    }

    public String getCancelLogger() {
        return cancelLogger;
    }

    public void setCancelLogger(String cancelLogger) {
        this.cancelLogger = cancelLogger;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public List<LifeOrderItemSnapshot> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<LifeOrderItemSnapshot> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }
}
