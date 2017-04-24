package com.alipay.refund.nopwd;

import java.util.HashMap;
import java.util.Map;

/**
 * 即时到账批量退款无密接口 异步通知参数，详细参考支付宝官方文档
 */
public class NopwdFastpayRefundAsynchronousNotify {

	/**
	 * 通知发送的时间。yyyy-MM-dd HH:mm:ss
	 */
	private String notify_time;

	/**
	 * 通知的类型
	 */
	private String notify_type;

	/**
	 * 通知校验ID
	 */
	private String notify_id;

	/**
	 * 签名方式。DSA、RSA、MD5 三个值可选，必须大写，目前{@link com.alipay.refund.util.FastpayRefundSignHelper}仅实现了RSA签名的校验
	 */
	private String sign_type;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 原请求退款批次号
	 */
	private String batch_no;

	/**
	 * 退交易成功的笔数。0<= success_num<=batch_num
	 */
	private String success_num;

	/**
	 * 处理结果详情。格式为：原付款支付宝交易号^退款总金额^处理结果码
	 */
	private String result_details;

	/**
	 * 解冻结果明细。如果发送请求时不添加use_freeze_amount参数使用冻结金额退款，则可忽略此结果
	 */
	private String unfreezed_details;

	public String getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getSuccess_num() {
		return success_num;
	}

	public void setSuccess_num(String success_num) {
		this.success_num = success_num;
	}

	public String getResult_details() {
		return result_details;
	}

	public void setResult_details(String result_details) {
		this.result_details = result_details;
	}

	public String getUnfreezed_details() {
		return unfreezed_details;
	}

	public void setUnfreezed_details(String unfreezed_details) {
		this.unfreezed_details = unfreezed_details;
	}

	/**
	 * 转换为<传入字段名, 字段值>映射的map
	 * @return map对象
	 */
	public Map<String, String> toParamMap() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("notify_time", notify_time);
		map.put("notify_type", notify_type);
		map.put("notify_id", notify_id);
		map.put("sign_type", sign_type);
		map.put("sign", sign);
		map.put("batch_no", batch_no);
		map.put("success_num", success_num);
		map.put("result_details", result_details);
		map.put("unfreezed_details", unfreezed_details);
		return map;
	}
	
}
