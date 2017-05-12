package com.dianba.pos.merchant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MerchantMapper {

    /**
     * 获取商家进货额度
     *
     * @param
     * @param
     * @param
     * @return
     */
    Map<String, Object> getMerchantProfit(@Param("merchant_id") Long merchantId
            , @Param("create_time") Long startTime, @Param("now_time") Long nowTime);

    /**
     * 获取商家进货总次数
     *
     * @param
     * @param
     * @param
     * @return
     */
    Map<String, Object> getMerchantStockCount(@Param("merchant_id") Long merchantId
            , @Param("create_time") Long startTime, @Param("now_time") Long nowTime);

    /**
     * 获取商家注册时间
     *
     * @param id
     * @return
     */
    Long getMerchantCreate(Long id);

    Map<String, Object> verifyMerchantUser(@Param("user_name") String userName
            , @Param("card") String card, @Param("phone") String phone);

}
