package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangyong on 2017/6/10.
 */
@Repository
public interface PosProtocolJpaRepository extends JpaRepository<PosProtocol,Long> {

    PosProtocol findPosProtocolById(Long id);
    @Query("SELECT pp FROM PosProtocol pp order by pp.createTime desc ")
    List<PosProtocol> findAll();

}
