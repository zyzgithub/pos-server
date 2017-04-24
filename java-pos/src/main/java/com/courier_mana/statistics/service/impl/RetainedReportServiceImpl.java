package com.courier_mana.statistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.base.config.BaseConfig;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.remote.RemoteClient;
import com.courier_mana.statistics.service.RetainedReportService;

@Service
public class RetainedReportServiceImpl implements RetainedReportService {

	@Autowired
	private BaseConfig baseConfig;
	
	@Override
	public JSONObject getRetainedReportData(SearchVo timeType, Integer warehouseId) {
		String timeTypeStr = timeType.getTimeType();
		StringBuilder url = new StringBuilder(baseConfig.SUPPLY_ERP_HOST);
		url.append("/courierManaController/getRetainedReportData?unit=");
		
		if("week".equals(timeTypeStr)){
			url.append(1);
		} else if("month".equals(timeTypeStr)){
			url.append(2);
		} else {
			url.append(0);
		}
		if(warehouseId != null){
			url.append("&warehouseId=");
			url.append(warehouseId);
		}
		Integer beginTimeSecond = timeType.getBeginTime();
		if(beginTimeSecond != null && beginTimeSecond != 0){
			url.append("&millisecond=");
			url.append(beginTimeSecond * 1000l);
		}
		String response = RemoteClient.get(url.toString());
		
		JSONObject json = JSONObject.parseObject(response);
		
		return json;
	}

	@Override
	public JSONObject getAllWarehouseInfo() {
		StringBuilder url = new StringBuilder(baseConfig.SUPPLY_ERP_HOST);
		url.append("/courierManaController/getWarehouseList");
		String response = RemoteClient.get(url.toString());
		JSONObject json = JSONObject.parseObject(response);
		return json;
	}
}
