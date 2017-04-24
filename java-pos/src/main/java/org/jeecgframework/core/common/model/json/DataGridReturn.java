package org.jeecgframework.core.common.model.json;

import java.util.List;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 * 
 * @author 
 * 
 */
public class DataGridReturn {

	@SuppressWarnings("rawtypes")
	public DataGridReturn(Integer total, List rows) {
		this.total = total;
		this.rows = rows;
	}

	private Integer total;// 总记录数
	@SuppressWarnings("rawtypes")
	private List rows;// 每行记录
	@SuppressWarnings("rawtypes")
	private List footer;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}

	@SuppressWarnings("rawtypes")
	public List getFooter() {
		return footer;
	}

	@SuppressWarnings("rawtypes")
	public void setFooter(List footer) {
		this.footer = footer;
	}

}
