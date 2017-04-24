package com.courier_mana.statistics.controller;

import java.util.List;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.WarehouseReportService;
import com.courier_mana.statistics.vo.WarehouseReportVo;

@Controller
@RequestMapping("/ci/warehouse")
public class WarehouseReportController extends BasicController{
	@Autowired
	private WarehouseReportService warehouseReportService;
	
	/**
	 * 仓库报表
	 * @param timeType
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllWarehouseReport")
	public AjaxJson getAllWarehouseReport(Long userId, SearchVo timeType, Integer page, Integer rows){
		// 参数检查
		if(userId == null){
			return BasicController.FAIL("参数: userId 不能为空");
		}
		
		AjaxJson result = BasicController.SUCCESS();
		page = page==null||page<=0?1:page;
		rows = rows==null||rows<=0?10:rows;
		
		try {
			List<WarehouseReportVo> warehouseReportList = this.warehouseReportService.getWarehouseReportData(userId, timeType, page, rows);
			if(warehouseReportList == null){
				return BasicController.FAIL("用户权限不足");
			}
			result.setObj(warehouseReportList);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("出错了");
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/getWarehouseReportDetail")
	public AjaxJson getWarehouseReportDetail(Integer warehouseId, SearchVo timeType, Integer page, Integer rows){
		// 检查参数
		if(warehouseId == null){
			return BasicController.FAIL("参数: warehouseId 不能为空");
		}
		
		AjaxJson result = BasicController.SUCCESS();
		page = page==null||page<=0?1:page;
		rows = rows==null||rows<=0?10:rows;
		
		try {
			result.setObj(this.warehouseReportService.getWarehouseReportDetail(warehouseId, timeType, page, rows));
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("出错了");
		}
		return result;
	}
	
	/**
	 * 创建回访记录
	 * @param merchantId
	 * @param userId
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordPhoneCall")
	public AjaxJson recordPhoneCall(Long merchantId, Long userId, String phone){
		// 参数检查
		if(merchantId == null){
			return BasicController.FAIL("参数: merchantId 不能为空");
		}
		if(userId == null){
			return BasicController.FAIL("参数: userId 不能为空");
		}
		if(phone == null){
			return BasicController.FAIL("参数: phone 不能为空");
		}
		
		AjaxJson result = BasicController.SUCCESS();
		
		try {
			result.setObj(this.warehouseReportService.recordCallLog(merchantId, userId, phone));
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("出错了");
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/insertWarehouseReportAdmin")
	public AjaxJson insertWarehouseReportAdmin(Long userId, String userName, String phone){
		// 参数检查
		if(userId == null){
			return BasicController.FAIL("参数: userId 不能为空");
		}
		if(userName == null){
			return BasicController.FAIL("参数: userName 不能为空");
		}
		if(phone == null){
			return BasicController.FAIL("参数: phone 不能为空");
		}
		
		AjaxJson result = BasicController.SUCCESS();
		
		try {
			this.warehouseReportService.insertWarehouseReportAdmin(userId, userName, phone);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("出错了");
		}
		return result;
	}


	@ResponseBody
	@RequestMapping("/getWarehouseReportNoPayDetail")
	public AjaxJson getWarehouseReportNoPayDetail(Integer warehouseId, SearchVo timeType, Integer page, Integer rows){
		// 检查参数
		if(warehouseId == null){
			return BasicController.FAIL("参数: warehouseId 不能为空");
		}

		AjaxJson result = BasicController.SUCCESS();
		page = page==null||page<=0?1:page;
		rows = rows==null||rows<=0?10:rows;

		try {
			result.setObj(this.warehouseReportService.getWarehouseReportNoPayDetailData(warehouseId, timeType, page, rows));
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("出错了");
		}
		return result;
	}
}
