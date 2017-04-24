package com.wm.entity.merchant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 */
@Entity
@Table(name = "0085_merchant_cashflow", schema = "")
@SuppressWarnings("serial")
public class MerchantCashflowEntity implements Serializable {
	
    private int id;
	private Integer inMerchantId;
	private Integer outMerchantId;
	private BigDecimal money;
	private Date createTime;
	private String remark;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID", nullable=false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name ="in_merchant_id", nullable=false)
	public Integer getInMerchantId() {
		return inMerchantId;
	}
	public void setInMerchantId(Integer inMerchantId) {
		this.inMerchantId = inMerchantId;
	}
	
	@Column(name ="out_merchant_id", nullable=false)
	public Integer getOutMerchantId() {
		return outMerchantId;
	}
	public void setOutMerchantId(Integer outMerchantId) {
		this.outMerchantId = outMerchantId;
	}
	
	@Column(name ="money", nullable=false)
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	@Column(name ="create_time", nullable=false)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name ="remark", nullable=false)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
}
