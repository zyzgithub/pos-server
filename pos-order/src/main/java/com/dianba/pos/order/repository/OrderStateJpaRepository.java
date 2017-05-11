package com.dianba.pos.order.repository;

import com.dianba.pos.order.po.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStateJpaRepository extends JpaRepository<OrderState, Integer> {


}
