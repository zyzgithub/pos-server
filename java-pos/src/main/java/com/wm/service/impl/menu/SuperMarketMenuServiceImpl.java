package com.wm.service.impl.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenuVariationLogDTO;
import com.wm.entity.menu.MenuVariationLogEntity;
import com.wm.entity.menu.MenuVo;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.menu.MenuunitEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantInfoExtendEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.service.menu.MenuPackageServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.SuperMarketMenuServiceI;

/**
 * @author  zhanxinming 
 * @date 创建时间：2016年8月11日 下午3:16:51    
 * @return
 * @version 1.0
 */
@Service("superMarketMenuService")
@Transactional
public class SuperMarketMenuServiceImpl extends CommonServiceImpl implements SuperMarketMenuServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(SuperMarketMenuServiceImpl.class);
	
	@Autowired
	private MenuPackageServiceI menuPackageService;
	
	@Autowired
	private MenuServiceI menuService;

	/**
	 * 校验录入商品数据
	 */
	@Override
	public String checkMenuInstock(MenuVariationLogDTO menuInstock) {
		MerchantEntity merchantEntity = this.get(MerchantEntity.class, menuInstock.getMerchantId());
		if(merchantEntity == null){
			return "找不到商家";
		}
		if(StringUtils.isBlank(menuInstock.getName())){
			return "商品名称不能为空哦";
		}
		if(menuInstock.getPrice() == null){
			return "售价不能为空哦";
		}
		if(!String.valueOf(menuInstock.getPrice()).matches("^(0|([1-9][0-9]{0,7}))(.[0-9]{1,2})?$")){
			return "售价限输入非负小数哦";
		}
//		if(menuInstock.getStockPrice() == null){
//			return "进价不能为空哦";
//		}
		if(!String.valueOf(menuInstock.getStockPrice()).matches("^(0|([1-9][0-9]{0,7}))(.[0-9]{1,2})?$")){
			return "进价限输入非负小数哦";
		}
		if(!String.valueOf(menuInstock.getBarcode()).matches("^[0-9]*$")){
			return "商品条码仅限输入正整数列哦";
		}
		if(menuInstock.getAddRepertory() == null){
			return "新增库存不能为空哦";
		}
		if(!String.valueOf(menuInstock.getAddRepertory()).matches("^[0-9]*$")){
			return "新增库存限输入非负整数哦";
		}
		if(menuInstock.getIsonline() == null){
			return "是否支持线上线下不能为空";
		}
		if(menuInstock.getDisplay() == null){
			return "上下架不能为空";
		}
		if(menuInstock.getTypeId() == null){
			return "商品分类不能为空哦";
		}
		if(menuInstock.getTypeId() != null){
			//没有该商品分类
			MenutypeEntity menutypeEntity = get(MenutypeEntity.class, menuInstock.getTypeId());
			if(menutypeEntity.getMerchantId().intValue() != menuInstock.getMerchantId().intValue()){
				return "您还没有该商品分类,请新建商品分类";
			}
		}
		
		//已存在的商品
		if(menuInstock.getMenuId() != null){
			logger.info("menuId:{}", menuInstock.getMenuId());
			MenuEntity menuEntity = this.get(MenuEntity.class, menuInstock.getMenuId());
			if(menuEntity == null){
				return "找不到商品信息";
			}
			
			//区分商品是自己原有的商品还是其他商家的商品，自己原有的商品只能添加库存，不可以修改其他属性
			//其他商家的商品需要校验存在重复
			if(menuInstock.getMerchantId().intValue() != menuEntity.getMerchantId().intValue()){
				logger.info("商品所属商家id：{}， pos商家id：{}",menuEntity.getMerchantId().intValue(), menuInstock.getMerchantId().intValue());
				//校验商品名是否重复
				Map<String,Object> nameMap = menuPackageService.getPackName(menuInstock.getName(),menuInstock.getMerchantId());
				if(Integer.parseInt(nameMap.get("cont").toString()) != 0 ){
					return "商品名称重复了哦";
				}
				
				if(menuInstock.getName().length() >15){
					return "商品名称不能超过15个汉字哦";
				}
				
				if(menuInstock.getBarcode().length() >15){
					return "商品条码不能超过15个正整数哦";
				}
//				//校验商品条码是否重复
//				Map<String,Object> barcodeMap = menuPackageService.getPackBarcode(menuInstock.getBarcode(),menuInstock.getMerchantId());
//				if(Integer.parseInt(barcodeMap.get("cont").toString()) != 0 ){
//					return "商品条码重复了哦";
//				}
			}
			else{
				//商品名称与原名称不同
				if(!menuEntity.getName().equals(menuInstock.getName())){
					return "已存在商品的名称不可以修改哦";
				}
				
				//商品条形码与原条码不同
				if(!menuEntity.getBarcode().equals(menuInstock.getBarcode())){
					return "已存在商品的条码不可以修改哦";
				}

				Double stockPrice = menuStockPrice(menuInstock.getMenuId());
				if(stockPrice == null){
					stockPrice = 0.00;
				}
				//商品进货价与原进货价不同
				if(!stockPrice.equals(menuInstock.getStockPrice())){
					return "已存在商品的进价不可以修改哦";
				}
				
				//商品售价与原售价不同
				if(!menuEntity.getPrice().equals(menuInstock.getPrice())){
					return "已存在商品的售价不可以修改哦";
				}
				
				//商品分类与原分类不同
				if(!menuEntity.getMenuType().getId().equals(menuInstock.getTypeId())){
					return "已存在商品的分类不可以修改哦";
				}
				
				//商品上下架状态与原上下架状态不同
//				if(!menuEntity.getDisplay().equals(menuInstock.getDisplay())){
//					return "已存在商品的上下架状态不可以修改哦";
//				}
				
				if(menuEntity.getIsonline() != null){
					//商品支持线上线下状态与原支持线上线下状态不同
					if(!menuEntity.getIsonline().toString().equals(menuInstock.getIsonline())){
						return "已存在商品的支持线上线下状态不可以修改哦";
					}
				}
			}
		}
		//新增商品
		else{
			//校验商品名是否重复
			Map<String,Object> nameMap = menuPackageService.getPackName(menuInstock.getName(),menuInstock.getMerchantId());
			if(Integer.parseInt(nameMap.get("cont").toString()) != 0 ){
				logger.error("menu cont by name:{}",nameMap.get("cont"));
				return "商品名称重复了哦";
			}
			
			if(menuInstock.getBarcode() != ""){
				//校验商品条码是否重复
				Map<String,Object> barcodeMap = menuPackageService.getPackBarcode(menuInstock.getBarcode(),menuInstock.getMerchantId());
				if(Integer.parseInt(barcodeMap.get("cont").toString()) != 0 ){
					logger.error("menu cont by barcode:{}",nameMap.get("cont"));
					return "商品条码重复了哦";
				}
			}
		}
		return "normal";
	}

	/**
	 * pos录入商品
	 */
	@Override
	public Map<String, Object> menuVariation(MenuVariationLogDTO menuInstock, int source, int instockType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		logger.info("MenuVariationLogDTO:{}, source:{}, instockType:{}",JSON.toJSON(menuInstock), source, instockType);
		MerchantInfoEntity merchantInfoEntity = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", menuInstock.getMerchantId());
		MerchantInfoExtendEntity merchantInfoExtendEntity = this.findUniqueByProperty(MerchantInfoExtendEntity.class, "merchantId", menuInstock.getMerchantId());
		
//		//招不到更多拍簧片的了，无法解锁更多姿势
//		logger.info("默认为自动审核，自动允许，merchantId:{}", menuInstock.getMerchantId());
//		saveMenuVariationLog(menuInstock, 2, "auto", source, instockType);
//		Map<String, Object> map = saveMenu(menuInstock, instockType);
//		resultMap.putAll(map);
//		resultMap.put("auditType", 1);
		
		//没有找找到商品变更审核设置
		if(merchantInfoExtendEntity == null || merchantInfoExtendEntity.getIsMenuAutoAudit() == null){
			//加盟店默认为自动审核，自动允许
			if(merchantInfoEntity.getShopFromType().intValue() == 1){
				logger.info("没有找到商家商品变更审核设置，加盟店默认为自动审核，自动允许，merchantId:{}", menuInstock.getMerchantId());
				saveMenuVariationLog(menuInstock, 2, "auto", source, instockType);
				Map<String, Object> map = saveMenu(menuInstock, instockType);
				resultMap.putAll(map);
				resultMap.put("auditType", 1);
			}
				
			//直营店默认为手动审核
			if(merchantInfoEntity.getShopFromType().intValue() == 2){
				logger.info("没有找到商家商品变更审核设置，直营店默认为手动审核，merchantId:{}", menuInstock.getMerchantId());
				saveMenuVariationLog(menuInstock, 1, null, source, instockType);
				resultMap.put("auditType", 2);
			}
		}
			
		//商品变更审核设置为自动审核，需要判断是自动审核还是自动拒绝
		else if(merchantInfoExtendEntity.getIsMenuAutoAudit().equals("Y")){
			//自动允许
			if(merchantInfoExtendEntity.getAutoAuditType().intValue() == 1){
				logger.info("商品录入自动审核，自动允许，merchantId:{}", menuInstock.getMerchantId());
				saveMenuVariationLog(menuInstock, 2, "auto", source, instockType);
				Map<String, Object> map = saveMenu(menuInstock, instockType);
				resultMap.putAll(map);
				resultMap.put("auditType", 1);
			}
			
			//自动拒绝
			if(merchantInfoExtendEntity.getAutoAuditType().intValue() == 2){
				logger.info("商品录入自动审核，自动拒绝，merchantId:{}", menuInstock.getMerchantId());
				saveMenuVariationLog(menuInstock, 3, "auto", source, instockType);
				resultMap.put("auditType", 3);
			}
		}
			
		//商品变更审核设置为手动审核
		else {
			logger.info("商品录入手动审核，merchantId:{}", menuInstock.getMerchantId());
			saveMenuVariationLog(menuInstock, 1, null, source, instockType);
			resultMap.put("auditType", 2);
		}
		return resultMap;
	}
	
	/**
	 * 保存商品变更记录
	 * @param menuInstock
	 */
	private void saveMenuVariationLog(MenuVariationLogDTO menuInstock, int auditState, String auditor, int source, int instockType){
		logger.info("Pos保存商品变更记录，merchantId：{}", menuInstock.getMerchantId());
		MenuVariationLogEntity menuVariationLogEntity = new MenuVariationLogEntity();
		menuVariationLogEntity.setAuditState(auditState);
		menuVariationLogEntity.setBarcode(menuInstock.getBarcode());
		menuVariationLogEntity.setStockPrice(menuInstock.getStockPrice());
		menuVariationLogEntity.setCreateTime(DateUtils.getSeconds());
		menuVariationLogEntity.setDisplay(menuInstock.getDisplay());
		menuVariationLogEntity.setInstockType(instockType);
		menuVariationLogEntity.setIsonline(menuInstock.getIsonline() == null ? null : Integer.valueOf(menuInstock.getIsonline()));
		menuVariationLogEntity.setImageUrl("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
		menuVariationLogEntity.setMerchantId(menuInstock.getMerchantId());
		menuVariationLogEntity.setName(menuInstock.getName());
		menuVariationLogEntity.setPrice(menuInstock.getPrice());
		menuVariationLogEntity.setTypeId(menuInstock.getTypeId());
		menuVariationLogEntity.setSource(source);
		menuVariationLogEntity.setOriginalName("");
		menuVariationLogEntity.setOriginalPrice(0.00);
		menuVariationLogEntity.setOriginalStockPrice(0.00);
		menuVariationLogEntity.setRepertory(0);
		menuVariationLogEntity.setStocktakeRepertory(menuInstock.getAddRepertory());
		menuVariationLogEntity.setDifferRepertory(menuInstock.getAddRepertory());
		//自动审核
		if(auditor != null){
			menuVariationLogEntity.setAuditor(auditor);
			menuVariationLogEntity.setAuditTime(DateUtils.getSeconds());
		}
		//收银员录入
		if(menuInstock.getCashierId() != null){
			menuVariationLogEntity.setCashierId(menuInstock.getCashierId());
		}
		//商品id不为null时，判断是自己的商品，还是其他商家的商品，自己的商品保存商品id，其他商家的商品不保存商品id
		if(menuInstock.getMenuId() != null){
			MenuEntity menuEntity = this.get(MenuEntity.class, menuInstock.getMenuId());
			if(menuEntity.getMerchantId().intValue() == menuInstock.getMerchantId().intValue()){
				menuVariationLogEntity.setMenuId(menuInstock.getMenuId());
				menuVariationLogEntity.setOriginalName(menuEntity.getName());
				menuVariationLogEntity.setOriginalPrice(menuEntity.getPrice());
				menuVariationLogEntity.setOriginalStockPrice(menuStockPrice(menuEntity.getId()));
				menuVariationLogEntity.setRepertory(menuEntity.getTodayRepertory());
				//入库单，变更后库存 = 原有库存 + 变更的库存；
				//库存差异 = 变更的库存
				if(instockType == 1){
					menuVariationLogEntity.setStocktakeRepertory(menuEntity.getTodayRepertory() + menuInstock.getAddRepertory());
					menuVariationLogEntity.setDifferRepertory(menuInstock.getAddRepertory());
				}
				//综合变更单，变更后库存 = 变更的库存；
				//库存差异 = 变更的库存 - 原有库存
				if(instockType == 2){
					//不改动商品库存的时候
					if(menuInstock.getAddRepertory() == null){
						menuInstock.setAddRepertory(menuEntity.getTodayRepertory());
					}
					menuVariationLogEntity.setStockPrice(menuStockPrice(menuEntity.getId()));
					menuVariationLogEntity.setStocktakeRepertory(menuInstock.getAddRepertory());
					menuVariationLogEntity.setDifferRepertory(menuInstock.getAddRepertory() - menuEntity.getTodayRepertory());
				}
			}
			//如果原来有商品图片
			if(menuEntity.getImage() != null && !menuEntity.getImage().equals(""))
				menuVariationLogEntity.setImageUrl(menuEntity.getImage());
		}
		//不是在POS上录入商品
		if(menuInstock.getUserId() != null){
			menuVariationLogEntity.setUserId(menuInstock.getUserId());
		}
		save(menuVariationLogEntity);
		logger.info("Pos保存商品变更记录成功");
	}
	
	/**
	 * 保存或跟新商品信息
	 * @param menuInstock
	 */
	private Map<String, Object> saveMenu(MenuVariationLogDTO menuInstock, int instockType){
		MenuEntity menuEntity = new MenuEntity();
		Map<String, Object> map = new HashMap<String, Object>();
		MenutypeEntity menutypeEntity = null;
		if(menuInstock.getTypeId() != null){
			menutypeEntity = this.get(MenutypeEntity.class, menuInstock.getTypeId());
		}
		//没有商品id,为新增商品
		if(menuInstock.getMenuId() == null){
			logger.info("新增商品，merchantId:{}", menuInstock.getMerchantId());
			menuEntity.setBarcode(menuInstock.getBarcode());
			menuEntity.setCreateTime(DateUtils.getSeconds());
			menuEntity.setDisplay(menuInstock.getDisplay());
			menuEntity.setImage("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
			
			menuEntity.setMenuType(menutypeEntity);
			menuEntity.setMerchantId(menuInstock.getMerchantId());
			menuEntity.setName(menuInstock.getName());
			menuEntity.setPrice(menuInstock.getPrice());
			menuEntity.setOriginalPrice(menuInstock.getPrice());
			menuEntity.setTodayRepertory(menuInstock.getAddRepertory());
			menuEntity.setProductionDate(menuInstock.getProductionDate());
			menuEntity.setShelfLife(menuInstock.getShelfLife());
			menuEntity.setBuyCount(0);
			if(StringUtils.isNotEmpty(menuInstock.getUnit())){
				menuEntity.setUnit(menuInstock.getUnit());
			} else {
				menuEntity.setUnit("份");
			}
			if(null != menuInstock.getUnitId()){
				menuEntity.setUnitId(menuInstock.getUnitId());
			} else {
				menuEntity.setUnitId(9);
			}
			menuEntity.setIsSync(1);
			menuEntity.setPriceOnline(0.00);
			menuEntity.setSyncTime(0);
			menuEntity.setIsDelete("N");
			menuEntity.setWarnInventory(20);
			menuEntity.setStandardInventory(50);
			if(menuInstock.getIsonline() != null){
				menuEntity.setIsonline(Integer.valueOf(menuInstock.getIsonline()));
			}
			save(menuEntity);
			saveMenuStock(menuInstock, menuEntity.getId());
			map.put("menuId", menuEntity.getId());
			map.put("instockType", "add");
		}
		else{
			menuEntity = this.get(MenuEntity.class, menuInstock.getMenuId());
			//有商品id,商品绑定的商家id相同，为更新商品
			if(menuEntity.getMerchantId().intValue() == menuInstock.getMerchantId().intValue()){
				logger.info("跟新商品，merchantId:{}, menuId:{}", menuInstock.getMerchantId(), menuInstock.getMenuId());
				if(menuInstock.getBarcode() != null && menuInstock.getBarcode() != ""){
					menuEntity.setBarcode(menuInstock.getBarcode());
				}
				if(menuInstock.getDisplay() != null){
					menuEntity.setDisplay(menuInstock.getDisplay());
				}
				if(menuInstock.getName() != null &&  menuInstock.getName() != ""){
					menuEntity.setName(menuInstock.getName());
				}
				if(menuInstock.getPrice() != null){
					menuEntity.setPrice(menuInstock.getPrice());
				}
				if(menuInstock.getIsonline() != null){
					menuEntity.setIsonline(Integer.valueOf(menuInstock.getIsonline()));
				}
				if(menuInstock.getProductionDate() != null){
					menuEntity.setProductionDate(menuInstock.getProductionDate());
				}
				if(menuInstock.getShelfLife() != null){
					menuEntity.setShelfLife(menuInstock.getShelfLife());
				}				
				if(StringUtils.isNotEmpty(menuInstock.getUnit())){
					menuEntity.setUnit(menuInstock.getUnit());
				} else {
					menuEntity.setUnit("份");
				}
				if(null != menuInstock.getUnitId()){
					menuEntity.setUnitId(menuInstock.getUnitId());
				} else {
					menuEntity.setUnitId(9);
				}				
				//更新商品库存
				if(instockType == 1){
					//入库单库存相加
					menuEntity.setTodayRepertory(menuInstock.getAddRepertory()+menuEntity.getTodayRepertory());
				}
				else{
					if(menuInstock.getAddRepertory() != null){
						menuEntity.setTodayRepertory(menuInstock.getAddRepertory());
					}
				}
				if(menutypeEntity != null){
					menuEntity.setMenuType(menutypeEntity);
				}
				updateEntitie(menuEntity);
//				if(menuInstock.getStockPrice() != null){
//					saveMenuStock(menuInstock, menuEntity.getId());
//				}
				map.put("menuId", menuEntity.getId());
				map.put("instockType", "update");
			}
			//有商品id,商品绑定的商家id不同，为新增商品
			else{
				logger.info("新增商品，此商品其他商家有，merchantId:{},otherMerchantId:{}, orderMenuId:{}", menuInstock.getMerchantId(),menuEntity.getMerchantId(), menuEntity.getId());
				MenuEntity menuEntity2 = new MenuEntity();
				menuEntity2.setCreateTime(DateUtils.getSeconds());
				menuEntity2.setTodayRepertory(menuInstock.getAddRepertory());
				menuEntity2.setMerchantId(menuInstock.getMerchantId());
				menuEntity2.setBarcode(menuInstock.getBarcode());
				menuEntity2.setDisplay(menuInstock.getDisplay());
				menuEntity2.setProductionDate(menuInstock.getProductionDate());
				menuEntity2.setShelfLife(menuInstock.getShelfLife());
				
				menuEntity2.setMenuType(menutypeEntity);
				menuEntity2.setName(menuInstock.getName());
				menuEntity2.setPrice(menuInstock.getPrice());
				menuEntity2.setOriginalPrice(menuInstock.getPrice());
				menuEntity2.setBuyCount(0);
				if(StringUtils.isNotEmpty(menuInstock.getUnit())){
					menuEntity2.setUnit(menuInstock.getUnit());
				} else {
					menuEntity2.setUnit("份");
				}
				if(null != menuInstock.getUnitId()){
					menuEntity2.setUnitId(menuInstock.getUnitId());
				} else {
					menuEntity2.setUnitId(9);
				}
				menuEntity2.setIsSync(1);
				menuEntity2.setPriceOnline(0.00);
				menuEntity2.setSyncTime(0);
				menuEntity2.setIsDelete("N");
				menuEntity2.setWarnInventory(20);
				menuEntity2.setStandardInventory(50);
				menuEntity2.setImage("http://oss.0085.com/courier/2016/0815/1471247874374.jpg");
				if(menuInstock.getIsonline() != null){
					menuEntity2.setIsonline(Integer.valueOf(menuInstock.getIsonline()));
				}
				if(menuEntity.getImage() != null && !menuEntity.getImage().equals("")){
					menuEntity2.setImage(menuEntity.getImage());
				}
				save(menuEntity2);
				saveMenuStock(menuInstock, menuEntity2.getId());
				map.put("menuId", menuEntity2.getId());
				map.put("instockType", "add");
			}
		}
		logger.info("保存商品成功,map:{}", map.toString());
		return map;
	}
	
	
	private void saveMenuStock(MenuVariationLogDTO menuInstock,int menuId){
		logger.info("录入商品时，保存进货价，menuId:{}, stockPrice:{}",menuId, menuInstock.getStockPrice());
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO 0085_stock (menu_id, create_time, creator, stock_price) VALUES (?,NOW(),?,?)");
		MerchantEntity merchantEntity = get(MerchantEntity.class, menuInstock.getMerchantId());
		Integer userId = merchantEntity.getWuser().getId();
		executeSql(sql.toString(), menuId, userId, menuInstock.getStockPrice());
		logger.info("保存进货价成功menuId:{}, stockPrice:{}",menuId, menuInstock.getStockPrice());
	}

	/**
	 * 根据条码商家id查询商品
	 */
	@Override
	public List<Map<String, Object>> findMenu(MenuVo menu, Integer page,
			Integer rows) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> menuList = new ArrayList<Map<String,Object>>();
		//根据商家id和条码查找
		menuList = findByEntityList(menu, page, rows);
		
		//商家自己没有，按条码全局搜索
		if(CollectionUtils.isEmpty(menuList)){
			logger.info("商家自己没有该商品，去其他商家匹配，merchantId:{}, barcode:{}", menu.getMerchantId(), menu.getBarcode());
			MenuVo menuVo = new MenuVo();
			menuVo.setBarcode(menu.getBarcode());
			menuList = findByEntityList(menuVo,page,rows);
			if(!CollectionUtils.isEmpty(menuList)){
				for(int i = 0; i < menuList.size(); i++){
					logger.info("商品信息：menuId:{}", menuList.get(i).get("id"));
					MenutypeEntity menutypeEntity = get(MenutypeEntity.class, Integer.valueOf(menuList.get(i).get("typeId").toString()));
					Map<String, Object> map = new HashMap<String, Object>();
					Double stockPrice = menuStockPrice(Integer.valueOf(menuList.get(i).get("id").toString()));
					map.putAll(menuList.get(i));
					map.put("menuSource", "ortherMerchant");//表明商品来自其他商家
					if(menutypeEntity != null && menutypeEntity.getName() != null){
						map.put("typeName", menutypeEntity.getName());
					}
					else {
						map.put("typeName", "");
					}
					if(stockPrice == null){
						map.put("stockPrice", 0);
					}
					else {
						map.put("stockPrice", stockPrice);
					}
					resultList.add(map);
				}
			}
		}
		else{
			for(int i = 0; i < menuList.size(); i++){
				MenutypeEntity menutypeEntity = get(MenutypeEntity.class, Integer.valueOf(menuList.get(i).get("typeId").toString()));
				Map<String, Object> map = new HashMap<String, Object>();
				Double stockPrice = menuStockPrice(Integer.valueOf(menuList.get(i).get("id").toString()));
				map.putAll(menuList.get(i));
				map.put("menuSource", "oneself");//表明商品是自己原有的
				map.put("typeName", menutypeEntity.getName());
				if(stockPrice == null){
					map.put("stockPrice", 0);
				}
				else {
					map.put("stockPrice", stockPrice);
				}
				resultList.add(map);
			}
		}
		return resultList;
	}

	
	private List<Map<String, Object>> findByEntityList(MenuVo menu, Integer page,Integer rows) {
		StringBuffer sql = new StringBuffer();
		logger.info("merchantId:{}, barcode:{}", menu.getMerchantId(), menu.getBarcode());
		sql.append(
				" select m.id,m.name,m.price,case when m.image is null then 'http://oss.0085.com/courier/2016/0815/1471247874374.jpg' when m.image = '' then 'http://oss.0085.com/courier/2016/0815/1471247874374.jpg' else m.image end image,"
						+ "m.type_id as typeId,m.merchant_id as merchantId,m.create_time as createTime,m.buy_count as buyCount,m.intro,m.print_type as printType,"
						+ "m.display,m.repertory,m.today_repertory as todayRepertory,m.begin_time as beginTime,m.end_time as endTime,m.limit_today limitToday,m.is_delete isDelete,m.menu_sort as menuSort,"
						+ "m.price_online as priceOnline,ifnull(m.isonline,3) as isonline,m.barcode,m.production_date as productionDate,m.shelf_life as shelfLife,m.unit,m.unit_id unitId from menu m where 1=1 ");
		sql.append(" and m.is_delete = 'N'");
		if (StringUtil.isNotEmpty(menu.getMerchantId())) {
			sql.append(" and m.merchant_id = ");
			sql.append(menu.getMerchantId());
		}
		if (StringUtil.isNotEmpty(menu.getBarcode())) {
			sql.append(" and m.barcode = ");
			sql.append("'");
			sql.append(menu.getBarcode());
			sql.append("'");
		}
		sql.append(" order by m.menu_sort asc ");
		
		return findForJdbc(sql.toString(), page, rows);
	}
	
	private Double menuStockPrice(Integer menuId){
		Double stockPrice = findOneForJdbc("select stock_price from 0085_stock where menu_id = ? order by id desc limit 0, 1", Double.class, menuId);
		return stockPrice;
	}

	@Override
	public MenuunitEntity saveMenuUnit(MenuunitEntity menuunit) throws Exception{
		logger.info("录入商品单位，name:{}",menuunit.toString());
		save(menuunit);
		return menuunit;
	}
	
	@Override
	public List<Map<String, Object>> findMenuUnit() {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,name from menu_unit where is_delete=0 order by sort");
		return findForJdbc(sql.toString());
	}

}
