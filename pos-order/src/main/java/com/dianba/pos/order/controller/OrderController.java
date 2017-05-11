package com.dianba.pos.order.controller;

import com.alibaba.fastjson.JSON;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.exception.BusinessException;
import com.dianba.pos.order.po.Order;
import com.dianba.pos.order.service.OrderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(OrderURLConstant.ORDER)
public class OrderController {

    private static Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private OrderManager orderManager;

    @ResponseBody
    @RequestMapping("create_order")
    public AjaxJson createOrderFromSuperMarket(HttpServletRequest request
            , @RequestParam(value = "merchantId") Integer merchantId
            , @RequestParam(value = "cashierId") Integer cashierId
            , @RequestParam(value = "mobile", required = false) String mobile
            , String params, String version, String uuid) {
        AjaxJson j = new AjaxJson();
        logger.info("开始创建超市订单, merchentId :" + merchantId + ", cashierId:" + cashierId + ", params:" + params);
        try {
            if (org.apache.commons.lang.StringUtils.isBlank(version)) {
                j = AjaxJson.failJson("您的版本号过低, 请先安装最新版本后使用!");
                return j;
            }
            String remark = orderManager.superMarketOrderPrefix;
            Order oDto = orderManager.createOrderFromSuperMarket(merchantId, cashierId, mobile
                    , params, null, remark + uuid);
            if (oDto == null || oDto.getOrderId() == null) {
                j.setMsg("创建订单失败");
                j.setStateCode("01");
                j.setSuccess(false);
                j.setObj(oDto);
            } else {
                j.setObj(oDto);
                j.setStateCode("00");
                j.setSuccess(true);
                j.setMsg("创建订单成功");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            j = AjaxJson.failJson(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg("创建订单失败");
            j.setStateCode("01");
            j.setSuccess(false);
            logger.warn("创建超市订单失败");
        }
        logger.info("创建超市订单, return:{}", JSON.toJSONString(j));
        return j;
    }
}
