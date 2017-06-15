package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.LifeAchieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeAchieveJpaRepository extends JpaRepository<LifeAchieve, Long> {

    LifeAchieve findByPassportId(Long passportId);
}
