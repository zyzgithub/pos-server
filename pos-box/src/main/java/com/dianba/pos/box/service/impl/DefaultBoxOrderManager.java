package com.dianba.pos.box.service.impl;

import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.service.BoxOrderManager;
import com.dianba.pos.box.util.ScanItemsUtil;
import com.dianba.pos.box.vo.BoxItemVo;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.po.LifeOrderItemSnapshot;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.util.OrderSequenceUtil;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DefaultBoxOrderManager implements BoxOrderManager {

    @Autowired
    private BoxItemLabelManager boxItemLabelManager;
    @Autowired
    private LifeOrderJpaRepository lifeOrderJpaRepository;

    @Transactional
    public LifeOrder createBoxOrder(Long passportId, String openId) {
        String rfids = ScanItemsUtil.getRFIDItems(passportId);
        List<BoxItemVo> boxItemVos = boxItemLabelManager.getItemsByRFID(passportId, rfids, true);
        LifeOrder lifeOrder = new LifeOrder();
        lifeOrder.setRemark(rfids);
        lifeOrder.setShippingPassportId(passportId);
        if (!StringUtils.isEmpty(openId)) {
            lifeOrder.setReceiptUserId(openId);
        }
        lifeOrder.setSequenceNumber(OrderSequenceUtil.generateOrderSequence());
        lifeOrder.setPartnerId(passportId + "");
        lifeOrder.setPartnerUserId(passportId + "");
        lifeOrder.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        lifeOrder.setType(OrderTypeEnum.SCAN_ORDER_TYPE.getKey());
        lifeOrder.setPaymentType("-1");
        lifeOrder.setTransType(PaymentTypeEnum.UNKNOWN.getKey());
        lifeOrder.setCreateTime(new Date());
        List<LifeOrderItemSnapshot> lifeOrderItemSnapshots = new ArrayList<>();
        for (BoxItemVo boxItemVo : boxItemVos) {
            LifeOrderItemSnapshot orderItemSnapshot = new LifeOrderItemSnapshot();
            orderItemSnapshot.setItemId(boxItemVo.getItemId());
            orderItemSnapshot.setItemTemplateId(boxItemVo.getItemId());
            orderItemSnapshot.setItemName(boxItemVo.getItemName());
            orderItemSnapshot.setItemTypeId(1L);
            orderItemSnapshot.setItemTypeName("");
            orderItemSnapshot.setItemUnitId(1L);
            orderItemSnapshot.setItemUnitName("");
            orderItemSnapshot.setItemBarcode("");
            orderItemSnapshot.setItemCode(boxItemVo.getRfids());
            BigDecimal itemPrice = boxItemVo.getItemPrice()
                    .multiply(BigDecimal.valueOf(100));
            orderItemSnapshot.setCostPrice(itemPrice);
            orderItemSnapshot.setNormalPrice(itemPrice);
            orderItemSnapshot.setNormalQuantity(boxItemVo.getItemQuantity());
            orderItemSnapshot.setTotalPrice(itemPrice);
            lifeOrder.setTotalPrice(lifeOrder.getTotalPrice().add(orderItemSnapshot.getTotalPrice()));
            lifeOrderItemSnapshots.add(orderItemSnapshot);
        }
        lifeOrder.setActualPrice(lifeOrder.getTotalPrice());
        if (lifeOrderItemSnapshots.size() > 0) {
            lifeOrder.setItemSnapshots(lifeOrderItemSnapshots);
        }
        return lifeOrderJpaRepository.save(lifeOrder);
    }
}
