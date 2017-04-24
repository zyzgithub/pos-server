package com.wm.controller.couriersalary;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wm.service.couriersalary.CourierSalaryServiceI;

public class CalcCourierSalaryTask implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(CalcCourierSalaryTask.class);

	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	
	private final String year;
	private final String month;
	
	public CalcCourierSalaryTask(String year, String month){
		this.year = year;
		this.month = month;
	}
	
	@Override
	public void run() {
		logger.info("启动计算{}年{}月快递员工资线程...", year, month);
		courierSalaryService.calcAllCourierSalary(year, month);
		logger.info("计算{}年{}月快递员工资线程完成！", year, month);
	}

	public CourierSalaryServiceI getCourierSalaryService() {
		return courierSalaryService;
	}

	public void setCourierSalaryService(CourierSalaryServiceI courierSalaryService) {
		this.courierSalaryService = courierSalaryService;
	}

}
