package com.wm.controller.order;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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

import com.alibaba.fastjson.JSON;
import com.base.constant.ErrorCode;
import com.base.exception.BusinessException;
import com.wm.controller.order.dto.OrderFromSuperMarketDTO;
import com.wm.controller.pos.PosOrderController;
import com.wm.controller.user.vo.CashierVo;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenuVariationLogDTO;
import com.wm.entity.menu.MenuVo;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.CashierOrderEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.supermarket.CashierEntity;
import com.wm.service.impl.pay.BarcodePayResponse;
import com.wm.service.menu.MenuPackageServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.MenutypeServiceI;
import com.wm.service.menu.SuperMarketMenuServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.BarcodePayServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.service.user.CashierServiceI;
import com.wm.util.AliOcs;
import com.wm.util.IDistributedLock;
import com.wm.util.MemcachedDistributedLock;
import com.wm.util.PageList;

import net.spy.memcached.MemcachedClient;

@Controller
@RequestMapping("ci/superMarketController")
public class SuperMarketController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(SuperMarketController.class);

    @Autowired
    private BarcodePayServiceI wxBarcodePayService;

    @Autowired
    private BarcodePayServiceI aliBarcodePayService;

    @Autowired
    private BarcodePayServiceI platformBarcodePayService;

    @Autowired
    private SuperMarketServiceI superMarketService;

    @Autowired
    private CashierServiceI cashierService;
    @Autowired
    private PayServiceI payService;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private MenuPackageServiceI menuPackageService;
    @Autowired
    private MenuServiceI menuService;
    @Autowired
    private MenutypeServiceI menutypeService;
    @Autowired
    private SuperMarketMenuServiceI superMarketMenuService;

    /**
     * 微信条形码付款
     *
     * @param request
     * @param orderId
     * @param authCode
     * @param deviceInfo
     * @param spBillCreateIP
     * @return
     */
    @RequestMapping(params = "onWeixinPay")
    @ResponseBody
    public AjaxJson onWeixinPay(HttpServletRequest request, @RequestParam Integer orderId,
                                @RequestParam String authCode, String deviceInfo, String spBillCreateIP) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setMsg("付款成功");
        try {
            logger.info("超市订单微信条形码付款, orderId:{}, authCode:{}, deviceInfo:{}, spBillCreateIP:{}",
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
                OrderEntity order = orderService.get(OrderEntity.class, orderId);
                //结算订单保存结算日志记录
                if (OrderEntity.OrderType.SUPERMARKET_SETTLEMENT.equals(order.getOrderType())) {
                    CashierOrderEntity cashierOrder = orderService.get(CashierOrderEntity.class, orderId);
                    if (cashierOrder != null) {
                        payService.saveSettleMentLog(order, cashierOrder.getCashierId());
                        payService.printSettlement(order, cashierOrder.getCashierId());
                        ajaxJson.setMsg("结算成功");
                    } else {
                        logger.warn("无法根据结算订单ID{}找到收银员与订单关联信息....", orderId);
                    }
                }
                OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
                logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(oDto));
                ajaxJson.setObj(oDto);
                ajaxJson.setStateCode("00");
                ajaxJson.setSuccess(true);
            } else {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg("付款失败！");
                ajaxJson.setSuccess(false);
                logger.error("微信条形码付款失败， " + response.getMsg());
            }
        } catch (BusinessException e) {
            logger.error(e.getMessage());
            if (ErrorCode.INVALID_ARGUMENT == e.getErrCode() && "小票机绑定异常，请检查".equals(e.getMessage())) {
                ajaxJson.setSuccess(true);
                ajaxJson.setStateCode("00");
                ajaxJson.setMsg(e.getMessage());
                logger.info("结算成功,结算订单:{}, 小票机异常", orderId);
            } else {
                ajaxJson = AjaxJson.failJson(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setStateCode("02");
            ajaxJson.setMsg("系统异常");
            ajaxJson.setSuccess(false);
        }
        logger.info("超市订单微信条形码付款, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
        return ajaxJson;
    }

    /**
     * 支付宝条形码付款
     *
     * @param request
     * @param orderId
     * @param authCode
     * @return
     */
    @RequestMapping(params = "onAliPay")
    @ResponseBody
    public AjaxJson onAliPay(HttpServletRequest request, @RequestParam Integer orderId,
                             @RequestParam String authCode) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setMsg("付款成功");
        
        /*boolean flag = true;
        if(flag){
	        // 暂停使用光曦支付宝账号20170111 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!        ////////////////////////////////
        	ajaxJson.setStateCode("02");
        	ajaxJson.setMsg("系统维护中，请稍后再试！");
	        ajaxJson.setSuccess(false);
	        return ajaxJson;
        }*/
        
        try {
            logger.info("超市订单支付宝条形码付款, orderId:{}, authCode:{}", orderId, authCode);
            Map<String, String> params = new HashMap<String, String>();
            params.put("auth_code", authCode);
            BarcodePayResponse response = aliBarcodePayService.payOrder(0, orderId, params);

            if (response.getCode() == BarcodePayResponse.SUCCESS_CODE) {
                OrderEntity order = orderService.get(OrderEntity.class, orderId);
                if (OrderEntity.OrderType.SUPERMARKET_SETTLEMENT.equals(order.getOrderType())) {
                    CashierOrderEntity cashierOrder = orderService.get(CashierOrderEntity.class, orderId);
                    if (cashierOrder != null) {
                        payService.saveSettleMentLog(order, cashierOrder.getCashierId());
                        payService.printSettlement(order, cashierOrder.getCashierId());
                        ajaxJson.setMsg("结算成功");
                    } else {
                        logger.warn("无法根据结算订单ID{}找到收银员与订单关联信息....", orderId);
                    }
                }
                OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
                logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(oDto));
                ajaxJson.setObj(oDto);
                ajaxJson.setStateCode("00");
                ajaxJson.setSuccess(true);
            } else {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg("付款失败");
                ajaxJson.setSuccess(false);
            }
        } catch (BusinessException e) {
            ajaxJson = AjaxJson.failJson(e.getMessage());
            logger.error(e.getMessage());
            if (ErrorCode.INVALID_ARGUMENT == e.getErrCode() && "小票机绑定异常，请检查".equals(e.getMessage())) {
                ajaxJson.setSuccess(true);
                ajaxJson.setStateCode("00");
                ajaxJson.setMsg(e.getMessage());
                logger.info("结算成功,结算订单:{}, 小票机异常", orderId);
            } else {
                ajaxJson = AjaxJson.failJson(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setStateCode("02");
            ajaxJson.setMsg("系统异常");
            ajaxJson.setSuccess(false);
        }
        logger.info("超市订单支付宝条形码付款, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
        return ajaxJson;
    }

    /**
     * 1号生活条形码付款
     *
     * @param request
     * @param orderId
     * @param authCode
     * @return
     */
    @RequestMapping(params = "onPlatformPay")
    @ResponseBody
    public AjaxJson onPlatformPay(HttpServletRequest request, @RequestParam Integer orderId,
                                  @RequestParam String authCode) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setMsg("付款成功");
        try {
            logger.info("超市订单1号生活平台条形码付款, orderId:{}, authCode:{}", orderId, authCode);
            Map<String, String> params = new HashMap<String, String>();
            params.put("barcodeNumber", authCode);
            BarcodePayResponse response = platformBarcodePayService.payOrder(0, orderId, params);

            if (response.getCode() == BarcodePayResponse.SUCCESS_CODE) {
                OrderEntity order = orderService.get(OrderEntity.class, orderId);
                if (OrderEntity.OrderType.SUPERMARKET_SETTLEMENT.equals(order.getOrderType())) {
                    CashierOrderEntity cashierOrder = orderService.get(CashierOrderEntity.class, orderId);
                    if (cashierOrder != null) {
                        payService.saveSettleMentLog(order, cashierOrder.getCashierId());
                        payService.printSettlement(order, cashierOrder.getCashierId());
                        ajaxJson.setMsg("结算成功");
                    } else {
                        logger.warn("无法根据结算订单ID{}找到收银员与订单关联信息....", orderId);
                    }
                }
                OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
                oDto.setMemberDiscountMoney(Double.valueOf(response.getResponse().getBody().get("memberDiscountMoney").toString()));
                oDto.setMinusDiscountMoney(Double.valueOf(response.getResponse().getBody().get("minusDiscountMoney").toString()));
                oDto.setActuallyPaid(order.getCredit());
                logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(oDto));
                ajaxJson.setObj(oDto);
                ajaxJson.setStateCode("00");
                ajaxJson.setSuccess(true);
            } else {
                ajaxJson.setStateCode("01");
                ajaxJson.setMsg("付款失败");
                ajaxJson.setSuccess(false);
                logger.error(response.getMsg() + ", orderId:{}, authCode:{}", orderId, authCode);
            }
        } catch (BusinessException e) {
            ajaxJson = AjaxJson.failJson(e.getMessage());
            logger.error(e.getMessage());
            if (ErrorCode.INVALID_ARGUMENT == e.getErrCode() && "小票机绑定异常，请检查".equals(e.getMessage())) {
                ajaxJson.setSuccess(true);
                ajaxJson.setStateCode("00");
                ajaxJson.setMsg(e.getMessage());
                logger.info("结算成功,结算订单:{}, 小票机异常", orderId);
            } else {
                ajaxJson = AjaxJson.failJson(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setStateCode("02");
            ajaxJson.setMsg("系统异常");
            ajaxJson.setSuccess(false);
        }
        logger.info("超市订单1号生活平台条形码付款, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
        return ajaxJson;
    }

    private final static int EXPIRE = 60;
    private final static int TRY_TIME = 30;

    /**
     * 下单打印
     *
     * @param orderId
     * @return
     */
    @RequestMapping(params = "concurrnetOrderPrint")
    @ResponseBody
    public AjaxJson concurrnetOrderPrint(Integer orderId, String accessToken) {
        String lockName = "concurrnetOrderPrint&" + orderId;
        logger.info(" lockName is " + lockName);
        AjaxJson j = new AjaxJson();
        String uuid = null;
        IDistributedLock lock = new MemcachedDistributedLock();
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在打印订单【{}】进行操作， 获取不到锁", orderId);
                j.setStateCode("01");
                j.setMsg("其它线程也在打印订单【" + orderId + "】进行操作， 获取不到锁");
                j.setSuccess(false);
                return j;
            }

            MemcachedClient client = AliOcs.getClient();
            Object o = client.get(lockName + "&result");
            if (o == null) {
                AjaxJson result = getSuperMarletOrderDetail(orderId);
                if (result.isSuccess()) {
                    client.add(lockName + "&result", EXPIRE * 60 * 12, accessToken);
                }
                return result;
            } else {
                j.setMsg("订单已经被其他客户端打印");
                j.setSuccess(false);
                j.setStateCode("02");
            }
        } catch (Exception e) {
            logger.error("打印订单失败", e);
            j.setMsg("订单已经被其他客户端打印");
            j.setSuccess(false);
            j.setStateCode("02");
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }
        return j;
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(params = "getSuperMarletOrderDetail")
    @ResponseBody
    public AjaxJson getSuperMarletOrderDetail(@RequestParam Integer orderId) {
        AjaxJson json = new AjaxJson();
        logger.info("获取超市订单" + orderId + "详情");
        try {
            OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
            if (oDto == null) {
                logger.warn("未找到超市订单" + orderId + "详情");
                json.setMsg("未找到该订单详情");
                json.setStateCode("02");
                json.setSuccess(false);
                return json;
            }
            json.setObj(oDto);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("获取订单详情成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("获取订单详情失败");
        }
        return json;
    }
    
    public static <E extends Map<?, ?>> E toMapByDeclaredFields(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<Object, Object> map = new HashMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.debug("toMap failed in fieldName : " + field.getName(), e);
			}
		}
		return (E) map;
	}

    /**
     * 更新订单付款方式
     *
     * @param orderId 订单id
     * @param payType 付款方式
     * @return
     */
    @RequestMapping(params = "paySupermarketOrder")
    @ResponseBody
    public AjaxJson paySupermarketOrder(@RequestParam Integer orderId, @RequestParam Integer cashierId, @RequestParam String payType, String cash) {
        AjaxJson json = new AjaxJson();
        logger.info("更新订单" + orderId + "付款方式为：" + payType);
        try {
            superMarketService.paySupermarketOrder(orderId, cashierId, payType, 0);
            Map<String, Object> resultMap = superMarketService.calChange(orderId, Double.valueOf(cash));
            OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
            Map<String, Object> o = toMapByDeclaredFields(oDto);
            o.putAll(resultMap);
            json.setObj(o);
            json.setMsg("订单付款成功");
            json.setSuccess(true);
            json.setStateCode("00");
            logger.info("超市订单现金付款成功，订单id:{}", orderId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            json.setMsg(e.getMessage());
            json.setSuccess(false);
            json.setStateCode("02");
            logger.warn("超市订单现金付款失败，{}，订单id:{}", e.getMessage(), orderId);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("订单更新失败");
            json.setSuccess(false);
            json.setStateCode("01");
            logger.warn("超市订单现金付款失败，订单id:{}", orderId);
        }
        return json;
    }


    /**
     * 创建结算订单
     *
     * @param cashierId
     * @return
     */
    @RequestMapping(params = "createSettlementOrder")
    @ResponseBody
    public AjaxJson createSettlementOrder(@RequestParam Integer cashierId) {
        AjaxJson json = new AjaxJson();
        logger.info("创建现金订单结算的结算订单, cashierId:{}", cashierId);
        try {
            OrderEntity order = superMarketService.createSettlementOrder(cashierId);

            Map<String, Object> orderStatistics = superMarketService.statisticAfterSettlement(cashierId);
            String key = "settle_" + order.getId() + "_" + cashierId;
            AliOcs.set(key, orderStatistics, 20 * 60);

            CashierVo cashier = cashierService.get(cashierId);

            DecimalFormat df = new DecimalFormat("#0.00");
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totalMoney", df.format(order.getOrigin()));
            result.put("orderId", order.getId());
            result.put("orderType", order.getOrderType());
            result.put("description", order.getTitle());
            result.put("cashierName", cashier.getName());
            result.putAll(orderStatistics);

            json.setObj(result);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("创建现金结算订单成功");
        } catch (BusinessException e) {
            json = AjaxJson.failJson(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("创建现金结算订单失败");
        }
        logger.info("创建现金订单结算的结算订单, return:{}", JSON.toJSONString(json));
        return json;
    }

    @RequestMapping(params = "cashierSettlement")
    @ResponseBody
    public AjaxJson cashierSettlement(@RequestParam Integer cashierId, @RequestParam String cash, @RequestParam String state) {
        AjaxJson json = new AjaxJson();
        logger.info("创建现金订单结算的结算订单, cashierId:{}, cash:{}, state:{}", cashierId, cash, state);
        try {
            if (0 > Double.parseDouble(cash)) {
                json.setSuccess(false);
                json.setStateCode("02");
                json.setMsg("请输入正确的金额");
                return json;
            }
            Map<String, Object> result = superMarketService.cashierSettlement(cashierId, cash, state);
            json.setObj(result);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("创建现金结算订单成功");
        } catch (BusinessException e) {
            json = AjaxJson.failJson(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("创建现金结算订单失败");
        }
        logger.info("创建现金订单结算的结算订单, return:{}", JSON.toJSONString(json));
        return json;
    }


    /**
     * 收银员登录时存在未付款结算订单，获取相应的详情（POS1.6）
     *
     * @param cashierId       收银员ID
     * @param settlementLogId 结算日志ID
     * @return
     */
    @RequestMapping(params = "getSettlementInfo")
    @ResponseBody
    public AjaxJson getSettlementInfo(Integer cashierId, Integer settlementLogId) {
        logger.info("收银员{}获取结算订单{}详情", cashierId, settlementLogId);
        AjaxJson json = new AjaxJson();
        try {
            Map<String, Object> result = superMarketService.getSettlementInfo(cashierId, settlementLogId);
            json.setObj(result);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("获取现金结算详情成功");
        } catch (BusinessException e) {
            e.printStackTrace();
            json = AjaxJson.failJson(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("获取现金结算详情失败");
        }
        return json;
    }

//	@RequestMapping(params = "onWeixinWftPay")
//	@ResponseBody
//	public AjaxJson onWeixinWftPay(HttpServletRequest request, @RequestParam Integer orderId, 
//			@RequestParam String authCode, String deviceInfo, String spBillCreateIP){
//		AjaxJson ajaxJson = new AjaxJson();
//		try {
//			logger.info("超市订单微信条形码付款, orderId:{}, authCode:{}, deviceInfo:{}, spBillCreateIP:{}",
//					orderId, authCode, deviceInfo, spBillCreateIP);
//			String openId = wxBarcodePayService.getOpenId(authCode);
//			Integer userId = 0;
//			logger.info("openid:{}", openId);
//			if(openId != null){
//				userId = getUserId(openId, request, new UserInfo());
//				logger.info("根据openId:{},获取用户id:{}", openId, userId);
//			}
//			
//			
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("service", "pay.weixin.micropay");
//			params.put("auth_code", authCode);
//			if(StringUtils.isNotBlank(deviceInfo)){
//				params.put("device_info", deviceInfo);
//			}
//			if(StringUtils.isNotBlank(spBillCreateIP)){
//				params.put("spbill_create_ip", spBillCreateIP);
//			}
//			
//			BarcodePayResponse response = wftBarcodePayService.payOrder(userId, orderId, params);
//			if(response.getCode() == BarcodePayResponse.SUCCESS_CODE){
//				OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
//				logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(oDto));
//				ajaxJson.setObj(oDto);
//				ajaxJson.setStateCode("00");
//				ajaxJson.setMsg("付款成功");
//				ajaxJson.setSuccess(true);
//			}
//			else {
//				ajaxJson.setStateCode("01");
//				ajaxJson.setMsg("付款失败！");
//				ajaxJson.setSuccess(false);
//			}
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//			ajaxJson.setStateCode("02");
//			ajaxJson.setMsg("系统异常");
//			ajaxJson.setSuccess(false);
//		}
//		logger.info("超市订单微信条形码付款, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
//		return ajaxJson;
//	}
//	
//	@RequestMapping(params = "onAliWftPay")
//	@ResponseBody
//	public AjaxJson onAliWftPay(HttpServletRequest request, @RequestParam Integer orderId, 
//			@RequestParam String authCode){
//		AjaxJson ajaxJson = new AjaxJson();
//		try {
//			logger.info("超市订单微信条形码付款, orderId:{}, authCode:{}", orderId, authCode);
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("service", "pay.alipay.micropay");
//			params.put("auth_code", authCode);
//			BarcodePayResponse response = wftBarcodePayService.payOrder(0, orderId, params);
//			
//			if(response.getCode() == BarcodePayResponse.SUCCESS_CODE){
//				OrderFromSuperMarketDTO oDto = superMarketService.getSuperMarketOrderDetail(orderId);
//				logger.info("OrderFromSuperMarketDTO:" + JSON.toJSONString(oDto));
//				ajaxJson.setObj(oDto);
//				ajaxJson.setStateCode("00");
//				ajaxJson.setMsg("付款成功");
//				ajaxJson.setSuccess(true);
//			}
//			else {
//				ajaxJson.setStateCode("01");
//				ajaxJson.setMsg("付款失败");
//				ajaxJson.setSuccess(false);
//			}
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//			ajaxJson.setStateCode("02");
//			ajaxJson.setMsg("系统异常");
//			ajaxJson.setSuccess(false);
//		}
//		logger.info("超市订单微信条形码付款, orderId:{}, authCode:{}, return:{}", orderId, authCode, JSON.toJSONString(ajaxJson));
//		return ajaxJson;
//	}

    /**
     * 判断收银员是否有结算订单
     *
     * @param cashierId 收银员ID
     * @return
     */
    @RequestMapping(params = "isHaveCashOrder")
    @ResponseBody
    public AjaxJson isHaveCashOrder(Integer cashierId) {
        AjaxJson json = new AjaxJson();
        try {
            Map<String, Object> resultMap = superMarketService.isHaveCashOrder(cashierId);
            if (resultMap.isEmpty()) {
                json.setSuccess(false);
                json.setStateCode("02");
                json.setMsg("没有待结算的现金订单");
                return json;
            }
            json.setObj(resultMap);
            json.setSuccess(true);
            json.setMsg("有现金订单需要结算");
            json.setStateCode("00");

        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("判断收银员是否有结算订单出错");
        }

        return json;
    }

    /**
     * 店长上、下架商品并修改商品信息
     *
     * @param menuVo    商品信息
     * @param cashierId 收银员id
     * @param menuId    商品id
     * @return
     */
    @RequestMapping(params = "updateMenuStatus")
    @ResponseBody
    public AjaxJson updateMenuStatus(MenuVo menuVo, @RequestParam Integer cashierId, @RequestParam Integer menuId) {
        logger.info("menuVO:{}", JSON.toJSONString(menuVo));
        String display = menuVo.getDisplay();
        Integer merchantId = menuVo.getMerchantId();
        menuVo.setId(menuId);
        menuVo.setIsDelete("N");

        MenuEntity menuEntity = menuService.get(MenuEntity.class, menuId);
        MenuVariationLogDTO menuInstock = new MenuVariationLogDTO();
        menuInstock.setAddRepertory(menuVo.getTodayRepertory() == null ? menuEntity.getTodayRepertory() : menuVo.getTodayRepertory());
        menuInstock.setCashierId(cashierId);
        menuInstock.setBarcode(menuVo.getBarcode() == null ? menuEntity.getBarcode() : menuVo.getBarcode());
        menuInstock.setDisplay(menuVo.getDisplay());
        menuInstock.setPrice(menuVo.getPrice() == null ? menuEntity.getPrice() : menuVo.getPrice());
        menuInstock.setName(menuVo.getName() == null ? menuEntity.getName() : menuVo.getName());
        menuInstock.setMenuId(menuId);
        menuInstock.setMerchantId(merchantId);
        menuInstock.setTypeId(menuEntity.getMenuType().getId());
        menuInstock.setIsonline(menuEntity.getIsonline().toString());
        logger.info("上下架商品,商家id:{},商品id:{},上下架状态:{},收银员id:{} ", merchantId, menuId, display, cashierId);
        AjaxJson json = new AjaxJson();
        try {
            //校验收银员权限
            CashierEntity cashierEntity = superMarketService.get(CashierEntity.class, cashierId);
            if (2 != cashierEntity.getCashierType().intValue()) {
                logger.warn("收银员：{}不是店长", cashierId);
                json.setMsg("店长才有权限上下架商品");
                json.setSuccess(false);
                json.setStateCode("03");
                return json;
            }
            //上架时更新商品信息
            if ("Y".equals(display)) {
                if (menuEntity == null) {
                    json.setMsg("找不到该商品");
                    json.setSuccess(false);
                    json.setStateCode("01");
                    return json;
                }
                if (!menuEntity.getBarcode().equals(menuVo.getBarcode())) {
                    json.setMsg("商品条形码不可以修改");
                    json.setSuccess(false);
                    json.setStateCode("01");
                    return json;
                }
                if (!menuEntity.getName().equals(menuVo.getName())) {
                    if (menuVo.getName() == null || menuVo.getName() == "") {
                        json.setMsg("商品名称不能为空哦");
                        json.setSuccess(false);
                        json.setStateCode("01");
                        return json;
                    }
                    if (menuVo.getName().length() > 15) {
                        json.setMsg("商品名称不能超过15个汉字哦");
                        json.setSuccess(false);
                        json.setStateCode("01");
                        return json;
                    }
                }
                List<Map<String, Object>> list = menuService.getAnotherMenuByBarcode(menuVo);
                if (CollectionUtils.isNotEmpty(list)) {
                    logger.error("已经存在相同名称的商品，请重新编辑");
                    json.setMsg("已经存在相同名称的商品，请重新编辑");
                    json.setSuccess(false);
                    json.setStateCode("04");
                    return json;
                }

//				menuService.updateMenu(menuVo);
                //上架的时候校验库存
//				Map<String,Object> stockNum = menuPackageService.selectTpmPackStock(merchantId,menuId);
//				if (Integer.parseInt(stockNum.get("today_repertory").toString()) == 0) {
//					json.setMsg("该菜品的库存为零，请先在“编辑”中添加库存!");
//					json.setSuccess(false);
//					json.setStateCode("02");
//					return json;	
//				}
            }
            //商品上下架
            Map<String, Object> map = superMarketMenuService.menuVariation(menuInstock, 1, 2);
            if (!map.get("auditType").toString().equals("1")) {
                menuVo = new MenuVo();
                menuVo.setId(menuId);
            }
//			menuPackageService.updateTpmPackageByStatus(merchantId, menuId, display);
            if ("Y".equals(display)) {
                PageList<com.wm.entity.menu.MenuVo> list = menuService.findByEntityList(menuVo, null, null);
                json.setObj(list.getResultList().get(0));
            }
            logger.info("商品上/下架操作成功，menuId:{}, display:{}", menuId, display);
            json.setMsg("操作成功!");
            json.setSuccess(true);
            json.setStateCode("00");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("商品上/下架操作失败，menuId:{}, display:{}", menuId, display);
            json.setMsg("操作失败!");
            json.setSuccess(false);
            json.setStateCode("01");
        }
        return json;
    }


    @RequestMapping(params = "franchiseSettle")
    @ResponseBody
    public AjaxJson franchiseSettle(@RequestParam Integer merchantId, @RequestParam Integer cashierId, @RequestParam Integer orderId) {
        logger.info("加盟店现金订单结算,商家Id:{},收银员id:{},结算订单id:{}", merchantId, cashierId, orderId);
        AjaxJson json = new AjaxJson();
        try {
            MerchantInfoEntity merchantInfoEntity = superMarketService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
            if (1 != merchantInfoEntity.getShopFromType().intValue()) {
                json.setSuccess(false);
                json.setStateCode("02");
                json.setMsg("不是加盟店");
                logger.error("不是加盟店,商家id:{}", merchantId);
                return json;
            }
            //没有现金订单是，打印小票和保存日志
            if (orderId.intValue() == -1) {
                OrderEntity order = new OrderEntity();
                order.setId(orderId);
                MerchantEntity m = superMarketService.get(MerchantEntity.class, merchantId);
                order.setMerchant(m);
                order.setOrigin(0.00);
                payService.saveSettleMentLog(order, cashierId);
                payService.printSettlement(order, cashierId);
                json.setSuccess(true);
                json.setStateCode("00");
                json.setMsg("结算成功");
                return json;
            }
            OrderEntity orderEntity = superMarketService.get(OrderEntity.class, orderId);
            if (orderEntity == null) {
                logger.error("找不到对应的结算订单，orderId：{}", orderId);
                json.setSuccess(false);
                json.setStateCode("03");
                json.setMsg("找不到对应的结算订单");
                return json;
            }
            if (OrderEntity.State.CONFIRM.equals(orderEntity.getState())) {
                logger.error("该结算订单已结算，orderId：{}", orderId);
                json.setSuccess(false);
                json.setStateCode("03");
                json.setMsg("订单已结算");
                return json;
            }
            superMarketService.franchiseSettle(orderEntity);
            payService.saveSettleMentLog(orderEntity, cashierId);
            payService.printSettlement(orderEntity, cashierId);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("结算成功");
            logger.info("结算成功,结算订单:{}", orderId);
        } catch (BusinessException e) {
            logger.error(e.getMessage());
            if (ErrorCode.INVALID_ARGUMENT == e.getErrCode() && "小票机绑定异常，请检查".equals(e.getMessage())) {
                json.setSuccess(true);
                json.setStateCode("00");
                json.setMsg(e.getMessage());
                logger.info("结算成功,结算订单:{}, 小票机异常", orderId);
            } else {
                json = AjaxJson.failJson(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("结算失败");
            logger.error("结算失败,结算订单:{}", orderId);
        }
        return json;
    }

    /**
     * 查询商家商品列表（支持模糊查询）
     *
     * @param merchantId
     * @param menuTypeId 商品分类ID
     * @param barcode    商品二维码
     * @param name       商品名称
     * @param isonline   1：只支持线上，2：只支持线下，3：同时支持线上线下，以逗号拼接
     * @return
     */
    @RequestMapping(params = "findAllMenus")
    @ResponseBody
    public AjaxJson findAllMenus(Integer merchantId, Integer menuTypeId, String barcode, String name, String isonline, Integer page, Integer rows, String display, Integer updateTime) {
        logger.info("商家：{}, 获取未删除的商品, menuTypeId:{}, barcode:{}, name:{}, isonline:{}, display:{}, page:{}, rows:{}", merchantId, menuTypeId, barcode, name, isonline, display, page, rows);
        MenuVo menu = new MenuVo();
        menu.setMerchantId(merchantId);
        menu.setTypeId(menuTypeId);
        menu.setBarcode(barcode);
        menu.setName(name);
        menu.setIsonline(isonline);
        menu.setDisplay(display);
        menu.setIsDelete("N");
        if (updateTime != null && updateTime > 0) {
            menu.setUpdateTime(updateTime);
        }

        AjaxJson j = new AjaxJson();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("currentTime", System.currentTimeMillis() / 1000);
        j.setAttributes(objectMap);
        try {
        	PageList<com.wm.entity.menu.MenuVo> list = null;
        	//如果是促销商品分类
        	List<Integer> activityIds = menutypeService.getPromotionActivityIds(merchantId);
        	if(0 == menuTypeId){
        		list = menuService.findByPromotionEntityList(menu, activityIds, page, rows);
        	} else {
        		list = menuService.findByEntityList(menu, activityIds, page, rows);
        	}
            j.setObj(list);
            j.setStateCode("00");
            j.setSuccess(true);
            j.setMsg("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            j.setStateCode("01");
            j.setSuccess(false);
            j.setMsg("操作失败");
        }

        return j;
    }


    @RequestMapping(params = "updateMenuInfo")
    @ResponseBody
    public AjaxJson updateMenuInfo(MenuVo menuVo, @RequestParam Integer cashierId) {
        AjaxJson json = new AjaxJson();
        try {
            CashierEntity cashierEntity = superMarketService.get(CashierEntity.class, cashierId);
            if (2 != cashierEntity.getCashierType().intValue()) {
                json.setMsg("店长才有权限上下架商品");
                json.setSuccess(false);
                json.setStateCode("03");
                return json;
            }
            menuService.updateMenu(menuVo);
            json.setMsg("操作成功！");
            json.setSuccess(true);
            json.setStateCode("00");
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("操作失败！");
            json.setSuccess(false);
            json.setStateCode("01");
        }
        return json;
    }

    /**
     * 根据商家ID获取所有商品的商品分类
     *
     * @param merchantId 商家ID
     * @return
     */
    @RequestMapping(params = "findAllMenuTypes")
    @ResponseBody
    public AjaxJson findAllMenuTypes(@RequestParam Integer merchantId, @RequestParam(defaultValue = "1") int type,@RequestParam(defaultValue = "1") int sysType) {
        logger.info("商家：{}，获取商品类型，type:{}", merchantId, type);
        AjaxJson json = new AjaxJson();
        try {
            List<MenutypeEntity> resultList = new ArrayList<MenutypeEntity>();
            if (type == 2) {
                resultList = menutypeService.getTypeByMerchantId(merchantId, 2);
            } else {
                resultList = menutypeService.getTypeByMerchantId(merchantId, 1);
            }
            // sysType = 1, 包含,促销商品（系统）分类
            if(1 == sysType){
            	List<Integer> activityIds = menutypeService.getPromotionActivityIds(merchantId);
            	if(CollectionUtils.isNotEmpty(activityIds)){
            		MenutypeEntity menutypeEntity = new MenutypeEntity();
            		menutypeEntity.setId(0);
            		menutypeEntity.setName("促销商品（系统）");
            		menutypeEntity.setSortNum(0);
            		menutypeEntity.setMerchantId(merchantId);
            		resultList.add(0,menutypeEntity);
            	}
            }
            json.setObj(resultList);
            json.setMsg("获取商品类型成功");
            json.setSuccess(true);
            json.setStateCode("00");
            logger.info("商家：{}，获取商品类型成功", merchantId);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("获取商品类型失败");
            json.setSuccess(false);
            json.setStateCode("01");
            logger.error("商家：{}，获取商品类型失败", merchantId);
        }
        return json;
    }

    /**
     * 根据商品id获取商品详情
     *
     * @param menuId 商品id
     * @return
     */
    @RequestMapping(params = "getMenuById")
    @ResponseBody
    public AjaxJson getMenuById(@RequestParam Integer menuId) {
        logger.info("获取商品:{}, 的详细信息", menuId);
        AjaxJson json = new AjaxJson();
        try {
            MenuEntity menu = menuService.get(MenuEntity.class, menuId);
            if (menu == null) {
                json.setSuccess(false);
                json.setStateCode("02");
                json.setMsg("没有找到该商品");
                logger.error("没有找到该商品：", menuId);
                return json;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("barcode", menu.getBarcode());
            map.put("name", menu.getName());
            map.put("todayRepertory", menu.getTodayRepertory());
            map.put("price", menu.getPrice());
            map.put("id", menu.getId());
            map.put("merchantId", menu.getMerchantId());
            json.setObj(map);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("操作成功");
            logger.info("获取商品:{}, 的详细信息成功", menuId);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("操作失败");
            logger.error("获取商品:{}, 的详细信息失败", menuId);
        }
        return json;
    }

    /**
     * 一键结算退出，创建订单(POS1.7)
     *
     * @param cashierId              收银员ID
     * @param cash                   清点的现金
     * @param settleState            结算状态（normal表正常结算，login表为正常结算，强制退出）
     * @param cashierSettlementLogId 结算日志（可选，当settleState为login是传）
     * @return
     */
    @RequestMapping(params = "settlementAndExit")
    @ResponseBody
    public AjaxJson settlementAndExit(@RequestParam Integer cashierId, @RequestParam String cash, @RequestParam String settleState, Integer cashierSettlementLogId) {
        AjaxJson json = new AjaxJson();
        logger.info("创建现金订单结算的结算订单, cashierId:{}, cash:{}, state:{}, cashierSettlementLogId:{}", cashierId, cash, settleState, cashierSettlementLogId);
        try {
            if (0 > Double.parseDouble(cash)) {
                json.setSuccess(false);
                json.setStateCode("02");
                json.setMsg("请输入正确的金额");
                return json;
            }
            Map<String, Object> result = superMarketService.settlementAndExit(cashierId, cash, settleState, cashierSettlementLogId);
            json.setObj(result);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("创建现金结算订单成功");
        } catch (BusinessException e) {
            json = AjaxJson.failJson(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("创建现金结算订单失败");
        }
        logger.info("创建现金订单结算的结算订单, return:{}", JSON.toJSONString(json));
        return json;
    }

    /**
     * 直营店结算交班没有现金订单，直接结算(POS1.7)
     *
     * @param merchantId 商家ID
     * @param cashierId  收银员ID
     * @param orderId    结算订单ID
     * @return
     */
    @RequestMapping(params = "directSettlement")
    @ResponseBody
    public AjaxJson directSettlement(@RequestParam Integer merchantId, @RequestParam Integer cashierId, @RequestParam Integer orderId) {
        AjaxJson json = new AjaxJson();
        logger.info("直营店收银员无现金结算，merchantId:{}, cashierId:{}, orderId:{}", merchantId, cashierId, orderId);
        try {
            //没有现金订单是，打印小票和保存日志
            if (orderId.intValue() == -1) {
                OrderEntity order = new OrderEntity();
                order.setId(orderId);
                MerchantEntity m = superMarketService.get(MerchantEntity.class, merchantId);
                order.setMerchant(m);
                order.setOrigin(0.00);
                payService.saveSettleMentLog(order, cashierId);
                payService.printSettlement(order, cashierId);

                json.setSuccess(true);
                json.setStateCode("00");
                json.setMsg("结算成功");
            }
        } catch (BusinessException e) {
            logger.error(e.getMessage());
            if (ErrorCode.INVALID_ARGUMENT == e.getErrCode() && "小票机绑定异常，请检查".equals(e.getMessage())) {
                json.setSuccess(true);
                json.setStateCode("00");
                json.setMsg(e.getMessage());
                logger.info("结算成功,结算订单:{}, 小票机异常", orderId);
            } else {
                json = AjaxJson.failJson(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setStateCode("01");
            json.setMsg("结算失败");
        }
        return json;

    }

    /**
     * 超市订单付款前校验订单金额（POS1.8）
     *
     * @param orderId 订单id
     * @param payType 付款方式
     * @return
     */
    @RequestMapping(params = "checkMaxPayMoney")
    @ResponseBody
    public AjaxJson checkMaxPayMoney(@RequestParam Integer orderId, @RequestParam String payType) {
        logger.info("超市订单付款前校验订单金额, orderId:{}, payType:{}", orderId, payType);
        AjaxJson json = new AjaxJson();
        try {
            superMarketService.checkMaxPayMoney(orderId, payType);
            json.setSuccess(true);
            json.setStateCode("00");
            json.setMsg("校验通过");
        } catch (BusinessException e) {
            logger.error("超市订单金额校验" + e.getMessage());
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            json.setStateCode("02");
        } catch (Exception e) {
            logger.error("超市订单金额校验失败");
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("校验失败");
            json.setStateCode("01");
        }
        return json;

    }


    @Autowired
    private  PosOrderController posOrderController;

    @RequestMapping(params = "uploadOfflineOrders")
    @ResponseBody
    public AjaxJson uploadOfflineOrders(HttpServletRequest request, String params) {
        return posOrderController.uploadOfflineOrders(request, params);
    }
}

