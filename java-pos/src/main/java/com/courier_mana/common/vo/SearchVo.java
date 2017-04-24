package com.courier_mana.common.vo;


/**   
 * @Title: Entity
 * @author suyuqiang
 * @date 2015-12-25
 * @version V1.8  
 *
 */
public class SearchVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 7612257485523792494L;

	/**
	 * 按区域搜索
	 */
	private Integer orgId;
	
	/**
	 * 按时间类型搜索： 按天：day  按周week  按月：month  自定义：other
	 */
	private String timeType;
	/**
	 * 自定义时间：开始时间
	 */
	private Integer beginTime = 0;
	
	/**
	 * 自定义时间：结束时间
	 */
	private Integer endTime = 0;
	
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public Integer getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Integer beginTime) {
		this.beginTime = beginTime;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "SearchVo: {orgId: " + this.orgId + ", timeType: \"" + timeType + "\", beginTime: " + beginTime + ", endTime: " + endTime + "}";
	}
}
