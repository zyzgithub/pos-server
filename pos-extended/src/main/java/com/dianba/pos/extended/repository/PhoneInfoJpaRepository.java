package com.dianba.pos.extended.repository;

import com.dianba.pos.extended.po.PosPhoneInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneInfoJpaRepository extends JpaRepository<PosPhoneInfo, Long> {

}
