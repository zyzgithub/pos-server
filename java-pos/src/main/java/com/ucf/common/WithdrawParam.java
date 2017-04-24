package com.ucf.common;

/**
 * 代发接口参数
 * 
 * @author Simon
 */
public class WithdrawParam extends BaseParam {

	private static final long serialVersionUID = -6784026971464303429L;

	/** 商户订单号 **/
	private String merchantNo;
	
	/** 来源，固定值：1 **/
	private String source = "1";
	
	/** 金额，单位：分 **/
	private String amount;
	
	/** 币种，固定值：156-人民币 **/
	private String transCur = "156";
	
	/** 用户类型，1：对私 2：对公 **/
	private String userType = "1";
	
	/** 账户类型，1（借记卡），2（贷记卡），4（对公账户） **/
//	private String accountType;
	
	/** 卡号 **/
	private String accountNo;
	
	/** 持卡人姓名 **/
	private String accountName;
	
	/** 手机号 **/
	private String mobileNo;
	
	/** 银行编码，大写的银行编号，如：ICBC,ABC **/
	private String bankNo;
	
	/** 联行号，userType=1时联行号为非必填项 **/
	private String issuer;
	
	/** 开户省（可空） **/
	private String branchProvince;
	
	/** 开户市（可空） **/
	private String branchCity;
	
	/** 开户支行名称（可空） **/
	private String branchName;
	
	/** 后台通知地址，不传此值不进行通知 **/
	private String noticeUrl;
	
	/** 保留域（可空），商户保留域原样回传 **/
	private String memo;
	
	/** 请求序列号 **/
	private String reqSn;
	
	/** 订单签名数据 **/
	private String sign;
	

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransCur() {
		return transCur;
	}

	public void setTransCur(String transCur) {
		this.transCur = transCur;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getBranchProvince() {
		return branchProvince;
	}

	public void setBranchProvince(String branchProvince) {
		this.branchProvince = branchProvince;
	}

	public String getBranchCity() {
		return branchCity;
	}

	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getReqSn() {
		return reqSn;
	}

	public void setReqSn(String reqSn) {
		this.reqSn = reqSn;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
