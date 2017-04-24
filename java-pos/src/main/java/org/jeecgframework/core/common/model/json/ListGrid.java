package org.jeecgframework.core.common.model.json;

import java.util.Collections;
import java.util.List;

public class ListGrid {
	private Long listSize = 0L;
	private List<?> list  = Collections.EMPTY_LIST;
	
	public Long getListSize() {
		return listSize;
	}
	public void setListSize(Long listSize) {
		this.listSize = listSize;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	
	
}
