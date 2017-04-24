package com.solr.service;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

@SuppressWarnings("rawtypes")
public interface PartnerSolrServiceI extends CommonService{
	/**
	 *
	 * @param group_id  商家分组id
	 * @param pt		经纬度，格式"经度:纬度"
	 * @param sortType	排序：geodist() asc按距离排序;comment_score desc味道最好;delivery_time asc最快配送
	 * @param start		开始位置
	 * @param rows		查询条数
	 * @param searchKeyword	关键字
	 * @return
	 */
	public List queryMerchantList(String group_id, String pt, String sortType,String start,String rows,String searchKeyword);

	public void cleanUpSolrCache();
}
