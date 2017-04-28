package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.Merchant;
import com.dianba.pos.menu.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public interface OrderDao extends JpaRepository<Order, Long> {


}
