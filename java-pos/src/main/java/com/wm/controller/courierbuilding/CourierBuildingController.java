package com.wm.controller.courierbuilding;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.courierbuilding.CourierBuildingEntity;
import com.wm.service.courierbuilding.CourierBuildingServiceI;

/**   
 * @Title: Controller
 * @Description: 0085_courier_building
 * @author wuyong
 * @date 2015-09-18 16:47:56
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/courierBuildingController")
public class CourierBuildingController extends BaseController {
	/**
	 * Logger for this class
	 */
	//private static final Logger logger = Logger.getLogger(CourierBuildingController.class);

	@Autowired
	private CourierBuildingServiceI courierBuildingService;
	
	@Autowired
	private SystemService systemService;

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(CourierBuildingEntity courierBuilding,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierBuildingEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierBuilding);
		this.courierBuildingService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}


	/**
	 * 添加0085_courier_building
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save"  , method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson save(CourierBuildingEntity courierBuilding) {
		AjaxJson j = new AjaxJson();
		String message = "";
		Integer courierId = courierBuilding.getCourierId();
		Integer buildingId = courierBuilding.getBuildingId();
		if (StringUtil.isNotEmpty(courierId) && StringUtil.isNotEmpty(buildingId)) {
			message = "更新成功";
//			CourierBuildingEntity t = courierBuildingService.get(CourierBuildingEntity.class, courierBuilding.getId());
			CourierBuildingEntity t = courierBuildingService.findByCidBid(courierId, buildingId);
			
			try {
				MyBeanUtils.copyBeanNotNull2Bean(courierBuilding, t);
				courierBuildingService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			courierBuildingService.save(courierBuilding);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}
	
	/**
	 * 批量保存快递员大厦
	 * 在快递员管理员选择录入的时候，可以批量指定大厦
	 * @param courierId
	 *            快递员id
	 * @param buildingIds
	 *            以,分隔的大厦列表，如("1,2,3,4")
	 */
	@RequestMapping(params = "batchSaveCourierBuilding", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson batchSaveCourierBuilding(Integer courierId, String buildingIds){
		AjaxJson j = new AjaxJson();
		String msg = "批量保存失败";
		j.setSuccess(false);
		if(courierId == null || courierId == 0 ){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		if(StringUtils.isBlank(buildingIds)){
			j.setMsg("大厦id不能为空");
			return j;
		}
		courierBuildingService.batchSaveCourierBuilding(courierId, buildingIds);
		msg = "批量保存成功";
		j.setMsg(msg);
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 保存快递员对应大厦的楼层
	 * 在快递员管理员选择录入的时候，可以批量指定大厦
	 * @param courierId
	 *            快递员id
	 * @param buildingIds
	 *            大厦id
	 * @param floors 大厦的楼层， 使用逗号分隔开，如(1,2,3,4)
	 */
	@RequestMapping(params = "saveCourierBuildingFloors", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveCourierBuildingFloors(Integer courierId, Integer buildingId, String floors){
		AjaxJson j = new AjaxJson();
		String msg = "保存失败";
		j.setSuccess(false);
		if(courierId == null || courierId == 0 ){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		if(buildingId == null || buildingId == 0 ){
			j.setMsg("大厦id不能为空或小于0");
			return j;
		}
		if(StringUtils.isBlank(floors)){
			j.setMsg("楼层不能为空");
			return j;
		}
		courierBuildingService.saveCourierBuildingFloors(courierId, buildingId, floors);
		msg = "保存成功";
		j.setMsg(msg);
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 查询快递员分配的对应网点的大厦和楼层列表
	 * @param courierId 快递员id
	 * @return
	 */
	@RequestMapping(params = "queryBldsFloorsByCid", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson queryBldsFloorsByCid(Integer courierId){
		AjaxJson j = new AjaxJson();
		String msg = "查询列表失败";
		j.setMsg(msg);
		if(courierId == null || courierId == 0 ){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		List<Map<String,Object>> list = courierBuildingService.queryBldsFloorsByCid(courierId);
		if(list != null && list.size() > 0){
			j.setMsg("查询列表成功");
			j.setObj(list);
		}else{
			j.setMsg("暂无查询结果");
		}
		j.setSuccess(true);
		return j;
	}
	
}
