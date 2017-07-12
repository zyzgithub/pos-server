package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.LifePassportAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangyong on 2017/6/5.
 */
public interface LifePassportAliasJpaRepository extends JpaRepository<LifePassportAlias,Integer> {

    LifePassportAlias findLifePassportAliasByAliasName(String userName);
    LifePassportAlias findLifePassportAliasByPassportId(Long passportId);

    List<LifePassportAlias> findAllByPassportId(Long passportId);
}
