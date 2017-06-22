package com.dianba.pos.order.mapper;

import com.dianba.pos.order.vo.MerchantOrderDayIncomeVo;
import com.dianba.pos.order.vo.MerchantOrderIncomeVo;
import com.dianba.pos.order.vo.MerchantOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantOrderMapper {

    /**
     * 根据商家ID获取订单
     *
     * @param merchantPassportId 商家ID
     * @return
     */
    List<MerchantOrderVo> findOrderForMerchant(@Param("merchantPassportId") Long merchantPassportId);

    Map<String, Object> findTodayAndMonthIncomeAmount(Long passportId);

    List<MerchantOrderDayIncomeVo> findMerchantDayIncomeOrder(@Param("passportId") Long passportId
            , @Param("beginDate") String beginDate
            , @Param("endDate") String endDate);

    List<MerchantOrderIncomeVo> findMerchantIncomeOrder(@Param("passportId") Long passportId
            , @Param("enterType") Integer enterType
            , @Param("date") String date);
}
