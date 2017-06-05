package com.dianba.pos.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.order.mapper.MerchantOrderMapper;
import com.dianba.pos.order.service.MerchantOrderManager;
import com.dianba.pos.order.vo.OrderDayIncomeVo;
import com.dianba.pos.order.vo.OrderIncomeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DefaultMerchantOrderManager implements MerchantOrderManager {

    @Autowired
    private MerchantOrderMapper merchantOrderMapper;

    @Override
    public BasicResult findTodayAndMonthIncomeAmount(Long passportId) {
        Map<String, Object> merchantIncomeMap = merchantOrderMapper.findTodayAndMonthIncomeAmount(passportId);
        BigDecimal todayTotalAmount = BigDecimal.ZERO;
        BigDecimal monthTotalAmount = BigDecimal.ZERO;
        if (merchantIncomeMap != null) {
            todayTotalAmount = (BigDecimal) merchantIncomeMap.get("todayTotalAmount");
            monthTotalAmount = (BigDecimal) merchantIncomeMap.get("monthTotalAmount");
            //前端自己转换金额，返回分为单位
//            todayTotalAmount = todayTotalAmount.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
//            monthTotalAmount = monthTotalAmount.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("todayTotalAmount", todayTotalAmount);
        jsonObject.put("monthTotalAmount", monthTotalAmount);
        basicResult.setResponse(jsonObject);
        return basicResult;
    }

    public BasicResult getMerchantIncomeDetail(Long passportId, Integer enterType
            , Integer pageIndex, Integer pageSize, String date) {
        if (StringUtils.isEmpty(date)) {
            date = DateUtil.getNowTime("yyyy-MM-dd");
        } else {
            if (date.length() == 7) {
                date = DateUtil.getNowTime("yyyy-MM-dd");
                enterType = 2;
            } else {
                enterType = 1;
            }
        }
        String orderDate = date;
        Integer orderEnterType = enterType;
        Page<OrderIncomeVo> orderIncomePage = PageHelper.startPage(pageIndex, pageSize)
                .doSelectPage(() -> merchantOrderMapper.findMerchantIncomeOrder(passportId, orderEnterType, orderDate));
        String beginDate = "";
        String endDate = "";
        for (OrderIncomeVo orderIncome : orderIncomePage) {
            if (beginDate.equals("") || endDate.equals("")) {
                beginDate = orderIncome.getTime();
                endDate = orderIncome.getTime();
            } else {
                Date date1 = DateUtil.strToDate(orderIncome.getTime());
                Date date2 = DateUtil.strToDate(beginDate);
                Date date3 = DateUtil.strToDate(endDate);
                if (date1.before(date2)) {
                    beginDate = orderIncome.getTime();
                }
                if (date1.after(date3)) {
                    endDate = orderIncome.getTime();
                }
            }
            //前端自己转换金额，返回分为单位
//            orderIncome.setAmount(orderIncome.getAmount()
//                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            try {
                PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(orderIncome.getTransType());
                orderIncome.setTransType(paymentTypeEnum.getKey());
                orderIncome.setTitle(paymentTypeEnum.getValue());
            } catch (Exception e) {
                orderIncome.setTransType(PaymentTypeEnum.UNKNOWN.getKey());
                orderIncome.setTitle(PaymentTypeEnum.UNKNOWN.getValue());
            }
        }
        List<OrderDayIncomeVo> orderDayIncomes = merchantOrderMapper
                .findMerchantDayIncomeOrder(passportId, beginDate, endDate);
        for (OrderDayIncomeVo dayIncome : orderDayIncomes) {
            if (CommonUtils.isToday(CommonUtils.dateFormatToLong(dayIncome.getTime() + " 00:00:00"))) {
                dayIncome.setTitle("今日");
            } else if (CommonUtils.isSameDay(CommonUtils.dateFormatToLong(dayIncome.getTime() + " 00:00:00")
                    , System.currentTimeMillis() - CommonUtils.DAY_MILLISECOND_TIME)) {
                dayIncome.setTitle("昨日");
            } else {
                dayIncome.setTitle(dayIncome.getTime() + " "
                        + CommonUtils.dayOfWeekForTime(CommonUtils.dateFormatToLong(
                        dayIncome.getTime() + " 00:00:00")));
            }
            //前端自己转换金额，返回分为单位
//            dayIncome.setTotalAmount(dayIncome.getTotalAmount()
//                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        }

        BasicResult basicResult = BasicResult.createSuccessResult();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statisticsArray", orderDayIncomes);
        jsonObject.put("currencyArray", orderIncomePage);
        basicResult.setResponse(jsonObject);
        return basicResult;
    }

}
