package com.dianba.pos.menu.mapper;

import com.dianba.pos.*;

import java.util.List;
import java.util.Map;

import com.dianba.pos.menu.po.Merchant;
import com.dianba.pos.menu.po.MerchantExample;
import org.apache.ibatis.annotations.Param;

public interface MerchantMapper {
    long countByExample(MerchantExample example);

    int deleteByExample(MerchantExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Merchant record);

    int insertSelective(Merchant record);

    List<Merchant> selectByExample(MerchantExample example);

    Merchant selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Merchant record, @Param("example") MerchantExample example);

    int updateByExample(@Param("record") Merchant record, @Param("example") MerchantExample example);

    int updateByPrimaryKeySelective(Merchant record);

    int updateByPrimaryKey(Merchant record);

    /**
     * 获取
     * @param merchant_id
     * @param start_time
     * @param now_time
     * @return
     */
    Map<String,Object> getMerchantProfit(@Param ("merchant_id") Long merchant_id,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    /**
     * 获取
     * @param merchant_id
     * @param start_time
     * @param now_time
     * @return
     */
    Map<String,Object> getMerchantStockCount(@Param ("merchant_id") Long merchant_id,@Param("create_time")Long start_time,@Param("now_time")Long now_time);

    Long getMerchantCreate(Long id);
}