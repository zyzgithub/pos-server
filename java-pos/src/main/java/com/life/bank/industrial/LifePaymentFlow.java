package com.life.bank.industrial;

public class LifePaymentFlow {

	// 数据库ID
	private String id;
	// 发起提现的用户ID
	private long userId;
	// 发起提现的用户名
	private String userName;
	// 提现的时间
	private long createTime;
	// 银行编号
	private String bankCode;
	// 收款帐号
	private String accountNumber;
	// 收款帐号名
	private String accountName;
	// 账户类型，银行已规定 0-储蓄卡;1-信用卡;2-企业账户
	private int accountType;
	// 订单币种，使用ISO4217标准货币代码，如：CNY
	private String currency;
	// 交易金额，单位为分
	private long transAmount;
	// 转账用途，描述
	private String transRemark;
	// 银行的处理流水号(成功时有效)
	private String bankSserialNo;
	// 银行收取的手续费
	private long transFee;
	// 银行回调时间(或确认到账、失败的时间)
	private long callbackTime;
	// 当失败时，银行给出的错误原因，当状态为失败时有效
	private String callbackRemark;
	// 最终的支付状态，默认为等待；如：0-发起代付请求 1-确认到款 2-银行返回失败 3-未决
	private int payState;
	// 我方对应的银行代码记录ID
	private int bankcardId;
	// 已尝试次数 当失败时才发起重试 payState值为0时有效
	private int errorCount;
	// 可存放使用者自定义的参数内容，一般为逻辑相关的流水号，不能超过200位
	private String defineParameter;

	/**
	 * 数据库ID
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 发起提现的用户ID
	 * 
	 * @return
	 */
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 发起提现的用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 提现的时间
	 * 
	 * @return
	 */
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 银行编号
	 * 
	 * @return
	 */
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 收款帐号
	 * 
	 * @return
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * 收款帐号名
	 * 
	 * @return
	 */
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * 账户类型，银行已规定 0-储蓄卡;1-信用卡;2-企业账户
	 * 
	 * @return
	 */
	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	/**
	 * 订单币种，使用ISO4217标准货币代码，如：CNY
	 * 
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * 交易金额，单位为分
	 * 
	 * @return
	 */
	public long getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(long transAmount) {
		this.transAmount = transAmount;
	}

	/**
	 * 转账用途，描述
	 * 
	 * @param transRemark
	 */
	public String getTransRemark() {
		return transRemark;
	}

	public void setTransRemark(String transRemark) {
		this.transRemark = transRemark;
	}

	/**
	 * 银行的处理流水号(成功时有效)
	 * 
	 * @return
	 */
	public String getBankSserialNo() {
		return bankSserialNo;
	}

	public void setBankSserialNo(String bankSserialNo) {
		this.bankSserialNo = bankSserialNo;
	}

	/**
	 * 银行收取的手续费
	 * 
	 * @return
	 */
	public long getTransFee() {
		return transFee;
	}

	public void setTransFee(long transFee) {
		this.transFee = transFee;
	}

	/**
	 * 银行回调时间(或确认到账、失败的时间)
	 * 
	 * @return
	 */
	public long getCallbackTime() {
		return callbackTime;
	}

	public void setCallbackTime(long callbackTime) {
		this.callbackTime = callbackTime;
	}

	/**
	 * 当失败时，银行给出的错误原因，当状态为失败时有效
	 * 
	 * @return
	 */
	public String getCallbackRemark() {
		return callbackRemark;
	}

	public void setCallbackRemark(String callbackRemark) {
		this.callbackRemark = callbackRemark;
	}

	/**
	 * 最终的支付状态，默认为等待；如：0-发起代付请求 1-确认到款 2-银行返回失败 3-未决
	 * 
	 * @return
	 */
	public int getPayState() {
		return payState;
	}

	public void setPayState(int payState) {
		this.payState = payState;
	}

	/**
	 * 我方对应的银行代码记录ID
	 * 
	 * @return
	 */
	public int getBankcardId() {
		return bankcardId;
	}

	public void setBankcardId(int bankcardId) {
		this.bankcardId = bankcardId;
	}

	/**
	 * 已尝试次数 当失败时才发起重试 payState值为0时有效
	 * 
	 * @return
	 */
	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
	/**
	 * 可存放使用者自定义的参数内容，一般为逻辑相关的流水号，不能超过200位
	 * 
	 * @return
	 */
	public String getDefineParameter() {
		return defineParameter;
	}
	
	public void setDefineParameter(String defineParameter) {
		this.defineParameter = defineParameter;
	}

	@Override
	public String toString() {
		return "LifePaymentFlow [id=" + id + ", userId=" + userId + ", userName=" + userName + ", createTime="
				+ createTime + ", bankCode=" + bankCode + ", accountNumber=" + accountNumber + ", accountName="
				+ accountName + ", accountType=" + accountType + ", currency=" + currency + ", transAmount="
				+ transAmount + ", transRemark=" + transRemark + ", bankSserialNo=" + bankSserialNo + ", transFee="
				+ transFee + ", callbackTime=" + callbackTime + ", callbackRemark=" + callbackRemark + ", payState="
				+ payState + ", bankcardId=" + bankcardId + ", errorCount=" + errorCount + ", defineParameter="
				+ defineParameter + "]";
	}
}