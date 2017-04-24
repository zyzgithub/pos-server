package com.wm.util;


public class CacheKeyUtil {

	public static String getCourierIdsOfCalcSalaryKey(String year, String month){
		return "courierIds_of_requried_calc_salary_" + year + "_" + month;
	}
	
	public static String getSalaryConfKey(Integer courierId, String year, String month){
		return "get_salary_conf_" + courierId + "_" + year + "_" + month;
	}
	
	public static String getAttendanceSalayRecordKey(Integer courierId, String date, int page){
		return "attendance_salary_record_" + courierId + "_" + date + "_" + page;
	}
	
	public static String getLowestWorkhoursPerdaySetKey(){
		return "lowest_workhours_perday_set";
	}
	
	public static String getSuspendConstraintKey(String code){
		return code + "_constraint_set";
	}
	
	public static String getEverydayDeductRecordKey(Integer courierId, String date, int page){
		return "everyday_deduct_" + courierId + "_" + date + "_" + page;
	}
	
	//备餐列表
	public static String getMealPreList(Integer merchantId){
		return "get_Meal_Pre_list_" +  merchantId;
	}
	//出餐列表
	public static String getMealList(Integer merchantId){
		return "get_Meal_list_" + merchantId;
	}
	//超市结算订单信息
	public static String getCashierSettlementKey(Integer orderId, Integer cashierId){
		return "settle_" + orderId + "_" + cashierId;
	}
}
