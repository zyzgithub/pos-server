package com.wm.entity.merchantdev;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员招商录入阶段完成情况
 * @author wuyong
 * @date 2016-03-22 18:17:23
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_dev_dtl", schema = "")
@SuppressWarnings("serial")
public class MerchantDevDtlEntity implements java.io.Serializable {
	
	private java.lang.Integer id;
	/**招商录入ID*/
	private java.lang.Integer devId;
	/**记录阶段*/
	private java.lang.Integer recordStage;
	/**阶段名称*/
	private java.lang.String stateName;
	/**阶段状态*/
	private java.lang.Integer status;
	

	/**阶段任务*/
	private java.lang.String stageTask;
	/**扩展*/
	private java.lang.String ext;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  招商录入ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  记录阶段
	 */
	@Column(name ="RECORD_STAGE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getRecordStage(){
		return this.recordStage;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  记录阶段
	 */
	public void setRecordStage(java.lang.Integer recordStage){
		this.recordStage = recordStage;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  阶段名称
	 */
	@Column(name ="STATE_NAME",nullable=true,length=20)
	public java.lang.String getStateName(){
		return this.stateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  阶段名称
	 */
	public void setStateName(java.lang.String stateName){
		this.stateName = stateName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  阶段状态
	 */
	@Column(name ="STATUS",nullable=true,precision=10,scale=0)
	public java.lang.Integer getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  阶段状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  阶段任务
	 */
	@Column(name ="STAGE_TASK",nullable=true,length=128)
	public java.lang.String getStageTask(){
		return this.stageTask;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  阶段任务
	 */
	public void setStageTask(java.lang.String stateTask){
		this.stageTask = stateTask;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  扩展
	 */
	@Column(name ="EXT",nullable=true,length=20)
	public java.lang.String getExt(){
		return this.ext;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  扩展
	 */
	public void setExt(java.lang.String ext){
		this.ext = ext;
	}
}
