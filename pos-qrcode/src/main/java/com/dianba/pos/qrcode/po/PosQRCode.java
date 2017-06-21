package com.dianba.pos.qrcode.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "life_pos.pos_code")
public class PosQRCode implements Serializable {

    public static final String CODE_PREFIX = "EWM";
    public static final int CODE_LENGTH = 8;
    public static final char CODE_APPEND_STR = '0';

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "merchant_id")
    private Long merchantId;
    @Column(name = "merchant_name")
    private String merchantName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
