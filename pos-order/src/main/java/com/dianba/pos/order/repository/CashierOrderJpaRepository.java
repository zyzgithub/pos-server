package com.dianba.pos.order.repository;

import com.dianba.pos.order.po.CashierOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashierOrderJpaRepository extends JpaRepository<CashierOrder, Integer> {
}
