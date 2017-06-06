package com.dianba.pos.settlement.repository;

import com.dianba.pos.settlement.po.PosSettlementDayly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosSettlementDaylyJpaRepository extends JpaRepository<PosSettlementDayly, Long> {

    List<PosSettlementDayly> findByPassportId(Long passportId);
}
