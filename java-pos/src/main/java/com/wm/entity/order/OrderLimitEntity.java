package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: order_limit
 * @author 
 * @date 2016-08-09 10:01:26
 * @version V1.0   
 *
 */
@Entity
@Table(name = "order_limit", schema = "")
@SuppressWarnings("serial")
public class OrderLimitEntity {
	
	private Integer id;
	
	private Integer merchantId;
	
	private Integer alipayLimit;
	
	private Integer wechatLimit;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public Integer getId() {
		return id;
	}

	@Column(name = "merchant_id")
	public Integer getMerchantId() {
		return merchantId;
	}

	@Column(name = "alipay_limit")
	public Integer getAlipayLimit() {
		return alipayLimit;
	}

	@Column(name = "wechat_limit") 
	public Integer getWechatLimit() {
		return wechatLimit;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public void setAlipayLimit(Integer alipayLimit) {
		this.alipayLimit = alipayLimit;
	}

	public void setWechatLimit(Integer wechatLimit) {
		this.wechatLimit = wechatLimit;
	}
	
	
}
