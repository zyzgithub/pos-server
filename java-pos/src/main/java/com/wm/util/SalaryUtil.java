package com.wm.util;

import java.util.ArrayList;
import java.util.List;

public class SalaryUtil {
	private final static double PERSONAL_TAX_THRESHOLD = 3500.0;
	
	private static List<TaxConf> taxConfList = new ArrayList<TaxConf>();
	
	/**
	 * （应发工资-个人社保-3500）*梯度-速算扣除数
	 * 全月应纳税所得额（含税级距）【税率资讯网提供】
	级数								税率(%)	速算扣除数
	1	不超过1,500元					3		0
	2	超过1,500元至4,500元的部分		10		105
	3	超过4,500元至9,000元的部分		20		555
	4	超过9,000元至35,000元的部分		25		1,005
	5	超过35,000元至55,000元的部分		30		2,755
	6	超过55,000元至80,000元的部分		35		5,505
	7	超过80,000元的部分				45		13,505
	 */
	static{
		taxConfList.add(new TaxConf(Integer.MIN_VALUE, 0, 0.00, 0.0));
		taxConfList.add(new TaxConf(0, 1500.0, 0.03, 0.0));
		taxConfList.add(new TaxConf(1500.0,4500.0, 0.1, 105));
		taxConfList.add(new TaxConf(4500.0, 9000, 0.2, 555));
		taxConfList.add(new TaxConf(9000.0, 35000, 0.25, 1005));
		taxConfList.add(new TaxConf(35000.0, 55000, 0.3, 2755));
		taxConfList.add(new TaxConf(55000.0, 80000, 0.35, 5505));
		taxConfList.add(new TaxConf(80000.0, Integer.MAX_VALUE, 0.45, 13505));
	}
	
	
	public static TaxConf getTaxConf(double salary, double socailInsurance){
		double personLevel = salary - socailInsurance - PERSONAL_TAX_THRESHOLD;
		
		TaxConf finalTaxConf = null;
		for(TaxConf taxConf: taxConfList){
			if(personLevel >= taxConf.getLow() && personLevel < taxConf.getHigh()){
				finalTaxConf = taxConf;
				break; 
			}
		}
		
		return finalTaxConf;
	}
	
	public static double calcPersonalIncomeTax(double salary, double socailInsurance){
		TaxConf finalTaxConf = getTaxConf(salary, socailInsurance);
		return (salary-socailInsurance-PERSONAL_TAX_THRESHOLD)*finalTaxConf.getTaxRate()-finalTaxConf.getDeduction();
	}
	
	
	
	public static void main(String[] args){
//		double salary1 = calcPersonalIncomeTax(80000, 5022.26);
//		double salary2 = calcPersonalIncomeTax(13000, 286.74);
//		double salary3 = calcPersonalIncomeTax(14000, 286.74);
//		double salary4 = calcPersonalIncomeTax(15000, 286.74);
//		double salary5 = calcPersonalIncomeTax(16000, 286.74);
		double salary6 = calcPersonalIncomeTax(17000, 286.74);
//		double salary7 = calcPersonalIncomeTax(1500, 286.74);
		
//		System.out.println(salary1);
//		System.out.println(salary2);
//		System.out.println(salary3);
//		System.out.println(salary4);
//		System.out.println(salary5);
		System.out.println(salary6);
//		System.out.println(salary7);
	}
	
	private static class TaxConf{
		private final double low;
		private final double high;
		private final double taxRate;
		private final double deduction;
		
		public TaxConf(double low, double high, double taxRate, double deduction) {
			this.low = low;
			this.high = high;
			this.taxRate = taxRate;
			this.deduction = deduction;
		}

		public double getLow() {
			return low;
		}

		public double getHigh() {
			return high;
		}

		public double getTaxRate() {
			return taxRate;
		}
		
		public double getDeduction() {
			return deduction;
		}
	}
}
