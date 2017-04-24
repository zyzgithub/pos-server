package com.wm.entity.merchantinfo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家回扣表
 * @author jiangpingmei
 * @date 2016-05-10 15:49:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_deduction", schema = "")
@SuppressWarnings("serial")
public class MerchantDeductionEntity implements Serializable {
	
	/**id*/
	private Integer id;
	/** 商家id */
	private Integer merchantId;
	/** 扣点类型，1=门店扣点，2=扫码扣点 */
	private Integer type;
	/** 扣点值 */
	private Double deduction;
	/** 预收入兑现天数 */
	private Integer incomeDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "merchant_id")
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name = "deduction")
	public Double getDeduction() {
		return deduction;
	}
	public void setDeduction(Double deduction) {
		this.deduction = deduction;
	}
	
	@Column(name = "income_date")
	public Integer getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(Integer incomeDate) {
		this.incomeDate = incomeDate;
	}
	
	

}
