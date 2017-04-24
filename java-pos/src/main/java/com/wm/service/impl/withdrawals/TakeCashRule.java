package com.wm.service.impl.withdrawals;

import java.util.Map;

import com.life.commons.CommonUtils;

public class TakeCashRule {

	private long id;
	private int takeMode;
	private String takeDesc;
	private int targetType;
	private double lowCost;
	private double highCost;
	private int rate;
	private int singleAmountLimit;
	private int dayAmountLimit;
	private int dayCountLimit;
	private int holidayDelay;
	private int delayHour;
	private long ruleEffectTime;
	private long ruleInvalidTime;
	private String showImage;
	private boolean defaultOption;

	public TakeCashRule(Map<String, Object> rule) {
		id = (long) rule.get("id");
		takeMode = (int) rule.get("take_mode");
		takeDesc = rule.get("take_desc").toString();
		targetType = (int) rule.get("target_type");
		lowCost = (double) rule.get("low_cost");
		highCost = (double) rule.get("high_cost");
		rate = (int) rule.get("rate");
		singleAmountLimit = (int) rule.get("single_amount_limit");
		dayAmountLimit = (int) rule.get("day_amount_limit");
		dayCountLimit = (int) rule.get("day_count_limit");
		holidayDelay = (int) rule.get("holiday_delay");
		delayHour = (int) rule.get("delay_hour");
		ruleEffectTime = CommonUtils.dateFormatToLong(rule.get("rule_effect_time").toString());
		ruleInvalidTime = CommonUtils.dateFormatToLong(rule.get("rule_invalid_time").toString());
		showImage = rule.get("show_image").toString();
		defaultOption = ((boolean) rule.get("default_option"));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTakeMode() {
		return takeMode;
	}

	public void setTakeMode(int takeMode) {
		this.takeMode = takeMode;
	}

	public String getTakeDesc() {
		return takeDesc;
	}

	public void setTakeDesc(String takeDesc) {
		this.takeDesc = takeDesc;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public double getLowCost() {
		return lowCost;
	}

	public void setLowCost(double lowCost) {
		this.lowCost = lowCost;
	}

	public double getHighCost() {
		return highCost;
	}

	public void setHighCost(double highCost) {
		this.highCost = highCost;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getSingleAmountLimit() {
		return singleAmountLimit;
	}

	public void setSingleAmountLimit(int singleAmountLimit) {
		this.singleAmountLimit = singleAmountLimit;
	}

	public int getDayAmountLimit() {
		return dayAmountLimit;
	}

	public void setDayAmountLimit(int dayAmountLimit) {
		this.dayAmountLimit = dayAmountLimit;
	}

	public int getDayCountLimit() {
		return dayCountLimit;
	}

	public void setDayCountLimit(int dayCountLimit) {
		this.dayCountLimit = dayCountLimit;
	}

	public int getHolidayDelay() {
		return holidayDelay;
	}

	public void setHolidayDelay(int holidayDelay) {
		this.holidayDelay = holidayDelay;
	}

	public int getDelayHour() {
		return delayHour;
	}

	public void setDelayHour(int delayHour) {
		this.delayHour = delayHour;
	}

	public long getRuleEffectTime() {
		return ruleEffectTime;
	}

	public void setRuleEffectTime(long ruleEffectTime) {
		this.ruleEffectTime = ruleEffectTime;
	}

	public long getRuleInvalidTime() {
		return ruleInvalidTime;
	}

	public void setRuleInvalidTime(long ruleInvalidTime) {
		this.ruleInvalidTime = ruleInvalidTime;
	}

	public String getShowImage() {
		return showImage;
	}

	public void setShowImage(String showImage) {
		this.showImage = showImage;
	}

	public void setDefaultOption(boolean defaultOption) {
		this.defaultOption = defaultOption;
	}

	public boolean isDefaultOption() {
		return defaultOption;
	}
}