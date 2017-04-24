package com.wm.service.impl.flow;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

public class FlowVo {

	private Integer userId;
	/** detailId */
	private Integer detailId;
	/** detail */
	private String detail;
	/** action */
	private String action;
	/** type */
	private String type;
	/** money */
	private BigDecimal money;
	/** 操作前余额 */
	private BigDecimal preMoney;
	/** 操作后余额 */
	private BigDecimal postMoney;

	FlowVo() {

	}

	FlowVo(Integer userId, Integer detailId, String type, String action,
			BigDecimal preMoney, BigDecimal money, BigDecimal postMoney, String detail) {
		this.userId = userId;
		this.detailId = detailId;
		this.type = type;
		this.action = action;
		this.preMoney = preMoney;
		this.money = money;
		this.postMoney = postMoney;
		this.detail = detail;
	}
	
	FlowVo(Integer userId, String type, String action, BigDecimal preMoney, BigDecimal money, BigDecimal postMoney, String detail) {
		this.userId = userId;
		this.type = type;
		this.action = action;
		this.preMoney = preMoney;
		this.money = money;
		this.postMoney = postMoney;
		this.detail = detail;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDetailId() {
		return detailId;
	}

	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(BigDecimal preMoney) {
		this.preMoney = preMoney;
	}

	public BigDecimal getPostMoney() {
		return postMoney;
	}

	public void setPostMoney(BigDecimal postMoney) {
		this.postMoney = postMoney;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
