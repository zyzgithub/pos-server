package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.service.BarCodeWeChatPayManager;
import com.dianba.pos.payment.support.WechatPayRemoteService;
import com.dianba.pos.payment.util.WxBarcodePayReturnHandler;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultBarCodeWeChatPayManager extends WechatPayRemoteService implements BarCodeWeChatPayManager {

    private static Logger logger = LogManager.getLogger(DefaultBarCodeWeChatPayManager.class);

    @Autowired
    private LifeOrderManager orderManager;
    @Autowired
    private PassportManager passportManager;

    @Override
    public BarcodePayResponse barcodePayment(Long passportId, Long orderId, String authCode
            , String deviceInfo, String spBillCreateIP) throws Exception {
        BarcodePayResponse response;
        OrderEntry order = orderManager.getOrder(orderId);
        if (OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey() == order.getStatus()) {
            throw new PosAccessDeniedException("订单已付款！无需重复付款！");
        }
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        String body = "1号生活715超市--" + merchantPassport.getShowName();
        try {
            Map<String, String> result = payOrder(authCode, body, order.getSequenceNumber(), null
                    , order.getTotalPrice().intValue(), deviceInfo, spBillCreateIP, null);
            if (result == null) {
                response = BarcodePayResponse.FAILURE;
                response.setMsg("未知错误");
                return response;
            }
            result.put("out_trade_no", order.getSequenceNumber());
            //对付款结果进行处理
            response = WxBarcodePayReturnHandler.handle(result);
        } catch (Exception e) {
            response = BarcodePayResponse.FAILURE;
            e.printStackTrace();
        }
        return response;
    }

}
