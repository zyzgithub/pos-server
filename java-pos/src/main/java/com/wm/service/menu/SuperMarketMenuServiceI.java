package com.wm.service.menu;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.menu.MenuVariationLogDTO;
import com.wm.entity.menu.MenuVo;
import com.wm.entity.menu.MenuunitEntity;

/**
 * @author  zhanxinming 
 * @date 创建时间：2016年8月11日 下午3:15:26    
 * @return
 * @version 1.0
 */
public interface SuperMarketMenuServiceI extends CommonService{
	
	/**
	 * 校验商品信息
	 * @param menuInstock
	 * @return
	 */
	public String checkMenuInstock(MenuVariationLogDTO menuInstock);
	
	/**
	 * 根据商家的商品变更审核设置，保存商品变更记录，保存和更新商品
	 * @param menuInstock  商品信息
	 * @param source       更来商品的来源 1：pos端，2：后台，3：商家app
	 * @param instockType  商品变更记录的类型   1：入库单，2：综合变更单，3：盘点单
	 */
	public Map<String, Object> menuVariation(MenuVariationLogDTO menuInstock, int source, int instockType);
	
	/**
	 * 搜索商品
	 * @param menu
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> findMenu(MenuVo menu, Integer page, Integer rows);
	
	public MenuunitEntity saveMenuUnit(MenuunitEntity menuunit) throws Exception;
	
	/**
	 * 获取商品单位列表
	 * @return
	 */
	public List<Map<String, Object>> findMenuUnit();

}
