package com.dianba.pos.extended.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.extended.po.PosCharge19eOrder;
import com.dianba.pos.extended.vo.ChargeFlowResult;
import com.dianba.pos.extended.vo.ChargeResult;
import com.dianba.pos.extended.vo.Order19EDto;


/**
 * Created by Administrator on 2017/5/9 0009.
 */

public interface Charge19eManager {

    /**
     * 19e平台话费充值
     *
     * @param //phone
     * @param //money
     * @param //fillType   到账类型（0 快充 24-24 慢充 48-48 慢充）--必填
     * @param //chargeType 充值类型（0 手机 1 固话 2 小灵通 3 宽带） --必填
     * @param
     * @return
     */
    boolean hfCharge(Order19EDto order19EDto);

    /**
     * 根据已支付订单去充值
     *
     * @return
     */
    void orderListHfCharge();

    boolean flowCharge(Order19EDto order19EDto);

    void orderListFlowCharge();

    /***
     * 保存话费订单
     * @param order19EDto
     */
    void saveHfChargeTable(Order19EDto order19EDto, ChargeResult chargeResult);

    /***
     * 保存话费订单
     * @param order19EDto
     */
    void saveFlowChargeTable(Order19EDto order19EDto, ChargeFlowResult chargeFlowResult);

    /**
     * 获取增值服务商品信息 1 话费 2 流量
     *
     * @param type
     * @param phone
     * @return
     */
    BasicResult chargeMenu(String type, String phone);

    PosCharge19eOrder findByMerchantOrderId(String merchantOrderId);
}
