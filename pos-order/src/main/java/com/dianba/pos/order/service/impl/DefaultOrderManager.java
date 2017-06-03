package com.dianba.pos.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.mapper.OrderMapper;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.order.support.OrderRemoteService;
import com.dianba.pos.order.vo.OrderVo;
import com.dianba.pos.passport.po.LifePassportAddress;
import com.dianba.pos.passport.repository.LifePassportAddressJpaRepository;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderItemSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultOrderManager extends OrderRemoteService implements OrderManager {

    private static Logger logger = LogManager.getLogger(DefaultOrderManager.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private LifeOrderJpaRepository orderJpaRepository;
    @Autowired
    private LifePassportAddressJpaRepository passportAddressJpaRepository;

    public OrderEntry getOrder(long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        BasicResult basicResult = postOrder(GET_ORDER, params);
        if (basicResult.isSuccess()) {
            JSONObject jsonObject = basicResult.getResponse();
            OrderEntry orderEntry = jsonObject.toJavaObject(OrderEntry.class);
            return orderEntry;
        }
        return null;
    }

    public LifeOrder getLifeOrder(long orderId) {
        return orderJpaRepository.findOne(orderId);
    }

    @Override
    public LifeOrder getLifeOrder(String sequenceNumber) {
        return orderJpaRepository.findBySequenceNumber(sequenceNumber);
    }

    public BasicResult prepareCreateOrder(long passportId, OrderTypeEnum orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("partnerUserId", passportId + "");
        params.put("orderType", orderType.getKey() + "");
        return postOrder(PREPARE_CREATE_ORDER, params);
    }

    public BasicResult generateOrder(long passportId, String sequenceNumber, String phoneNumber
            , long actualPrice, long totalPrice
            , List<OrderItemPojo> orderItems) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("sequenceNumber", sequenceNumber);
        params.put("partnerUserId", passportId + "");

        params.put("userSource", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey() + "");
        params.put("transType", TransTypeEnum.PAYMENT.getKey() + "");
        //商家ID
        params.put("shippingPassportId", passportId + "");
        //商家名称
        params.put("shippingNickName", passportId + "");
        //收货人ID
        params.put("receiptUserId", passportId + "");
        //收货人手机号码
        if (!StringUtils.isEmpty(phoneNumber)) {
            params.put("receipt_phone", phoneNumber);
        }
        //订单应收费用
        params.put("actualAmount", actualPrice + "");
        //订单实际收费
        params.put("totalAmount", totalPrice + "");
        params.put("discountAmount", "0");
        params.put("priceLogger", "0");
        params.put("items", JsonHelper.toJSONString(createOrderItemSnapshots(orderItems)));
        return postOrder(GENERATE_ORDER, params);
    }

    public BasicResult generatePurchaseOrder(long passportId, String sequenceNumber, Long warehouseId
            , Map<String, Object> itemSet) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", passportId + "");
        params.put("sequenceNumber", sequenceNumber);
        params.put("partnerUserId", passportId + "");
        params.put("userSource", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey() + "");
        LifePassportAddress passportAddress = passportAddressJpaRepository.findByPassportId(passportId);
        if (passportAddress == null) {
            throw new PosNullPointerException("商家地址信息不存在！" + passportId);
        }
        //商家ID
        params.put("receiptProvince", passportAddress.getProvince());
        params.put("receiptCity", passportAddress.getCity());
        params.put("receiptDistrict", passportAddress.getDistrict());
        params.put("receiptAddress", passportAddress.getStreet());
        params.put("receiptNickName", passportAddress.getName());
        params.put("receiptPhone", passportAddress.getPhoneNumber());
        params.put("receiptLocation", passportAddress.getLatitude() + "," + passportAddress.getLongitude());
        JSONObject jsonObject = new JSONObject();
        for (String key : itemSet.keySet()) {
            jsonObject.put(key, warehouseId);
        }
        params.put("warehouseRemarkSet", jsonObject.toJSONString());
        JSONObject itemSetObj = (JSONObject) itemSet;
        params.put("itemSet", itemSetObj.toJSONString());
        return postPurchaseOrder(GENERATE_ORDER, params);
    }

    private List<OrderItemSnapshot> createOrderItemSnapshots(List<OrderItemPojo> orderItems) {
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
        }
        return orderItemSnapshots;
    }

    public BasicResult paymentOrder(long orderId, int transType) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        params.put("transType", transType + "");
        return postOrder(PAYMENT_ORDER, params);
    }

    public BasicResult confirmOrder(long passportId, long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        params.put("partnerUserId", passportId + "");
        return postOrder(CONFIRM_ORDER, params);
    }

    public BasicResult getOrderForMerchant(long merchantPassportId, int pageNum, int pageSize) {
        Page<List<OrderVo>> orderPage = PageHelper.startPage(pageNum, pageSize).doSelectPage(()
                -> orderMapper.findOrderForMerchant(merchantPassportId));
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(orderPage);
        basicResult.getResponse().put("pageNum", pageNum);
        basicResult.getResponse().put("pageSize", pageSize);
        basicResult.getResponse().put("total", orderPage.getTotal());
        return basicResult;
    }
}
