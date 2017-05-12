package com.dianba.pos.extended.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
@Mapper
public interface Charge19eMapper {
    /**
     * 查询订单充值次数
     *
     * @param orderId
     * @return
     */
    Integer chargeCountByOrder(@Param("orderId") Long orderId);
}
