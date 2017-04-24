package com.wm.service.menu;

import java.util.List;
import java.util.Map;

import com.wm.entity.menu.MenutypeEntity;

public interface MenuClassifyServiceI {
	/**
	 * 菜品分类列表查询
	 * @author chengyinghao
	 * @param merchantId
	 * @return
	 */
	public List<Map<String,Object>> getTpmOfferClassifyList(int merchantId,int start,int num);
	
	/**
	 * 添加菜品分类名
	 * @author chengyinghao
	 * @param tpmOfferClassify
	 * @return
	 */
	public int createTpmOfferClassify(MenutypeEntity menutype);
	
	/**
	 * 修改菜品分类名
	 * @author chengyinghao
	 * @param tpmOfferClassify
	 * @return
	 */
	public int updateTpmOfferClassify(MenutypeEntity menutype);
	
	/**
	 * 菜品分类排序
	 * @author chengyinghao
	 * @param IDs
	 * @return
	 */
	public int updateTpmOfferClassifyBitch(List<MenutypeEntity> list);
	
	/**
	 * 删除菜品分类
	 * @author chengyinghao
	 * @param tpmOfferClassify
	 * @return
	 */
	public int deleteTpmOfferClassify(Integer merchantId , Integer id ,Integer countSum);
	
	/**
	 * 判断是否有重复的分类名
	 * @param menutype
	 * @return
	 */
	public Map<String,Object> getClassifyName(MenutypeEntity menutype , int po);
	
	/**
	 * pos端添加商品分类
	 * @param menutype
	 * @return
	 */
	public MenutypeEntity posCreateMenutype(MenutypeEntity menutype);
}