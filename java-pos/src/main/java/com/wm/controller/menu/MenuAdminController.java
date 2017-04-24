package com.wm.controller.menu;
import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.menu.MenuVo;
import com.wm.service.menu.MenuServiceI;
import com.wm.util.PageList;

/**
 * 
* @ClassName: MenuAdminController 
* @Description: 菜品控制器
* @author 黄聪
* @date 2015年9月18日 下午2:34:45 
*
 */
@Controller
@RequestMapping("menuAdminController")
public class MenuAdminController extends BaseController {
	
	@Autowired
	private MenuServiceI menuService;
	
	/**
	 * 根据菜品表字段查询菜品分页列表
	 * 
	 * @param request
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "findByEntityList")
	@ResponseBody
	public AjaxJson findByEntityList(HttpServletRequest request,
			MenuVo menu,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows) {
		AjaxJson j = new AjaxJson();
		PageList list = menuService.findByEntityList(menu, page, rows);
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
}


