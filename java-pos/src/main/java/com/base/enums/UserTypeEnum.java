package com.base.enums;

public enum UserTypeEnum {

	USER(1, "用户"), MERCHANT(2, "商家"), COURIER(3, "快递员"), OTHER(4, "其它");

	private Integer typeNum;
	private String cn;

	private UserTypeEnum(Integer typeNum, String cn) {
		this.typeNum = typeNum;
		this.cn = cn;
	}

	public Integer getTypeNum() {
		return typeNum;
	}

	public void setTypeNum(Integer typeNum) {
		this.typeNum = typeNum;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public static String getCn(Integer typeNum) {
		for(UserTypeEnum ute :values()){
			if(ute.getTypeNum() == typeNum){
				return ute.getCn();
			}
		}
		return "";
	}
}
