package com.dianba.pos.order.repository;

import com.dianba.pos.order.po.LifeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface LifeOrderJpaRepository extends JpaRepository<LifeOrder, Long> {
}
