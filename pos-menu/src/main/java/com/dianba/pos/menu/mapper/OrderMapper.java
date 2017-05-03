package com.dianba.pos.menu.mapper;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    Map<String, Object> queryOrderList(@Param ("id") Long id,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    Long getPosStrtTimeByMerchant(Long id);
}