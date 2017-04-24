package com.wm.service.merchantinfo;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.merchantinfo.MerchantDeductionEntity;


public interface MerchantDeductionServiceI extends CommonService {

	/**
	 * 获得商家扣点信息
	 * @param merchantId
	 * @param type  1=门店扣点，2=扫码扣点
	 * @return
	 */
	public MerchantDeductionEntity getMerchantDeduction(Integer merchantId,Integer type);
}
