package com.dianba.pos.order.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.mapper.LifeOrderMapper;
import com.dianba.pos.order.service.MerchantOrderManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Api("商家订单管理器")
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

    /**
     * 商家日报表 以及邮箱导出
     * @param merchantId
     * @param itId
     * @param itemName
     * @return
     */
    @ApiOperation("商家POS端日报表信息")
    @ResponseBody
    @RequestMapping(value = "findMerchantDayReport",method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResult findMerchantDayReport(Long merchantId,Long itId,String itemName,String email,String createTime){
       return merchantOrderManager.findMerchantDayReport(merchantId, itId, itemName, email,createTime);
    }

    @ApiOperation("商家收银员每日盈利信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", paramType = "query", required = true)
            ,@ApiImplicitParam(name = "createTime", value = "要查询的时间", paramType = "query")
    })
    @ResponseBody
    @RequestMapping(value = "findMerchantCashierDayProfitInfo",method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResult findMerchantCashierDayProfitInfo(Long merchantId,String createTime){
        return merchantOrderManager.findMerchantCashierDayProfitInfo(merchantId, createTime);
    }

    @ResponseBody
    @ApiOperation("商家收银员结算详情")
    @RequestMapping(value = "findSettlementInfoByPassportId",method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResult findSettlementInfoByPassportId(Long passportId,String createTime){
        return merchantOrderManager.findSettlementInfoByPassportId(passportId, createTime);
    }
}
