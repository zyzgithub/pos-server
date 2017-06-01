package com.dianba.pos.payment.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "life_payment.payment_transaction_logger", schema = "life_payment")
@DynamicInsert
@DynamicUpdate
public class LifePaymentTransactionLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "trans_sequence_number")
    private String transSequenceNumber;
    @Column(name = "trans_status")
    private Integer transStatus;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "trans_type")
    private Integer transType;
    @Column(name = "partner_id")
    private String partnerId;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "partner_user_id")
    private String partnerUserId;
    @Column(name = "partner_trade_number")
    private String partnerTradeNumber;
    @Column(name = "channel_id")
    private Integer channelId;
    @Column(name = "channel_trade_number")
    private String channelTradeNumber;
    @Column(name = "channel_user_id")
    private String channelUserId;
    @Column(name = "channel_user_name")
    private String channelUserName;
    @Column(name = "channel_remark")
    private String channelRemark;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "account_type")
    private Integer accountType;
    @Column(name = "currency_type")
    private String currencyType;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "bank_branch_code")
    private String bankBranchCode;
    @Column(name = "bank_simple_name")
    private String bankSimpleName;
    @Column(name = "trans_unit_amount")
    private Long transUnitAmount;
    @Column(name = "trans_number")
    private Integer transNumber;
    @Column(name = "trans_total_amount")
    private Long transTotalAmount;
    @Column(name = "trans_title")
    private String transTitle;
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "trans_create_time")
    private Date transCreateTime;
    @Column(name = "payment_time")
    private Date paymentTime;
    @Column(name = "use_conpon")
    private Byte useConpon;
    @Column(name = "discount_amount")
    private Long discountAmount;
    @Column(name = "refund_status")
    private Byte refundStatus;
    @Column(name = "refund_time")
    private Date refundTime;
    @Column(name = "notify_front_url")
    private String notifyFrontUrl;
    @Column(name = "notify_url")
    private String notifyUrl;
    @Column(name = "extend_parameter")
    private String extendParameter;

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

    public String getTransSequenceNumber() {
        return transSequenceNumber;
    }

    public void setTransSequenceNumber(String transSequenceNumber) {
        this.transSequenceNumber = transSequenceNumber == null ? null : transSequenceNumber.trim();
    }

    public Integer getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(Integer transStatus) {
        this.transStatus = transStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId == null ? null : partnerId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId == null ? null : partnerUserId.trim();
    }

    public String getPartnerTradeNumber() {
        return partnerTradeNumber;
    }

    public void setPartnerTradeNumber(String partnerTradeNumber) {
        this.partnerTradeNumber = partnerTradeNumber;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelTradeNumber() {
        return channelTradeNumber;
    }

    public void setChannelTradeNumber(String channelTradeNumber) {
        this.channelTradeNumber = channelTradeNumber == null ? null : channelTradeNumber.trim();
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId == null ? null : channelUserId.trim();
    }

    public String getChannelUserName() {
        return channelUserName;
    }

    public void setChannelUserName(String channelUserName) {
        this.channelUserName = channelUserName == null ? null : channelUserName.trim();
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark == null ? null : channelRemark.trim();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? null : accountNumber.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode == null ? null : bankBranchCode.trim();
    }

    public String getBankSimpleName() {
        return bankSimpleName;
    }

    public void setBankSimpleName(String bankSimpleName) {
        this.bankSimpleName = bankSimpleName == null ? null : bankSimpleName.trim();
    }

    public Long getTransUnitAmount() {
        return transUnitAmount;
    }

    public void setTransUnitAmount(Long transUnitAmount) {
        this.transUnitAmount = transUnitAmount;
    }

    public Integer getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(Integer transNumber) {
        this.transNumber = transNumber;
    }

    public Long getTransTotalAmount() {
        return transTotalAmount;
    }

    public void setTransTotalAmount(Long transTotalAmount) {
        this.transTotalAmount = transTotalAmount;
    }

    public String getTransTitle() {
        return transTitle;
    }

    public void setTransTitle(String transTitle) {
        this.transTitle = transTitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTransCreateTime() {
        return transCreateTime;
    }

    public void setTransCreateTime(Date transCreateTime) {
        this.transCreateTime = transCreateTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Byte getUseConpon() {
        return useConpon;
    }

    public void setUseConpon(Byte useConpon) {
        this.useConpon = useConpon;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Byte getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Byte refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getNotifyFrontUrl() {
        return notifyFrontUrl;
    }

    public void setNotifyFrontUrl(String notifyFrontUrl) {
        this.notifyFrontUrl = notifyFrontUrl == null ? null : notifyFrontUrl.trim();
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
    }

    public String getExtendParameter() {
        return extendParameter;
    }

    public void setExtendParameter(String extendParameter) {
        this.extendParameter = extendParameter == null ? null : extendParameter.trim();
    }
}
