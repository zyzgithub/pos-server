package com.dianba.pos.extended.service.impl;

import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.extended.mapper.Charge19eMapper;
import com.dianba.pos.extended.po.Charge19e;
import com.dianba.pos.extended.repository.Charge19eJpaRepository;
import com.dianba.pos.extended.service.Charge19eManager;
import com.dianba.pos.extended.util.FlowCharge19EApi;
import com.dianba.pos.extended.util.FlowCharge19EUtil;
import com.dianba.pos.extended.util.HfCharge19EApi;
import com.dianba.pos.extended.util.HfCharge19EUtil;
import com.dianba.pos.extended.vo.Charge19E;
import com.dianba.pos.extended.vo.ChargeFlow;
import com.dianba.pos.extended.vo.ChargeFlowResult;
import com.dianba.pos.extended.vo.ChargeResult;
import com.dianba.pos.order.mapper.OrderMapper;
import com.dianba.pos.order.repository.OrderJpaRepository;
import com.dianba.pos.order.vo.Order19EDto;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
@SuppressWarnings("all")
@Service
public class DefaultCharge19eManager implements Charge19eManager {

    private static Logger logger = LogManager.getLogger(DefaultCharge19eManager.class);
    @Autowired
    private Charge19eJpaRepository charge19eJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private Charge19eMapper charge19eMapper;

    @Override
    public boolean hfCharge(Order19EDto or) {

        boolean flag = false;
        Charge19E charge19E = new Charge19E();
        charge19E.setChargeNumber(or.getMobile());
        charge19E.setChargeMoney(or.getPrice().toString());
        //默认生成的订单号
        String orderNum = DateUtil.getCurrDate("yyyyMMddHHmmss")
                + RandomStringUtils.random(4, "0123456789") + "hfcharge" + or.getOrderId();
        charge19E.setMerchantOrderId(orderNum);
        charge19E.setFillType("0");
        charge19E.setChargeType("0");
        ChargeResult cr = HfCharge19EApi.hfCharge(HfCharge19EUtil.HF_CHARGE_19E_URL, charge19E);
        if(cr.getResultCode().equals("SUCCESS")){
            //保存话费充值订单信息
            saveHfChargeTable(or, cr);
            if (cr.getResultCode().equals("success")) {
                logger.info("19e话费下单成功并保存订单信息成功！订单号：" + cr.getMerchantOrderId() + ",充值手机：" + or.getMobile() +
                        ",充值金额：" + or.getPrice() + ",第三方订单号：" + cr.getEhfOrderId());
                //修改订单信息状态
                orderMapper.editOrderInfoBy19e("processing", orderNum,0);
                flag = true;
            }
        }
        return flag;

    }

    @Override
    public void orderListHfCharge() {
        /**
         * 如果订单为为成功状态都去充值
         */
        List<Order19EDto> list = orderMapper.getOrderListBy19EMenu(-1, "pay", "success",
                3);
        for (Order19EDto od : list) {

            if(!StringUtil.isEmpty(od.getMobile())){
                //查询此订单的充值次数
                Integer count = charge19eMapper.chargeCountByOrder(od.getOrderId());
                if (count < 3) { //没有充值3次

                    boolean flag = hfCharge(od);
                    if (flag) {
                        logger.info("话费充值下订单成功！" + "orderNum" + od.getOrderNum() + "phone" + od.getMobile()
                                + "money" + od.getPrice());
                    }

                }
            }


        }

    }


    @Override
    public boolean flowCharge(Order19EDto order19EDto) {

        boolean flag = false;
        ChargeFlow cf = new ChargeFlow();
        cf.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
        //默认生成的订单号
        String orderNum = DateUtil.getCurrDate("yyyyMMddHHmmss")
                + RandomStringUtils.random(4, "0123456789") + "hfcharge" + order19EDto.getOrderId();

        cf.setMerOrderNo(orderNum);
        cf.setProductId(order19EDto.getMenuKey());
        cf.setMobile(order19EDto.getMobile());
        cf.setRemark("流量充值!");
        ChargeFlowResult chargeFlowResult = FlowCharge19EApi.flowCharge(FlowCharge19EUtil.FLOW_CHARGE_URL, cf);
        //保存流量充值订单信息
        saveFlowChargeTable(order19EDto, chargeFlowResult);
        if (chargeFlowResult.getResultCode().equals("success")) {
            logger.info("19e流量下单成功并保存订单信息成功！订单号：" + chargeFlowResult.getMerOrderId() + ",充值手机："
                    + chargeFlowResult.getMobile() + ",充值金额：" + order19EDto.getPrice() + ",第三方订单号：" + chargeFlowResult.getOrderNo());
            orderMapper.editOrderInfoBy19e("processing", orderNum,0);
            flag = true;
        }
        return flag;
    }

    @Override
    public void orderListFlowCharge() {

        /**
         * 如果订单为为成功状态都去充值
         */
        List<Order19EDto> list = orderMapper.getOrderListBy19EMenu(-1, "pay", "success",
                4);
        for (Order19EDto od : list) {

            //查询此订单的充值次数
            Integer count = charge19eMapper.chargeCountByOrder(od.getOrderId());
            if (count < 3) { //没有充值3次

                boolean flag = flowCharge(od);
                if (flag) {
                    logger.info("流量充值下订单成功！" + "orderNum" + od.getOrderNum() + "phone" + od.getMobile()
                            + "money" + od.getPrice());
                }

            }

        }
    }

    @Override
    public void saveHfChargeTable(Order19EDto or, ChargeResult cr) {
        Charge19e ct = new Charge19e();
        ct.setChargeNumber(or.getPrice().toString());
        ct.setChargePhone(or.getMobile());
        ct.setCreateTime(DateUtil.getCurrDate("yyyyMMddHHmmss"));

        ct.setResultCode(cr.getResultCode());
        ct.setResultDesc(cr.getResultDesc());
        ct.setQueryResultUrl(cr.getQueryResultUrl());
        //第三方订单id

        ct.seteOrderId(cr.getEhfOrderId());
        ct.setMerchantOrderId(cr.getMerchantOrderId());

        //关联order表id
        ct.setOrderId(or.getOrderId());
        //此单为话费订单
        ct.setType(1);
        //新增订单信息
        charge19eJpaRepository.save(ct);
    }

    @Override
    public void saveFlowChargeTable(Order19EDto order19EDto, ChargeFlowResult chargeFlowResult) {
        Charge19e ct = new Charge19e();
        ct.setChargeNumber(order19EDto.getPrice().toString());
        ct.setChargePhone(order19EDto.getMobile());
        ct.setCreateTime(DateUtil.getCurrDate("yyyyMMddHHmmss"));

        ct.setResultCode(chargeFlowResult.getResultCode());
        ct.setResultDesc(chargeFlowResult.getResultDesc());
        ct.setQueryResultUrl("");
        //第三方订单id
        ct.seteOrderId(chargeFlowResult.getOrderNo());
        ct.setMerchantOrderId(chargeFlowResult.getMerOrderId());

        ct.seteOrderId(chargeFlowResult.getOrderNo());
        //关联order表id
        ct.setOrderId(order19EDto.getOrderId());
        //此单为流量充值订单
        ct.setType(2);
        //新增订单信息
        charge19eJpaRepository.save(ct);
    }


}
