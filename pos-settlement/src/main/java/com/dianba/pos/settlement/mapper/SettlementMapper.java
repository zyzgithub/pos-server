package com.dianba.pos.settlement.mapper;

import com.dianba.pos.settlement.po.PosSettlementDayly;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettlementMapper {

    /**
     * 统计一天的营业额
     *
     * @param passportId
     * @return
     */
    List<PosSettlementDayly> statisticsOrderByDay(Long passportId);
}
