package com.dianba.pos.merchant.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MerchantMapper {

    /**
     * 获取
     * @param merchant_id
     * @param start_time
     * @param now_time
     * @return
     */
    Map<String,Object> getMerchantProfit(@Param ("merchant_id") Long merchant_id
            ,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    /**
     * 获取
     * @param merchant_id
     * @param start_time
     * @param now_time
     * @return
     */
    Map<String,Object> getMerchantStockCount(@Param ("merchant_id") Long merchant_id
            ,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    Long getMerchantCreate(Long id);
}