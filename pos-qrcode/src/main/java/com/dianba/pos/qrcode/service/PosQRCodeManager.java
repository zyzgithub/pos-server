package com.dianba.pos.qrcode.service;

import com.dianba.pos.qrcode.po.PosQRCode;

public interface PosQRCodeManager {

    PosQRCode getQRCodeByMerchantId(Long passportId);


    PosQRCode getQRCodeByCode(String code);
}
