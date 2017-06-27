package com.dianba.pos.extended.mapper;

import com.dianba.pos.extended.vo.Order19EDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
@Mapper
public interface Charge19eMapper {
    /**
     * 获取未充值订单
     */
    List<Order19EDto> getOrderListBy19EMenu(@Param("type") Integer type, @Param("deliverStatus") Integer deliverStatus);
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


    /**
     * 更新增值服务订单信息为成功状态
     */
    void editOrderInfoBy19e(@Param("deliverStatus") Integer deliverStatus, @Param("orderNum") String orderNum);


    Object getByPayId(@Param("orderNum") String orderNum);
}
