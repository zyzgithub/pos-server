package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.PosType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/26.
 */
@Transactional
@Repository
public interface PosTypeJpaRepository extends JpaRepository<PosType,Integer> {

    List<PosType> getAllByPassportId(Long passportId);

}
