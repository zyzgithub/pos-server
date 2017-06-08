package com.dianba.pos.order.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.mapper.LifeOrderMapper;
import com.dianba.pos.order.service.MerchantOrderManager;
import com.dianba.pos.order.vo.MerchantDayReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(OrderURLConstant.MERCHANT)
public class MerchantOrderController {

    @Autowired
    private MerchantOrderManager merchantOrderManager;

    @Autowired
    private LifeOrderMapper lifeOrderMapper;
    /**
     * 商家端根据商家ID获取订单列表
     *
     * @param merchantPassportId 商家ID
     * @return
     */
    @ResponseBody
    @RequestMapping("get_order")
    public BasicResult getOrderForMerchant(long merchantPassportId, int pageNum, int pageSize) {
        return merchantOrderManager.getOrderForMerchant(merchantPassportId, pageNum, pageSize);
    }

    /**
     * 商家端根据商家ID获取收入金额（本日，本月）
     *
     * @param passportId
     * @return
     */
    @ResponseBody
    @RequestMapping("get_merchant_income")
    public BasicResult getMerchantIncome(Long passportId) {
        return merchantOrderManager.findTodayAndMonthIncomeAmount(passportId);
    }

    /**
     * @param passportId
     * @param enterType  0、全部，1、今日、2、本月
     * @param pageIndex
     * @param pageSize
     * @param date       指定此参数按照此参数返回,月查询格式为（2017-04）  日查询格式为(2017-04-11)
     * @return
     */
    @ResponseBody
    @RequestMapping("get_merchant_income_detail")
    public BasicResult getMerchantIncomeDetail(Long passportId, Integer enterType
            , Integer pageIndex, Integer pageSize, String date) {
        return merchantOrderManager.getMerchantIncomeDetail(passportId, enterType, pageIndex, pageSize, date);
    }

    @ResponseBody
    @RequestMapping("findMerchantDayReport")
    public BasicResult findMerchantDayReport(Long merchantId,Long itId,String itemName){

       List<MerchantDayReportVo> merchantDayReportVos= lifeOrderMapper.findMerchantDayReport(merchantId,itId,itemName);

       return BasicResult.createSuccessResultWithDatas("获取成功",merchantDayReportVos);

    }
}
