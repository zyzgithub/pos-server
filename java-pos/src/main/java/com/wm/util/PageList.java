package com.wm.util;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: PageList
 * @Description: 分页查询结果封装类
 * @author 黄聪
 * @date 2015年9月17日 上午10:11:21
 *
 */
public class PageList<T> implements Serializable {

	private static final long serialVersionUID = -9031306110720442400L;

	private int currentPage = 1;// 当前页数

	public int totalPages = 0;// 总页数

	private int pageSize = 10;// 每页显示数

	private int totalRows = 0;// 总数据数

	private int startNum = 0;// 开始记录

	private int nextPage = 0;// 下一页

	private int previousPage = 0;// 上一页

	private boolean hasNextPage = false;// 是否有下一页

	private boolean hasPreviousPage = false;// 是否有前一页
	
	private List<T> resultList = null; //结果集

	public PageList(Integer pageSize, Integer currentPage, Integer totalRows,List<T> resultList) {

		this.totalRows = totalRows == null ? 0 : totalRows;
		this.pageSize =  pageSize == null ? 10 : pageSize;
		this.pageSize = this.pageSize == 0 ? 10 : this.pageSize;
		this.currentPage = currentPage == null ? 0 : currentPage;
		this.resultList = resultList;

		if ((this.totalRows % this.pageSize) == 0) {
			totalPages = this.totalRows / this.pageSize;
		} else {
			totalPages = this.totalRows / this.pageSize + 1;
		}

		if (this.currentPage >= totalPages) {
			hasNextPage = false;
			this.currentPage = totalPages;
		} else {
			hasNextPage = true;
		}

		if (this.currentPage <= 1) {
			hasPreviousPage = false;
			this.currentPage = 1;
		} else {
			hasPreviousPage = true;
		}

		startNum = (this.currentPage - 1) * this.pageSize;

		nextPage = this.currentPage + 1;

		if (nextPage >= totalPages) {
			nextPage = totalPages;
		}

		previousPage = this.currentPage - 1;

		if (previousPage <= 1) {
			previousPage = 1;
		}

	}

	public boolean isHasNextPage() {

		return hasNextPage;

	}

	public boolean isHasPreviousPage() {

		return hasPreviousPage;

	}

	/**
	 * @return the nextPage
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * @param nextPage
	 *            the nextPage to set
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the previousPage
	 */
	public int getPreviousPage() {
		return previousPage;
	}

	/**
	 * @param previousPage
	 *            the previousPage to set
	 */
	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the totalRows
	 */
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * @param totalRows
	 *            the totalRows to set
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	/**
	 * @param hasNextPage
	 *            the hasNextPage to set
	 */
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * @param hasPreviousPage
	 *            the hasPreviousPage to set
	 */
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	/**
	 * @return the startNum
	 */
	public int getStartNum() {
		return startNum;
	}

	/**
	 * @param startNum
	 *            the startNum to set
	 */
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	
	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

}
