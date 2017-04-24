package com.wm.controller.address;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.address.AddressEntity;
import com.wm.service.address.AddressServiceI;

/**   
 * @Title: Controller
 * @Description: address
 * @author wuyong
 * @date 2015-01-07 09:41:10
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/addressController")
public class AddressController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(AddressController.class);

	@Autowired
	private AddressServiceI addressService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * address列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "address")
	public ModelAndView address(HttpServletRequest request) {
		return new ModelAndView("com/wm/address/addressList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(AddressEntity address,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AddressEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, address);
		this.addressService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除address
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(AddressEntity address, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		address = systemService.getEntity(AddressEntity.class, address.getId());
		message = "删除成功";
		addressService.delete(address);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加address
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(AddressEntity address, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(address.getId())) {
			message = "更新成功";
			AddressEntity t = addressService.get(AddressEntity.class, address.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(address, t);
				addressService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			addressService.save(address);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * address列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(AddressEntity address, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(address.getId())) {
			address = addressService.getEntity(AddressEntity.class, address.getId());
			req.setAttribute("addressPage", address);
		}
		return new ModelAndView("com/wm/address/address");
	}
	
	
	@RequestMapping(params = "addDeliveryAddr")
	@ResponseBody
	public AjaxJson addDeliveryAddr(int userId,String name, String mobile, String city,
			String address_detail,String location){
		AjaxJson j = new AjaxJson();
		
		int addrId = addressService.addDeliveryAddr(userId, name, mobile, city, address_detail,location);
		
		if(addrId!=0){
			j.setSuccess(true);
			j.setObj(addrId);
		}else{
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("添加失败");
		}
		return j;
	}
}
