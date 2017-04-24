package com.courier_mana.common.vo;

/**
 * 用于用户统计排名搜索的Vo
 * 继承SearchVo
 * @author hyj
 *
 */
@SuppressWarnings("serial")
public class SearchVo4UserRank extends SearchVo {
	/**
	 * 按新、旧用户搜索
	 */
	private Boolean isNewUser;
	
	/**
	 * 按用户类型搜索
	 */
	private String userType;
	
	/**
	 * 目标用户类型(VIPx)的最低消费
	 */
	private Integer userTotalSpentFrom;
	
	/**
	 * 目标用户类型(VIPx)的最高消费
	 */
	private Integer userTotalSpentTo;
	
	public Boolean getIsNewUser() {
		return isNewUser;
	}
	public void setIsNewUser(Boolean isNewUser) {
		this.isNewUser = isNewUser;
	}
	public Integer getUserTotalSpentFrom() {
		return userTotalSpentFrom;
	}
	public void setUserTotalSpentFrom(Integer userTotalSpentFrom) {
		this.userTotalSpentFrom = userTotalSpentFrom;
	}
	public Integer getUserTotalSpentTo() {
		return userTotalSpentTo;
	}
	public void setUserTotalSpentTo(Integer userTotalSpentTo) {
		this.userTotalSpentTo = userTotalSpentTo;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@Override
	public String toString() {
		return "SearchVo4UserRank [isNewUser=" + isNewUser
				+ ", userTotalSpentFrom=" + userTotalSpentFrom
				+ ", userTotalSpentTo=" + userTotalSpentTo + ", getOrgId()="
				+ getOrgId() + ", getTimeType()=" + getTimeType()
				+ ", getBeginTime()=" + getBeginTime() + ", getEndTime()="
				+ getEndTime() + "]";
	}
}
