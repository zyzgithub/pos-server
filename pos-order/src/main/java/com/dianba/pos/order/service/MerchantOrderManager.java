package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;

public interface MerchantOrderManager {

    BasicResult findTodayAndMonthIncomeAmount(Long merchantPassportId);

    BasicResult getMerchantIncomeDetail(Long passportId, Integer enterType
            , Integer pageIndex, Integer pageSize, String date);
}
