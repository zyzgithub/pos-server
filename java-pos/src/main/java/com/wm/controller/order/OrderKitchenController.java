/*package com.wm.controller.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.order.OrderKitchenServiceI;

@Controller
@RequestMapping("ci/orderKitController")
public class OrderKitchenController extends BaseController{
	
	@Autowired
	OrderKitchenServiceI orderKitchenService;
	
	*//**
	 * 获取商家公告
	 * @param id 商家id
	 * @return
	 *//*
	@RequestMapping(params = "getNotice")
	@ResponseBody
	public AjaxJson getNotice(HttpServletRequest request, HttpServletResponse response, Long id ) {
		AjaxJson aj = new AjaxJson();
		if(id!=null && !"".equals(id)){
			Map<String, Object> map = orderKitchenService.getNotice(id);
			aj.setObj(map);
			aj.setSuccess(true);
		}else{
			aj.setMsg("id不能为空");
			aj.setSuccess(false);
		}
		return aj;
	}

	*//**
	 * 添加或更新商家公告
	 * @param id 商家id
	 * @param notice 商家公告
	 * @param notice_time 上一次发布公告的时间
	 * @return
	 *//*
	@RequestMapping(params = "createOrUpdateNotice")
	@ResponseBody
	public AjaxJson createOrUpdateNotice(HttpServletRequest request, HttpServletResponse response, Long id, String notice){
		AjaxJson aj = new AjaxJson();
		if(id==null || "".equals(id)){
			aj.setMsg("操作失败");
			aj.setSuccess(false);
			aj.setStateCode("01");
		}else{
			
			orderKitchenService.createOrUpdateNotice(id, notice);
			aj.setMsg("操作成功");
			aj.setSuccess(true);
			aj.setStateCode("00");
		}
		return aj;
	}
}
*/