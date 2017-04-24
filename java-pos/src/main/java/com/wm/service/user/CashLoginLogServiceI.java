package com.wm.service.user;



import org.jeecgframework.core.common.service.CommonService;

import com.base.VO.PageVO;
import com.wm.service.impl.user.CashierLoginLogServiceImpl.MerchantCashierLoginLog;

public interface CashLoginLogServiceI extends CommonService {
	
	/**
	 * 获取某一个商家快递员的登录日志
	 * @param merchantId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageVO<MerchantCashierLoginLog> getLoginLogs(Integer merchantId, Integer pageNo, Integer pageSize);
}
