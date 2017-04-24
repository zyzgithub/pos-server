package com.wm.controller.menu;


import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wm.entity.menu.MenuVariationLogDTO;
import com.wm.entity.menu.MenuVo;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.MenutypeServiceI;
import com.wm.service.menu.SuperMarketMenuServiceI;
import com.wm.util.PageList;

/**
 * @author  zhanxinming 
 * @date 创建时间：2016年8月8日 上午10:48:16    
 * @return
 * @version 1.0
 */
@Controller
@RequestMapping("ci/superMarketMenuController")
public class SuperMarketMenuController {

	private static final Logger logger = LoggerFactory.getLogger(SuperMarketMenuController.class);
	
	@Autowired
	private SuperMarketMenuServiceI superMarketMenuService;
	@Autowired
	private MenutypeServiceI menutypeService;
	@Autowired
	private MenuServiceI menuService;
	
	@RequestMapping(params = "menuInstock")
	@ResponseBody
	public AjaxJson menuInstock(MenuVariationLogDTO menuInstock){
		logger.info("menuInstock:{}", JSON.toJSONString(menuInstock));
		AjaxJson json = new AjaxJson();
		try {
			if(menuInstock != null && menuInstock.getIsonline() != null){
				if(menuInstock.getIsonline().equals("同时支持线上线下")){
					menuInstock.setIsonline("3");
				}
				else if(menuInstock.getIsonline().equals("只支持线下")){
					menuInstock.setIsonline("2");
				}
				else if(menuInstock.getIsonline().equals("只支持线上")){
					menuInstock.setIsonline("1");
				}
				else{
					json.setMsg("请选择是否支持线上线下");
					json.setStateCode("01");
					json.setSuccess(false);
					return json;
				}
			}
			if(menuInstock != null && menuInstock.getDisplay() != null){
				if(menuInstock.getDisplay().equals("上架")){
					menuInstock.setDisplay("Y");
				}
				else if(menuInstock.getDisplay().equals("下架")){
					menuInstock.setDisplay("N");
				}
				else{
					json.setMsg("请选择上/下架");
					json.setStateCode("01");
					json.setSuccess(false);
					return json;
				}
			}
			String checkString = superMarketMenuService.checkMenuInstock(menuInstock);
			if(!"normal".equals(checkString)){
				logger.error(checkString);
				json.setMsg(checkString);
				json.setStateCode("01");
				json.setSuccess(false);
				return json;
			}
			
			Map<String, Object> map = superMarketMenuService.menuVariation(menuInstock, 1, 1);
			if(map.get("auditType").toString().equals("1")){
				MenuVo menuVo = new MenuVo();
				menuVo.setId(Integer.valueOf(map.get("menuId").toString()));
				PageList<com.wm.entity.menu.MenuVo> list = menuService.findByEntityList(menuVo, null, null);
				menuVo = list.getResultList().get(0);
				List<MenutypeEntity> menutypeEntitys = menutypeService.getTypeByTypeId(menuVo.getTypeId());
				map.put("menuVo", menuVo);
				map.put("menutypeEntity", menutypeEntitys.get(0));
			}
			map.remove("menuId");
			json.setObj(map);
			json.setMsg("录入成功");
			json.setStateCode("00");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("录入失败");
			json.setStateCode("01");
			json.setSuccess(false);
		}
		return json;
	}
	
	
	/**
	 * 查询商家商品列表
	 * @param merchantId
	 * @param menuTypeId 商品分类ID
	 * @param barcode 商品二维码
	 * @param name 商品名称
	 * @param isonline 1：只支持线上，2：只支持线下，3：同时支持线上线下，以逗号拼接
	 * @return
	 */
	@RequestMapping(params="findMenu")
	@ResponseBody
	public AjaxJson findMenu(MenuVo menu, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "6") Integer rows) {
		logger.info("pos录入时查询商品, merchantId:{}, barcode:{}, page:{}, rows:{}", menu.getMerchantId(), menu.getBarcode(), page, rows);
		menu.setIsDelete("N");
		
		AjaxJson j = new AjaxJson();
		try {
			List<Map<String, Object>> list = superMarketMenuService.findMenu(menu, page, rows);
			if(CollectionUtils.isEmpty(list)){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("没有更多相关商品");
				return j;
			}
			j.setObj(list);
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("操作失败");
		}
		
		return j;
	}
	
}
