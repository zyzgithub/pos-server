package com.dianba.pos.settlement.mapper;

import com.dianba.pos.settlement.po.PosSettlementDayly;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PosSettlementDaylyMapper {

    /**
     * 统计时间点之后的营业额
     *
     * @param passportId
     * @return
     */
    List<PosSettlementDayly> statisticsOrderByDay(@Param("passportId") Long passportId
            ,@Param("dateTime") String dateTime);

    /**
     * 查找上一次结算时间
     *
     * @param passportId
     * @return
     */
    String findLastSettlementTime(Long passportId);
}
