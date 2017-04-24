package com.wm.controller.couriersalary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;

@Controller
@RequestMapping("ci/courierSalaryController")
public class CourierSalaryController extends BasicController {
	
	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;
	
	@Autowired
	private DeductLogServiceI deductLogService;
	
	/**
	 * 获取快递员上月收入
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(params = "getTotalIncome")
	@ResponseBody
	public AjaxJson getTotalIncome(@RequestParam Integer courierId){
		
		AjaxJson json = new AjaxJson();
		try {
			String year = DateTime.now().minusMonths(1).toString("yyyy");
			String month = DateTime.now().minusMonths(1).toString("MM");
			json.setObj(courierSalaryService.getTotalIncome(courierId, year, month));
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取快递员上月收入失败");
		}
		return json;
	}
	
	/**
	 * 获取快递员考勤工资详情
	 * @param courierId
	 * @param date
	 * @return
	 */
	@RequestMapping(params = "getAttendanceSalary")
	@ResponseBody
	public AjaxJson getAttendanceSalary(@RequestParam Integer courierId, @RequestParam int page, @RequestParam int rows){
		AjaxJson json = new AjaxJson();
		Map<String, Object> attendanceSalaryMap = new HashMap<String, Object>();
		List<Map<String, Object>> attendanceSalaryList = new ArrayList<Map<String,Object>>();
		try {
			String year = DateTime.now().minusMonths(1).toString("yyyy");
			String month = DateTime.now().minusMonths(1).toString("MM");
			String date = DateTime.now().minusMonths(1).toString("yyyy-MM");
			Map<String, Object> totalIncomeMap = courierSalaryService.getTotalIncome(courierId, year, month);
			if(totalIncomeMap.get("state").equals("1")){
				date = DateTime.now().minusMonths(2).toString("yyyy-MM");
			}
			if (totalIncomeMap.get("state").equals("2")) {
				attendanceSalaryMap.put("attendanceSalaryRecord", attendanceSalaryList);
				attendanceSalaryMap.put("attendanceSalary", "0.00");
				attendanceSalaryMap.put("lowestWorkhoursPerday", courierSalaryService.getLowestWorkHoursPerDay());
				json.setObj(attendanceSalaryMap);
				json.setSuccess(true);
				json.setStateCode("00");
				return json;
			}
			attendanceSalaryList = attendanceSalaryService.getAttendanceSalary(courierId, date, page, rows);
			attendanceSalaryMap.put("attendanceSalaryRecord", attendanceSalaryList);
			attendanceSalaryMap.put("attendanceSalary", totalIncomeMap.get("attendanceSalary"));
			attendanceSalaryMap.put("lowestWorkhoursPerday", courierSalaryService.getLowestWorkHoursPerDay());
			json.setObj(attendanceSalaryMap);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("获取考勤工资详情失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
	
	/**
	 * 获取快递员每日提成详情
	 * @param courierId
	 * @param date
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getEverydayDeduct")
	@ResponseBody
	public AjaxJson getEverydayDeduct(@RequestParam Integer courierId, @RequestParam int page, @RequestParam int rows){
		AjaxJson json = new AjaxJson();
		Map<String, Object> everydayDeductMap = new HashMap<String, Object>();
		List<Map<String, Object>> everydayDeductList = new ArrayList<Map<String,Object>>();
		try {
			String year = DateTime.now().minusMonths(1).toString("yyyy");
			String month = DateTime.now().minusMonths(1).toString("MM");
			String date = DateTime.now().minusMonths(1).toString("yyyy-MM");
			Map<String, Object> totalIncomeMap = courierSalaryService.getTotalIncome(courierId, year, month);
			if(totalIncomeMap.get("state").equals("1")){
				date = DateTime.now().minusMonths(2).toString("yyyy-MM");
			}
			if (totalIncomeMap.get("state").equals("2")) {
				everydayDeductMap.put("everydayDeductRecord", everydayDeductList);
				everydayDeductMap.put("deduct", "0.00");
				json.setObj(everydayDeductMap);
				json.setSuccess(true);
				json.setStateCode("00");
				return json;
			}
			everydayDeductList = deductLogService.getEverydayDeduct(courierId, date, page, rows);
			everydayDeductMap.put("everydayDeductRecord", everydayDeductList);
			everydayDeductMap.put("deduct", totalIncomeMap.get("deduct"));
			json.setObj(everydayDeductMap);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("获取快递员每日提成详情失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
	
	/**
	 * 获取快递员补贴和奖金详情
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(params = "getSubsidyAndReward")
	@ResponseBody
	public AjaxJson getSubsidyAndReward(@RequestParam Integer courierId){
		AjaxJson json = new AjaxJson();
		Map<String, Object> subsidyAndReward = new HashMap<String, Object>();
		try {
			String year = DateTime.now().minusMonths(1).toString("yyyy");
			String month = DateTime.now().minusMonths(1).toString("MM");
			Map<String, Object> totalIncomeMap = courierSalaryService.getTotalIncome(courierId, year, month);
			if(totalIncomeMap.get("state").equals("1")){
				year = DateTime.now().minusMonths(2).toString("yyyy");
				month = DateTime.now().minusMonths(2).toString("MM");
			}
			if (totalIncomeMap.get("state").equals("2")) {
				subsidyAndReward.put("subsidyAndReward", "0.00");
				subsidyAndReward.put("mealSubsidy", "0.00");
				subsidyAndReward.put("lodgingSubsidy", "0.00");
				subsidyAndReward.put("attendenceReward", "0.00");
				subsidyAndReward.put("requiredAttendanceDays", "0");
				subsidyAndReward.put("realAttendanceDays", "0");
				json.setObj(subsidyAndReward);
				json.setSuccess(true);
				json.setStateCode("00");
				return json;
			}
			subsidyAndReward = courierSalaryService.getSubsidyAndReward(courierId, year, month);
			json.setObj(subsidyAndReward);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取快递员补贴和奖金详情失败");
		}
		return json;
	}

	/**
	 * 获取其他详情
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(params = "getOther")
	@ResponseBody
	public AjaxJson getOther(@RequestParam Integer courierId){
		AjaxJson json = new AjaxJson();
		Map<String, Object> otherMap = new HashMap<String, Object>();
		try {
			String year = DateTime.now().minusMonths(1).toString("yyyy");
			String month = DateTime.now().minusMonths(1).toString("MM");
			Map<String, Object> totalIncomeMap = courierSalaryService.getTotalIncome(courierId, year, month);
			if(totalIncomeMap.get("state").equals("1")){
				year = DateTime.now().minusMonths(2).toString("yyyy");
				month = DateTime.now().minusMonths(2).toString("MM");
			}
			if (totalIncomeMap.get("state").equals("2")) {
				otherMap.put("personalIncomeTax", "0.00");
				otherMap.put("companySoicalInsurance", "0.00");
				otherMap.put("otherDebit", "0.00");
				otherMap.put("otherSupply", "0.00");
				otherMap.put("other", "0.00");
				json.setObj(otherMap);
				json.setSuccess(true);
				json.setStateCode("00");
				return json;
			}
			otherMap = courierSalaryService.getOther(courierId, year, month);
			json.setObj(otherMap);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取快递员其他详情失败");
		}
		return json;
		
	}
	
	/**
	 * 计算所有快递员工资
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(params = "calcAllCourierSalay")
	@ResponseBody
	public AjaxJson calcAllCourierSalay(@RequestParam String year, @RequestParam String month){
		AjaxJson json = new AjaxJson();
		try {
			logger.info("开始计算{}年{}月快递员的工资...", year, month);
			//清除缓存
			AliOcs.remove(CacheKeyUtil.getCourierIdsOfCalcSalaryKey(year, month));
			CalcCourierSalaryTask task = new CalcCourierSalaryTask(year, month);
			task.setCourierSalaryService(courierSalaryService);
			Thread thread = new Thread(task);
			thread.start();
			json.setSuccess(true);
			json.setMsg("启动计算所有快递员工资任务成功，请等待计算结果");
		} 
		catch (Exception e) {
			json.setSuccess(false);
			json.setMsg("启动计算所有快递员工资任务失败");
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 重新计算某一个快递员工资
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(params = "reCalcCourierSalay")
	@ResponseBody
	public AjaxJson reCalcCourierSalay(@RequestParam Integer courierId, @RequestParam String year, @RequestParam String month){
		AjaxJson json = new AjaxJson();
		try {
			logger.info("开始计算快递员{}，{}年{}月快递员的工资...", new Object[]{courierId, year, month});
			courierSalaryService.calcAndSaveCourierRealSalary(courierId, year, month);
			json.setSuccess(true);
			json.setMsg("计算快递员工资成功!");
			logger.info("开始计算快递员{}，{}年{}月快递员的工资完成", new Object[]{courierId, year, month});
		} 
		catch (Exception e) {
			json.setSuccess(false);
			json.setMsg("计算快递员工资失败!");
			e.printStackTrace();
		}
		return json;
	}
	
}
