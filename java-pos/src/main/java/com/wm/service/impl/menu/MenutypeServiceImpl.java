package com.wm.service.impl.menu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.dao.menu.MenuTypeDao;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.service.menu.MenutypeServiceI;

@Service("menutypeService")
@Transactional
public class MenutypeServiceImpl extends CommonServiceImpl implements MenutypeServiceI {

	@Autowired
	private MenuTypeDao menuTypeDao;
	
	@Override
	public List<MenutypeEntity> queryByMerchantId(Integer merchantId) {
		// TODO Auto-generated method stub
		return menuTypeDao.queryByMerchantId(merchantId);
	}

	/**
	 * 获取未删除商品的商品类型.
	 * @param merchantId
	 * @param type:1.过滤没有商品的分类; 2.查询所有的商品分类，该分类下没有商品也查出来
	 */
	@Override
	public List<MenutypeEntity> getTypeByMerchantId(Integer merchantId, int type) {
		StringBuilder query = new StringBuilder();
		query.append("select mt.id, mt.name, mt.sort_num sortNum, mt.merchant_id merchantId, mt.create_time createTime, mt.cost_lunch_box costLunchBox from ");
		query.append(" menu_type mt where " );
		if(type == 1){
			query.append(" mt.id in ( select distinct m.type_id from menu m where m.merchant_id = ? and m.is_delete = 'N' )");
		}
		if(type == 2){
			query.append(" mt.merchant_id = ?");
		}
		query.append(" order by mt.sort_num asc");
		return this.findObjForJdbc(query.toString(), MenutypeEntity.class, merchantId);
	}

	@Override
	public List<MenutypeEntity> getTypeByTypeId(Integer typeId) {
		StringBuilder query = new StringBuilder();
		query.append("select mt.id, mt.name, mt.sort_num sortNum, mt.merchant_id merchantId, mt.create_time createTime, mt.cost_lunch_box costLunchBox from ");
		query.append(" menu_type mt where " );
		query.append(" id = ? ");
		return findObjForJdbc(query.toString(), MenutypeEntity.class, typeId);
	}

	@Override
	public List<Integer> getPromotionActivityIds(Integer merchantId) {
		List<Integer> activityIds = new ArrayList<Integer>();
		try {
			String sql = "select id from promotion_activity where merchant_id = ? and ( deadline_type = 1"
					+ " OR (deadline_type = 2 and start_date <= UNIX_TIMESTAMP(current_date()) and end_date >= UNIX_TIMESTAMP(current_date())"
					+ " and start_hour <= TIME_TO_SEC(NOW()) and end_hour >= TIME_TO_SEC(NOW())) ) and state = 1 and range_type in (1,2)"; 
			List<Map<String, Object>> idsMap = this.findForJdbc(sql,merchantId);
			if(CollectionUtils.isNotEmpty(idsMap)){
				for(Map<String, Object> idMap: idsMap){
					activityIds.add(Integer.parseInt(idMap.get("id").toString()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activityIds;
	}
	
}