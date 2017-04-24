package com.test.service;

import java.util.List;
import java.util.Map;

public interface TestOrderI {
	
	public void testCopyOrder();
	
	public boolean testCopyOrder1(Integer merchantId, Integer money);

	public boolean testCopyMarketOrder(String yearMonth);

	public List<Map<String, Object>> selectSourceOrderIds(Integer sourceMchId, String ymd);

	public void copyMarketOrder(String hashCode, Integer targetMchId, Long sourceOrderId);
	
}
