package com.dianba.pos.account.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zhangyong on 2017/5/28.
 * 收银员账号表
 */
@Entity
@Table(name = "cashier_account")
public class PosCashierAccount implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**passport表里面的商家id**/
    @Column(name = "merchant_id")
    private Long merchantId;

    /**passport表里面端收银员id**/
    @Column(name = "cashier_id")
    private Long cashierId;

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


}
