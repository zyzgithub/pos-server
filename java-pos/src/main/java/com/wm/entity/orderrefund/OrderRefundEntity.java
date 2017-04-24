package com.wm.entity.orderrefund;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 订单退款记录表
 * @author wuyong
 * @date 2015-08-29 15:40:43
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_order_refund", schema = "")
@SuppressWarnings("serial")
public class OrderRefundEntity implements java.io.Serializable {
	
	public static final String STATUS_SUCCESS = "Y";
	
	public static final String STATUS_FAIL = "N";
	
	/**id*/
	private java.lang.Integer id;
	/**商户订单号(对应订单表的pay_id)*/
	private java.lang.String outTradeNo;
	/**商户退款单号*/
	private java.lang.String outRefundNo;
	/**支付类型：tenpay 财付通，weixinpay 微信支付，unionpay银联支付，alipay支付宝支付*/
	private java.lang.String payType ="balance";
	/**第三方交易订单流水号（支付宝，微信支付等）*/
	private java.lang.String transactionId;
	/**订单总金额，单位为分，只能为整数*/
	private java.lang.Integer totalFee;
	/**退款总金额，订单总金额，单位为分，只能为整数*/
	private java.lang.Integer refundFee;
	/**操作员*/
	private java.lang.Integer opUserId;
	/**退款状态：Y 成功，N 失败，*/
	private java.lang.String status = "Y";
	/**创建时间*/
	private java.lang.Integer createTime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商户订单号(对应订单表的pay_id)
	 */
	@Column(name ="OUT_TRADE_NO",nullable=false,length=32)
	public java.lang.String getOutTradeNo(){
		return this.outTradeNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商户订单号(对应订单表的pay_id)
	 */
	public void setOutTradeNo(java.lang.String outTradeNo){
		this.outTradeNo = outTradeNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商户退款单号
	 */
	@Column(name ="OUT_REFUND_NO",nullable=false,length=50)
	public java.lang.String getOutRefundNo(){
		return this.outRefundNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商户退款单号
	 */
	public void setOutRefundNo(java.lang.String outRefundNo){
		this.outRefundNo = outRefundNo;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  支付类型：tenpay 财付通，weixinpay 微信支付，unionpay银联支付，alipay支付宝支付
	 */
	@Column(name ="PAY_TYPE",nullable=false)
	public java.lang.String getPayType(){
		return this.payType;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  支付类型：tenpay 财付通，weixinpay 微信支付，unionpay银联支付，alipay支付宝支付
	 */
	public void setPayType(java.lang.String payType){
		this.payType = payType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  第三方交易订单流水号（支付宝，微信支付等）
	 */
	@Column(name ="TRANSACTION_ID",nullable=false,length=50)
	public java.lang.String getTransactionId(){
		return this.transactionId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  第三方交易订单流水号（支付宝，微信支付等）
	 */
	public void setTransactionId(java.lang.String transactionId){
		this.transactionId = transactionId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  订单总金额，单位为分，只能为整数
	 */
	@Column(name ="TOTAL_FEE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTotalFee(){
		return this.totalFee;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  订单总金额，单位为分，只能为整数
	 */
	public void setTotalFee(java.lang.Integer totalFee){
		this.totalFee = totalFee;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  退款总金额，订单总金额，单位为分，只能为整数
	 */
	@Column(name ="REFUND_FEE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getRefundFee(){
		return this.refundFee;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  退款总金额，订单总金额，单位为分，只能为整数
	 */
	public void setRefundFee(java.lang.Integer refundFee){
		this.refundFee = refundFee;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  操作员
	 */
	@Column(name ="OP_USER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOpUserId(){
		return this.opUserId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  操作员
	 */
	public void setOpUserId(java.lang.Integer opUserId){
		this.opUserId = opUserId;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  退款状态：Y 成功，N 失败，
	 */
	@Column(name ="STATUS",nullable=false,length=1)
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  退款状态：Y 成功，N 失败，
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
}
