package com.wm.service.pos;

import java.util.Map;

import com.wm.entity.pos.PosOrderModifyMoneyDetail;

import org.jeecgframework.core.common.service.CommonService;

/**
 * Created by mjorcen on 16/8/15.
 */
public interface PosOrderModifyMoneyDetailService extends CommonService {
    /**
     * 修改订单价格.
     *
     * @param detail 折扣详情
     * @param remark 整单备注
     */
    Map<String, Object> modify(PosOrderModifyMoneyDetail detail, String remark);
    
    Map<String, Object> getOrderModifyDetail(Integer orderId);
}
