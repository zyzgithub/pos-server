package com.courier_mana.statistics.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.StringUtil;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.statistics.service.ExpenseDetailsService;
import com.courier_mana.statistics.service.IncomeDetailsService;

@Controller
@RequestMapping("/ci/courier/expense")
public class ExpenseDetailsController extends BasicController {
	@Autowired
	private ExpenseDetailsService expenseDetailsService;
	
	@Autowired
	private IncomeDetailsService incomeDetailsService;
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	
	/**(OvO)
	 * 员工支出模块
	 * @param userId	用户ID
	 * @param orgId		网点ID
	 * @return
	 */
	@RequestMapping("/wageAndDeductExpense")
	@ResponseBody
	public AjaxJson wageAndDeductExpense(Integer userId, SearchVo vo){
		/**
		 * 检查参数
		 */
		if(userId == null){
			return BasicController.FAIL("01", "userId不能为空");
		}
		if("other".equals(vo.getTimeType()) && vo.getBeginTime() >= vo.getEndTime()){
			return BasicController.FAIL("01", "搜索条件中结束时间(" + vo.getEndTime() + ")应该大于开始时间(" + vo.getBeginTime() + ")");
		}
		
		try {
			/**
			 * 获取用户管理的区域ID
			 * 以及用户管理的快递员
			 */
			List<Integer> orgIds = null;
			Integer orgId = vo.getOrgId();
			orgIds = this.courierOrgService.getManageOrgIds(userId, orgId);
			String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
			List<Integer> courierIds = courierOrgService.getCourierIdsByOrgId(orgIdsStr);
			
			String courierIdsStr = StringUtil.checkIdsString(StringUtils.join(courierIds, ","));
			
			/**
			 * 此接口不考虑合作商
			 */
			Map<String, Object> map = this.expenseDetailsService.wageAndDeductExpense(orgIdsStr, courierIdsStr, vo);
			
			// 总支出
			BigDecimal totalExpense = this.getTotalExpanse(map, orgIdsStr, vo);
			
			// 总收入
			BigDecimal totalIncome = this.getTotalIncome(orgIdsStr, vo);
			
			// 亏损
			BigDecimal totalLoss = totalExpense.subtract(totalIncome);
			
			map.put("totalIncome", totalIncome);
			map.put("totalLoss", totalLoss);
			
			return BasicController.SUCCESS(map);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("02", "获取员工支出失败: " + e.getMessage());
		}
	}
	
	/**(OvO)
	 * 营销支出模块
	 * @param userId	用户ID
	 * @param orgId		网点ID
	 * @return
	 */
	@RequestMapping("/marketingExpense")
	@ResponseBody
	public AjaxJson marketingExpense(Integer userId, SearchVo vo){
		/**
		 * 检查参数
		 */
		if(userId == null){
			return BasicController.FAIL("01", "userId不能为空");
		}
		if("other".equals(vo.getTimeType()) && vo.getBeginTime() >= vo.getEndTime()){
			return BasicController.FAIL("01", "搜索条件中结束时间(" + vo.getEndTime() + ")应该大于开始时间(" + vo.getBeginTime() + ")");
		}
		
		try {
			/**
			 * 此接口不考虑合作商
			 */
			return BasicController.SUCCESS(this.expenseDetailsService.marketingExpense(userId, vo, true));
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("02", "获取营销支出失败: " + e.getMessage());
		}
	}
	
