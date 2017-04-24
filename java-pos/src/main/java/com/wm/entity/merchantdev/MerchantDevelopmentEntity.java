package com.wm.entity.merchantdev;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员招商录入
 * @author wuyong
 * @date 2016-03-22 18:16:28
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_development", schema = "")
@SuppressWarnings("serial")
public class MerchantDevelopmentEntity implements java.io.Serializable {
	/**招商录入ID*/
	private java.lang.Integer devId;
	/**店名*/
	private java.lang.String merchantTitle;
	/**店主*/
	private java.lang.String merchantHolder;
	/**商家电话*/
	private java.lang.String merchantMobile;
	/**正在进行阶段*/
	private java.lang.Integer ongoingStage;
	/**子任务*/
	private java.lang.Integer subTask;
	/**subTaskId*/
	private java.lang.Integer subTaskId;
	/**业务员*/
	private java.lang.Integer courierId;
	/**createDate*/
	private java.util.Date createDate;
	/**doneDate*/
	private java.util.Date doneDate;
	/**完成状态*/
	private java.lang.Integer state;
	/**备注说明*/
	private java.lang.String remark;
	/**stateName*/
	private java.lang.String stageName;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  招商录入ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="DEV_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getDevId(){
		return this.devId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  招商录入ID
	 */
	public void setDevId(java.lang.Integer devId){
		this.devId = devId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店名
	 */
	@Column(name ="MERCHANT_TITLE",nullable=false,length=128)
	public java.lang.String getMerchantTitle(){
		return this.merchantTitle;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店名
	 */
	public void setMerchantTitle(java.lang.String merchantTitle){
		this.merchantTitle = merchantTitle;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店主
	 */
	@Column(name ="MERCHANT_HOLDER",nullable=false,length=64)
	public java.lang.String getMerchantHolder(){
		return this.merchantHolder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店主
	 */
	public void setMerchantHolder(java.lang.String merchantHolder){
		this.merchantHolder = merchantHolder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商家电话
	 */
	@Column(name ="MERCHANT_MOBILE",nullable=false,length=20)
	public java.lang.String getMerchantMobile(){
		return this.merchantMobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商家电话
	 */
	public void setMerchantMobile(java.lang.String merchantMobile){
		this.merchantMobile = merchantMobile;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  正在进行阶段
	 */
	@Column(name ="ONGOING_STAGE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOngoingStage(){
		return this.ongoingStage;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  正在进行阶段
	 */
	public void setOngoingStage(java.lang.Integer ongoingStage){
		this.ongoingStage = ongoingStage;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  子任务
	 */
	@Column(name ="SUB_TASK",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSubTask(){
		return this.subTask;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  子任务
	 */
	public void setSubTask(java.lang.Integer subTask){
		this.subTask = subTask;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  subTaskId
	 */
	@Column(name ="SUB_TASK_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getSubTaskId(){
		return this.subTaskId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  subTaskId
	 */
	public void setSubTaskId(java.lang.Integer subTaskId){
		this.subTaskId = subTaskId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务员
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务员
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createDate
	 */
	@Column(name ="CREATE_DATE",nullable=false)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createDate
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  doneDate
	 */
	@Column(name ="DONE_DATE",nullable=false)
	public java.util.Date getDoneDate(){
		return this.doneDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  doneDate
	 */
	public void setDoneDate(java.util.Date doneDate){
		this.doneDate = doneDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  完成状态
	 */
	@Column(name ="STATE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  完成状态
	 */
	public void setState(java.lang.Integer state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注说明
	 */
	@Column(name ="REMARK",nullable=true,length=128)
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注说明
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  stateName
	 */
	@Column(name ="STAGE_NAME",nullable=true,length=64)
	public java.lang.String getStageName(){
		return this.stageName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  stateName
	 */
	public void setStageName(java.lang.String stageName){
		this.stageName = stageName;
	}
}
