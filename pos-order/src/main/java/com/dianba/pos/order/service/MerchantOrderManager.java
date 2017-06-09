package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;

public interface MerchantOrderManager {

    BasicResult getOrderForMerchant(Long merchantPassportId, Integer pageNum, Integer pageSize);

    BasicResult findTodayAndMonthIncomeAmount(Long merchantPassportId);

    BasicResult getMerchantIncomeDetail(Long passportId, Integer enterType
            , Integer pageIndex, Integer pageSize, String date);

    BasicResult findMerchantDayReport(Long merchantId,Long itId,String itemName,String email);
}
