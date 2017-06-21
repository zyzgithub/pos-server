package com.dianba.pos.qrcode.service.impl;

import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.qrcode.po.PosQRCode;
import com.dianba.pos.qrcode.repository.PosQRCodeJpaRepository;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPosQRCodeManager implements PosQRCodeManager {

    @Autowired
    private PosQRCodeJpaRepository posQRCodeJpaRepository;

    @Override
    public PosQRCode getQRCodeByMerchantId(Long passportId) {
        PosQRCode posQRCode = posQRCodeJpaRepository.findByMerchantId(passportId);
        if (posQRCode == null) {
            throw new PosNullPointerException("该商家还未绑定二维码！");
        }
        return posQRCode;
    }

    @Override
    public PosQRCode getQRCodeByCode(String code) {
        if (code != null) {
            if (code.length() <= PosQRCode.CODE_LENGTH) {
                code = StringUtil.lpad(code, PosQRCode.CODE_LENGTH, PosQRCode.CODE_APPEND_STR);
                code = PosQRCode.CODE_PREFIX + code;
            }
        } else {
            throw new PosIllegalArgumentException("未包含支付码！");
        }
        PosQRCode posQRCode = posQRCodeJpaRepository.findByCode(code);
        if (posQRCode != null && posQRCode.getMerchantId() != -1) {
            return posQRCode;
        }
        throw new PosNullPointerException("该二维码未绑定商家信息！");
    }
}
