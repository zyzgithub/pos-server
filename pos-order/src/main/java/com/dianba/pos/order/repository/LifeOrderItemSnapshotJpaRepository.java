package com.dianba.pos.order.repository;

import com.dianba.pos.order.po.LifeOrderItemSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeOrderItemSnapshotJpaRepository extends JpaRepository<LifeOrderItemSnapshot, Long> {

    List<LifeOrderItemSnapshot> findByOrderIdIn(List<Long> orderIds);
}
