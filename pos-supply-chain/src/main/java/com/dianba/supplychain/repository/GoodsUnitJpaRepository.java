package com.dianba.supplychain.repository;

import com.dianba.supplychain.po.GoodsUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsUnitJpaRepository extends JpaRepository<GoodsUnit, Integer> {
}
