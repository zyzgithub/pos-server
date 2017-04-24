package com.wm.service.impl.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.JSONHelper;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.wm.entity.menu.MenuPackVo;
import com.wm.service.menu.MenuPackageServiceI;

@Service("menuPackageService")
@Transactional
public class MenuPackageServiceImpl extends CommonServiceImpl implements MenuPackageServiceI{
	/**
	 * 获取菜品列表
	 * @param merchantId:商家ID
	 * @param typeId：分类ID
	 */
	public List<Map<String,Object>> getTpmPackageMainList(int merchantId,int typeId,int merchantSource,Integer start,Integer num){
		List<Map<String, Object>> map;
		String sql = "select m.id,m.`name`,m.image,m.price,m.display,print_type from `menu` m LEFT JOIN `menu_type` n ON m.type_id = n.id " 
				+" where m.merchant_id = ? "; 
		if(merchantSource != 2){
			sql = sql + " and m.type_id = ? ";
		}
		sql = sql + " and m.is_delete = ? ORDER BY m.menu_sort ASC,m.create_time DESC limit ?,?";
		
		if(merchantSource != 2){
			map= this.findForJdbc(sql, merchantId, typeId,"N",start,num);
		}else{
			map= this.findForJdbc(sql, merchantId,"N",start,num);
		}
		
		return map;
	}
	
	/**
	 * 删除菜品
	 * @param merchantId:商家ID
	 * @param id:菜品id
	 */
	public Integer deleteTpmPackageList(int merchantId , int id){
		String sql = "update `menu` set is_delete = ? , display='N' where merchant_id =? and id =?";
		
		Integer delmenu=this.executeSql(sql,"Y",merchantId , id);
		return delmenu;
	}
	
	/**
	 * 菜品  上架/下架
	 * @param merchantId:商家ID
	 * @param id:菜品id
	 */
	public Integer updateTpmPackageByStatus(int merchantId , int id , String dispaly){
		String sql = "update `menu` set display = ? where  merchant_id =? and id =?";
		
		Integer updismenu = this.executeSql(sql,dispaly ,merchantId ,id);
		
		return updismenu;
	}
	
	/**
	 *  添加菜品
	 */
	public Integer createTpmPackage(MenuPackVo menuVo){
		Integer insertId = 0 ;
		
		if(Integer.parseInt(menuVo.getMerchantSource().toString()) == 2){
			int merchantId = Integer.parseInt(menuVo.getMerchantId().toString());
			String sql = "select id,count(id) coun from `menu_type` where merchant_id = ?";
			Map<String,Object> kimapMap = this.findOneForJdbc(sql, merchantId);
			if(Integer.parseInt(kimapMap.get("coun").toString()) == 2){
				return 0;
			}
			
			if(Integer.parseInt(kimapMap.get("coun").toString()) == 0){
				sql = "INSERT INTO `menu_type`(name,merchant_id,sort_num,create_time) VALUES(?,?,?,unix_timestamp(now()))";
				this.executeSql(sql, "私厨系列",merchantId, 0);
				
				sql = "select last_insert_id() lastInsertId";
				Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
				insertId = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
				menuVo.setTypeId(insertId);
			}else{
				menuVo.setTypeId(Integer.parseInt(kimapMap.get("id").toString()));
			}
		}
		
		String sql = "insert into `menu`(name,image,type_id,merchant_id,create_time,intro,repertory,today_repertory,menu_sort"
				+ ",price,print_type) values(?,?,?,?,unix_timestamp(now()),?,?,?,?,?,?)"; 
		
		Integer crepackInteger =this.executeSql(sql,menuVo.getName(),menuVo.getImage(),menuVo.getTypeId(),menuVo.getMerchantId(),menuVo.getIntro(),
				menuVo.getTodayRepertory(),menuVo.getTodayRepertory(),menuVo.getMenuSort(),menuVo.getPrice(),1);
		
		return crepackInteger;
	}
	
	/**
	 * 编辑菜品查询
	 * @param merchantId : 商家ID
	 * @param id : 菜品ID
	 */
	public Map<String,Object> selectTpmPackagepo(int merchantId ,Integer id){
		String sql = "select m.image,m.name,m.price,m.today_repertory,m.intro from `menu` m "
				+ " where m.merchant_id = ? and m.id = ? ";
		
		Map<String,Object> selMap= this.findOneForJdbc(sql, merchantId,id);
		
		return selMap;
	}
	
	/**
	 * 编辑菜品
	 */
	public Integer updateTpmPackage(MenuPackVo menuVo){
		String sql = "update `menu` set image= ?, name = ? , price = ? , today_repertory = ? ,intro = ? "
				+ " where merchant_id =? and id =?";
		
		Integer updpackInteger = this.executeSql(sql, menuVo.getImage(),menuVo.getName(),menuVo.getPrice(),menuVo.getTodayRepertory(),menuVo.getIntro(),
				menuVo.getMerchantId(),menuVo.getId());
		
		return updpackInteger;
	}
	
	/**
	 * 菜品排序
	 */
	public AjaxJson sortList(Integer merchantId,String menuIds) throws JSONException{
		AjaxJson j = new AjaxJson();
		
		List<Map<String, Object>> list = JSONHelper.toList(menuIds);
		Map<String, Object> map=new HashMap<String, Object>();
		if (list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				String sql = "update `menu` set menu_sort = ? where merchant_id = ? and id = ? ";
				this.executeSql(sql, map.get("menuSort"),merchantId,map.get("id"));
			}
		}
		
		return j;
	}
	
	/**
	 * 获取菜品库存
	 */
	public Map<String,Object> selectTpmPackStock(Integer merchantId , Integer id){
		String sql = "select today_repertory from `menu` where merchant_id  = ? and id = ?";
		Map<String,Object> selmap = this.findOneForJdbc(sql, merchantId,id);
		
		return selmap;
	}
	
	/**
	 * 验证是否有重复名字
	 */
	public Map<String,Object> getPackName(String name , int merchantId){
		String sql = "select COUNT(name) cont from `menu` where merchant_id = ? and name = ? and is_delete = 'N' ";
		Map<String,Object> bog = this.findOneForJdbc(sql, merchantId, name);
		return bog;
	}
	
	public Map<String,Object> getPackNameIo(String name , int merchantId , int id){
		String sql = "select COUNT(name) cont from `menu` where merchant_id = ? and name = ? and is_delete = 'N' and id <> ? ";
		Map<String,Object> bog = this.findOneForJdbc(sql, merchantId, name , id);
		return bog;
	}
	
	/**
	 * 验证是否有重复条码
	 */
	public Map<String,Object> getPackBarcode(String Barcode , int merchantId){
		String sql = "select COUNT(Barcode) cont from `menu` where merchant_id = ? and Barcode = ? and is_delete = 'N' ";
		Map<String,Object> bog = this.findOneForJdbc(sql, merchantId, Barcode);
		return bog;
	}
}
