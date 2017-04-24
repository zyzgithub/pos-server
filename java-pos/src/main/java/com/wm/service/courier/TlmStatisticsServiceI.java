package com.wm.service.courier;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

/**
 * 快递员统计表数据
 * @author lzh
 *
 */
public interface TlmStatisticsServiceI extends CommonService{

	public void findFromTlmStatisticsByCourierId(Integer courierId);
	
	public void updateTotalOrder(Integer courierId,Integer completeMin,Integer money);
}
