package com.ci.controller;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import jeecg.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.ci.entity.CIParam;
import com.ci.entity.CustomerInterface;
import com.ci.service.CustomerInterfaceServiceI;

/**   
 * @Title: Controller
 * @Description: 自定义接口
 * @author zhenjunzhuo
 * @date 2014-08-01 17:15:35
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/customerInterfaceController")
public class CustomerInterfaceController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CustomerInterfaceController.class);

	@Autowired
	private CustomerInterfaceServiceI customerInterfaceService;
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
	 * 自定义接口列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "customerInterface")
	public ModelAndView customerInterface(HttpServletRequest request) {
		return new ModelAndView("/ci/customerInterfaceList");
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
	@ResponseBody
	public List<TreeGrid> datagrid(CustomerInterface customerInterface,HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
		CriteriaQuery cq = new CriteriaQuery(CustomerInterface.class);
		if (treegrid.getId() != null) {
			cq.eq("customerInterface.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("customerInterface");
		}
		cq.add();
		
		List<CustomerInterface> customerInterfaceList = systemService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIdField("id");
		treeGridModel.setTextField("name");
		treeGridModel.setIcon("descrition");
		treeGridModel.setSrc("methodName");
		treeGridModel.setChildList("CustomerInterfaces");
		
		treeGrids = systemService.treegrid(customerInterfaceList, treeGridModel);
		return treeGrids;
	}

	/**
	 * 删除自定义接口
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CustomerInterface customerInterface, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		customerInterface = systemService.getEntity(CustomerInterface.class, customerInterface.getId());
		message = "删除成功";
		customerInterfaceService.delete(customerInterface);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加自定义接口
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CustomerInterface customerInterface, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		if (customerInterface.getMethodLevel() == 1) {
			customerInterface.setCustomerInterface(null);
		}
		List<CustomerInterface> data = customerInterfaceService.findByQueryString("from CustomerInterface where id= '"+ customerInterface.getId().trim() +"'");
		
		if (data != null && data.size() > 0) {
			message = "更新成功";
			CustomerInterface t = customerInterfaceService.get(CustomerInterface.class, customerInterface.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(customerInterface, t);
				customerInterfaceService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			customerInterface.setId(customerInterface.getId().trim());
			customerInterfaceService.save(customerInterface);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 自定义接口列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CustomerInterface customerInterface, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(customerInterface.getId())) {
			customerInterface = customerInterfaceService.getEntity(CustomerInterface.class, customerInterface.getId());
			req.setAttribute("customerInterfacePage", customerInterface);
		}
		return new ModelAndView("/ci/customerInterface");
	}
	
	//选择父接口
	@RequestMapping(params = "setPInterface")
	@ResponseBody
	public List<ComboTree> setPInterface(CustomerInterface customerInterface,HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(CustomerInterface.class);
		if (comboTree.getId() != null) {
			cq.eq("customerInterface.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("customerInterface");
		}
		cq.add();
		List<CustomerInterface> execontentList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "name", "customerInterfaces");
		comboTrees = systemService.ComboTree(execontentList, comboTreeModel, null);
		return comboTrees;
	}
	
	//检查method是否重复
	@RequestMapping(params = "checkMethodName")
	@ResponseBody
	public AjaxJson checkMethodName(String methodName,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		List<CustomerInterface> data = systemService.findByQueryString("from CustomerInterface where methodName = '"+ methodName +"'");
		if(data != null && data.size() > 0){
			j.setSuccess(true);
		}else{
			j.setSuccess(false);
		}
		return j;
	}
	
	/**跳转到测试界面**/
	@RequestMapping(params = "test")
	public ModelAndView test(CustomerInterface customerInterface,HttpServletRequest req) {
		String id = req.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			customerInterface = customerInterfaceService.findUniqueByProperty(CustomerInterface.class, "id", id);
			req.setAttribute("ci", customerInterface);
			
			List<CIParam> paramList = new ArrayList<CIParam>();
			paramList = systemService.findByProperty(CIParam.class, "customerInterface", customerInterface);
			req.setAttribute("paramList", paramList);
			
			//拼接参数的名称
			String str = "";
			if(paramList != null && paramList.size() > 0){
				for(CIParam c:paramList){
					str = str + c.getParamName() + ",";
				}
				req.setAttribute("paramNames", str);
			}
		}
		return new ModelAndView("/ci/test");
	}
	
	/**保存参数信息**/
	@RequestMapping(params = "saveNewParam")
	@ResponseBody
	public AjaxJson saveNewParam(String name,String comment,String dataType,String ciId, HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		try {
			CIParam p = new CIParam();
			p.setParamName(name);
			p.setParamType(dataType);
			p.setDescrition(comment);
			p.setCustomerInterface(systemService.get(CustomerInterface.class,ciId));
			
			systemService.save(p);
		}catch (Exception e) {
			j.setMsg("保存失败,请重试");
			e.printStackTrace();
		}
		j.setMsg("保存成功");
		return j;
	}
	
	/**删除参数**/
	@RequestMapping(params = "deleteParam")
	@ResponseBody
	public AjaxJson deleteParam(String id,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		try {
			CIParam p = systemService.getEntity(CIParam.class, id);
			if(p != null){
				systemService.delete(p);
			}
		}catch (Exception e) {
			j.setMsg("删除失败,请重试");
			e.printStackTrace();
		}
		j.setMsg("删除成功");
		return j;
	}
	
	/**保存编辑的请求参数**/
	@RequestMapping(params = "saveEditParam")
	@ResponseBody
	public AjaxJson saveEditParam(String name,String descrition,String dataType,String id,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		try {
			CIParam p = systemService.getEntity(CIParam.class, id);
			if(p != null){
				p.setDescrition(descrition);
				p.setParamName(name);
				p.setParamType(dataType);
				systemService.updateEntitie(p);
			}
		}catch (Exception e) {
			j.setMsg("编辑失败,请重试");
			e.printStackTrace();
		}
		j.setMsg("编辑成功");
		return j;
	}
}
