package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

public interface TpmScanPaySetServiceI extends CommonService{
	public void updateEveryDayCount(Integer merchantId);

}
