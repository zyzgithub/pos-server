package com.wm.service.impl.couriersalary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.Constants;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.impl.couriersalary.ex.CalcSalaryException;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;
import com.wm.util.SalaryUtil;

@Service("courierSalaryService")
public class CourierSalaryServiceImpl extends CommonServiceImpl implements
		CourierSalaryServiceI {

	private final static Logger logger = LoggerFactory.getLogger(CourierSalaryServiceImpl.class);
	
	@Autowired
	private SystemconfigServiceI systemconfigService;
	
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getCourierIdsOfRequriedCalcSalary(String year, String month){
		String key = CacheKeyUtil.getCourierIdsOfCalcSalaryKey(year, month);
		Object obj = AliOcs.getObject(key);
		if(obj != null){
			return (List<Integer>)obj;
		}
		
		String sql = "select distinct user_id from 0085_courier_salary where year = ? and month = ?";
		List<Map<String, Object>> list = this.findForJdbc(sql, year, month);
		
		List<Integer> courierIds = new ArrayList<Integer>();
		
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map: list){
				courierIds.add(Integer.valueOf(map.get("user_id").toString()));
			}
		}
		AliOcs.set(key, courierIds, 24*60*60);
		
		return courierIds;
	}
	
	@Override
	public double getLowestWorkHoursPerDay(){
		
		String key = CacheKeyUtil.getLowestWorkhoursPerdaySetKey();
		Object obj = AliOcs.getObject(key);
		if(obj != null){
			return (double)obj;
		}
		
		
		double lowestWorkhours = 8.0;
		String value = systemconfigService.getValByCode(Constants.LOWEST_WORKHOURS_SET);
		if(value != null){
			lowestWorkhours = Double.valueOf(value);
		}
		AliOcs.set(key, lowestWorkhours, 60*60*24);
		return lowestWorkhours;
	}
	
	
	@Override
	public Integer getRequiredAttendanceDays(Integer courierId, String year, String month){
		Map<String, Object> courierSalaryConf = this.getCourierSalaryRecord(courierId, year, month);
		if(courierSalaryConf != null){
			Object requiredAttendanceDays = courierSalaryConf.get("required_attendance_days");
 			if(requiredAttendanceDays != null){
 				return Integer.parseInt(requiredAttendanceDays.toString());
 			}
		}
		return null;
	}
	
	@Override
	public int getRealAttendanceDays(Integer courierId, String year, String month){
		Map<String, Object> courierSalaryConf = getCourierSalaryRecord(courierId,year, month);
		
		//首先从工资设置中查找
		if(courierSalaryConf != null){
			Object realAttendanceDays = courierSalaryConf.get("real_attendance_days");
			if(realAttendanceDays != null){
				return Integer.parseInt(realAttendanceDays.toString());
			}
		}
		//如果查不到从考勤表中查找
		return attendanceSalaryService.getRealAttendanceDaysFromKQ(courierId, year, month);
		
	}
	
	
	@Override
	public Double getSalary(Integer courierId,String year, String month){
		Map<String, Object> courierSalaryConf = getCourierSalaryRecord(courierId, year, month);
		
		if(courierSalaryConf != null){
			Object salary = courierSalaryConf.get("salary");
			if(salary != null){
				return Double.parseDouble(salary.toString());
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCourierSalaryRecord(Integer courierId, String year, String month){
		String key = CacheKeyUtil.getSalaryConfKey(courierId, year, month);
		
		Object obj = AliOcs.getObject(key);
		
		if(obj != null){
			return (Map<String, Object>)obj;
		}
		else{
			
			String sql = "select * from 0085_courier_salary s "
					+ " where user_id=? and s.year=? and s.month=? ";
			
			Map<String, Object> conf = this.findOneForJdbc(sql, new Object[]{courierId, year, month});
			if(conf != null){
				AliOcs.set(key, conf, 24*60*60);
			}
			return conf;
		}
	}
	
	public void cleanSalaryCache(Integer courierId, String year, String month){
		String key = CacheKeyUtil.getSalaryConfKey(courierId, year, month);
		AliOcs.remove(key);
	}
	
	@Override
	public void saveRealAttendanceDays(Integer courierId, int realAttendanceDays, String year, String month){
		
		String sql = "update 0085_courier_salary set real_attendance_days = ? ";
		sql += " where user_id=? and year=? and month=?  ";
		
		
		int updateRow = this.executeSql(sql, new Object[]{realAttendanceDays, courierId, year, month});
		
		if(updateRow != 1){
			logger.error("更新快递员的考勤天数失败，参数:{}", StringUtils.join(new Object[]{realAttendanceDays, courierId, year, month}, ","));
		}
		else{
			cleanSalaryCache(courierId, year, month);
		}
	}
	
	@Override
	public void calcAndSaveCourierRealSalary(Integer courierId, String year, String month)
			throws CalcSalaryException{
		//计算前先清除缓存
		cleanSalaryCache(courierId, year, month);
		
		Map<String, Object> courierSalaryConf = getCourierSalaryRecord(courierId, year, month);
		if(courierSalaryConf == null){
			logger.error("没有找到快递员:{}, {}年{}月的工资基础数据", new Object[]{courierId, year, month});
			throw new CalcSalaryException(CalcSalaryException.NO_BASIC_SET_ERR_CODE, CalcSalaryException.NO_CALA_ATTENDENCE_ERR_MSG);
		}
		
		int realAttendanceDays = getRealAttendanceDays(courierId, year, month);
		Integer requriedAttendanceDays = getRequiredAttendanceDays(courierId, year, month);
		if(requriedAttendanceDays == null){
			logger.error("没有找到快递员:{}, {}年{}月的需要出勤的天数设置", new Object[]{courierId, year, month});
			throw new CalcSalaryException(CalcSalaryException.NO_BASIC_SET_ERR_CODE, CalcSalaryException.NO_CALA_ATTENDENCE_ERR_MSG);
		}
		
		Double salary = getSalary(courierId, year, month);
		if(salary == null){
			logger.error("没有找到快递员:{}, {}年{}月的工资设置", new Object[]{courierId, year, month});
			throw new CalcSalaryException(CalcSalaryException.NO_BASIC_SET_ERR_CODE, CalcSalaryException.NO_CALA_ATTENDENCE_ERR_MSG);
		}
		
		double deduct = Double.parseDouble(courierSalaryConf.get("deduct")==null?"0.0":courierSalaryConf.get("deduct").toString());
		double lodgingSubsidy = Double.parseDouble(courierSalaryConf.get("lodging_subsidy") == null?"0.0":courierSalaryConf.get("lodging_subsidy").toString());
		double mealSubsidy = Double.parseDouble(courierSalaryConf.get("meal_subsidy") == null?"0.0":courierSalaryConf.get("meal_subsidy").toString());
		double attendenceReward = Double.parseDouble(courierSalaryConf.get("attendence_reward")==null?"0.0":courierSalaryConf.get("attendence_reward").toString());
		double otherSupply = Double.parseDouble(courierSalaryConf.get("other_supply") == null? "0.0": courierSalaryConf.get("other_supply").toString());
		double personalInsurance = Double.parseDouble(courierSalaryConf.get("personal_social_insurance") == null? "0.0":courierSalaryConf.get("personal_social_insurance").toString());
		double platformDeductSum = Double.parseDouble(courierSalaryConf.get("platform_deduct_sum") == null?"0.0":courierSalaryConf.get("platform_deduct_sum").toString());
		double platformDeductUnpay = Double.parseDouble(courierSalaryConf.get("platform_deduct_unpay") == null? "0.0":courierSalaryConf.get("platform_deduct_unpay").toString());
		double otherDebit = Double.parseDouble(courierSalaryConf.get("other_debit") == null? "0.0":courierSalaryConf.get("other_debit").toString());
		
		double attendanceSalary = (salary.doubleValue() / requriedAttendanceDays.intValue()) * (realAttendanceDays>requriedAttendanceDays?requriedAttendanceDays:realAttendanceDays);
		double requiredPaidSarary = attendanceSalary+deduct+lodgingSubsidy+mealSubsidy+attendenceReward+otherSupply;
		double personalIncomeTax = SalaryUtil.calcPersonalIncomeTax(requiredPaidSarary, personalInsurance);
		double realSalarySum = requiredPaidSarary-personalInsurance-personalIncomeTax-otherDebit;
		double realPaySalary = realSalarySum-platformDeductSum-platformDeductUnpay;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" update 0085_courier_salary set real_attendance_days= ?, attendance_salary = ?, required_paid_sarary = ?, personal_income_tax = ? ");
		sql.append(" , real_salary_sum = ?, real_pay_salary = ? ");
		sql.append("  where user_id=? and year=? and month=? ");
		
		
		int updateRow = this.executeSql(sql.toString(), new Object[]{realAttendanceDays, attendanceSalary, requiredPaidSarary, 
				personalIncomeTax, realSalarySum, realPaySalary,
				courierId, year, month});
		logger.info("更新快递员的工资，参数:{}", StringUtils.join(new Object[]{realAttendanceDays, courierId, year, month}, ","));
		if(updateRow != 1){
			logger.error("更新快递员的工资失败，参数:{}", StringUtils.join(new Object[]{realAttendanceDays, courierId, year, month}, ","));
		}
		else{
			cleanSalaryCache(courierId, year, month);
		}
		
	}
	
	@Override
	public void calcAllCourierSalary(String year, String month){
		logger.info("---------------------------开始计算{}年-{}月工资---------------------------", year, month);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = getCourierIdsOfRequriedCalcSalary(year, month);
		
		logger.info("---------------------------本次共计算{}个快递员的工资---------------------------", courirerIds.size());
		if(CollectionUtils.isNotEmpty(courirerIds)){
			for(Integer courierId: courirerIds){
				
				logger.info("---------------------------开始计算快递员{},{}年-{}月工资---------------------------", new Object[]{courierId, year, month});
				try {
					//清空缓存
					cleanSalaryCache(courierId, year, month);
					//保存快递员每天的考勤薪水
					attendanceSalaryService.updateAttendanceSalaryPerday(courierId, year, month);
					//计算快递员的实际工资
					calcAndSaveCourierRealSalary(courierId, year, month);
				} 
				catch (Exception e) {
					logger.error("开始计算快递员{},{}年-{}月工资发生错误,错误原因:{}", new Object[]{courierId, year, month, e.getMessage()});
				}
				
				logger.info("---------------------------计算快递员{},{}年-{}月工资完成---------------------------", new Object[]{courierId, year, month});
			}
		}
		
		logger.info("---------------------------计算{}年-{}月工资完成---------------------------", year, month);
	}
	
	

	@Override
	public Map<String, Object> getTotalIncome(Integer courierId, String year,
			String month) {
		Map<String, Object> totalIncomeMap = new HashMap<String, Object>();
		Map<String, Object> courierSalaryConf = getCourierSalaryRecord(courierId, year, month);
		if(courierSalaryConf != null && courierSalaryConf.get("real_pay_salary") != null){
			Double realPaySalary = Double.parseDouble(courierSalaryConf.get("real_pay_salary").toString());
			totalIncomeMap.put("realPaySalary", realPaySalary);
			totalIncomeMap.put("attendanceSalary", courierSalaryConf.get("attendance_salary") == null ? "0.00" : courierSalaryConf.get("attendance_salary"));
			totalIncomeMap.put("deduct", courierSalaryConf.get("deduct") == null ? "0.00" : courierSalaryConf.get("deduct"));
			totalIncomeMap.put("subsidyAndReward", this.getSubsidyAndReward(courierId, year, month).get("subsidyAndReward"));
			totalIncomeMap.put("other", this.getOther(courierId, year, month).get("other"));
			totalIncomeMap.put("year", courierSalaryConf.get("year"));
			totalIncomeMap.put("month", courierSalaryConf.get("month"));
			totalIncomeMap.put("state", "0");
			return totalIncomeMap;
		}
		else {
			year = DateTime.now().minusMonths(2).toString("yyyy");
			month = DateTime.now().minusMonths(2).toString("MM");
			courierSalaryConf = getCourierSalaryRecord(courierId, year, month);
			if(courierSalaryConf != null && courierSalaryConf.get("real_pay_salary") != null){
				Double realPaySalary = Double.parseDouble(courierSalaryConf.get("real_pay_salary").toString());
				totalIncomeMap.put("realPaySalary", realPaySalary);
				totalIncomeMap.put("attendanceSalary", courierSalaryConf.get("attendance_salary") == null ? "0.00" : courierSalaryConf.get("attendance_salary"));
				totalIncomeMap.put("deduct", courierSalaryConf.get("deduct") == null ? "0.00" : courierSalaryConf.get("deduct"));
				totalIncomeMap.put("subsidyAndReward", this.getSubsidyAndReward(courierId, year, month).get("subsidyAndReward"));
				totalIncomeMap.put("other", this.getOther(courierId, year, month).get("other"));
				totalIncomeMap.put("year", courierSalaryConf.get("year"));
				totalIncomeMap.put("month", courierSalaryConf.get("month"));
				totalIncomeMap.put("state", "1");
				return totalIncomeMap;
			}
			else{
				totalIncomeMap.put("realPaySalary", "0.00");
				totalIncomeMap.put("attendanceSalary", "0.00");
				totalIncomeMap.put("deduct", "0.00");
				totalIncomeMap.put("subsidyAndReward", "0.00");
				totalIncomeMap.put("other", "0.00");
				totalIncomeMap.put("year", year);
				totalIncomeMap.put("month", month);
				totalIncomeMap.put("state", "2");
				return totalIncomeMap;
			}
		}
		
	}

	@Override
	public Map<String, Object> getSubsidyAndReward(Integer courierId,
			String year, String month) {
		Map<String, Object> subsidyAndRewardMap = new HashMap<String, Object>();
		Map<String, Object> courierSalaryConf = getCourierSalaryRecord(courierId, year, month);
		Double mealSubsidy = Double.parseDouble((courierSalaryConf.get("meal_subsidy") == null ? "0.00" : courierSalaryConf.get("meal_subsidy")).toString());
		Double lodgingSubsidy = Double.parseDouble((courierSalaryConf.get("lodging_subsidy") == null ? "0.00" : courierSalaryConf.get("lodging_subsidy")).toString());
		Double attendenceReward = Double.parseDouble((courierSalaryConf.get("attendence_reward") == null ? "0.00" : courierSalaryConf.get("attendence_reward")).toString());
		subsidyAndRewardMap.put("subsidyAndReward", mealSubsidy + lodgingSubsidy + attendenceReward);
		subsidyAndRewardMap.put("mealSubsidy", mealSubsidy);
		subsidyAndRewardMap.put("lodgingSubsidy", lodgingSubsidy);
		subsidyAndRewardMap.put("attendenceReward", attendenceReward);
		subsidyAndRewardMap.put("requiredAttendanceDays", courierSalaryConf.get("required_attendance_days"));
		subsidyAndRewardMap.put("realAttendanceDays", courierSalaryConf.get("real_attendance_days"));
		return subsidyAndRewardMap;
	}

	@Override
	public Map<String, Object> getOther(Integer courierId, String year,
			String month) {
		Map<String, Object> otherMap = new HashMap<String, Object>();
		Map<String, Object> courierSalaryConf = getCourierSalaryRecord(courierId, year, month);
		Double personalIncomeTax = Double.parseDouble((courierSalaryConf.get("personal_income_tax") == null ? "0.00" : courierSalaryConf.get("personal_income_tax")).toString());
		Double otherDebit = Double.parseDouble((courierSalaryConf.get("other_debit") == null ? "0.00" : courierSalaryConf.get("other_debit")).toString());
		Double otherSupply = Double.parseDouble((courierSalaryConf.get("other_supply") == null ? "0.00" : courierSalaryConf.get("other_supply")).toString());
		Double personalSoicalInsurance = Double.parseDouble((courierSalaryConf.get("personal_social_insurance") == null ? "0.00" : courierSalaryConf.get("personal_social_insurance")).toString());

		otherMap.put("personalIncomeTax", personalIncomeTax);
		otherMap.put("personalSoicalInsurance", personalSoicalInsurance);
		otherMap.put("otherDebit", otherDebit);
		otherMap.put("otherSupply", otherSupply);
		otherMap.put("other", otherSupply-(personalIncomeTax + personalSoicalInsurance + otherDebit));
		return otherMap;
	}

}
