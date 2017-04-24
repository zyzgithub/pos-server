package com.wm.service.menu;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.json.JSONException;

import com.wm.entity.menu.MenuPackVo;

public interface MenuPackageServiceI {
	/**
	 * 添加菜品
	 * 
	 * @author chengyinghao
	 * @param tpmMainPackage
	 * @return
	 */
	public Integer createTpmPackage(MenuPackVo menuVo);

	/**
	 * 编辑菜品
	 * 
	 * @author chengyinghao
	 * @param tpmMainPackage
	 * @return
	 */
	public Integer updateTpmPackage(MenuPackVo menuVo);

	/**
	 * 获得菜品列表
	 * 
	 * @param tpmPackage
	 * @author chengyighao
	 * @return
	 */
	public List<Map<String,Object>> getTpmPackageMainList(int merchantId,int typeId,int merchantSource,Integer start,Integer num);

	/**
	 * 删除菜品
	 * 
	 * @author chengyinghao
	 * @param tpmPackage
	 * @param bindingResult
	 * @return
	 */
	public Integer deleteTpmPackageList(int merchantId , int id);

	/**
	 * 上下架菜品
	 * 
	 * @param tpmPackage
	 * @param bindingResult
	 * @author bin
	 * @return
	 */
	public Integer updateTpmPackageByStatus(int merchantId , int id , String dispaly);
	
	/**
	 * 菜品编辑查询
	 * @author chengyinghao
	 * @param packageId
	 */
	public Map<String,Object>  selectTpmPackagepo(int merchantId ,Integer id);
	
	/**
	 * 菜品排序
	 * @param merchantId
	 * @param menuIds
	 * @return
	 */
	public AjaxJson sortList(Integer merchantId,String menuIds) throws JSONException;
	
	/**
	 * 查询菜品库存
	 * @param merchantId
	 * @param id
	 * @return
	 */
	public Map<String,Object> selectTpmPackStock(Integer merchantId , Integer id);
	
	/**
	 * 验证是否有重复名字
	 * @param name
	 */
	public Map<String,Object> getPackName(String name , int merchantId);
	
	/**
	 * 编辑菜品验证是否有重复名字
	 * @param name
	 */
	public Map<String,Object> getPackNameIo(String name , int merchantId,int id);
	
	/**
	 * 验证是否有重复条码
	 * @param Barcode
	 * @param merchantId
	 * @return
	 */
	public Map<String,Object> getPackBarcode(String Barcode , int merchantId);
}