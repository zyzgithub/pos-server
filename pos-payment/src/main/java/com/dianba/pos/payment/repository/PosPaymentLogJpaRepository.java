package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.PosPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosPaymentLogJpaRepository extends JpaRepository<PosPaymentLog, Long> {

}
