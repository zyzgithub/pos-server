package com.wm.entity.tvlogin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: TV登录二维码实体
 * @author wuyong
 * @date 2015-04-28 15:04:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "tv_login", schema = "")
@SuppressWarnings("serial")
public class TvLoginEntity implements java.io.Serializable {
	/**二维码id,使用UUID生成*/
	private java.lang.String id;
	/**商家id*/
	private java.lang.Integer merchantId;
	/**生成时间*/
	private java.util.Date createTime;
	/**状态:0商家未确认,1商家已确认,2已使用过登录*/
	private java.lang.String status;
	/**二维码的内容*/
	private java.lang.String content;
	
	/*** 客户端版本类型:
	 * 01 前台(Down_OrdersActivity)
	 * 02 菜单(MenuTvActivity)
	 * 03 厨房电视(ShopCoolActivity)
	 * 04 配餐PAD(ShopOutActivity)
	 * 05 厨房制作完成(ShopOutsActivity)
	 * 06 外卖电视(ShopOutTvNumberActivity)
	 * 07 外卖Pad(ShopOutTvNumberPadActivity)
	 * 08 排号(ShopTvNumsActivity)
	 * 其他类型时请传入其他相应的代号，然后客户端跳转时做相应的跳转即可
	 **/
	private String versionType;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  二维码id,使用UUID生成
	 */
	@Id
	@Column(name ="ID",nullable=false,length=50)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  二维码id,使用UUID生成
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@Column(name ="MERCHANT_ID",nullable=true,precision=10,scale=0)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  生成时间
	 */
	@Column(name ="CREATE_TIME",nullable=false)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  生成时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态:0商家未确认,1商家已确认,2已使用过登录
	 */
	@Column(name ="STATUS",nullable=false,length=2)
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态:0商家未确认,1商家已确认,2已使用过登录
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  二维码的内容
	 */
	@Column(name ="CONTENT",nullable=false,length=200)
	public java.lang.String getContent() {
		return content;
	}
	/**
	 *方法: 设置java.lang.String
	 *@return: java.lang.String  二维码的内容
	 */
	@Column(name ="CONTENT",nullable=false,length=200)
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	/**
	 * 获取客户端版本类型
	 * @return
	 * 01 前台
	 * 02 菜单
	 * 03 厨房电视
	 * 04 配餐PAD
	 * 05 厨房制作完成
	 * 06 外卖电视
	 * 07 外卖Pad
	 * 08 排号
	 */
	@Column(name ="VERSION_TYPE",nullable=true,length=10)
	public String getVersionType() {
		return versionType;
	}

	/**
	 * 设置客户端版本类型
	 * @author lfq
	 * @param versionType 
	 * 01 前台
	 * 02 菜单
	 * 03 厨房电视
	 * 04 配餐PAD
	 * 05 厨房制作完成
	 * 06 外卖电视
	 * 07 外卖Pad
	 * 08 排号
	 */
	@Column(name ="VERSION_TYPE",nullable=true,length=10)
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}
	
	
}
