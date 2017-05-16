package com.dianba.pos.report.mapper;

import com.dianba.pos.report.pojo.PosStatistics;
import com.dianba.pos.report.pojo.TopMerchant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PosReportMapper {

    /**
     * 今日新增商家数，今日使用商家数，今日订单数，累计商家，累计订单，累计营业额
     * @return
     */
    PosStatistics statisticsPosMerchantOrder();

    /**
     * 今日营业额
     * @return
     */
    List<Map<String,Object>> statisticsPosTurnover();

    /**
     * 今日营业额前10的商家
     * @return
     */
    List<TopMerchant> statisticsPosMerchantTopTen();
}