	/**(OvO)
	 * 总支出费用模块(员工支出+营销支出)
	 * @param userId	用户ID
	 * @param orgId		网点ID
	 * @return
	 */
	@RequestMapping("/expenseDetails")
	@ResponseBody
	public AjaxJson expenseDetails(Integer userId, SearchVo vo){
		/**
		 * 检查参数
		 */
		if(userId == null){
			return BasicController.FAIL("01", "userId不能为空");
		}
		if("other".equals(vo.getTimeType()) && vo.getBeginTime() >= vo.getEndTime()){
			return BasicController.FAIL("01", "搜索条件中结束时间(" + vo.getEndTime() + ")应该大于开始时间(" + vo.getBeginTime() + ")");
		}
		
		/**
		 * 获取用户管理的区域ID
		 * 以及用户管理的快递员
		 */
		List<Integer> orgIds = null;
		Integer orgId = vo.getOrgId();
		orgIds = this.courierOrgService.getManageOrgIds(userId, orgId);
		String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
		
		List<Integer> courierIds = courierOrgService.getCourierIdsByOrgId(orgIdsStr);
		String courierIdsStr = StringUtil.checkIdsString(StringUtils.join(courierIds, ","));
		
		try {
			/**
			 * 此接口不考虑合作商
			 */
			Map<String, Object> result = new HashMap<String, Object>();
			result.putAll(this.expenseDetailsService.wageAndDeductExpense(orgIdsStr, courierIdsStr, vo));
			result.putAll(this.expenseDetailsService.marketingExpense(orgIdsStr, vo, true));
			
			// 总支出
			BigDecimal totalExpense = this.getTotalExpanse(result);
			
			// 总收入
			BigDecimal totalIncome = this.getTotalIncome(orgIdsStr, vo);
			
			// 亏损
			BigDecimal totalLoss = totalExpense.subtract(totalIncome);
			
			result.put("totalIncome", totalIncome);
			result.put("totalLoss", totalLoss);
			
			return BasicController.SUCCESS(result);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicController.FAIL("02", "获取营销支出失败: " + e.getMessage());
		}
	}
	
//	/**(OvO)
//	 * 获取总支出
//	 * @param courierId	当前登录用户
//	 * @return
//	 */
//	private BigDecimal getTotalExpanse(Integer courierId,Integer orgId){
//		BigDecimal totalExpanse = BigDecimal.ZERO;
//		/**
//		 * 获取所有支出
//		 */
//		Map<String, Object> expanseMap = this.expenseDetailsService.wageAndDeductExpense(courierId, orgId,null);
//		expanseMap.putAll(this.expenseDetailsService.marketingExpense(courierId, orgId,true,null));
//		
//		/**
//		 * 遍历所有支出项, 求和
//		 */
//		for(Object item: expanseMap.values()){
//			BigDecimal i = new BigDecimal(item.toString());
//			totalExpanse = totalExpanse.add(i);
//		}
//		
//		return totalExpanse.setScale(2, BigDecimal.ROUND_HALF_UP);
//	}
	
	/**(OvO)
	 * 获取总支出(员工支出+营销支出)
	 * @param expanseMap	当前登录用户
	 * @param orgIdsStr		机构ID
	 * @param vo			搜索条件
	 * @return
	 */
	private BigDecimal getTotalExpanse(Map<String, Object> expanseMap, String orgIdsStr, SearchVo vo){
		BigDecimal totalExpanse = BigDecimal.ZERO;
		/**
		 * 向员工支出Map中补充营销支出
		 */
		expanseMap.putAll(this.expenseDetailsService.marketingExpense(orgIdsStr, vo, true));
		
		/**
		 * 遍历所有支出项, 求和
		 */
		for(Object item: expanseMap.values()){
			BigDecimal i = new BigDecimal(item.toString());
			totalExpanse = totalExpanse.add(i);
		}
		
		return totalExpanse.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	/**(OvO)
	 * 获取总支出(员工支出+营销支出)
	 * @param courierId		当前登录用户
	 * @param orgId			机构ID
	 * @param expanseMap	员工支出+营销支出Map
	 * @return
	 */
	private BigDecimal getTotalExpanse(Map<String, Object> expanseMap){
		BigDecimal totalExpanse = BigDecimal.ZERO;
		
		/**
		 * 遍历所有支出项, 求和
		 */
		for(Object item: expanseMap.values()){
			BigDecimal i = new BigDecimal(item.toString());
			totalExpanse = totalExpanse.add(i);
		}
		
		return totalExpanse.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 总收入
	 * @param courierId 用户ID
	 * @return
	 */
	private BigDecimal getTotalIncome(String orgIdsStr, SearchVo vo){
		BigDecimal totalIncome = BigDecimal.ZERO;
		/**
		 * 获取所有收入
		 */
		Map<String, Object> incomeMap = this.incomeDetailsService.getIncomeDetails(orgIdsStr, vo, true);
		
		/**
		 * 遍历所有收入项, 求和
		 */
		for(Object item: incomeMap.values()){
			BigDecimal i = new BigDecimal(item.toString());
			totalIncome = totalIncome.add(i);
		}
		
		return totalIncome;
	}
	
}
