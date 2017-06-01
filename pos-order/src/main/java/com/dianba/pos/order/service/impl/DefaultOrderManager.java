package com.dianba.pos.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.mapper.OrderMapper;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.order.support.OrderRemoteService;
import com.dianba.pos.order.vo.OrderVo;
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


    public BasicResult prepareCreateOrder(long passportId, OrderTypeEnum orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("partnerUserId", passportId + "");
        params.put("orderType", orderType.getKey() + "");
        return postOrder(PREPARE_CREATE_ORDER, params);
    }

    public BasicResult generateOrder(long passportId, String sequenceNumber
            , String phoneNumber
            , OrderTypeEnum orderType, long actualPrice, long totalPrice
            , List<Map<String, Object>> orderItems) throws Exception {
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
        List<OrderItemSnapshot> orderItemSnapshots = new ArrayList<>();
        for (Map<String, Object> item : orderItems) {
            double itemCostPrice = Double.parseDouble(item.get("costPrice").toString());
            double itemSalePrice = Double.parseDouble(item.get("totalPrice").toString());
            OrderItemSnapshot orderItemSnapshot = new OrderItemSnapshot();
            orderItemSnapshot.setItemId(Long.parseLong(item.get("itemId").toString()));
            orderItemSnapshot.setItemTemplateId(Long.parseLong(item.get("itemTemplateId").toString()));
            orderItemSnapshot.setItemName(item.get("itemName").toString());
            orderItemSnapshot.setItemTypeId(Long.parseLong(item.get("itemTypeId").toString()));
            orderItemSnapshot.setItemTypeName(item.get("itemTypeName").toString());
//            orderItemSnapshot.setItemUnitId(Long.parseLong(item.get("itemTypeUnitId").toString()));
//            orderItemSnapshot.setItemUnitName(item.get("itemTypeUnitName").toString());
            orderItemSnapshot.setItemBarcode(item.get("itemBarcode").toString());
            orderItemSnapshot.setCostPrice((long) (itemCostPrice * 100));
            orderItemSnapshot.setNormalPrice((long) (itemSalePrice * 100));
            int normalQuantity = Integer.parseInt(item.get("normalQuantity").toString());
            orderItemSnapshot.setTotalPrice((long) (itemSalePrice * 100) * normalQuantity);
            orderItemSnapshot.setNormalQuantity(normalQuantity);
            orderItemSnapshots.add(orderItemSnapshot);
        }
        params.put("items", JsonHelper.toJSONString(orderItemSnapshots));
        return postOrder(GENERATE_ORDER, params);
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
