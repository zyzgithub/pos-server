package com.wm.service.merchant;

import org.jeecgframework.core.common.service.CommonService;

public interface MerchantSettlementServiceI extends CommonService{

	void settleDayly(Integer merchantId);

}
