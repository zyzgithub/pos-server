package com.dianba.pos.order.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "life_order.order_entry")
@DynamicInsert
@DynamicUpdate
public class LifeOrder implements Serializable {

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
    private Integer deliverStatus = 0;
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
    @Column(name = "shipping_province")
    private String shippingProvince;
    @Column(name = "shipping_city")
    private String shippingCity;
    @Column(name = "shipping_district")
    private String shippingDistrict;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "shipping_location")
    private String shippingLocation;
    @Column(name = "shipping_phone")
    private String shippingPhone;

    @Column(name = "receipt_user_id")
    private String receiptUserId;
    @Column(name = "receipt_nick_name")
    private String receiptNickName;
    @Column(name = "receipt_province")
    private String receiptProvince;
    @Column(name = "receipt_city")
    private String receiptCity;
    @Column(name = "receipt_district")
    private String receiptDistrict;
    @Column(name = "receipt_address")
    private String receiptAddress;
    @Column(name = "receipt_phone")
    private String receiptPhone;
    @Column(name = "receipt_location")
    private String receiptLocation;

    @Column(name = "courier_passport_id")
    private Long courierPassportId = 0L;
    @Column(name = "courier_nick_name")
    private String courierNickName;
    @Column(name = "courier_phone")
    private String courierPhone;
    @Column(name = "total_distance")
    private Long totalDistance;
    @Column(name = "current_location")
    private String currentLocation;
    @Column(name = "collecting_fees")
    private Byte collectingFees;

    @Column(name = "remark")
    private String remark;
    @Column(name = "actual_price")
    private BigDecimal actualPrice = BigDecimal.ZERO;
    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;
    @Column(name = "discount_price")
    private BigDecimal discountPrice = BigDecimal.ZERO;
    @Column(name = "distribution_fee")
    private BigDecimal distributionFee = BigDecimal.ZERO;
    @Column(name = "price_logger")
    private String priceLogger;
    @Column(name = "cancel_logger")
    private String cancelLogger;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "payment_time")
    private Date paymentTime = new Date(0L);
    @Column(name = "confirm_time")
    private Date confirmTime = new Date(0L);

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

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscountPrice() {
        if (discountPrice == null) {
            discountPrice = BigDecimal.ZERO;
        }
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getDistributionFee() {
        if (distributionFee == null) {
            distributionFee = BigDecimal.ZERO;
        }
        return distributionFee;
    }

    public void setDistributionFee(BigDecimal distributionFee) {
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

    public String getShippingProvince() {
        return shippingProvince;
    }

    public void setShippingProvince(String shippingProvince) {
        this.shippingProvince = shippingProvince;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(String shippingDistrict) {
        this.shippingDistrict = shippingDistrict;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingLocation() {
        return shippingLocation;
    }

    public void setShippingLocation(String shippingLocation) {
        this.shippingLocation = shippingLocation;
    }

    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName;
    }

    public String getReceiptProvince() {
        return receiptProvince;
    }

    public void setReceiptProvince(String receiptProvince) {
        this.receiptProvince = receiptProvince;
    }

    public String getReceiptCity() {
        return receiptCity;
    }

    public void setReceiptCity(String receiptCity) {
        this.receiptCity = receiptCity;
    }

    public String getReceiptDistrict() {
        return receiptDistrict;
    }

    public void setReceiptDistrict(String receiptDistrict) {
        this.receiptDistrict = receiptDistrict;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public Long getCourierPassportId() {
        return courierPassportId;
    }

    public void setCourierPassportId(Long courierPassportId) {
        this.courierPassportId = courierPassportId;
    }

    public String getCourierNickName() {
        return courierNickName;
    }

    public void setCourierNickName(String courierNickName) {
        this.courierNickName = courierNickName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Long totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Byte getCollectingFees() {
        return collectingFees;
    }

    public void setCollectingFees(Byte collectingFees) {
        this.collectingFees = collectingFees;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public List<LifeOrderItemSnapshot> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<LifeOrderItemSnapshot> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }
}
