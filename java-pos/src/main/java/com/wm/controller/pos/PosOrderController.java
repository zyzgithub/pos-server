package com.wm.controller.pos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.exception.BusinessException;
import com.wm.controller.order.dto.OrderFromSuperMarketDTO;
import com.wm.entity.pos.OfflineOrder;
import com.wm.entity.pos.OrderMenuParams;
import com.wm.service.impl.menu.SuperMarketMenuServiceImpl;
import com.wm.service.impl.supermarket.SuperMarketServiceImpl;
import com.wm.service.menu.SuperMarketMenuServiceI;
import com.wm.service.order.MongoService;
import com.wm.service.supermarket.SuperMarketServiceI;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.RecursiveTask;

/**
 * Created by zc on 2016/11/23.
 */
@Controller
@RequestMapping("ci/posOrderController")
public class PosOrderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PosOrderController.class);
    @Autowired
    private MongoService mongoService;
    @Autowired
    private SuperMarketServiceI superMarketService;

    @RequestMapping(params = "uploadOfflineOrders")
    @ResponseBody
    public AjaxJson uploadOfflineOrders(HttpServletRequest request, String params) {
        // Integer merchantId, Integer cashierId, String params, String version
        AjaxJson j = new AjaxJson();
        logger.info("开始创建超市订单, params :" + params);
        try {
            long createTime = System.currentTimeMillis() / 1000;
            final List<OfflineOrder> offlineOrderList = JSONArray.parseArray(params, OfflineOrder.class);
            for (OfflineOrder offlineOrder : offlineOrderList) {
                if (offlineOrder.getCreateTime() < createTime) {
                    createTime = offlineOrder.getCreateTime();
                }
                if (org.springframework.util.StringUtils.isEmpty(offlineOrder.getUuid())) {
                    offlineOrder.setState(3);
                }

            }

            mongoService.insertAll(offlineOrderList);
            createOrder(offlineOrderList);
            logger.info("创建超市订单, return:{}", JSON.toJSONString(j));
            j.setObj(createTime);

            j.setSuccess(true);
            j.setMsg("订单上传成功");
        } catch (Exception e) {
            j.setMsg("订单上传失败");
            j.setStateCode("01");
            j.setSuccess(false);
            logger.warn("离线超市订单上传失败");
            e.printStackTrace();

        }
        return j;
    }

    private void createOrder(final List<OfflineOrder> offlineOrderList) {
        final RecursiveTask<Boolean> recursiveTask = new RecursiveTask<Boolean>() {
            @Override
            protected Boolean compute() {
                try {
                    doCreateOrder(offlineOrderList);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        ForkJoinPoolCenter.submit(recursiveTask);
    }

    private void doCreateOrder(List<OfflineOrder> offlineOrderList) {
        String paramJson;
        for (OfflineOrder offlineOrder : offlineOrderList) {
            paramJson = JSONObject.toJSONString(offlineOrder.getParams());
            OrderFromSuperMarketDTO oDto = null;
            try {
                // 兼容前端错误.
                int createTime = (int) (offlineOrder.getCreateTime());
                offlineOrder = mongoService.getMongoTemplate().findOne(Query.query(Criteria.where("uuid").is(offlineOrder.getUuid())), OfflineOrder.class, "off_line_order");
                if (offlineOrder == null || (offlineOrder.getState() > 1)) {
                    if (offlineOrder == null) {
                        logger.info("离线订单订单有问题 . ");
                    } else {
                        logger.info("离线订单状态不正确: {} ", offlineOrder.toString());
                    }
                    break;
                }
                oDto = superMarketService.createOrderFromSuperMarket((int) offlineOrder.getMerchantId(), (int) offlineOrder.getCashierId(), paramJson, createTime, SuperMarketServiceImpl.offlineOrderPrefix + offlineOrder.getUuid());
                if (oDto == null || oDto.getOrderId() == null) {
                    throw new RuntimeException("创建订单失败 oDto : " + oDto);
                } else {
                    offlineOrder.setState(2);
                    AjaxJson aj = this.paySupermarketOrder(oDto.getOrderId(), (int) offlineOrder.getCashierId(), "supermarkt_cash", createTime, StringUtils.isEmpty(offlineOrder.getCash()) ? "9999999"
                            : offlineOrder.getCash());
                    if (aj.isSuccess()) {
                        offlineOrder.setState(4);
                    } else {
                        offlineOrder.setState(5);
                    }
                }
            } catch (Exception e) {
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    offlineOrder.setState(be.getErrCode());
                } else {
                    offlineOrder.setState(3);
                }
                e.printStackTrace();
            }
            try {
                mongoService.getMongoTemplate().updateFirst(Query.query(Criteria.where("uuid").is(offlineOrder.getUuid())), Update.update("state", offlineOrder.getState()), OfflineOrder.class, "off_line_order");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public AjaxJson paySupermarketOrder(Integer orderId, Integer cashierId, String payType, Integer createTime, String cash) {
        AjaxJson json = new AjaxJson();
        try {
            superMarketService.paySupermarketOrder(orderId, cashierId, payType, createTime);
            Map<String, Object> resultMap = superMarketService.calChange(orderId, Double.valueOf(cash));
            json.setObj(resultMap);
            json.setMsg("订单付款成功");
            json.setSuccess(true);
            json.setStateCode("00");
            logger.info("超市订单现金付款成功，订单id:{}", orderId);
        } catch (RuntimeException e) {
            logger.warn("超市订单现金付款失败，{}，订单id:{}", e.getMessage(), orderId);
            json.setMsg(e.getMessage());
            json.setSuccess(false);
            json.setStateCode("02");
            e.printStackTrace();
        } catch (Exception e) {
            logger.warn("超市订单现金付款失败，订单id:{}", orderId);
            json.setMsg("订单更新失败");
            json.setSuccess(false);
            json.setStateCode("01");
            e.printStackTrace();
        }
        return json;
    }
}
