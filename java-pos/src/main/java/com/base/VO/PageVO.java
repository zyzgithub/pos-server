package com.base.VO;

import java.util.List;

public class PageVO<T> {
	private final int totalRecords;
	private final int pageSize;
	
	private int totalPage;
	private List<T> results;

	public PageVO(int totalRecords, int pageSize){
		this.totalRecords = totalRecords;
		this.pageSize = pageSize;
		
		totalPage = totalRecords % pageSize == 0 ? totalRecords/pageSize: totalRecords/pageSize+1;
	}
	
	
	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}


	public int getTotalRecords() {
		return totalRecords;
	}


	public int getPageSize() {
		return pageSize;
	}


	public int getTotalPage() {
		return totalPage;
	}
	
}
