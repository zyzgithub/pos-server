package com.wm.controller.building;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.controller.building.dto.BuildingDTO;
import com.wm.controller.takeout.vo.CourierPositionVo;
import com.wm.entity.building.BuildingEntity;
import com.wm.service.building.BuildingServiceI;
import com.wm.service.courier.CourierServiceI;

/**   
 * @Title: Controller
 * @Description: 快递员负责配送的楼
 * @author wuyong
 * @date 2015-08-13 10:23:31
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/buildingController")
public class BuildingController extends BaseController {

	@Autowired
	private BuildingServiceI buildingService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private CourierServiceI courierService;
	
	private String message;

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	@RequestMapping(params = "getCourierServArea")
	@ResponseBody
	public AjaxJson getCourierServArea(String courierId) {
		AjaxJson j = new AjaxJson();
		Map<String, Object> servAreaMap = buildingService.getCourierServArea(courierId);
		j.setObj(servAreaMap);
		j.setSuccess(true);
		return j;	
	}
	

	/**
	 * 快递员负责配送的楼列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "building")
	public ModelAndView building(HttpServletRequest request) {
		return new ModelAndView("com/wm/building/buildingList");
	}

	/**
	 * 保存或者更新大厦以及大厦的起止楼层
	 * @param building
	 * @param result
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdateBuilding", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateBuilding( @Valid BuildingDTO building, BindingResult result){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		if(result.hasErrors()){
			j.setMsg("字段"+ result.getFieldError().getField()+" 不能为 " + result.getFieldError().getRejectedValue());
			return j;
		}
		BuildingEntity be = new BuildingEntity();
		be.setAddress(building.getAddress());
		be.setFirstFloor(building.getFirstFloor());
		be.setId(building.getId());
		be.setLastFloor(building.getLastFloor());
		be.setLat(building.getLat());
		be.setLng(building.getLng());
		be.setName(building.getName());
//		be.setRegionId(building.getRegionId());
//		be.setOrgId(building.getOrgId());
//		be.setIsDelete(building.getIsDelete());
//		if(building.getOrgId() == null || building.getOrgId() == 0){
			//通过courierid查第一个区域
		int wangdian = 6;
		List<Map<String, Object>> orgList = courierService.queryOrgByCourierIdLevel(building.getCourierId(), wangdian);
		if(orgList != null && orgList.size() > 0){
			be.setOrgId(Integer.parseInt(orgList.get(0).get("id").toString()));
		}
//		}
		buildingService.saveOrUpdate(be);
		j.setSuccess(true);
		j.setMsg("保存大厦成功");
		return j;
	}

	/**
	 * 查询快递员对应网点的大厦 只能使用在物流组长或最小单位为网点的时候
	 * @param building
	 * @param result
	 * @return
	 */
	@RequestMapping(params = "queryBuildingsByCourierId", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson queryBuildingsByCourierId( Integer courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		if(courierId == null || courierId <= 0){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		List<CourierPositionVo> plist = courierService.queryPositionByUserId(courierId, 1, 10);
		if(plist == null || plist.isEmpty()){
			j.setMsg("快递员目前没有职位");
			return j;
		}
		boolean isManager = false;
		for (CourierPositionVo cpv : plist) {
			if("物流组长".equals(cpv.getPositionName())){
				isManager = true;
				break;
			}
		}
		if(!isManager){
			j.setMsg("快递员不是物流组长");
			return j;
		}
		List<Map<String, Object>> list = buildingService.queryBuildingsByCourierId(courierId, page, rows);
		j.setSuccess(true);
		if(list != null && list.size() > 0){
			j.setMsg("查询列表成功");
			j.setObj(list);
		}else{
			j.setMsg("无查询结果");
		}
		return j;
	}
	
	
}
