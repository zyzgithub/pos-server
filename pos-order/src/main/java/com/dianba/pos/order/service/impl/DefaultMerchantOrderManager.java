package com.dianba.pos.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.order.mapper.LifeOrderMapper;
import com.dianba.pos.order.mapper.MerchantOrderMapper;
import com.dianba.pos.order.service.MerchantOrderManager;
import com.dianba.pos.order.vo.MerchantOrderDayIncomeVo;
import com.dianba.pos.order.vo.MerchantOrderIncomeVo;
import com.dianba.pos.order.vo.MerchantOrderVo;
import com.dianba.pos.passport.po.PosMerchantRate;
import com.dianba.pos.passport.service.PosMerchantRateManager;
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
    private LifeOrderMapper orderMapper;
    @Autowired
    private MerchantOrderMapper merchantOrderMapper;
    @Autowired
    private PosMerchantRateManager posMerchantRateManager;

    public BasicResult getOrderForMerchant(Long merchantPassportId, Integer pageNum, Integer pageSize) {
        Page<List<MerchantOrderVo>> orderPage = PageHelper.startPage(pageNum, pageSize).doSelectPage(()
                -> orderMapper.findOrderForMerchant(merchantPassportId));
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(orderPage);
        basicResult.getResponse().put("pageNum", pageNum);
        basicResult.getResponse().put("pageSize", pageSize);
        basicResult.getResponse().put("total", orderPage.getTotal());
        return basicResult;
    }

    @Override
    public BasicResult findTodayAndMonthIncomeAmount(Long passportId) {
        Map<String, Object> merchantIncomeMap = merchantOrderMapper.findTodayAndMonthIncomeAmount(passportId);
        PosMerchantRate posMerchantRate = posMerchantRateManager.findByMerchantPassportId(passportId);
        BigDecimal rate = PosMerchantRate.COMMISSION_RATE;
        if (posMerchantRate != null) {
            rate = posMerchantRate.getCommissionRate();
        }
        BigDecimal todayTotalAmount = BigDecimal.ZERO;
        BigDecimal monthTotalAmount = BigDecimal.ZERO;
        if (merchantIncomeMap != null) {
            todayTotalAmount = (BigDecimal) merchantIncomeMap.get("todayTotalAmount");
            monthTotalAmount = (BigDecimal) merchantIncomeMap.get("monthTotalAmount");
            todayTotalAmount = todayTotalAmount.subtract(todayTotalAmount.multiply(rate))
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            monthTotalAmount = monthTotalAmount.subtract(monthTotalAmount.multiply(rate))
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
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
                date = date + "-01";
                enterType = 2;
            } else {
                enterType = 1;
            }
        }
        String orderDate = date;
        Integer orderEnterType = enterType;
        Page<MerchantOrderIncomeVo> orderIncomePage = PageHelper.startPage(pageIndex, pageSize)
                .doSelectPage(() -> merchantOrderMapper.findMerchantIncomeOrder(passportId, orderEnterType, orderDate));
        PosMerchantRate posMerchantRate = posMerchantRateManager.findByMerchantPassportId(passportId);
        BigDecimal rate = PosMerchantRate.COMMISSION_RATE;
        if (posMerchantRate != null) {
            rate = posMerchantRate.getCommissionRate();
        }
        String beginDate = "";
        String endDate = "";
        for (MerchantOrderIncomeVo orderIncome : orderIncomePage) {
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
            orderIncome.setAmount(orderIncome.getAmount().subtract(orderIncome.getAmount().multiply(rate))
                    .setScale(0, BigDecimal.ROUND_HALF_UP));
            try {
                PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(orderIncome.getTransType());
                orderIncome.setTransType(paymentTypeEnum.getKey());
                orderIncome.setTitle(paymentTypeEnum.getValue());
            } catch (Exception e) {
                orderIncome.setTransType(PaymentTypeEnum.UNKNOWN.getKey());
                orderIncome.setTitle(PaymentTypeEnum.UNKNOWN.getValue());
            }
        }
        List<MerchantOrderDayIncomeVo> orderDayIncomes = merchantOrderMapper
                .findMerchantDayIncomeOrder(passportId, beginDate, endDate);
        for (MerchantOrderDayIncomeVo dayIncome : orderDayIncomes) {
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
            dayIncome.setTotalAmount(dayIncome.getTotalAmount().subtract(dayIncome.getTotalAmount().multiply(rate))
                    .setScale(0, BigDecimal.ROUND_HALF_UP));
        }

        BasicResult basicResult = BasicResult.createSuccessResult();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statisticsArray", orderDayIncomes);
        jsonObject.put("currencyArray", orderIncomePage);
        basicResult.setResponse(jsonObject);
        return basicResult;
    }

}
