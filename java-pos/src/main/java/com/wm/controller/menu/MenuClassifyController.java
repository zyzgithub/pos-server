package com.wm.controller.menu;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.service.menu.MenuClassifyServiceI;

import jeecg.system.controller.core.UserController;

@Controller
@RequestMapping("ci/offerClassify")
public class MenuClassifyController extends BaseController {
	private static final Logger logger = Logger.getLogger(MenuClassifyController.class);

	@Autowired
	private MenuClassifyServiceI offerClassifyService;

	/**
	 * 菜品分类列表查询
	 * 
	 * @author chengyinghao
	 * @param Object
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params = "list")
	@ResponseBody
	public AjaxJson list(HttpServletRequest request, HttpServletResponse response, int merchantId,
			@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "10") int num) {
		AjaxJson j = new AjaxJson();
		if ((Integer) merchantId == null) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("商家ID不能为空!");

			return j;
		}

		List<Map<String, Object>> accountInfo = offerClassifyService.getTpmOfferClassifyList(merchantId, start, num);

		if (accountInfo != null) {
			j.setObj(accountInfo);
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("操作成功");
		} else {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("操作失败");
		}

		return j;
	}

	/**
	 * 添加菜品分类名
	 * 
	 * @author chengyinghao
	 * @param tpmOfferClassify
	 * @return
	 */
	@RequestMapping(params = "addishes")
	@ResponseBody
	public AjaxJson addDishes(HttpServletRequest request, HttpServletResponse response, MenutypeEntity menutype) {
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setSuccess(false);
		if (menutype.getMerchantId() == null) {
			j.setMsg("商家ID不能为空!");
			return j;
		}
		if (menutype.getName() == null && menutype.getName() == "") {
			j.setMsg("菜品分类名不能为空!");
			return j;
		}
		if (menutype.getName().length()>15) {
			j.setMsg("分类名称长度请不要超过15个字");
			return j;
		}
		if (menutype.getSortNum() == null) {
			menutype.setSortNum(1);
		}

		Map<String, Object> klmap = offerClassifyService.getClassifyName(menutype, 1);
		if (Integer.parseInt(klmap.get("cont").toString()) != 0) {
			j.setMsg("菜品分类名重复!");
			return j;
		}

		Integer offerClassifyId = offerClassifyService.createTpmOfferClassify(menutype);

		if (offerClassifyId == 0) {
			j.setMsg("添加菜品分类失败");
			j.setStateCode("01");
			j.setSuccess(false);
		} else {
			j.setMsg("添加菜品分类成功");
			j.setStateCode("00");
			j.setSuccess(true);
		}

		return j;
	}

	/**
	 * 修改菜品分类名
	 * 
	 * @author chengyinghao
	 * @param tpmOfferClassify
	 * @return
	 */
	@RequestMapping(params = "updateDishes")
	@ResponseBody
	public AjaxJson updateDishes(HttpServletRequest request, HttpServletResponse response, MenutypeEntity menutype) {

		logger.info("更改菜品信息 : " + menutype);

		AjaxJson j = new AjaxJson();

		j.setStateCode("01");
		j.setSuccess(false);
		if (menutype.getMerchantId() == null) {
			j.setMsg("商家ID不能为空!");
			return j;
		}
		if (menutype.getId() == null) {
			j.setMsg("菜品分类ID不能为空!");
			return j;
		}
		if (menutype.getName() == null && menutype.getName() == "") {
			j.setMsg("菜品分类名不能为空!");
			return j;
		}
		Map<String, Object> klmap = offerClassifyService.getClassifyName(menutype, 2);
		if (Integer.parseInt(klmap.get("cont").toString()) != 0) {
			j.setMsg("菜品分类名重复!");
			return j;
		}

		Integer updateoffId = offerClassifyService.updateTpmOfferClassify(menutype);
		if (updateoffId == 0) {
			j.setMsg("修改菜品分类失败");
			j.setStateCode("01");
			j.setSuccess(false);
		} else {
			j.setMsg("修改菜品分类成功");
			j.setStateCode("00");
			j.setSuccess(true);
		}
		return j;
	}

