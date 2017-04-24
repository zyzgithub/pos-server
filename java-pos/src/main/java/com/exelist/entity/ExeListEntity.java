package com.exelist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "c_exelist", schema = "")
@SuppressWarnings("serial")
public class ExeListEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**exeid*/
	private java.lang.String exeid;
	/**sql*/
	private java.lang.String sql;
	
	private java.util.Date dated;
	
	private java.lang.String params;


	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator") 
	@Column(name ="ID",nullable=false,length=32)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  exeid
	 */
	@Column(name ="EXEID",nullable=false,length=32)
	public java.lang.String getExeid(){
		return this.exeid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  exeid
	 */
	public void setExeid(java.lang.String exeid){
		this.exeid = exeid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  sql
	 */
	@Column(name ="sql_statment",nullable=false,length=200)
	public java.lang.String getSql() {
		return sql;
	}

	public void setSql(java.lang.String sql) {
		this.sql = sql;
	}
	
	@Column(name ="dated")
	public java.util.Date getDated() {
		return dated;
	}

	public void setDated(java.util.Date dated) {
		this.dated = dated;
	}
	
	@Column(name ="params",nullable=false,length=200)
	public java.lang.String getParams() {
		return params;
	}

	public void setParams(java.lang.String params) {
		this.params = params;
	}
}
