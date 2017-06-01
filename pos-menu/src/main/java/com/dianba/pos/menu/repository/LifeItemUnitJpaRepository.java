package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.LifeItemUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeItemUnitJpaRepository extends JpaRepository<LifeItemUnit, Integer> {
}
