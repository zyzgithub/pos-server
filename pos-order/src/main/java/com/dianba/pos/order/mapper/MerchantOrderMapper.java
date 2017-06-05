package com.dianba.pos.order.mapper;

import com.dianba.pos.order.vo.OrderDayIncomeVo;
import com.dianba.pos.order.vo.OrderIncomeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantOrderMapper {

    Map<String, Object> findTodayAndMonthIncomeAmount(Long passportId);

    List<OrderDayIncomeVo> findMerchantDayIncomeOrder(@Param("passportId") Long passportId
            , @Param("beginDate") String beginDate
            , @Param("endDate") String endDate);

    List<OrderIncomeVo> findMerchantIncomeOrder(@Param("passportId") Long passportId
            , @Param("enterType") Integer enterType
            , @Param("date") String date);
}
