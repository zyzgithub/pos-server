package com.wm.entity.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 组织架构表
 * @author wuyong
 * @date 2015-08-28 09:22:47
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_org", schema = "")
@SuppressWarnings("serial")
public class OrgEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**orgName*/
	private java.lang.String orgName;
	/**父组织*/
	private java.lang.Integer pid;
	/**是否有效，0：失效，1：有效*/
	private java.lang.Integer status;
	/**城市区号*/
	private java.lang.String areaCode;
	/**等级，默认为0=未知，1=国家，2=省，3=市，4=区，5=片区*/
	private java.lang.Integer level;
	
	private String pPath;
	
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
	 *@return: java.lang.String  orgName
	 */
	@Column(name ="ORG_NAME",nullable=true,length=64)
	public java.lang.String getOrgName(){
		return this.orgName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  orgName
	 */
	public void setOrgName(java.lang.String orgName){
		this.orgName = orgName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  父组织
	 */
	@Column(name ="PID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPid(){
		return this.pid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  父组织
	 */
	public void setPid(java.lang.Integer pid){
		this.pid = pid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否有效，0：失效，1：有效
	 */
	@Column(name ="STATUS",nullable=true,precision=3,scale=0)
	public java.lang.Integer getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否有效，0：失效，1：有效
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  城市区号
	 */
	@Column(name ="AREA_CODE",nullable=true,length=6)
	public java.lang.String getAreaCode(){
		return this.areaCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  城市区号
	 */
	public void setAreaCode(java.lang.String areaCode){
		this.areaCode = areaCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  等级，默认为0=未知，1=国家，2=省，3=市，4=区，5=片区
	 */
	@Column(name ="LEVEL",nullable=false,precision=3,scale=0)
	public java.lang.Integer getLevel(){
		return this.level;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  等级，默认为0=未知，1=国家，2=省，3=市，4=区，5=片区
	 */
	public void setLevel(java.lang.Integer level){
		this.level = level;
	}
	
	@Column(name ="p_path")
	public String getPPath(){
		return pPath;
	}

	public void setPPath(String pPath){
		this.pPath = pPath;
	}
}
