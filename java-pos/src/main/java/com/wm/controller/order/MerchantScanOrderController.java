package com.wm.controller.order;

import com.alibaba.fastjson.JSON;
import com.wm.service.impl.pay.BarcodePayResponse;
import com.wm.service.impl.pay.PlatformBarcodePayResponse;
import com.wm.service.merchantinfo.MerchantInfoServiceI;
import com.wm.service.order.MerchantScanOrderServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.pay.BarcodePayServiceI;
import com.wm.service.supermarket.SuperMarketServiceI;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.common.UserInfo;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("ci/merchantScanOrderController")
public class MerchantScanOrderController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(SuperMarketController.class);

    @Autowired
    private BarcodePayServiceI wxBarcodePayService;

    @Autowired
    private MerchantInfoServiceI merchantInfoService;

    @Autowired
    private BarcodePayServiceI aliBarcodePayService;

    @Autowired
    private SuperMarketServiceI superMarketService;

    @Autowired
    private MerchantScanOrderServiceI merchantScanOrderService;

    @Autowired
    private OrderIncomeServiceI orderIncomeService;

    @Autowired
    private BarcodePayServiceI platformBarcodePayService;

    //通过商家ID 得到 商家类型
    public Integer getMerchantlatformType(Integer merchantId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.user_id, mi.platform_type from merchant m, 0085_merchant_info mi where m.id = mi.merchant_id and m.id = ? ");

        Map<String, Object> objectMap = merchantInfoService.findOneForJdbc(sql.toString(), merchantId);
        if (objectMap != null) {
            Integer platformType = (Integer) objectMap.get("platform_type");
            return platformType == null ? 0 : platformType;
        } else {
            return 0;
        }
    }

    private boolean isJiuZhou(Integer merchantId) {
        return getMerchantlatformType(merchantId) == 2;
    }

    @RequestMapping(params = "merchantOnWeixinPay")
    @ResponseBody
    public AjaxJson merchantOnWeixinPay(HttpServletRequest request, @RequestParam Integer merchantId, @RequestParam BigDecimal origin,
                                        @RequestParam String authCode, String remark, String deviceInfo, String spBillCreateIP) {
        AjaxJson ajaxJson = new AjaxJson();
        if (1 == 1) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("微信扫码收款暂时关闭");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        if (isJiuZhou(merchantId)) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("微信扫码收款暂时关闭");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }


        //微信限额
        BigDecimal limitMoney = merchantScanOrderService.getLimitMoney(merchantId, 1);//1：微信     2：支付宝
        if ("-1".equals(String.valueOf(limitMoney.compareTo(origin)))) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("微信收款限额" + limitMoney);
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        Integer orderId = merchantScanOrderService.createMerchantScanOrder(merchantId, origin, remark);
        if (orderId == 0) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("支付不成功！");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        try {
            logger.info("商家扫用户微信条形码支付, orderId:{}, authCode:{}, deviceInfo:{}, spBillCreateIP:{}",
                    orderId, authCode, deviceInfo, spBillCreateIP);
            String openId = wxBarcodePayService.getOpenId(authCode);
            Integer userId = 0;
            logger.info("openid:{}", openId);
            if (openId != null) {
                userId = getUserId(openId, request, new UserInfo());
                logger.info("根据openId:{},获取用户id:{}", openId, userId);
            }

            Map<String, String> params = new HashMap<String, String>();
            params.put("auth_code", authCode);
            if (StringUtils.isNotBlank(deviceInfo)) {
                params.put("device_info", deviceInfo);
            }
            if (StringUtils.isNotBlank(spBillCreateIP)) {
                params.put("spbill_create_ip", spBillCreateIP);
            }

            BarcodePayResponse response = wxBarcodePayService.payOrder(userId, orderId, params);
            if (response.getCode() == BarcodePayResponse.SUCCESS_CODE) {
                Map<String, Object> map = merchantScanOrderService.responseMap(orderId);
                logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(map));
                ajaxJson.setObj(map);
                ajaxJson.setStateCode("00");
                ajaxJson.setMsg("支付成功");
                ajaxJson.setSuccess(true);
            } else {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg("支付失败！");
                ajaxJson.setSuccess(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("系统异常");
            ajaxJson.setSuccess(false);
        }
        logger.info("商家扫用户微信条形码支付, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
        return ajaxJson;
    }

    @RequestMapping(params = "merchantOnAliPay")
    @ResponseBody
    public AjaxJson merchantOnAliPay(HttpServletRequest request, @RequestParam Integer merchantId, @RequestParam BigDecimal origin,
                                     @RequestParam String authCode, String remark) {
        AjaxJson ajaxJson = new AjaxJson();
        if (1 == 1) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("支付宝扫码收款暂时关闭");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        if (isJiuZhou(merchantId)) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("支付宝扫码收款暂时关闭");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        //微信限额
        BigDecimal limitMoney = merchantScanOrderService.getLimitMoney(merchantId, 2);//1：微信     2：支付宝
        if ("-1".equals(String.valueOf(limitMoney.compareTo(origin)))) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("支付宝收款限额" + limitMoney);
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        Integer orderId = merchantScanOrderService.createMerchantScanOrder(merchantId, origin, remark);
        if (orderId == 0) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("支付不成功！");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        try {
            logger.info("商家扫用户阿里条形码支付, orderId:{}, authCode:{}", orderId, authCode);
            Map<String, String> params = new HashMap<String, String>();
            params.put("auth_code", authCode);
            BarcodePayResponse response = aliBarcodePayService.payOrder(0, orderId, params);

            if (response.getCode() == BarcodePayResponse.SUCCESS_CODE) {
                Map<String, Object> map = merchantScanOrderService.responseMap(orderId);
                logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(map));
                ajaxJson.setObj(map);
                ajaxJson.setStateCode("00");
                ajaxJson.setMsg("支付成功");
                ajaxJson.setSuccess(true);
            } else {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg("支付失败");
                ajaxJson.setSuccess(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("系统异常");
            ajaxJson.setSuccess(false);
        }
        logger.info("商家扫用户阿里条形码支付, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
        return ajaxJson;
    }

    @RequestMapping(params = "merchantOnPlatformPay")
    @ResponseBody
    public AjaxJson merchantOnPlatformPay(Integer merchantId, BigDecimal origin, String authCode, String remark) {
        AjaxJson ajaxJson = new AjaxJson();
        try {

            if (isJiuZhou(merchantId)) {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg("一号生活扫码收款暂时关闭");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }

            logger.info("商家扫一号生活条形码支付, money:{}, authCode:{}", origin, authCode);
            Map<String, String> params = new HashMap<String, String>();
            params.put("barcodeNumber", authCode);
            params.put("merchantId", merchantId.toString());
            params.put("money", origin.toString());
            params.put("receive_from_source", "merchant");
            BarcodePayResponse response = platformBarcodePayService.payOrder(0, 0, params);

            if (response.getCode() == BarcodePayResponse.SUCCESS_CODE) {
                PlatformBarcodePayResponse platResponse = response.getResponse();
                Integer orderId = platResponse.getOrderId();
                Map<String, Object> map = merchantScanOrderService.responseMap(orderId);
                logger.info("商家扫一号生活条形码支付:" + JSON.toJSONString(map));
                ajaxJson.setObj(map);
                ajaxJson.setStateCode("00");
                ajaxJson.setMsg("支付成功");
                ajaxJson.setSuccess(true);
            } else {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg(response.getMsg());
                ajaxJson.setSuccess(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("系统异常");
            ajaxJson.setSuccess(false);
        }
        logger.info("商家扫一号生活条形码支付, authCode:{}, return:{}", authCode, JSON.toJSONString(ajaxJson));
        return ajaxJson;
    }


}
