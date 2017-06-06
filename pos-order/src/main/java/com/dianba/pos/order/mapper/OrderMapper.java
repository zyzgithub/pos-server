package com.dianba.pos.order.mapper;

import com.dianba.pos.order.vo.Order19EDto;
import com.dianba.pos.order.vo.MerchantOrderVo;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    /**
     * 查询商家使用商米pos每个月的盈利信息
     */
    Map<String, Object> queryOrderList(@Param("id") Long id
            , @Param("create_time") Long startTime, @Param("nowTime") Long nowTime);

    /**
     * 获取商家使用pos机的时间
     */
    Long getPosStrtTimeByMerchant(Long id);

    /**
     * 获取商家注册时间
     */
    Long getMerchantCreate(Long id);


    Integer getRemarkCount(List<String> remark);

    /**
     * 获取未充值订单
     */
    List<Order19EDto> getOrderListBy19EMenu(@Param("merchantId") Integer merchantId
            , @Param("payState") String payState, @Param("type") Integer type);

    /**
     * 更新增值服务订单信息为成功状态
     */
    void editOrderInfoBy19e(@Param("chargeState") String payState
            , @Param("orderNum") String orderNum, @Param("completeTime") int timestamp);

    Object getByPayId(@Param("orderNum") String orderNum);

    /**
     * 根据商家ID获取订单
     *
     * @param merchantPassportId 商家ID
     * @return
     */
    List<MerchantOrderVo> findOrderForMerchant(@Param("merchantPassportId") Long merchantPassportId);

    /**
     * 根据商家ID，类型状态获取订单
     *
     * @param passportId
     * @param orderType
     * @param orderStatus
     * @return
     */
    List<OrderEntry> findOrderForPos(@Param("passportId") Long passportId
            , @Param("orderType") Integer orderType, @Param("orderStatus") Integer orderStatus);
}
