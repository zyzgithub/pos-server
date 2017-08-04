package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosBlackListJpaRepository extends JpaRepository<PosBlackList, Long> {

    PosBlackList findByPassportId(Long passportId);
}
