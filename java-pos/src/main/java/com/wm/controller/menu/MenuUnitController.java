package com.wm.controller.menu;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.menu.MenuunitEntity;
import com.wm.service.menu.SuperMarketMenuServiceI;

@Controller
@RequestMapping("ci/menuUnitController")
public class MenuUnitController extends BaseController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MenuUnitController.class);

	@Autowired
	private SuperMarketMenuServiceI superMarketMenuService;
	
	/**
	 * 查询商家商品列表
	 * @param merchantId
	 * @param menuTypeId 商品分类ID
	 * @param barcode 商品二维码
	 * @param name 商品名称
	 * @param isonline 1：只支持线上，2：只支持线下，3：同时支持线上线下，以逗号拼接
	 * @return
	 */
	@RequestMapping(params="saveMenuUnit")
	@ResponseBody
	public AjaxJson saveMenuUnit(String name, @RequestParam(defaultValue = "0")Integer sort) {
		AjaxJson json = new AjaxJson();
		try {
			MenuunitEntity menuunit = new MenuunitEntity();
			menuunit.setName(name);
			menuunit.setSort(sort == null ? 0: sort.intValue());
			menuunit.setCreateTime(new Date());
			menuunit.setIsDelete(0);
			menuunit = superMarketMenuService.saveMenuUnit(menuunit);
			json.setObj(menuunit);
			json.setMsg("录入成功");
			json.setStateCode("00");
			json.setSuccess(true);
		} catch (Exception e) {
			logger.error("商品单位录入失败,单位名称重复,",e);
			json.setMsg("录入失败,单位名称重复");
			json.setStateCode("01");
			json.setSuccess(false);
			return json;
		}
		return json;
	}
	
	/**
	 * 查询商家商品单位列表
	 * @return
	 */
	@RequestMapping(params="findMenuUnit")
	@ResponseBody
	public AjaxJson findMenuUnit(Integer merchantId) {
		logger.info("查询商品单位列表");
		AjaxJson j = new AjaxJson();
		try {
			List<Map<String, Object>> list = superMarketMenuService.findMenuUnit();
			if(CollectionUtils.isEmpty(list)){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("没有更多商品单位");
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
