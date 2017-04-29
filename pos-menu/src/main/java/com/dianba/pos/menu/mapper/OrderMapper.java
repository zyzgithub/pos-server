package com.dianba.pos.menu.mapper;


import java.util.List;
import java.util.Map;

import com.dianba.pos.menu.po.Order;
import com.dianba.pos.menu.po.OrderExample;
import com.dianba.pos.menu.po.OrderWithBLOBs;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    long countByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderWithBLOBs record);

    int insertSelective(OrderWithBLOBs record);

    List<OrderWithBLOBs> selectByExampleWithBLOBs(OrderExample example);

    List<Order> selectByExample(OrderExample example);

    OrderWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderWithBLOBs record, @Param("example") OrderExample example);

    int updateByExampleWithBLOBs(@Param("record") OrderWithBLOBs record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByPrimaryKeySelective(OrderWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(OrderWithBLOBs record);

    int updateByPrimaryKey(Order record);

   Map<String, Object> queryOrderList(@Param ("id") Long id,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

   Long getPosStrtTimeByMerchant(Long id);
}