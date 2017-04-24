package com.wm.service.provider.impl;

public class PageUtil {
	public final static int getFristResult(int pageNo, int maxPageSize) {
		int fristResult = pageNo * maxPageSize - maxPageSize;
		return fristResult > 0 ? 0 : fristResult;
	}

	public final static int totalPage(int totalRows, int maxPageSize) {
		int maxPageNo = 0;
		if (maxPageSize <= 0) {
			return maxPageNo;
		}
		maxPageNo = totalRows % maxPageSize == 0 ? totalRows / maxPageSize : totalRows / maxPageSize + 1;
		return maxPageNo;
	}
}