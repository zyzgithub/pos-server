package com.wm.entity.user;

/**
 * 收支记录
 * @author Simon
 */
public class UserBudget implements Comparable<UserBudget> {
	
	// 类型：充值、提现、代付
	private String budgetType;
	
	// 订单ID
	private String orderNum;
	
	// 状态
	private String state;
	
	// 状态时间
	private String stateTime;
	
	// 金额
	private Double money;

	public String getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateTime() {
		return stateTime;
	}

	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	/**
	 * 按时间降序排序
	 */
	@Override
	public int compareTo(UserBudget o) {
		return o.stateTime.compareTo(this.stateTime);
	}
	
	
}
