package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.LifePassportAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifePassportAddressJpaRepository extends JpaRepository<LifePassportAddress, Long> {

    LifePassportAddress findByPassportId(Long passportId);
}
