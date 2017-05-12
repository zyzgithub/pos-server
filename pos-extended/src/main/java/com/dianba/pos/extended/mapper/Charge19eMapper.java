package com.dianba.pos.extended.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
@Mapper
public interface Charge19eMapper {
    /**
     * 查询订单充值次数
     *
     * @param orderId
     * @return
     */
    Integer chargeCountByOrder(@Param("orderId") Long orderId);

    /**
     * 更新第三方订单为成功状态
     * @param chargeState
     * @param finishTime
     * @param merchantOrderId
     */
    void editCharge19e(@Param("chargeState") String chargeState,@Param("finishTime") String finishTime,
                       @Param("merchantOrderId") String merchantOrderId);
}