	/**
	 * 删除菜品分类
	 * 
	 * @author chengyinghao
	 * @param tpmOfferClassify
	 * @return
	 */
	@RequestMapping(params = "deleteDishes")
	@ResponseBody
	public AjaxJson deleteDishes(HttpServletRequest request, HttpServletResponse response, Integer merchantId,
			Integer id, Integer countSum) {
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setSuccess(false);
		if (merchantId == null) {
			j.setMsg("商家ID不能为空!");
			return j;
		}
		if (id == null) {
			j.setMsg("菜品分类ID不能为空!");
			return j;
		}
		if (countSum == null) {
			j.setMsg("菜品总数不能为空!");
			return j;
		}
		if (countSum > 0) {
			j.setMsg("菜品分类有菜品不能删除!");
			return j;
		}

		Integer deloffId = offerClassifyService.deleteTpmOfferClassify(merchantId, id, countSum);
		if (deloffId == 0) {
			j.setMsg("删除菜品分类失败");
			j.setStateCode("01");
			j.setSuccess(false);
		} else {
			j.setMsg("删除菜品分类成功");
			j.setStateCode("00");
			j.setSuccess(true);
		}
		return j;
	}

	/**
	 * 菜品分类重新排序 传所有的类别
	 * 
	 * @author chengyinghao
	 * @param IDs
	 * @return
	 */
	@RequestMapping(params = "updateDishSort")
	@ResponseBody
	public AjaxJson updateDishesList(HttpServletRequest request, HttpServletResponse response, String IDs) {
		AjaxJson j = new AjaxJson();
		if (IDs == null || IDs.isEmpty()) {
			j.setMsg("排序相关信息不能为空!");
			j.setStateCode("01");
			j.setSuccess(false);

			return j;
		}

		TypeReference<List<MenutypeEntity>> lisTypeReference = new TypeReference<List<MenutypeEntity>>() {
		};
		List<MenutypeEntity> list = JSON.parseObject(IDs, lisTypeReference);
		for (MenutypeEntity t : list) {
			if (t.getId() == null || t.getMerchantId() == null) {
				j.setMsg("用户ID、菜品分类ID不能为空");
				j.setStateCode("01");
				j.setSuccess(false);

				return j;
			}
		}

		Integer updatesortID = offerClassifyService.updateTpmOfferClassifyBitch(list);
		if (updatesortID == 0) {
			j.setMsg("更新菜品排序失败");
			j.setStateCode("01");
			j.setSuccess(false);
		} else {
			j.setMsg("更新菜品排序成功");
			j.setStateCode("00");
			j.setSuccess(true);
		}

		return j;
	}

	/**
	 * pos添加商品分类
	 * @param request
	 * @param response
	 * @param menutype
	 * @return
	 */
	@RequestMapping(params = "posAddishes")
	@ResponseBody
	public AjaxJson posAddishes(HttpServletRequest request, HttpServletResponse response, MenutypeEntity menutype) {
		AjaxJson j = new AjaxJson();
		logger.info("pos端添加商品分类,menutype: " + JSON.toJSONString(menutype));
		j.setStateCode("01");
		j.setSuccess(false);
		if (menutype.getMerchantId() == null) {
			j.setMsg("商家ID不能为空!");
			return j;
		}
		if (menutype.getName() == null && menutype.getName() == "") {
			j.setMsg("分类名称一定要填写哦!");
			return j;
		}
		if (menutype.getName().length()>15) {
			j.setMsg("分类名称长度请不要超过15个字");
			return j;
		}
		if (menutype.getSortNum() == null) {
			menutype.setSortNum(1);
		}

		try {
			Map<String, Object> klmap = offerClassifyService.getClassifyName(menutype, 1);
			if (Integer.parseInt(klmap.get("cont").toString()) != 0) {
				j.setMsg("商家已创建该分类，请不要重复哦!");
				return j;
			}

			MenutypeEntity menutypeEntity = offerClassifyService.posCreateMenutype(menutype);
			j.setObj(menutypeEntity);
			j.setMsg("保存成功");
			j.setStateCode("00");
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return j;
	}
}