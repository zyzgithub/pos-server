package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.LifePassportProperties;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangyong on 2017/6/5.
 */
public interface LifePassportPropertiesJpaRepository extends JpaRepository<LifePassportProperties,Integer> {
    LifePassportProperties findLifePassportPropertiesByPassportIdAndKAndV(Long passportId,String k,String v);

    LifePassportProperties findLifePassportPropertiesByPassportId(Long passportId);
}
