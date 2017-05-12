package com.dianba.pos.order.repository;

import com.dianba.pos.order.po.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuJpaRepository extends JpaRepository<OrderMenu, Integer> {
}
