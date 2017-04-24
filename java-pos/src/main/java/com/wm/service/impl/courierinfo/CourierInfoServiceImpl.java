package com.wm.service.impl.courierinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.courierinfo.CourierInfoServiceI;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierInfoService")
@Transactional
public class CourierInfoServiceImpl extends CommonServiceImpl implements CourierInfoServiceI {

	@Override
	public Integer getCourierTypeByUserId(Integer userId) {
		StringBuilder query = new StringBuilder();
		query.append(" select courier_type courierType ");
		query.append(" from `user` u ");
		query.append(" left join 0085_courier_info ci on ci.courier_id = u.id ");
		query.append(" where u.id = ? ");
		return this.findOneForJdbc(query.toString(), Integer.class, userId);
	}

	@Override
	public Integer getCourierBindUserId(Integer courierId) {
		StringBuilder query = new StringBuilder();
		query.append(" select bind_user_id bindUserId from 0085_courier_info where courier_type = 2 and courier_id = ? ");
		return findOneForJdbc(query.toString(), Integer.class, courierId);
	}

	@Override
	public List<Integer> getCouriersByCourierType(Integer courierType) {
		StringBuilder query = new StringBuilder();
		query.append(" select courier_id from 0085_courier_info where courier_type = ?");
		List<Map<String, Object>> couriers = findForJdbc(query.toString(), courierType);
		List<Integer> ids = new ArrayList<Integer>();
		if(CollectionUtils.isNotEmpty(couriers)){
			for(Map<String, Object> map : couriers){
				ids.add(Integer.parseInt(map.get("courier_id").toString()));
			}
		}
		return ids;
	}

}