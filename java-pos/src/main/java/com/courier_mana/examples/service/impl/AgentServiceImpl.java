package com.courier_mana.examples.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.AgentServiceI;

@Service
public class AgentServiceImpl extends CommonServiceImpl implements AgentServiceI {

	@Override
	public List<Integer> getAgentCourierIds(Integer agentUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT courier_id ");
		sql.append(" FROM 0085_courier_info ");
		sql.append(" WHERE courier_type = 2 ");
		sql.append(" 	AND bind_user_id = ? ");
		List<Map<String, Object>> courierIds = this.findForJdbc(sql.toString(), agentUserId);
		/**
		 * 将数据库中取出的信息重新整理
		 */
		List<Integer> result = new ArrayList<Integer>();
		for(Map<String, Object> item: courierIds){
			result.add((Integer)item.get("courier_id"));
		}
		return result;
	}

}
