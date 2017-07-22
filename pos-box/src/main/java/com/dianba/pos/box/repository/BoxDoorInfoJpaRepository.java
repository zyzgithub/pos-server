package com.dianba.pos.box.repository;

import com.dianba.pos.box.po.BoxDoorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxDoorInfoJpaRepository extends JpaRepository<BoxDoorInfo, String> {

    BoxDoorInfo findByPassportId(Long passportId);

    BoxDoorInfo findByAccessSN(String accessSN);
}
