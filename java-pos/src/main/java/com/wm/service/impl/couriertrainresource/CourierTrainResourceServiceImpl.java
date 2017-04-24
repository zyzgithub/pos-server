package com.wm.service.impl.couriertrainresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jodd.servlet.HtmlDecoder;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wm.service.couriertrainresource.CourierTrainResourceServiceI;

@Service
public class CourierTrainResourceServiceImpl extends CommonServiceImpl implements
		CourierTrainResourceServiceI {
	
	private final static Logger logger = LoggerFactory.getLogger(CourierTrainResourceServiceImpl.class);

	@Override
	public List<Map<String, Object>> getCourierTrainType(int page, int rows) {
		StringBuilder query = new StringBuilder();
		query.append(" select type from train_resource ");
		query.append(" group by type ");
		return findForJdbc(query.toString(), page, rows);
	}

	@Override
	public List<Map<String, Object>> getCourierTrainInfo(Integer courierType, Integer trainType,
			int page, int rows) {
		logger.info("快递员类型 : " + courierType + "培训资料类型 : " + trainType);
		StringBuilder query = new StringBuilder();
		query.append(" select id, name, courier_type suitCourierType, DATE_FORMAT(create_time, '%Y年%m月%d日') createTime, url ");
		query.append(" from train_resource");
		query.append(" where type = ? and is_delete = '0' ");
		//根据培训资料类型      查出培训资料的名称
		List<Map<String, Object>>  trainList = findForJdbc(query.toString(), trainType);
		//根据快递员类型   获取对应的培训资料名称
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> map : trainList){
			String courierTypeString = map.get("suitCourierType").toString();
			String[] str = courierTypeString.split("-");
			for(String string : str){
				if(Integer.parseInt(string) == courierType){
					map.put("name", HtmlDecoder.decode(map.get("name").toString()));
					list.add(map);
					break;
				}
			}
		}
		//分页
		List<Map<String, Object>> trainNameList = new ArrayList<Map<String,Object>>();
		int start = page-1;
		int end = page*rows;
		for(int i=start; i<end; i++){
			if(i>list.size() - 1){
				break;
			}
				trainNameList.add(list.get(i));
		}
		return trainNameList;
	}

	@Override
	public Map<String, Object> getCourierTrainUrl(Integer trainId) {
		StringBuilder query = new StringBuilder();
		query.append("select url  from train_resource where id = ?");
		return findOneForJdbc(query.toString(), trainId);
	}

}
