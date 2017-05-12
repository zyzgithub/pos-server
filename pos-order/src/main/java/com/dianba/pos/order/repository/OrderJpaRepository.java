package com.dianba.pos.order.repository;

import com.dianba.pos.order.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Integer> {


    /**
     * 根据订单号获取订单信息
     *
     * @param orderNum
     * @return
     */
    Order getByPayId(String orderNum);
}
