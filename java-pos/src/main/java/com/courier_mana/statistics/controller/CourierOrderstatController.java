package com.courier_mana.statistics.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.StringUtil;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.statistics.service.CourierOrderstatService;
import com.courier_mana.statistics.service.ExpenseDetailsService;
import com.courier_mana.statistics.service.IncomeDetailsService;
import com.courier_mana.statistics.vo.DemoData;
import com.wm.util.ChineseCharToEn;
import com.wm.util.IntegerUtil;


@Controller
@RequestMapping("/ci/courier/orderstat")
public class CourierOrderstatController extends BasicController{

	@Autowired
	private CourierOrgServicI courierOrgServicI;
	
	@Autowired
	private CourierOrderstatService courierOrderstatService;
	
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@Autowired
	private ExpenseDetailsService expenseDetailsService;
	
	@Autowired
	private IncomeDetailsService incomeDetailsService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 交易金额 前5天
	 * @return
	 */
	@RequestMapping("/getTransactionFive")
	@ResponseBody
	public AjaxJson getTransactionFive(Integer courierId){
		if(!IntegerUtil.isEmpty(courierId)) return FAIL("02", "courierId 值为null,请检查...");
		AjaxJson j = new AjaxJson();
		List<Map<String,Object>> list = null;
		try {
			List<Integer> ids = courierOrgServicI.getManageOrgIds(courierId);
			list  = courierOrderstatService.getTransactionFive(ids);
		} catch (Exception e){
		}
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	
	/**
	 * 决策报表已完成
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getDecisionMakingReports")
	@ResponseBody
	public AjaxJson getDecisionMakingReports(Integer courierId, SearchVo vo, Integer noCache){
		
		if(!IntegerUtil.isEmpty(courierId)) return FAIL("02", "courierId 值为null,请检查...");
		
		AjaxJson j = new AjaxJson();
		List<Object> list = null;
		try {
			if(noCache == null){
				// 如果按时间查询就去那假数据
				DemoData data = this.getDemoData(vo,true);
				if(data != null){
					list = new ArrayList<Object>();
					list.add(data);
					j.setObj(list);
					j.setStateCode("00");
					j.setSuccess(true);
					j.setMsg("操作成功");
					return j;
				}
			}
			/**
			 * 获取用户管理的区域ID
			 */
			Integer orgId = vo.getOrgId();
			List<Integer> orgIds = this.courierOrgServicI.getManageOrgIds(courierId, orgId);
			
			String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
			List<Integer> courierIds = courierOrgServicI.getCourierIdsByOrgId(orgIdsStr);
			
			String courierIdsStr = StringUtil.checkIdsString(StringUtils.join(courierIds, ","));
			Map<String,Object> map = courierOrderstatService.getDecisionMakingReports(orgIdsStr, vo);
			
			/**
			 * 获得总用户数、新用户数
			 */
			map.putAll(courierOrderstatService.getUserCount(orgIdsStr,1,vo));
			
			/**
			 * 获取外卖订单、堂食订单、扫码订单数量
			 */
			map.putAll(courierOrderstatService.getOrderCountDetails(orgIdsStr, 1, vo));
			
			/**
			 * 获取总支出
			 */
			BigDecimal totalExpense = this.getTotalExpanse(orgIdsStr, courierIdsStr, true, vo);
			map.put("totalExpense", totalExpense);
			
			/**
			 * 获取总收入
			 */
			BigDecimal totalIncome = this.getTotalIncome(orgIdsStr, true, vo);
			
			/**
			 * 计算亏损并输出
			 */
			map.put("totalLoss", totalExpense.subtract(totalIncome));
			
			BigDecimal moneyCurrent = BigDecimal.ZERO;
			Integer orderNumCurrent = null;
			Object money = map.get("moneyCurrent");
			if(money != null){
				moneyCurrent = new BigDecimal(money.toString());
			}
			
			Object orderNum = map.get("orderNumCurrent");
			
			if(orderNum==null){
				orderNumCurrent = 0;
			}else{
				orderNumCurrent = new Integer(orderNum.toString());
			}
			
			/**
			 * 计算复购率((订单数-新用户数)/订单数)
			 */
			if(orderNumCurrent == 0){
				map.put("reorderRate", "0%");
			}else{
				BigDecimal newUserCount = new BigDecimal(map.get("newUser").toString());
				BigDecimal reorderRate = BigDecimal.valueOf(orderNumCurrent).subtract(newUserCount).divide(BigDecimal.valueOf(orderNumCurrent), 4, BigDecimal.ROUND_HALF_UP).movePointRight(2);
				map.put("reorderRate", reorderRate + "%");
			}
			
			/**
			 * 计算客单价(总额/总用户)
			 */
			BigDecimal userCount = new BigDecimal(map.get("userCount").toString());
			if(userCount.compareTo(BigDecimal.ZERO) == 0){
				map.put("price", 0);
			}else{
				map.put("price", moneyCurrent.divide(userCount, 2, BigDecimal.ROUND_HALF_UP));
			}
			
			list = new ArrayList<Object>();
			list.add(map);
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL("异常信息:",e.getMessage());
		}
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**
	 * 决策报表未完成
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getDecisionMakingReportsNo")
	@ResponseBody
	public AjaxJson getDecisionMakingReportsNo(Integer courierId, SearchVo vo){
		
		if(!IntegerUtil.isEmpty(courierId)) return FAIL("02", "courierId 值为null,请检查...");
		
		AjaxJson j = new AjaxJson();
		List<Object> list = null;
		try {
			// 如果按时间查询就去那假数据
			DemoData data = this.getDemoData(vo,true);
			if(data != null){
				list = new ArrayList<Object>();
				list.add(data);
				j.setObj(list);
				j.setStateCode("00");
				j.setSuccess(true);
				j.setMsg("操作成功");
				return j;
			}
			/**
			 * 获取用户管理的区域ID
			 */
			List<Integer> orgIds = null;
			Integer orgId = vo.getOrgId();
			orgIds = this.courierOrgServicI.getManageOrgIds(courierId, orgId);
			
			String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
			List<Integer> courierIds = courierOrgServicI.getCourierIdsByOrgId(orgIdsStr);
			
			String courierIdsStr = StringUtil.checkIdsString(StringUtils.join(courierIds, ","));
			Map<String,Object> map = courierOrderstatService.getDecisionMakingReportsNo(orgIdsStr, vo);
			
			/**
			 * 获得总用户数、新用户数
			 */
			map.putAll(courierOrderstatService.getUserCount(orgIdsStr,0, vo));
			
			/**
			 * 获取外卖订单、堂食订单、扫码订单数量
			 */
			map.putAll(courierOrderstatService.getOrderCountDetails(orgIdsStr, 0, vo));
			
			/**
			 * 获取总支出
			 */
			BigDecimal totalExpense = this.getTotalExpanse(orgIdsStr, courierIdsStr, false, vo);
			map.put("totalExpense", totalExpense);
			
			/**
			 * 获取总收入
			 */
			BigDecimal totalIncome = this.getTotalIncome(orgIdsStr, false, vo);
			
			/**
			 * 计算亏损并输出
			 */
			map.put("totalLoss", totalExpense.subtract(totalIncome));
		
			BigDecimal moneyCurrent = BigDecimal.ZERO;
			Integer orderNumCurrent = null;
			Object money = map.get("moneyCurrent");
			if(money != null){
				moneyCurrent = new BigDecimal(money.toString());
			}
			
			Object orderNum = map.get("orderNumCurrent");
			
			if(orderNum==null){
				orderNumCurrent = 0;
			}else{
				orderNumCurrent = new Integer(orderNum.toString());
			}
			
			/**
			 * 计算复购率((订单数-新用户数)/订单数)
			 */
			if(orderNumCurrent == 0){
				map.put("reorderRate", "0%");
			}else{
				BigDecimal newUserCount = new BigDecimal(map.get("newUser").toString());
				BigDecimal reorderRate = BigDecimal.valueOf(orderNumCurrent).subtract(newUserCount).divide(BigDecimal.valueOf(orderNumCurrent), 4, BigDecimal.ROUND_HALF_UP).movePointRight(2);
				map.put("reorderRate", reorderRate + "%");
			}
			
			/**
			 * 计算客单价(总额/总用户)
			 */
			BigDecimal userCount = new BigDecimal(map.get("userCount").toString());
			if(userCount.compareTo(BigDecimal.ZERO) == 0){
				map.put("price", 0);
			}else{
				map.put("price", moneyCurrent.divide(userCount, 2, BigDecimal.ROUND_HALF_UP));
			}
			
			list = new ArrayList<Object>();
			list.add(map);
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL("异常信息:",e.getMessage());
		}
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	
	/**
	 * 新增商家排名
	 * @param courierId 用户id
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getMerchantNewList")
	@ResponseBody
	public AjaxJson getMerchantNew(Integer courierId,@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer rows, SearchVo vo){
		
		if(!IntegerUtil.isEmpty(courierId)) return FAIL("02", "courierId 值为null,请检查...");
		AjaxJson j = new AjaxJson();
		List<Map<String, Object>> list = null;
		try{
			List<Integer> ids = courierOrgServicI.getManageOrgIds(courierId);
			int size = ids.size();
			if(size>0){
				list = courierOrderstatService.getMerchantNew(ids, vo, page, rows);
				
				/**
				 * 为记录添加序号
				 */
				int rank = (page-1) * rows;
				for(Map<String, Object> item: list){
					item.put("rank", ++rank);
				}
			}else{
				list = new ArrayList<Map<String,Object>>();
			}
			
		} catch (Exception e) {
			return FAIL("异常信息:",e.getMessage());
		}
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**
	 * 网点排名
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getDotRank")
	@ResponseBody
	public AjaxJson getDotRank(Integer courierId,@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer rows, SearchVo vo){
		// 是否为空
		if(!IntegerUtil.isEmpty(courierId)) return FAIL("02", "courierId 值为null,请检查...");
		AjaxJson j = new AjaxJson();
		try{
			// 获取所在机构
			
			List<Integer> ids = courierOrgServicI.getManagerOrgIdsTwo(courierId);
			List<Map<String,Object>> list = null;
			if(ids.size()>0){
				list = courierOrderstatService.getDotRank(ids, vo,page,rows);
				
				/**
				 * 为记录添加序号
				 */
				int rank = (page-1) * rows;
				for(Map<String, Object> item: list){
					item.put("rank", ++rank);
				}
			}else{
				list = new ArrayList<Map<String,Object>>();
			}
			j.setObj(list);
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("操作成功");
		} catch (Exception e) {
			return FAIL("异常信息:",e.getMessage());
		}
		return j;
	}
	
	/**
	 * 按网点统计订单
	 * @param request
	 * @param searchVo
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getOrderByOrg")
	@ResponseBody
	public AjaxJson getOrderByOrg(HttpServletRequest request, SearchVo searchVo, @RequestParam Integer courierId, 
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		
		if(this.courierMyInfoService.isAgentUser(courierId)){
			return FAIL("02", "合作商家无机构信息");
		}
		
		Integer orgId = searchVo.getOrgId();
		if(orgId==null){
			orgId = courierOrgServicI.getParentOrgId(courierId);
		}
		
		List<Integer> orgIds = courierOrgServicI.getManageOrgIds(courierId, orgId);
		
		//List<Integer> managerOrgIdsTow = courierOrgServicI.getManagerOrgIdsTwo(courierId);  // 优化后的
		
		List<Map<String, Object>> list = courierOrderstatService.getOrderstatByOrg(orgIds, page, rows, searchVo);
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**
	 * 根据courierId查询对应的网点
	 * @param request
	 * @param courierId
	 * @return
	 */
    @RequestMapping("/getDotRights")
    @ResponseBody
    public AjaxJson getDotRights(HttpServletRequest request, @RequestParam Integer courierId) {
        AjaxJson j = new AjaxJson();
        
        if(this.courierMyInfoService.isAgentUser(courierId)){
            return FAIL("02", "合作商家无机构信息");
        }
        
        Integer orgId = courierOrgServicI.getParentOrgId(courierId);
        List<Map<String, Object>> orgList = courierOrgServicI.getDotRights(courierId, orgId); 
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> map : orgList){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", map.get("id"));
            String name = map.get("org_name").toString();
            item.put("name", name);
            String words = ChineseCharToEn.getAllFirstLetter(name).toUpperCase();
            item.put("word", words.substring(0, 1));
            item.put("words", words);
            list.add(item);
        }
        j.setObj(list);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }
	
    /**
     * 品类报表
     * @param request
     * @param courierId
     * @param searchVo
     * @return
     */
    @RequestMapping("/getCategoryByReports")
	@ResponseBody
    public AjaxJson getCategoryByReports(HttpServletRequest request, @RequestParam Integer courierId,SearchVo searchVo){
    	
    	/**
		 * 检查参数
		 */
		if(courierId == null){
			return FAIL("02", "courierId不能为空");
		}
		AjaxJson j = new AjaxJson();
		List<Integer> orgIds = courierOrgServicI.getManageOrgIds(courierId);
		List<Map<String, Object>> list = courierOrderstatService.getCategoryByReports(orgIds,searchVo);
		
		/**
		 * 为记录添加序号
		 */
		int i = 0;
		for(Map<String, Object> item: list){
			item.put("rank", ++i);
		}
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
    	return j;
    }
    
    /**
     * 品类报表详情
     * @param request
     * @param courierId
     * @param searchVo
     * @param categoryId   品类Id
     * @return
     */
    @RequestMapping("/getCategoryByReportsInfo")
	@ResponseBody
    public AjaxJson getCategoryByReportsInfo(HttpServletRequest request, @RequestParam Integer courierId,SearchVo searchVo,Integer categoryId,@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer rows){

    	
    	/**
		 * 检查参数
		 */
		if(courierId == null){
			return FAIL("02", "courierId不能为空");
		}
		AjaxJson j = new AjaxJson();
		List<Integer> orgIds = courierOrgServicI.getManageOrgIds(courierId);
		List<Map<String, Object>> list = courierOrderstatService.getCategoryByReportsInfo(orgIds,searchVo,categoryId,page,rows);
		
		/**
		 * 为记录添加序号
		 */
		int rank = (page-1) * rows;
		for(Map<String, Object> item: list){
			item.put("rank", ++rank);
		}
		
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
    	return j;
    }
	
	/**
	 * 按店铺统计订单
	 * @param request
	 * @param searchVo
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getOrderByMerchant")
	@ResponseBody
	public AjaxJson getOrderByMerchant(HttpServletRequest request, SearchVo searchVo, @RequestParam Integer courierId, 
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer rows) {
		/**
		 * 检查参数
		 */
		if(courierId == null){
			return FAIL("02", "courierId不能为空");
		}
		
		AjaxJson j = new AjaxJson();
		/**
		 * 如果是合作商, 则调用专用的Service
		 */
		if(this.courierMyInfoService.isAgentUser(courierId)){
			j.setObj(this.courierOrderstatService.getOrderstatByMerchant4Agent(courierId, page, rows, searchVo));
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("操作成功");
			return j;
		}
		
		Integer orgId = searchVo.getOrgId();
		if(orgId==null){
			orgId = courierOrgServicI.getParentOrgId(courierId);
		}
		//List<Integer> courierIds = courierOrgServicI.getManageCouriersId(courierId, orgId);
		List<Integer> orgIds = courierOrgServicI.getManageOrgIds(courierId, orgId);
		//  courierOrgServicI.getManageOrgIdsTwo(courierId, orgId);  优化之后
		
		List<Map<String, Object>> list = courierOrderstatService.getOrderstatByMerchant(orgIds, page, rows, searchVo);
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**(OvO)
	 * 获取总支出
	 * @param courierId	当前登录用户
	 * @param isConfirm	要统计的订单状态(true: 已完成, false: 未完成)
	 * @return
	 */
	private BigDecimal getTotalExpanse(String orgIdsStr, String courierIdsStr, boolean isConfirm, SearchVo vo){
		BigDecimal totalExpanse = BigDecimal.ZERO;
		
		/**
		 * 获取所有支出
		 */
		Map<String, Object> expanseMap = this.expenseDetailsService.wageAndDeductExpense(orgIdsStr, courierIdsStr, vo);
		expanseMap.putAll(this.expenseDetailsService.marketingExpense(courierIdsStr, vo, isConfirm));
		
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
	 * 统计收入金额
	 * @param orgIdsStr	若干个区域ID(ID用","分隔)
	 * @param isConfirm	要统计的订单状态(true: 已完成, false: 未完成)
	 * @return
	 */
	private BigDecimal getTotalIncome(String orgIdsStr, boolean isConfirm, SearchVo vo){
		BigDecimal totalIncome = BigDecimal.ZERO;
		
		/**
		 * 构造一个空的searchvo用于调用统计收入、支出的方法
		 */
//		SearchVo vo = new SearchVo();
		
		/**
		 * 获取所有收入
		 */
		Map<String, Object> incomeMap = this.incomeDetailsService.getIncomeDetails(orgIdsStr, vo, isConfirm);
		
		/**
		 * 遍历所有收入项, 求和
		 */
		for(Object item: incomeMap.values()){
			BigDecimal i = new BigDecimal(item.toString());
			totalIncome = totalIncome.add(i);
		}
		
		return totalIncome;
	}
	
	private DemoData getDemoData(SearchVo vo, boolean isConfirm){
		String timeType = vo.getTimeType();
		DemoData result = null;
		if("month".equals(timeType)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
			String time = sdf.format(new Date());
			result = this.getDemoData(time, isConfirm);
		} else if("customMonth".equals(timeType) && vo.getBeginTime() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
			String time = sdf.format(new Date(vo.getBeginTime() * 1000L));
			result = this.getDemoData(time, isConfirm);
		}
		return result;
	}
	
	/**
	 * 构造演示数据
	 * @author hyj
	 * @return
	 */
	private DemoData getDemoData(String time, boolean isConfirm) {
		Query query = new Query(Criteria.where("time").is(time).andOperator(Criteria.where("isConfirm").is(isConfirm)));
		DemoData data = this.mongoTemplate.findOne(query, DemoData.class);
		return data;
	}
}


