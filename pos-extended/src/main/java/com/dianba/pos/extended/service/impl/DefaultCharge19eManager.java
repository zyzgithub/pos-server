package com.dianba.pos.extended.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.extended.po.Charge19e;
import com.dianba.pos.extended.repository.Charge19eJpaRepository;
import com.dianba.pos.extended.service.Charge19eJpaManager;
import com.dianba.pos.extended.util.HfCharge19EApi;
import com.dianba.pos.extended.util.HfCharge19EUtil;
import com.dianba.pos.extended.vo.Charge19E;
import com.dianba.pos.order.mapper.OrderMapper;
import com.dianba.pos.order.repository.OrderJpaRepository;
import com.dianba.pos.order.vo.Order19EDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
@Service
public class DefaultCharge19eManager implements Charge19eJpaManager {

    private static Logger logger = LogManager.getLogger(DefaultCharge19eManager.class);
    @Autowired
    private Charge19eJpaRepository charge19eJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderMapper orderMapper;

    public boolean hfCharge(String phone, String money, String fillType, String chargeType, String merchantOrderId) {

        boolean flag = false;
        Charge19E charge19E = new Charge19E();
        charge19E.setChargeNumber(phone);
        charge19E.setChargeMoney(money);
        charge19E.setFillType(fillType);
        charge19E.setChargeType(chargeType);

        String message = HfCharge19EApi.hfCharge(HfCharge19EUtil.HF_CHARGE_19E_URL, charge19E);

        JSONObject jo = JSONObject.parseObject(message);
        String resultCode = jo.getString("resultCode");

        String resultDesc = jo.getString("resultDesc");
        String ehfOrderId = jo.getString("ehfOrderId"); //第三方订单id
        String queryResultUrl = jo.getString("queryResultUrl");
        String payMoney = jo.getString("payMoney");
        String orderId = jo.getString("merchantOrderId");
        logger.info("19e话费充值成功！订单号：" + ehfOrderId + ",充值手机：" + phone + ",充值金额" + money);
        Charge19e ct = new Charge19e();
        ct.setChargeNumber(money);
        ct.setChargePhone(phone);
        ct.setCreateTime(DateUtil.getCurrDate("yyyyMMddHHmmss"));

        ct.setResultCode(resultCode);
        ct.setResultDesc(resultDesc);
        ct.setQueryResultUrl(queryResultUrl);
        //第三方订单id
        ct.seteOrderId(ehfOrderId);
        ct.setMerchantOrderId(orderId);
        //话费充值订单信息
        ct.setType(1);
        //新增订单信息
        charge19eJpaRepository.save(ct);
        if (resultCode.equals("success")) {
            logger.info("19e话费充值成功并保存订单信息成功！订单号：" + ehfOrderId + ",充值手机：" + phone + ",充值金额：" + money + ",第三方订单号：" + ehfOrderId);
            //修改订单信息为confirm状态
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Map<String, Object>> getOrderListBy19EMenu(Integer merchantId, String payState) {

        List<Order19EDto> maplst = orderMapper.getOrderListBy19EMenu(merchantId, payState);

        for (Order19EDto od : maplst) {
            Long orderId = od.getOrderId();
            Integer type = od.getType();
            String phone = od.getMobile();
            Double price = od.getPrice();
            String orderNum = od.getOrderNum();
            if (3 == type) {
                boolean flag = hfCharge(phone, payState, "0", "0", orderNum);
                if (flag) { //修改订单状态为充值成功!

                }
            } else if (4 == type) { //流量充值


            }

        }


        return null;
    }
}
