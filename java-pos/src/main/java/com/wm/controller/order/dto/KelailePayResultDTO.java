package com.wm.controller.order.dto;

import java.io.Serializable;


/**
 * 通过客来乐小票扫码支付结果
 * @author Simon
 */
public class KelailePayResultDTO implements Serializable {

	private static final long serialVersionUID = -7216291778522574180L;

	/**
	 * 商户的客来乐APP ID
	 */
	private String app_id;
	
	/**
	 * 商户方的订单号,某些场景下该值为空, 例如: 直接键盘输入价格出票扫一扫支付, 此时该值为空
	 */
	private String out_trade_no;
	
	/**
	 * 支付方式,1:微信支付, 8:支付宝支付
	 */
	private Integer pay_type;
	
	/**
	 * 第三方支付交易号
	 */
	private String transaction_id;
	
	/**
	 * 第三方支付用户相关ID, 微信用户open_id, 支付宝buy_user_id等
	 */
	private String pay_user_id;
	
	/**
	 * 订单状态,4:支付完成
	 */
	private Integer trade_state;
	
	/**
	 * 设备编号
	 */
	private String device_no;
	
	/**
	 * 10.01
	 */
	private Float total_price;
	
	/**
	 * 客来乐门店code
	 */
	private String shop_code;
	
	/**
	 * 订单创建时间,10位UNIX时间戳
	 */
	private Integer create_time;
	
	/**
	 * 订单支付时间,10位UNIX时间戳
	 */
	private Integer pay_time;
	
	/**
	 * 通知时间,10位UNIX时间戳
	 */
	private Integer timestamp;
	
	/**
	 * 签名串
	 */
	private String sign;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getPay_user_id() {
		return pay_user_id;
	}

	public void setPay_user_id(String pay_user_id) {
		this.pay_user_id = pay_user_id;
	}

	public Integer getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(Integer trade_state) {
		this.trade_state = trade_state;
	}

	public String getDevice_no() {
		return device_no;
	}

	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	public Float getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Float total_price) {
		this.total_price = total_price;
	}

	public String getShop_code() {
		return shop_code;
	}

	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}

	public Integer getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}

	public Integer getPay_time() {
		return pay_time;
	}

	public void setPay_time(Integer pay_time) {
		this.pay_time = pay_time;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	@Override
	public String toString() {
		return "app_id=" + app_id + ",out_trade_no=" + out_trade_no
				+ ",pay_type=" + pay_type + ",transaction_id=" + transaction_id
				+ ",pay_user_id=" + pay_user_id + ",trade_state=" + trade_state
				+ ",device_no=" + device_no + ",total_price=" + total_price
				+ ",shop_code=" + shop_code + ",create_time=" + create_time
				+ ",pay_time=" + pay_time + ",timestamp=" + timestamp;
	}
	
}
