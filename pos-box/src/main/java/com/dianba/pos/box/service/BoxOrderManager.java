package com.dianba.pos.box.service;

import com.dianba.pos.order.po.LifeOrder;

public interface BoxOrderManager {

    LifeOrder createBoxOrder(Long passportId);
}
