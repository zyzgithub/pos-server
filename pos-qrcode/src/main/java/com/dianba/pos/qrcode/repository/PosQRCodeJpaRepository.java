package com.dianba.pos.qrcode.repository;

import com.dianba.pos.qrcode.po.PosQRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosQRCodeJpaRepository extends JpaRepository<PosQRCode, Long> {

    PosQRCode findByMerchantId(Long merchantId);

    PosQRCode findByCode(String code);
}
