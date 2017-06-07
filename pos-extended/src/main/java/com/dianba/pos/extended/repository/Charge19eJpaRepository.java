package com.dianba.pos.extended.repository;

import com.dianba.pos.extended.po.PosCharge19eOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
@Repository
public interface Charge19eJpaRepository extends JpaRepository<PosCharge19eOrder, Long> {



}
