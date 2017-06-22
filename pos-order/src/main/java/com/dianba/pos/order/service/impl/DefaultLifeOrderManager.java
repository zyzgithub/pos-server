package com.dianba.pos.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.mapper.LifeOrderMapper;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.po.LifeOrderItemSnapshot;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.dianba.pos.order.pojo.OrderPojo;
import com.dianba.pos.order.repository.LifeOrderItemSnapshotJpaRepository;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.order.support.OrderRemoteService;
import com.dianba.pos.order.util.OrderSequenceUtil;
import com.dianba.pos.order.vo.LifeOrderVo;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.repository.PassportJpaRepository;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.supplychain.service.LifeSupplyChainPrinterManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderItemSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DefaultLifeOrderManager extends OrderRemoteService implements LifeOrderManager {

    private static Logger logger = LogManager.getLogger(DefaultLifeOrderManager.class);

    @Autowired
    private LifeOrderMapper orderMapper;
    @Autowired
    private LifeOrderJpaRepository orderJpaRepository;
    @Autowired
    private LifeSupplyChainPrinterManager supplyChainPrinterManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private LifeOrderJpaRepository lifeOrderJpaRepository;
    @Autowired
    private LifeOrderItemSnapshotJpaRepository itemSnapshotJpaRepository;
    @Autowired
    private PassportJpaRepository passportJpaRepository;

    public OrderEntry getOrder(long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        BasicResult basicResult = postOrder(GET_ORDER, params);
        if (basicResult.isSuccess()) {
            JSONObject jsonObject = basicResult.getResponse();
            OrderEntry orderEntry = jsonObject.toJavaObject(OrderEntry.class);
            if (orderEntry == null) {
                throw new PosNullPointerException("订单不存在！");
            }
            return orderEntry;
        }
        return null;
    }

    public LifeOrderVo getLifeOrder(long orderId) {
        LifeOrder lifeOrder = orderJpaRepository.findOne(orderId);
        lifeOrderTransformation(lifeOrder);
        LifeOrderVo lifeOrderVo = new LifeOrderVo();
        BeanUtils.copyProperties(lifeOrder, lifeOrderVo);
        if (PaymentTypeEnum.CASH.getKey().equals(lifeOrderVo.getTransType())) {
            lifeOrderVo.setTransType(PaymentTypeEnum.CASH.getValue());
        } else if (PaymentTypeEnum.ALIPAY.getKey().equals(lifeOrderVo.getTransType())) {
            lifeOrderVo.setTransType(PaymentTypeEnum.ALIPAY.getValue());
        } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(lifeOrderVo.getTransType())) {
            lifeOrderVo.setTransType(PaymentTypeEnum.WEIXIN_NATIVE.getValue());
        }
        return lifeOrderVo;
    }

    public LifeOrder getLifeOrder(String sequenceNumber) {
        LifeOrder lifeOrder = orderJpaRepository.findBySequenceNumber(sequenceNumber);
        lifeOrderTransformation(lifeOrder);
        return lifeOrder;
    }

    private void lifeOrderTransformation(LifeOrder lifeOrder) {
        lifeOrder.setActualPrice(lifeOrder.getActualPrice()
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        lifeOrder.setTotalPrice(lifeOrder.getTotalPrice()
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        lifeOrder.setDiscountPrice(lifeOrder.getDiscountPrice()
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        lifeOrder.setDistributionFee(lifeOrder.getDistributionFee()
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        if (lifeOrder.getItemSnapshots() != null && lifeOrder.getItemSnapshots().size() > 0) {
            for (LifeOrderItemSnapshot itemSnapshot : lifeOrder.getItemSnapshots()) {
                itemSnapshot.setCostPrice(itemSnapshot.getCostPrice()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                itemSnapshot.setMarketPrice(itemSnapshot.getMarketPrice()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                itemSnapshot.setNormalPrice(itemSnapshot.getNormalPrice()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                itemSnapshot.setReturnPrice(itemSnapshot.getReturnPrice()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                itemSnapshot.setTotalPrice(itemSnapshot.getTotalPrice()
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
            }
        }
    }

    public BasicResult prepareCreateOrder(long passportId, OrderTypeEnum orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("partnerUserId", passportId + "");
        params.put("orderType", orderType.getKey() + "");
        return postOrder(PREPARE_CREATE_ORDER, params);
    }

    public BasicResult generateOrder(long passportId, String sequenceNumber, String phoneNumber
            , long actualPrice, List<OrderItemPojo> orderItems) throws Exception {
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        Map<String, String> params = new HashMap<>();
        params.put("sequenceNumber", sequenceNumber);
        params.put("partnerUserId", passportId + "");

        params.put("userSource", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey() + "");
        params.put("transType", PaymentTypeEnum.UNKNOWN.getKey());
        //商家ID
        params.put("shippingPassportId", merchantPassport.getId() + "");
        //商家名称
        params.put("shippingNickName", merchantPassport.getShowName() + "");
        //收货人手机号码
        if (!StringUtils.isEmpty(phoneNumber)) {
            params.put("receipt_phone", phoneNumber);
        }
        //订单实收金额
        params.put("actualAmount", actualPrice + "");
        params.put("discountAmount", "0");
        params.put("priceLogger", "0");
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItemSnapshot> orderItemSnapshots = new ArrayList<>();
        for (OrderItemPojo item : orderItems) {
            long itemCostPrice = item.getCostPrice().multiply(BigDecimal.valueOf(100))
                    .longValue();
            long itemSalePrice = item.getTotalPrice().multiply(BigDecimal.valueOf(100))
                    .longValue();
            OrderItemSnapshot orderItemSnapshot = new OrderItemSnapshot();
            orderItemSnapshot.setItemId(item.getItemId());
            orderItemSnapshot.setItemTemplateId(item.getItemTemplateId());
            orderItemSnapshot.setItemName(item.getItemName());
            orderItemSnapshot.setItemTypeId(item.getItemTypeId());
            orderItemSnapshot.setItemTypeName(item.getItemTypeName());
            orderItemSnapshot.setItemUnitId(item.getItemTypeUnitId());
            orderItemSnapshot.setItemUnitName(item.getItemTypeUnitName());
            orderItemSnapshot.setItemBarcode(item.getItemBarcode());
            orderItemSnapshot.setCostPrice(itemCostPrice);
            orderItemSnapshot.setNormalPrice(itemSalePrice);
            orderItemSnapshot.setTotalPrice(itemSalePrice * item.getNormalQuantity());
            orderItemSnapshot.setNormalQuantity(item.getNormalQuantity());
            orderItemSnapshots.add(orderItemSnapshot);
            totalPrice = totalPrice.add(BigDecimal.valueOf(orderItemSnapshot.getTotalPrice()));
        }
        params.put("items", JsonHelper.toJSONString(orderItemSnapshots));
        //订单总金额
        params.put("totalAmount", totalPrice + "");
        return postOrder(GENERATE_ORDER, params);
    }

    public BasicResult paymentOrder(Long orderId, PaymentTypeEnum paymentTypeEnum) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        params.put("transType", paymentTypeEnum.getKey());
        BasicResult basicResult = postOrder(PAYMENT_ORDER, params);
        if (basicResult.isSuccess()) {
            OrderEntry orderEntry = getOrder(orderId);
            if (OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey() == orderEntry.getType()) {
                //打印采购单
                BasicResult result
                        = supplyChainPrinterManager.printerPurchaseOrder(orderEntry.getShippingPassportId(), orderId);
                if (!result.isSuccess()) {
                    logger.error("采购订单打印失败！" + basicResult.getMsg() + "，订单ID:" + orderId);
                }
            }
        }
        return basicResult;
    }

    public BasicResult confirmOrder(long passportId, long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        params.put("partnerUserId", passportId + "");
        return postOrder(CONFIRM_ORDER, params);
    }

    public BasicResult createOrder(Long passportId, Integer orderType, BigDecimal actualPrice
            , String phoneNumber
            , List<OrderItemPojo> orderItems) throws Exception {
        if (orderItems == null || orderItems.size() < 0) {
            return BasicResult.createFailResult("订单商品为空！");
        }
        if (OrderTypeEnum.POS_SCAN_ORDER_TYPE.getKey() == orderType) {
            orderType = OrderTypeEnum.SCAN_ORDER_TYPE.getKey();
        }
        if (OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey() == orderType) {
            return createExtendOrder(passportId, phoneNumber, orderItems);
        } else if (OrderTypeEnum.SCAN_ORDER_TYPE.getKey() == orderType) {
            BasicResult basicResult = prepareCreateOrder(passportId, OrderTypeEnum.SCAN_ORDER_TYPE);
            if (basicResult.isSuccess()) {
                String sequenceNumber = basicResult.getResponse().getString("sequenceNumber");
                basicResult = generateOrder(passportId, sequenceNumber, phoneNumber
                        , actualPrice.multiply(BigDecimal.valueOf(100)).longValue()
                        , orderItems);
            }
            return basicResult;
        }
        throw new PosIllegalArgumentException("订单类型非法！" + orderType);
    }

    public LifeOrder buildLifeOrder(Long passportId, String phoneNumber, Long merchantPassportId
            , OrderStatusEnum orderStatus, OrderTypeEnum orderType, PaymentTypeEnum paymentType
            , Date createTime, Date paymentTime
            , BigDecimal actualPrice
            , List<OrderItemPojo> orderItems) {
        LifeOrder lifeOrder = new LifeOrder();
        if (merchantPassportId != null) {
            lifeOrder.setShippingPassportId(merchantPassportId);
        }
        if (phoneNumber != null) {
            lifeOrder.setReceiptPhone(phoneNumber);
        }
        lifeOrder.setSequenceNumber(OrderSequenceUtil.generateOrderSequence());
        lifeOrder.setPartnerId(passportId + "");
        lifeOrder.setPartnerUserId(passportId + "");
        lifeOrder.setStatus(orderStatus.getKey());
        lifeOrder.setType(orderType.getKey());
        lifeOrder.setPaymentType("-1");
        lifeOrder.setTransType(paymentType.getKey());
        lifeOrder.setActualPrice(actualPrice.multiply(BigDecimal.valueOf(100)));
        lifeOrder.setCreateTime(createTime);
        if (paymentTime != null) {
            lifeOrder.setPaymentTime(paymentTime);
        }
        List<LifeOrderItemSnapshot> lifeOrderItemSnapshots = new ArrayList<>();
        for (OrderItemPojo itemSnapshot : orderItems) {
            LifeOrderItemSnapshot orderItemSnapshot = new LifeOrderItemSnapshot();
            orderItemSnapshot.setItemId(itemSnapshot.getItemId());
            orderItemSnapshot.setItemTemplateId(itemSnapshot.getItemTemplateId());
            orderItemSnapshot.setItemName(itemSnapshot.getItemName());
            orderItemSnapshot.setItemTypeId(itemSnapshot.getItemTypeId());
            orderItemSnapshot.setItemTypeName(itemSnapshot.getItemTypeName());
            orderItemSnapshot.setItemUnitId(itemSnapshot.getItemTypeUnitId());
            orderItemSnapshot.setItemUnitName(itemSnapshot.getItemTypeUnitName());
            orderItemSnapshot.setItemBarcode(itemSnapshot.getItemBarcode());
            orderItemSnapshot.setCostPrice(itemSnapshot.getCostPrice().multiply(BigDecimal.valueOf(100)));
            orderItemSnapshot.setNormalPrice(itemSnapshot.getTotalPrice().multiply(BigDecimal.valueOf(100)));
            orderItemSnapshot.setNormalQuantity(itemSnapshot.getNormalQuantity());
            orderItemSnapshot.setTotalPrice(itemSnapshot.getTotalPrice().multiply(BigDecimal.valueOf(100))
                    .multiply(BigDecimal.valueOf(itemSnapshot.getNormalQuantity())));
            lifeOrder.setTotalPrice(lifeOrder.getTotalPrice().add(orderItemSnapshot.getTotalPrice()));
            lifeOrderItemSnapshots.add(orderItemSnapshot);
        }
        if (lifeOrderItemSnapshots.size() > 0) {
            lifeOrder.setItemSnapshots(lifeOrderItemSnapshots);
        }
        return lifeOrder;
    }

    @Transactional
    public BasicResult createExtendOrder(Long passportId, String phoneNumber, List<OrderItemPojo> orderItems) {
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        LifeOrder lifeOrder = buildLifeOrder(passportId, phoneNumber, merchantPassport.getId()
                , OrderStatusEnum.ORDER_STATUS_DEFAULT, OrderTypeEnum.POS_EXTENDED_ORDER_TYPE
                , PaymentTypeEnum.UNKNOWN, new Date(), null, BigDecimal.ZERO, orderItems);
        for (LifeOrderItemSnapshot lifeOrderItemSnapshot : lifeOrder.getItemSnapshots()) {
            lifeOrder.setActualPrice(lifeOrder.getActualPrice().add(lifeOrderItemSnapshot.getCostPrice()));
        }
        lifeOrder = lifeOrderJpaRepository.save(lifeOrder);
        if (lifeOrder.getId() != null) {
            BasicResult basicResult = BasicResult.createSuccessResult();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", lifeOrder.getId());
            basicResult.setResponse(jsonObject);
            return basicResult;
        }
        return BasicResult.createFailResult("下单失败！");
    }

    @Transactional
    public BasicResult syncOfflineOrders(List<OrderPojo> orders) {
        List<Map<String, String>> faileOrderIds = new ArrayList<>();
        try {
            List<LifeOrder> lifeOrders = new ArrayList<>();
            Long passportId = null;
            for (OrderPojo orderPojo : orders) {
                if (passportId == null) {
                    passportId = orderPojo.getPassportId();
                }
                Date createDate = new Date();
                if (orderPojo.getCreateTime() != null && !"".equals(orderPojo.getCreateTime())) {
                    Long createTime = Long.parseLong(orderPojo.getCreateTime()) / 1000;
                    createDate = DateUtil.strToDate(DateUtil.stampToDate(createTime));
                }
                Date paymentDate = null;
                if (orderPojo.getPaymenTime() != null && !"".equals(orderPojo.getPaymenTime())) {
                    Long paymentTime = Long.parseLong(orderPojo.getPaymenTime()) / 1000;
                    paymentDate = DateUtil.strToDate(DateUtil.stampToDate(paymentTime));
                }
                LifeOrder lifeOrder = buildLifeOrder(orderPojo.getPassportId(), null, null
                        , OrderStatusEnum.ORDER_STATUS_PAYMENT, OrderTypeEnum.POS_SCAN_ORDER_TYPE
                        , PaymentTypeEnum.CASH
                        , createDate, paymentDate
                        , orderPojo.getActualPrice()
                        , orderPojo.getItemSnapshots());
                lifeOrders.add(lifeOrder);
            }
            Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
            for (LifeOrder lifeOrder : lifeOrders) {
                lifeOrder.setShippingPassportId(merchantPassport.getId());
            }
            lifeOrderJpaRepository.save(lifeOrders);
        } catch (Exception e) {
            for (OrderPojo orderPojo : orders) {
                Map<String, String> map = new HashMap<>();
                map.put("id", orderPojo.getId());
                faileOrderIds.add(map);
            }
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(faileOrderIds);
        return basicResult;
    }

    public BasicResult getOrderForPos(Long passportId, Integer orderType, Integer orderStatus
            , Integer pageNum, Integer pageSize) {
        Page<LifeOrder> orderPage = PageHelper.startPage(pageNum, pageSize).doSelectPage(()
                -> orderMapper.findOrderForPos(passportId, orderType, orderStatus));
        List<Long> orderIds = new ArrayList<>();
        for (LifeOrder lifeOrder : orderPage) {
            orderIds.add(lifeOrder.getId());
        }
        if (orderIds.size() != 0) {
            List<LifeOrderItemSnapshot> orderItemSnapshots = itemSnapshotJpaRepository.findByOrderIdIn(orderIds);
            for (LifeOrder lifeOrder : orderPage) {
                List<LifeOrderItemSnapshot> itemSnapshots = new ArrayList<>();
                for (LifeOrderItemSnapshot itemSnapshot : orderItemSnapshots) {
                    if (itemSnapshot.getOrderId().longValue() == lifeOrder.getId().longValue()) {
                        itemSnapshots.add(itemSnapshot);
                    }
                }
                lifeOrder.setItemSnapshots(itemSnapshots);
                lifeOrderTransformation(lifeOrder);
            }
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(orderPage);
        basicResult.getResponse().put("pageNum", pageNum);
        basicResult.getResponse().put("pageSize", pageSize);
        basicResult.getResponse().put("total", orderPage.getTotal());
        return basicResult;
    }

    public JSONObject getJSONObject(Map<String, Object> posProfitMoney, Map<String, Object> merchantStockMoney
            , int chu) {
        JSONObject jsonObject = new JSONObject();
        //月转换
        BigDecimal m = new BigDecimal(chu);
        BigDecimal dv = new BigDecimal(100);
        if (posProfitMoney == null) {
            jsonObject.put("posProfitMoney", "");
        } else {
            //商家总盈利金额
            Long posSumMoney = Long.parseLong(posProfitMoney.get("sumMoney").toString());
            BigDecimal posSumMoneyBd = new BigDecimal(posSumMoney);

            Double money = posSumMoneyBd.divide(dv, 2, BigDecimal.ROUND_UP)
                    .divide(m, 2, BigDecimal.ROUND_UP)
                    .doubleValue();
            jsonObject.put("posProfitMoney", money + "");

        }
        if (merchantStockMoney.get("count").toString().equals("0")) {
            jsonObject.put("mStockMoney", "");
            jsonObject.put("mStockCount", "");
        } else {
            //商家总进货金额
            Long merchantSumMoney = Long.parseLong(merchantStockMoney.get("sumMoney").toString());
            BigDecimal merchantSumMoneyBd = new BigDecimal(merchantSumMoney);
            Double money = merchantSumMoneyBd.divide(dv, 2, BigDecimal.ROUND_UP)
                    .divide(m, 2, BigDecimal.ROUND_UP)
                    .doubleValue();
            //商家总进货次数
            int count = Integer.parseInt(merchantStockMoney.get("count").toString());
            BigDecimal countBd = new BigDecimal(count);
            int c = countBd.divide(m, 0).intValue();

            jsonObject.put("mStockMoney", money + "");
            jsonObject.put("mStockCount", c + "");
        }
        return jsonObject;
    }


    @Override
    public BasicResult getMerchantProfitInfo(Long merchantId, String phone) {

        JSONObject jsonObject = new JSONObject();
        Passport passport = passportJpaRepository.getPassportById(merchantId);
        if (passport == null) {
            return BasicResult.createFailResult("获取失败,没有此商家账号");
        } else {

            //查询6个月内
            int month = 6;
            //商家创建时间
            Date createTime = passport.getCreateTime();
            //当前时间
            Date nowTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
            int yueCha = DateUtil.yueDiff(createTime, nowTime);

            if (yueCha == 0 || yueCha == 1) { //一个月不用算平均数
                Map<String, Object> posProfitMoney = orderMapper.findPosProfitMoney(merchantId, createTime, nowTime);

                Map<String, Object> merchantStockMoney = orderMapper.findMerchantStockMoney(
                        merchantId, createTime, nowTime);

                jsonObject = getJSONObject(posProfitMoney, merchantStockMoney, 1);

            } else if (yueCha > month) { //商家使用超过6个月算最近6个月值
                //获取最近前6个月的时间
                Date beforeDate = DateUtil.getDateByMonth(-6);
                Map<String, Object> posProfitMoney = orderMapper.findPosProfitMoney(merchantId, beforeDate, nowTime);
                Map<String, Object> merchantStockMoney = orderMapper.findMerchantStockMoney(
                        merchantId, beforeDate, nowTime);
                jsonObject = getJSONObject(posProfitMoney, merchantStockMoney, 6);

            } else { //计算商家当前使用月值

                Map<String, Object> posProfitMoney = orderMapper.findPosProfitMoney(merchantId, createTime, nowTime);
                Map<String, Object> merchantStockMoney = orderMapper.findMerchantStockMoney(
                        merchantId, createTime, nowTime);
                jsonObject = getJSONObject(posProfitMoney, merchantStockMoney, yueCha);
            }
            jsonObject.put("userName", passport.getRealName());
            jsonObject.put("userPhone", passport.getPhoneNumber());
            jsonObject.put("merchantId", passport.getId());
            return BasicResult.createSuccessResult("获取成功", jsonObject);
        }

    }

}
