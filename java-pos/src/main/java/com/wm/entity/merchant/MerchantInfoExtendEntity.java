package com.wm.entity.merchant;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家信息扩展
 * @author zhanxinming
 * @date 2016-08-11 16:25:21
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_info_extend", schema = "")
@SuppressWarnings("serial")
public class MerchantInfoExtendEntity implements java.io.Serializable {
	/**主键id*/
	private java.lang.Integer id;
	/**商家id*/
	private java.lang.Integer merchantId;
	/**商家商品变更，是否开启自动审核，'Y'自动审核，'N'手动审核*/
	private java.lang.String isMenuAutoAudit;
	/**自动审核方式， 1.自动允许,   2.自动拒绝*/
	private java.lang.Integer autoAuditType;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家id
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商家商品变更，是否开启自动审核，'Y'自动审核，'N'手动审核
	 */
	@Column(name ="IS_MENU_AUTO_AUDIT",nullable=true,length=2)
	public java.lang.String getIsMenuAutoAudit(){
		return this.isMenuAutoAudit;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商家商品变更，是否开启自动审核，'Y'自动审核，'N'手动审核
	 */
	public void setIsMenuAutoAudit(java.lang.String isMenuAutoAudit){
		this.isMenuAutoAudit = isMenuAutoAudit;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  自动审核方式， 1.自动允许,   2.自动拒绝
	 */
	@Column(name ="AUTO_AUDIT_TYPE",nullable=true,precision=3,scale=0)
	public java.lang.Integer getAutoAuditType(){
		return this.autoAuditType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  自动审核方式， 1.自动允许,   2.自动拒绝
	 */
	public void setAutoAuditType(java.lang.Integer autoAuditType){
		this.autoAuditType = autoAuditType;
	}
}
