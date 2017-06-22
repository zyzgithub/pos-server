package com.dianba.pos.maintenance.mapper;

import com.dianba.pos.order.po.LifeOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MTOrderMapper {

    List<LifeOrder> findNoneOffsetAmountOrders();
}
