package com.wm.entity.order;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 退款/退货退款表
 * @author db
 * @date 2016-04-11
 */

@Entity
@Table(name="flash_order_return", schema="")
@SuppressWarnings("serial")
public class FlashOrderReturnEntity implements java.io.Serializable {

	/**  主键  */
	private Long id;
	/**  订单id  */
	private Integer orderId;
	/**  用户id  */
	private Integer userId;
	/** 0:退款,1:退货退款   */
	private char type;
	/**  退款金额  */
	private Double refundAmount;
	/**  0:申请中,1:商家同意,2:商家拒绝,3:退款成功  */
//	private char state;
	/**  处理时间  */
	private Timestamp updateTime;
	/**  退单原因  */
	private String refundDesc;
	/**  退单图片  */
	private String refundImg;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false, precision=20, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="order_id")
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name="type")
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	
	@Column(name="refund_amount")
	public Double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}
	
//	@Column(name="state")
//	public char getState() {
//		return state;
//	}
//	public void setState(char state) {
//		this.state = state;
//	}
	
	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name="refund_desc")
	public String getRefundDesc() {
		return refundDesc;
	}
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	
	@Column(name="refund_img")
	public String getRefundImg() {
		return refundImg;
	}
	public void setRefundImg(String refundImg) {
		this.refundImg = refundImg;
	}
	
	
	
}
