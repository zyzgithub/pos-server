package com.base.VO;

import java.math.BigInteger;

public class TableFieldVO {
	private String fieldName;  //表字段名称
	private String paramName;  //参数名称
	private String comment;
	private String datatype;
	private String isNull;
	private BigInteger datalength;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getIsNull() {
		return isNull;
	}
	public BigInteger getDatalength() {
		return datalength;
	}
	public void setDatalength(BigInteger datalength) {
		this.datalength = datalength;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
