package com.courier_mana.statistics.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.statistics.service.OrgUserCountService;

@Controller
@RequestMapping("/ci/courier/org")
public class OrgUserCountController extends BasicController{
	
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@Autowired
	private OrgUserCountService orgUserCountService;
	
	@RequestMapping("/getOrgUserInfo")
	@ResponseBody
	public AjaxJson getOrgUserInfo(Integer courierId, SearchVo vo, Integer page, Integer rows){
		/**
		 * 检查参数
		 */
		if(courierId == null){
			return BasicController.FAIL("01", "courierId不能为空");
		}
		if(!this.checkSearchVo(vo)){
			return BasicController.FAIL("01", "timeType为 other时, 要确保beginTime(" + vo.getBeginTime() + ")大于 endTime(" + vo.getEndTime() +")");
		}
		page = this.initPageParam(page);
		rows = this.initRowsParam(rows);
		
		try {
			/**
			 * 检查用户是否合作商
			 */
			if(this.courierMyInfoService.isAgentUser(courierId)){
				return BasicController.FAIL("02", "合作商无机构信息");
			}
			
			return BasicController.SUCCESS(this.orgUserCountService.getOrgUserInfo(courierId, vo, page, rows));
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("02", "获取网点人数统计信息失败: " + e.getMessage());
		}
	}
	
	/**(OvO)
	 * 初始化(检查)page参数
	 * @param page	页码
	 * @return
	 */
	private Integer initPageParam(Integer page){
		if(page == null){
			return 1;
		}
		return page;
	}
	
	/**(OvO)
	 * 初始化(检查)rows参数
	 * @param rows	每页显示记录数
	 * @return
	 */
	private Integer initRowsParam(Integer rows){
		if(rows == null){
			return 10;
		}
		return rows;
	}
	
	/**(OvO)
	 * 检查搜索条件中时间条件的合法性
	 * 保证timeType为other时beginTime和endTime不为空
	 * (由于实体中已经对这两个变量进行默认初始化:所以不可能为空)
	 * (改为判断结束时间是否大于开始时间)
	 * @param vo
	 * @return
	 */
	private boolean checkSearchVo(SearchVo vo){
		if("other".equals(vo.getTimeType())){
			if(vo.getBeginTime() < vo.getEndTime()){
				return false;
			}
		}
		return true;
	}
}
