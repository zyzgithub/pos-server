package com.wm.service.impl.merchantinfo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.merchantinfo.MerchantInfoServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("merchantInfoService")
@Transactional
public class MerchantInfoServiceImpl extends CommonServiceImpl implements MerchantInfoServiceI {

	@Override
	public List<Map<String, Object>> getMerchantList() {
		String sql = "select merchant_id,merchant_source from 0085_merchant_info";
        return this.findForJdbc(sql);
	}
	
}