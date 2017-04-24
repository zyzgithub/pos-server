package org.jeecgframework.core.common.model.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DBTable implements Serializable {

	public String tableName;
	public String entityName;
	public String tableTitle;
	@SuppressWarnings("rawtypes")
	public Class class1;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getTableTitle() {
		return tableTitle;
	}
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	@SuppressWarnings("rawtypes")
	public Class getClass1() {
		return class1;
	}
	@SuppressWarnings("rawtypes")
	public void setClass1(Class class1) {
		this.class1 = class1;
	}
	

}
