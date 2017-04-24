package com.wm.service.impl.courierorg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courier_mana.common.Constants;
import com.wm.entity.org.OrgEntity;
import com.wm.service.courierorg.CourierOrgServiceI;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierOrgService")
@Transactional
public class CourierOrgServiceImpl extends CommonServiceImpl implements CourierOrgServiceI {
	/**
	 * 根据parentOrgId,查找这个机构下的所有快递员
	 * @param parentOrgId 父机构ID
	 * @return 所有快递员
	 */
	@Override
	public List<Integer> queryCouriersByParentOrgId(Integer parentOrgId){
		List<Integer> courierIds = new ArrayList<Integer>();
		try {
			OrgEntity parentOrg = get(OrgEntity.class, parentOrgId);
			
			if(parentOrg == null){
				return new ArrayList<Integer>();
			}
			else {
				String sql = "";
				List<Map<String, Object>> idMapList = new ArrayList<Map<String,Object>>();
				//是网点，没有子机构了
				if(parentOrg.getLevel() == Constants.BRANCH_LEVEL){
					sql = "select distinct courier_id from 0085_courier_org co where co.org_id = ?";
					idMapList = this.findForJdbc(sql, parentOrgId);
				}
				else {
					String path = parentOrg.getPPath() + parentOrg.getId() + "_";
					sql = "select distinct courier_id from 0085_courier_org co where co.org_id = ? "
							+ " or co.org_id in (";
					sql += " 		select id org_id from 0085_org o where o.p_path like ? "
						+ "		) ";
					idMapList = this.findForJdbc(sql, parentOrgId, path + "%");
				}
				
				if(CollectionUtils.isNotEmpty(idMapList)){
					for(Map<String, Object> idMap: idMapList){
						courierIds.add(Integer.parseInt(idMap.get("courier_id").toString()));
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courierIds;
	}
}