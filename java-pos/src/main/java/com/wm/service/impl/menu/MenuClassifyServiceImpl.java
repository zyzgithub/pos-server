package com.wm.service.impl.menu;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.menu.MenutypeEntity;
import com.wm.service.menu.MenuClassifyServiceI;

@Service("offerClassifyService")
@Transactional
public class MenuClassifyServiceImpl extends CommonServiceImpl implements MenuClassifyServiceI {

	/**
	 * 查询菜品分类
	 */
	public List<Map<String, Object>> getTpmOfferClassifyList(int merchantId, int start, int num) {

		String sql = "SELECT a.id,a.name,a.sort_num,a.cost_lunch_box,CASE WHEN a.id = b.type_id then count(*) else  0  end count_sum "
				+ " FROM `menu_type` a LEFT JOIN `menu` b on (a.id  = b.type_id and b.merchant_id =? and b.is_delete = 'N')"
				+ " where a.merchant_id=? and a.sort_num <> 0 GROUP BY a.id ORDER BY a.sort_num ASC,a.create_time DESC limit ?,?";

		List<Map<String, Object>> offlist = this.findForJdbc(sql, merchantId, merchantId, start, num);

		return offlist;
	}

	/**
	 * 添加菜品分类
	 */
	public int createTpmOfferClassify(MenutypeEntity menutype) {
		String sql = "INSERT INTO `menu_type`(name,merchant_id,sort_num,cost_lunch_box,create_time) VALUES(?,?,?,?,unix_timestamp(now()))";

		Double costLunchBox = menutype.getCostLunchBox();
		if (costLunchBox == null) {
			costLunchBox = 0.00;
		}
		int offerClassifyId = this.executeSql(sql, menutype.getName(), menutype.getMerchantId(), menutype.getSortNum(),
				costLunchBox);
		if ((Integer) offerClassifyId == null) {
			return 0;
		}

		return offerClassifyId;
	}

	/**
	 * 更新菜品分类名
	 */
	public int updateTpmOfferClassify(MenutypeEntity menutype) {
		String sql = "UPDATE `menu_type` SET name=?,cost_lunch_box=? where merchant_id= ? and id = ?";
		
		Double costLunchBox = menutype.getCostLunchBox();
		if(costLunchBox==null){
			costLunchBox = 0.00;
		}
		Integer updateoffId = this.executeSql(sql,menutype.getName(),costLunchBox,menutype.getMerchantId(),menutype.getId());
		if(updateoffId == null||updateoffId==0){
			updateoffId = this.getCountForJdbc("select count(*) count from menu_type where merchant_id= "+menutype.getMerchantId()+" and id = "+ menutype.getId()+" ").intValue();
			return updateoffId;			
		}
		
		return updateoffId;
	}

	/**
	 * 删除菜品分类
	 */
	public int deleteTpmOfferClassify(Integer merchantId, Integer id, Integer countSum) {
		String sql = "delete from `menu_type` where merchant_id = ? and id = ?";
		int validate = this.executeSql(sql, merchantId, id);

		return validate;
	}

	/**
	 * 菜品分类重新排序
	 */
	public int updateTpmOfferClassifyBitch(List<MenutypeEntity> list) {
		for (MenutypeEntity tpmOfferClass : list) {
			int sortNum = tpmOfferClass.getSortNum();
			Integer merchantId = tpmOfferClass.getMerchantId();
			Integer id = tpmOfferClass.getId();

			String sql = "UPDATE `menu_type` SET sort_num=? WHERE merchant_id=? and id=? ";
			Integer updatesortID = this.executeSql(sql, sortNum, merchantId, id);
			if (updatesortID == null) {
				return 0;
			}
		}

		return 1;
	}

	/**
	 * 判断是否有重复的分类名
	 */
	public Map<String, Object> getClassifyName(MenutypeEntity menutype, int po) {
		int merchantId = menutype.getMerchantId();
		String name = menutype.getName();
		Map<String, Object> bog;
		String sql = "select COUNT(name) cont from `menu_type` where merchant_id = ? and name = ? ";
		if (po == 2) {
			sql = sql + " and id <> ?";
			bog = this.findOneForJdbc(sql, merchantId, name, menutype.getId());
		} else {
			bog = this.findOneForJdbc(sql, merchantId, name);
		}

		return bog;
	}

	@Override
	public MenutypeEntity posCreateMenutype(MenutypeEntity menutype) {
		if(menutype.getCostLunchBox() == null){
			menutype.setCostLunchBox(0.00);
		}
		menutype.setCreateTime(DateUtils.getSeconds());
		save(menutype);
		return menutype;
	}
}