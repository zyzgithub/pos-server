package com.dianba.pos.report.service.impl;

import com.dianba.pos.report.mapper.PosReportMapper;
import com.dianba.pos.report.pojo.PosStatistics;
import com.dianba.pos.report.pojo.TopMerchant;
import com.dianba.pos.report.service.PosReportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class DefaultPosReportManager implements PosReportManager{

    @Autowired
    private PosReportMapper posReportMapper;

    @Override
    public PosStatistics posStatistics() {
        PosStatistics posStatistics=posReportMapper.statisticsPosMerchantOrder();
        List<Map<String,Object>> posTurnoverMaps=posReportMapper.statisticsPosTurnover();
        for (Map<String,Object> posTurnoverMap:posTurnoverMaps){
            if (null!=posTurnoverMap){
                String payType=posTurnoverMap.get("pay_type").toString();
                if ("supermarkt_cash".equals(payType)){
                    BigDecimal cashTurnover=BigDecimal.valueOf(
                            Double.parseDouble(posTurnoverMap.get("cash_turnover").toString()));
                    posStatistics.setCashTurnover(cashTurnover);
                }else if("supermarkt_alibarcode".equals(payType)){
                    BigDecimal cashTurnover=BigDecimal.valueOf(
                            Double.parseDouble(posTurnoverMap.get("ali_turnover").toString()));
                    posStatistics.setAliTurnover(cashTurnover);
                }else if("supermarkt_wxbarcode".equals(payType)){
                    BigDecimal cashTurnover=BigDecimal.valueOf(
                            Double.parseDouble(posTurnoverMap.get("wechat_turnover").toString()));
                    posStatistics.setWeChatTurnover(cashTurnover);
                }
            }
        }
        posStatistics.setSumTurnover(posStatistics.getCashTurnover()
                .add(posStatistics.getAliTurnover()).add(posStatistics.getWeChatTurnover()));
        List<TopMerchant> topMerchants=posReportMapper.statisticsPosMerchantTopTen();
        for (int i = 0; i < topMerchants.size(); i++) {
            topMerchants.get(i).setRanking(i+1);
        }
        posStatistics.setTopMerchants(topMerchants);
        return posStatistics;
    }
}
