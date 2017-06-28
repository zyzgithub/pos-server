package com.dianba.pos.payment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface PosRewardMapper {

    Map<String, Object> findTotalRewardAmountByDate(@Param("passportId") Long passportId
            , @Param("date") String date);
}
