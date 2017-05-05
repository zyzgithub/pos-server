package com.dianba.pos.menu.mapper;


import java.util.List;
import java.util.Map;

import com.dianba.pos.menu.po.Order;

import com.dianba.pos.menu.po.OrderWithBLOBs;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {


    /**
     *  查询商家使用商米pos每个月的盈利信息
     * @param id
     * @param start_time
     * @param now_time
     * @return
     */
   Map<String, Object> queryOrderList(@Param ("id") Long id,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    /**
     * 获取商家使用pos机的时间
     * @param id
     * @return
     */
   Long getPosStrtTimeByMerchant(Long id);

    /**
     * 查询商家每天营业数据
     * @param id
     * @return
     */
     //List<PosProfitByDayEntity> selectPosProfitByDay(Long id);

    /**
     * 获取商家注册时间
     * @param id
     * @return
     */
    Long getMerchantCreate(Long id);
     Map<String,Object> verifyMerchantUser(@Param("user_name") String user_name,@Param("card")
            String card ,@Param("phone") String phone);
}