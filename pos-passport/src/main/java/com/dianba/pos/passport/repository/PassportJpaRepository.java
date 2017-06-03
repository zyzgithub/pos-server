package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Transactional
@Repository
public interface PassportJpaRepository extends JpaRepository<Passport,Long> {


    /**
     * 根据id获取信息
     * @param passportId
     * @return
     */
    Passport getPassportById(Long passportId);
}
