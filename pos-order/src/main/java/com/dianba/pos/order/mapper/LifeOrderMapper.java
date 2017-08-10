package com.dianba.pos.order.mapper;

import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.vo.MerchantCashierDayProfitInfo;
import com.dianba.pos.order.vo.MerchantDayReportVo;
import com.dianba.pos.order.vo.OrderTransactionRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface LifeOrderMapper {


    LifeOrder findOrderById(@Param("orderId") Long orderId);

    /**
     * 根据商家ID，类型状态获取订单
     *
     * @param passportId
     * @param orderType
     * @param orderStatus
     * @return
     */
    List<LifeOrder> findOrderForPos(@Param("passportId") Long passportId
            , @Param("orderType") Integer orderType, @Param("orderStatus") Integer orderStatus);

    List<LifeOrder> findOrderByPartnerUserAndPaymentTime(@Param("passportId") Long passportId
            , @Param("date") String date);

    List<LifeOrder> findNotSyncScanOrder(@Param("passportId") Long passportId
            , @Param("orderAmounts") List<Long> orderAmounts
            , @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 查询商家使用pos盈利信息
     */
    Map<String, Object> findPosProfitMoney(@Param("id") Long id
            , @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 查询商家进货信息
     */
    Map<String, Object> findMerchantStockMoney(@Param("id") Long id
            , @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<MerchantDayReportVo> findMerchantDayReport(@Param("merchantId") Long merchantId, @Param("itId") Long idId
            , @Param("itemName") String itemName,@Param("createTime") String createTime);

    List<MerchantCashierDayProfitInfo> findMerchantCashierDayProfitInfo(@Param("merchantId") Long merchantId
            , @Param("createTime") String createTime);

    /**
     * pos端交易记录
     *
     * @param merchantId
     * @param enterType  1 日,2 月
     * @return
     */
    List<OrderTransactionRecordVo> findOrderTransactionRecord(@Param("passportId") Long merchantId
            , @Param("enterType") Integer enterType, @Param("createTime") String createTime);

    List<OrderTransactionRecordVo> findOrderTransactionRecordSum(@Param("passportId") Long merchantId
            , @Param("enterType") Integer enterType, @Param("createTime") String createTime);
}
