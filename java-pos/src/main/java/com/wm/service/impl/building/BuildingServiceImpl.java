package com.wm.service.impl.building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.building.CityAreaEntity;
import com.wm.service.building.BuildingServiceI;
import com.wm.service.building.CityAreaServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("buildingService")
@Transactional
public class BuildingServiceImpl extends CommonServiceImpl implements BuildingServiceI {
	
	@Autowired
	private CityAreaServiceI cityAreaService;

	@Override
	public Map<String, Object> getCourierServArea(String courierId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String sql = "select a.name areaname,b.name buildingname,b.longitude,b.latitude,cb.first_floor,cb.last_floor,a.id areaid from 0085_courier_building cb"
			+" left join 0085_building b on b.id=cb.building_id"
			+" left join 0085_city_area a on a.id=b.region_id"
			+" where cb.courier_id=?";
		List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{courierId});
		if(list != null && list.size() > 0){
			retMap = list.get(0);
			Integer areaId = Integer.parseInt(retMap.get("AREAID").toString());
			String chargeArea = this.genFullAreaName(areaId, "");
			retMap.put("chargeArea", chargeArea);
		}
		return retMap;
	}

	/**
	 * 根据区域ID，生成区域全名,如：海珠区的id为1，返回“广州市海珠区”
	 * @param areaId
	 * @return
	 */
	private String genFullAreaName(Integer areaId, String fullName) {
		CityAreaEntity area = cityAreaService.getEntity(CityAreaEntity.class, areaId);
		if(areaId == 0){
			return fullName;
		} else {
			return genFullAreaName(area.getParentId(), area.getName()) + fullName;
		}
	}

	@Override
	public List<Map<String, Object>> queryBuildingsByCourierId(Integer courierId,
			Integer page,
			Integer rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT");
		sbsql.append(" bld.`name`,");
		sbsql.append(" bld.id AS building_id,");
		sbsql.append(" bld.longitude,");
		sbsql.append(" bld.latitude,");
		sbsql.append(" bld.address,");
		sbsql.append(" bld.first_floor,");
		sbsql.append(" bld.last_floor, ");
		sbsql.append(" bld.is_delete, ");
		sbsql.append(" bld.region_id, ");
		sbsql.append(" bld.org_id ");
		sbsql.append(" FROM");
		sbsql.append("	0085_courier_org AS cour_org,");
		sbsql.append("	0085_building AS bld");
		sbsql.append(" WHERE");
		sbsql.append("	cour_org.courier_id = ? ");
		sbsql.append(" AND bld.org_id = cour_org.org_id");

		return this.findForJdbcParam(sbsql.toString(), page, rows, courierId);
	}

	
}