package com.wm.service.impl.couriersalary.ex;


@SuppressWarnings("serial")
public class CalcSalaryException extends Exception {
	
	public final static int NO_BASIC_SET_ERR_CODE = 10;
	public final static int NO_CALA_ATTENDENCE_ERR_CODE = 20;
	public final static int OTHER_ERROR_CODE = 30;
	
	public final static String NO_BASIC_SET_ERR_MSG =  "缺乏工资基础数据设置";
	public final static String NO_CALA_ATTENDENCE_ERR_MSG =  "没有计算考勤数据";
	public final static String OTHER_ERR_MSG =  "没有计算考勤数据";
	
	private int errorCode;
	
	public CalcSalaryException(){
	}
	
	
	public CalcSalaryException(int errorCode, String errorMsg){
		super(errorMsg);
		this.errorCode = errorCode;
	}
	
	public CalcSalaryException(int errorCode, String errorMsg, Throwable e){
		super(errorMsg, e);
		this.errorCode = errorCode;
	}


	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}


