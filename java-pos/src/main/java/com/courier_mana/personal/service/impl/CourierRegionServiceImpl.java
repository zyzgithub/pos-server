package com.courier_mana.personal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.courier_mana.personal.service.CourierRegionService;

@Service
public class CourierRegionServiceImpl extends CommonServiceImpl implements CourierRegionService {
	private static final Logger logger = LoggerFactory.getLogger(CourierRegionServiceImpl.class);
	
	@Override
	public Map<String, Object> getCourierOrgInfo(Integer courierId) {
		logger.info("invoke method getCourierOrgInfo, params:{}", courierId);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id, o.org_name orgName, o.level, o.p_path orgPath ");
		sql.append("FROM 0085_courier_org co LEFT JOIN `0085_org` o ON co.org_id=o.id ");
		sql.append("WHERE co.courier_id=?");
		
		return this.findOneForJdbc(sql.toString(), courierId);
	}

	@Override
	public Map<String, Object> getOrgInfo(Integer orgId) {
		logger.info("invoke method getOrgInfo, params:{}", orgId);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT org_name orgName, pid, level, p_path orgPath ");
		sql.append("FROM 0085_org ");
		sql.append("WHERE id=?");
		return this.findOneForJdbc(sql.toString(), orgId);
	}

	@Override
	public Map<String, Object> getAdminRegion(Integer courierId) {
		logger.info("invoke method getAdminRegion, params:{}", courierId);
		Map<String, Object> result = new HashMap<String, Object>();
		try {			
			Map<String, Object> courierOrgInfo = this.getCourierOrgInfo(courierId);
			if(courierOrgInfo == null){
				return null;
			}
		
			Integer orgLevel = (Integer)courierOrgInfo.get("level");
			String province = null;//省
			String city = null;//市
			
			//根据组织的级别构造输出的对象
			switch(orgLevel){
			case 2: province = (String)courierOrgInfo.get("orgName");break;
			case 3: city = (String)courierOrgInfo.get("orgName");break;
			case 4: result.put("district", courierOrgInfo.get("orgName"));break;
			case 5: result.put("area", courierOrgInfo.get("orgName"));break;
			case 6: result.put("network", courierOrgInfo.get("orgName"));break;
			}
			
			String str = (String)courierOrgInfo.get("orgPath");

			if(str != null){			
				String[] strs = str.split("_");
				for(String s:strs){
				    if(s.length() < 1) continue;
					Map<String, Object> org = this.getOrgInfo(Integer.valueOf(s));
					Integer level = (Integer)org.get("level");
					switch(level){
					case 2: province = (String)org.get("orgName");break;
					case 3: city = (String)org.get("orgName");break;
					case 4: result.put("district", org.get("orgName"));break;
					case 5: result.put("area", org.get("orgName"));break;
					case 6: result.put("network", org.get("orgName"));break;
					}
				}
			}
			
			result.put("city", province+city);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
	}

}
