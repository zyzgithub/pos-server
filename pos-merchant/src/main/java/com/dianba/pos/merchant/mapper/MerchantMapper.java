package com.dianba.pos.merchant.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MerchantMapper {

    /**
     * 获取商家进货额度
     * @param merchant_id
     * @param start_time
     * @param now_time
     * @return
     */
    Map<String,Object> getMerchantProfit(@Param ("merchant_id") Long merchant_id
            ,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    /**
     * 获取商家进货总次数
     * @param merchant_id
     * @param start_time
     * @param now_time
     * @return
     */
    Map<String,Object> getMerchantStockCount(@Param ("merchant_id") Long merchant_id
            ,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    /**
     * 获取商家注册时间
     * @param id
     * @return
     */
    Long getMerchantCreate(Long id);

    Map<String,Object> verifyMerchantUser(@Param("user_name") String user_name,@Param("card") String card ,@Param("phone") String phone);

}
