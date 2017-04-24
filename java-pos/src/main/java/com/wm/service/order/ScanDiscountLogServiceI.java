package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface ScanDiscountLogServiceI extends CommonService{
    
    /**
     * 扫码订单 折扣逻辑处理
     * @param order
     */
    public void processSanDiscount(OrderEntity order);

}
