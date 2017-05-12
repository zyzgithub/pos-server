package com.dianba.pos.extended.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public interface Charge19eJpaManager {

    /**
     * 19e平台话费充值
     *
     * @param phone
     * @param money
     * @param fillType        到账类型（0 快充 24-24 慢充 48-48 慢充）--必填
     * @param chargeType      充值类型（0 手机 1 固话 2 小灵通 3 宽带） --必填
     * @param merchantOrderId 订单id长度50位
     * @return
     */
    boolean hfCharge(String phone, String money, String fillType, String chargeType, String merchantOrderId);

    /**
     * 获取增值服务平台订单支付成功,未请求第三方服务
     *
     * @param merchantId
     * @param payState
     * @return
     */
    List<Map<String, Object>> getOrderListBy19EMenu(Integer merchantId, String payState);
}
