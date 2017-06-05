package com.dianba.pos.passport.po;

import javax.persistence.*;

/**
 * Created by zhangyong on 2017/5/31.
 */
@Entity
@Table(name = "life_pos.pos_cashier_account")
public class PosCashierAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "cashier_id")
    private Long cashierId;

    @Column(name = "account_type")
    private Integer accountType;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "cashier_photo")
    private String cashierPhoto;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCashierPhoto() {
        return cashierPhoto;
    }

    public void setCashierPhoto(String cashierPhoto) {
        this.cashierPhoto = cashierPhoto;
    }
}
