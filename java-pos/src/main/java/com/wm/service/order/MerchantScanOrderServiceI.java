package com.wm.service.order;

import java.math.BigDecimal;
import java.util.Map;

public interface MerchantScanOrderServiceI {
	
	public Integer createMerchantScanOrder(Integer merchantId, BigDecimal origin, String remark);

	Integer createMerchantScanOrder(Integer merchantId, BigDecimal origin, String remark, String payType, String payId);

	public Map<String, Object> responseMap(Integer orderId);
	
	public BigDecimal getLimitMoney(Integer merchantId, int type);
}
