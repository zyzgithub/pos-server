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
import com.dianba.pos.order.vo.OrderTransactionRecordVo;
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
    private LifeSupplyChainPrinterManager supplyChainPrinterManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private LifeOrderJpaRepository lifeOrderJpaRepository;
    @Autowired
    private LifeOrderMapper lifeOrderMapper;
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
        return getLifeOrder(orderId, true);
    }

    public LifeOrderVo getLifeOrder(long orderId, boolean convertRMBUnit) {
        LifeOrder lifeOrder = orderMapper.findOrderById(orderId);
        if (lifeOrder == null) {
            throw new PosNullPointerException("订单不存在！");
        }
        LifeOrderVo lifeOrderVo = new LifeOrderVo();
        BeanUtils.copyProperties(lifeOrder, lifeOrderVo);
        if (convertRMBUnit) {
            lifeOrderTransformation(lifeOrderVo);
        }
        if (PaymentTypeEnum.CASH.getKey().equals(lifeOrderVo.getTransType())) {
            lifeOrderVo.setTransType(PaymentTypeEnum.CASH.getValue());
        } else if (PaymentTypeEnum.ALIPAY.getKey().equals(lifeOrderVo.getTransType())
                || PaymentTypeEnum.ALIPAY_JS.getKey().equals(lifeOrderVo.getTransType())) {
            lifeOrderVo.setTransType(PaymentTypeEnum.ALIPAY.getValue());
        } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(lifeOrderVo.getTransType())
                || PaymentTypeEnum.WEIXIN_JS.getKey().equals(lifeOrderVo.getTransType())) {
            lifeOrderVo.setTransType(PaymentTypeEnum.WEIXIN_NATIVE.getValue());
        }
        return lifeOrderVo;
    }

    public LifeOrderVo getLifeOrder(String sequenceNumber) {
        return getLifeOrder(sequenceNumber, true);
    }

    public LifeOrderVo getLifeOrder(String sequenceNumber, boolean convertRMBUnit) {
        LifeOrder lifeOrder = lifeOrderJpaRepository.findBySequenceNumber(sequenceNumber);
        LifeOrderVo lifeOrderVo = new LifeOrderVo();
        BeanUtils.copyProperties(lifeOrder, lifeOrderVo);
        if (convertRMBUnit) {
            lifeOrderTransformation(lifeOrderVo);
        }
        return lifeOrderVo;
    }

    private void lifeOrderTransformation(LifeOrderVo lifeOrder) {
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

    @Transactional
    public BasicResult paymentOrder(LifeOrderVo lifeOrderVo, PaymentTypeEnum paymentTypeEnum) {
        LifeOrder lifeOrder = new LifeOrder();
        BeanUtils.copyProperties(lifeOrderVo, lifeOrder);
        lifeOrder.setPaymentTime(new Date());
        lifeOrder.setStatus(OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey());
        lifeOrder.setTransType(paymentTypeEnum.getKey());
        lifeOrderJpaRepository.save(lifeOrder);
        if (OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey() == lifeOrder.getType()) {
            //打印采购单
            BasicResult result = supplyChainPrinterManager.printerPurchaseOrder(lifeOrder.getShippingPassportId()
                    , lifeOrder.getId());
            if (!result.isSuccess()) {
                logger.error("采购订单打印失败！" + result.getMsg() + "，订单ID:" + lifeOrder.getId());
            }
            return result;
        }
        return BasicResult.createSuccessResult();
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
        List<Map<String, String>> successOrderIds = new ArrayList<>();
        try {
            List<LifeOrder> cashLifeOrders = new ArrayList<>();
            List<LifeOrder> unknownLifeOrders = new ArrayList<>();
            Long passportId = null;
            List<Long> orderAmounts = new ArrayList<>();
            Date beginTime = null;
            Date endTime = null;
            for (OrderPojo orderPojo : orders) {
                if (passportId == null) {
                    passportId = orderPojo.getPassportId();
                }
                Date createDate = new Date();
                if (orderPojo.getCreateTime() != null && !"".equals(orderPojo.getCreateTime())) {
                    Long createTime = Long.parseLong(orderPojo.getCreateTime()) / 1000;
                    createDate = DateUtil.StringtoDate(DateUtil.stampToDate(createTime), DateUtil.FORMAT_ONE);
                }
                Date paymentDate = null;
                if (orderPojo.getPaymentTime() != null && !"".equals(orderPojo.getPaymentTime())) {
                    Long paymentTime = Long.parseLong(orderPojo.getPaymentTime()) / 1000;
                    paymentDate = DateUtil.StringtoDate(DateUtil.stampToDate(paymentTime), DateUtil.FORMAT_ONE);
                }
                List<OrderItemPojo> orderItemPojos = JsonHelper.toList(orderPojo.getItemSnapshots()
                        , OrderItemPojo.class);
                if (StringUtils.isEmpty(orderPojo.getPaymentType())) {
                    orderPojo.setPaymentType(PaymentTypeEnum.CASH.getKey());
                }
                PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(orderPojo.getPaymentType());
                LifeOrder lifeOrder = buildLifeOrder(orderPojo.getPassportId(), null, null
                        , OrderStatusEnum.ORDER_STATUS_PAYMENT, OrderTypeEnum.SCAN_ORDER_TYPE
                        , paymentTypeEnum
                        , createDate, paymentDate
                        , orderPojo.getActualPrice()
                        , orderItemPojos);
                if (PaymentTypeEnum.UNKNOWN.getKey().equals(lifeOrder.getTransType())) {
                    if (beginTime == null) {
                        beginTime = lifeOrder.getCreateTime();
                        endTime = lifeOrder.getCreateTime();
                    } else {
                        if (beginTime.after(lifeOrder.getCreateTime())) {
                            beginTime = lifeOrder.getCreateTime();
                        }
                        if (endTime.before(lifeOrder.getCreateTime())) {
                            endTime = lifeOrder.getCreateTime();
                        }
                    }
                    orderAmounts.add(lifeOrder.getTotalPrice().longValue());
                    unknownLifeOrders.add(lifeOrder);
                } else {
                    cashLifeOrders.add(lifeOrder);
                }
                Map<String, String> map = new HashMap<>();
                map.put("id", orderPojo.getId());
                successOrderIds.add(map);
            }
            Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
            for (LifeOrder lifeOrder : cashLifeOrders) {
                lifeOrder.setShippingPassportId(merchantPassport.getId());
            }
            if (orderAmounts.size() > 0) {
                beginTime = new Date(beginTime.getTime() - 180000);
                endTime = new Date(endTime.getTime() + 180000);
                List<LifeOrder> notSyncOrder = lifeOrderMapper.findNotSyncScanOrder(merchantPassport.getId()
                        , orderAmounts
                        , DateUtil.DateToString(beginTime, DateUtil.FORMAT_ONE)
                        , DateUtil.DateToString(endTime, DateUtil.FORMAT_ONE));
                if (notSyncOrder.size() > 0) {
                    List<LifeOrderItemSnapshot> lifeOrderItemSnapshots = new ArrayList<>();
                    for (LifeOrder lifeOrder : notSyncOrder) {
                        for (int i = unknownLifeOrders.size() - 1; i >= 0; i--) {
                            if (unknownLifeOrders.get(i).getTotalPrice().compareTo(lifeOrder.getTotalPrice()) == 0) {
                                Date date1 = unknownLifeOrders.get(i).getPaymentTime();
                                Date date2 = lifeOrder.getPaymentTime();
                                Integer minutes = DateUtil.getMinuteByDate(date1, date2);
                                if (Math.abs(minutes) <= 3) {
                                    lifeOrder.setPartnerUserId(passportId + "");
                                    for (LifeOrderItemSnapshot lifeOrderItemSnapshot : unknownLifeOrders
                                            .get(i).getItemSnapshots()) {
                                        lifeOrderItemSnapshot.setOrderId(lifeOrder.getId());
                                        lifeOrderItemSnapshots.add(lifeOrderItemSnapshot);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    lifeOrderJpaRepository.save(notSyncOrder);
                    itemSnapshotJpaRepository.save(lifeOrderItemSnapshots);
                }
            }
            lifeOrderJpaRepository.save(cashLifeOrders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(successOrderIds);
        return basicResult;
    }

    public BasicResult getOrderForPos(Long passportId, Integer orderType, Integer orderStatus
            , Integer pageNum, Integer pageSize) {
        Page<LifeOrderVo> orderPage = PageHelper.startPage(pageNum, pageSize).doSelectPage(()
                -> orderMapper.findOrderForPos(passportId, orderType, orderStatus));
        List<LifeOrderVo> lifeOrderVos = new ArrayList<>();
        for (int i = 0; i < orderPage.size(); i++) {
            LifeOrderVo lifeOrderVo = new LifeOrderVo();
            BeanUtils.copyProperties(orderPage.get(i), lifeOrderVo);
            lifeOrderVos.add(lifeOrderVo);
        }
        List<Long> orderIds = new ArrayList<>();
        for (LifeOrderVo lifeOrder : lifeOrderVos) {
            orderIds.add(lifeOrder.getId());
        }
        if (orderIds.size() != 0) {
            List<LifeOrderItemSnapshot> orderItemSnapshots = itemSnapshotJpaRepository.findByOrderIdIn(orderIds);
            for (LifeOrderVo lifeOrder : lifeOrderVos) {
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
        basicResult.setResponseDatas(lifeOrderVos);
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
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(merchantId);
        Passport passport = passportJpaRepository.getPassportById(merchantPassport.getId());
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

    @Override
    public BasicResult findOrderTransactionRecord(Long merchantId, Integer enterType, String createTime
            , Integer pageNum, Integer pageSize) {
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(merchantId);
        Page<OrderTransactionRecordVo> list = PageHelper.startPage(pageNum, pageSize).doSelectPage(()
                -> orderMapper.findOrderTransactionRecord(merchantPassport.getId(), enterType, createTime));
        List<OrderTransactionRecordVo> lst = new ArrayList<>();

        Integer wxSum = 0;
        Integer zfbSum = 0;
        Integer cashSum = 0;
        Integer wxjsSum = 0;
        Integer zfbjsSum = 0;
        BigDecimal wxMoney = new BigDecimal(0);
        BigDecimal cashMoney = new BigDecimal(0);
        BigDecimal zfbMoney = new BigDecimal(0);
        BigDecimal wxjsMoney = new BigDecimal(0);
        BigDecimal zfbjsMoney = new BigDecimal(0);
        BigDecimal rateMoney = new BigDecimal(0);
        BigDecimal normalMoney = new BigDecimal(0);
        BigDecimal a = new BigDecimal(100);
        List<Long> orderIds = new ArrayList<>();
        for (OrderTransactionRecordVo lifeOrder : list) {
            orderIds.add(lifeOrder.getId());
        }
        if (list.size() > 0) {
            List<LifeOrderItemSnapshot> orderItemSnapshots = itemSnapshotJpaRepository.findByOrderIdIn(orderIds);
            for (OrderTransactionRecordVo recordVo : list) {
                List<LifeOrderItemSnapshot> itemSnapshots = new ArrayList<>();
                for (LifeOrderItemSnapshot itemSnapshot : orderItemSnapshots) {
                    if (itemSnapshot.getOrderId().longValue() == recordVo.getId().longValue()) {
                        itemSnapshots.add(itemSnapshot);
                        recordVo.setItemName(itemSnapshot.getItemName());
                    }
                }
                recordVo.setCount(itemSnapshots.size());
            }

            for (OrderTransactionRecordVo recordVo : list) {
                if (recordVo.getCount() > 1) {
                    recordVo.setItemName(recordVo.getItemName() + "等..." + recordVo.getCount() + "件商品");
                }
                //现金支付
                if (PaymentTypeEnum.CASH.getKey().equals(recordVo.getTransType())) {
                    recordVo.setTransType(PaymentTypeEnum.CASH.getValue());
                } else if (PaymentTypeEnum.ALIPAY.getKey().equals(recordVo.getTransType())) { //支付宝支付
                    recordVo.setTransType(PaymentTypeEnum.ALIPAY.getValue());
                } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(recordVo.getTransType())) {//微信支付
                    recordVo.setTransType(PaymentTypeEnum.WEIXIN_NATIVE.getValue());
                } else if (PaymentTypeEnum.UNKNOWN.getKey().equals(recordVo.getTransType())) {
                    recordVo.setTransType(PaymentTypeEnum.UNKNOWN.getValue());
                } else if (PaymentTypeEnum.WEIXIN_JS.getKey().equals(recordVo.getTransType())) {
                    recordVo.setTransType(PaymentTypeEnum.WEIXIN_NATIVE.getValue());
                } else if (PaymentTypeEnum.ALIPAY_JS.getKey().equals(recordVo.getTransType())) {
                    recordVo.setTransType(PaymentTypeEnum.ALIPAY.getValue());
                }
                recordVo.setTotalPrice(recordVo.getTotalPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP));
                recordVo.setRatePrice(recordVo.getRatePrice().divide(a,2,BigDecimal.ROUND_HALF_UP));
                recordVo.setCashPrice(recordVo.getCashPrice().divide(a,2,BigDecimal.ROUND_HALF_UP));
                recordVo.setActualPrice(recordVo.getActualPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP));
                lst.add(recordVo);
            }
        }

        List<OrderTransactionRecordVo> map = orderMapper.findOrderTransactionRecordSum(merchantPassport.getId()
                , enterType, createTime);
        if (map != null && map.size() > 0) {
            for (OrderTransactionRecordVo recordVo : map) {
                rateMoney=rateMoney.add(recordVo.getRatePrice());
              //  normalMoney=normalMoney.add(recordVo.getCashPrice());
                //现金支付
                if (PaymentTypeEnum.CASH.getKey().equals(recordVo.getTransType())) {
                    cashSum = recordVo.getCountMap();
                    cashMoney = recordVo.getTotalPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP);
                } else if (PaymentTypeEnum.ALIPAY.getKey().equals(recordVo.getTransType())) { //支付宝支付
                    zfbSum = recordVo.getCountMap();
                    zfbMoney = recordVo.getTotalPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP);
                } else if (PaymentTypeEnum.WEIXIN_NATIVE.getKey().equals(recordVo.getTransType())) {//微信支付
                    wxSum = recordVo.getCountMap();
                    wxMoney = recordVo.getTotalPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP);
                } else if (PaymentTypeEnum.WEIXIN_JS.getKey().equals(recordVo.getTransType())) {//微信支付
                    wxjsSum = recordVo.getCountMap();
                    wxjsMoney = recordVo.getTotalPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP);
                } else if (PaymentTypeEnum.ALIPAY_JS.getKey().equals(recordVo.getTransType())) {//
                    zfbjsSum = recordVo.getCountMap();
                    zfbjsMoney = recordVo.getTotalPrice().divide(a, 2, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sumCount", wxSum + cashSum + zfbSum + wxjsSum + zfbjsSum);
        BigDecimal sumMoney = wxMoney.add(cashMoney).add(zfbMoney).add(wxjsMoney).add(zfbjsMoney);
        jsonObject.put("sumMoney", sumMoney);
        BigDecimal outMoney =rateMoney.divide(a, 2, BigDecimal.ROUND_HALF_UP);
        jsonObject.put("outMoney",outMoney);
        jsonObject.put("normalMoney",sumMoney.add(outMoney));
        jsonObject.put("wxSum", wxSum + wxjsSum);
        jsonObject.put("cashSum", cashSum);
        jsonObject.put("zfbSum", zfbSum + zfbjsSum);
        jsonObject.put("wxMoney", wxMoney.add(wxjsMoney));
        jsonObject.put("cashMoney", cashMoney);
        jsonObject.put("zfbMoney", zfbMoney.add(zfbjsMoney));
        jsonObject.put("transactionRecordList", lst);
        return BasicResult.createSuccessResult("获取交易记录成功", jsonObject);
    }

}
