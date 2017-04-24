package com.wm.service.other;

import org.jeecgframework.core.common.service.CommonService;

public interface OtherServiceI extends CommonService{

	public void otherAlipayDone(Integer orderId,String title,Integer otherId,double alipayBalance, String payType) throws Exception;
}
