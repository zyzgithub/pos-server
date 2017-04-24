package com.wm.service.impl.courierbuilding;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.wm.entity.courierbuilding.CourierBuildingEntity;
import com.wm.service.courierbuilding.CourierBuildingServiceI;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierBuildingService")
@Transactional
public class CourierBuildingServiceImpl extends CommonServiceImpl implements CourierBuildingServiceI {

	@Override
	public CourierBuildingEntity findByCidBid(Integer courierId,
			Integer buildingId) {
		String sql = "select * from 0085_courier_building where building_id = ? and courier_id = ?";
		CourierBuildingEntity courierBuildingEntity = this.findOneForJdbc(sql, CourierBuildingEntity.class, buildingId, courierId);
		return courierBuildingEntity;
	}

	@Override
	public void batchSaveCourierBuilding(Integer courierId,
			String buildingIds) {
		List<CourierBuildingEntity> entitys = Lists.newArrayList();
		String[] blds = StringUtils.split(buildingIds, ",");
		if(blds != null && blds.length > 0){
			for (String bid : blds) {
				if(StringUtils.isBlank(bid)){
					continue;
				}
				CourierBuildingEntity cbe = new CourierBuildingEntity();
				cbe.setBuildingId(Integer.valueOf(bid.trim()));
				cbe.setCourierId(courierId);
				entitys.add(cbe);
			}
		}
		this.batchSave(entitys );
	}

	@Override
	public List<Map<String, Object>> queryBldsFloorsByCid(Integer courierId) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT ");
		sbsql.append("   bld.id AS building_id, ");
		sbsql.append("   bld.name, ");
		sbsql.append("   bld.address, ");
		sbsql.append("   bld.first_floor AS bld_first_floor, ");
		sbsql.append("   bld.last_floor AS bld_last_floor, ");
		sbsql.append("   cb.floors ");
//		sbsql.append("   cb.first_floor AS cb_first_floor, ");
//		sbsql.append("   cb.last_floor AS cb_first_floor ");
		sbsql.append(" FROM ");
		sbsql.append("   0085_courier_org AS co, ");
		sbsql.append("   0085_courier_building AS cb, ");
		sbsql.append("   0085_building AS bld ");
		sbsql.append(" WHERE 1 = 1  ");
		sbsql.append("   AND cb.courier_id = ?  ");
		sbsql.append("   AND cb.courier_id = co.courier_id ");
		sbsql.append("   AND bld.org_id = co.org_id ");
		sbsql.append("   AND bld.id = cb.building_id ");
		return this.findForJdbc(sbsql.toString(), courierId);
	}


	@Override
	public void saveCourierBuildingFloors(Integer courierId,
			Integer buildingId, String floors) {
		CourierBuildingEntity cbe = findByCidBid(courierId, buildingId);
		if(cbe != null){
			cbe.setFloors(floors);
			this.save(cbe);
		}
	}
	
}