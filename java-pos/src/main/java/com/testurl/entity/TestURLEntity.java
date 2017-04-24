package com.testurl.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: test的URL
 * @author leichanglin
 * @date 2014-07-11 10:37:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_testurl", schema = "")
@SuppressWarnings("serial")
public class TestURLEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**url*/
	private java.lang.String url;
	/**dated*/
	private java.util.Date dated;
	/**exeid*/
	private java.lang.String exeid;
	
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
	 *@return: java.lang.String  url
	 */
	@Column(name ="URL",nullable=true,length=400)
	public java.lang.String getUrl(){
		return this.url;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  url
	 */
	public void setUrl(java.lang.String url){
		this.url = url;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  dated
	 */
	@Column(name ="DATED",nullable=true)
	public java.util.Date getDated(){
		return this.dated;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  dated
	 */
	public void setDated(java.util.Date dated){
		this.dated = dated;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  exeid
	 */
	@Column(name ="EXEID",nullable=true,length=32)
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
}
