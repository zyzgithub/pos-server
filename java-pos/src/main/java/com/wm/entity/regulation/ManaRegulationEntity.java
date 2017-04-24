package com.wm.entity.regulation;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员管理条例
 * @author wuyong
 * @date 2016-01-26 09:17:22
 * @version V1.0   
 *
 */
@Entity
@Table(name = "mana_regulation", schema = "")
@SuppressWarnings("serial")
public class ManaRegulationEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**条例标题*/
	private java.lang.String title;
	/**条例内容,可以保存文字或html*/
	private java.lang.String content;
	/**条例类型 0 普通的条例，其内容保存在content中  1 内容指向一个链接（href）  */
	private java.lang.Integer type;
	/**条例对应的链接*/
	private java.lang.String href;
	/**创建人*/
	private java.lang.Integer creator;
	/**创建时间*/
	private java.util.Date createTime;
	/**更新时间*/
	private java.util.Date updateTime;
	/**logo*/
	private java.lang.String logo;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
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
	 *@return: java.lang.String  条例标题
	 */
	@Column(name ="TITLE",nullable=false,length=255)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  条例标题
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  条例内容,可以保持文字或html
	 */
	@Column(name ="CONTENT",nullable=true,length=5000)
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  条例内容,可以保持文字或html
	 */
	public void setContent(java.lang.String content){
		this.content = content;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  条例类型 0 普通的条例，其内容保存在content中  1 内容指向一个链接（href）  
	 */
	@Column(name ="TYPE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  条例类型 0 普通的条例，其内容保存在content中  1 内容指向一个链接（href）  
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  条例对应的链接
	 */
	@Column(name ="HREF",nullable=true,length=255)
	public java.lang.String getHref(){
		return this.href;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  条例对应的链接
	 */
	public void setHref(java.lang.String href){
		this.href = href;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
	@Column(name ="CREATOR",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreator(){
		return this.creator;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建人
	 */
	public void setCreator(java.lang.Integer creator){
		this.creator = creator;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	@Column(name ="UPDATE_TIME",nullable=true)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  logo
	 */
	@Column(name ="LOGO",nullable=true,length=255)
	public java.lang.String getLogo(){
		return this.logo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  logo
	 */
	public void setLogo(java.lang.String logo){
		this.logo = logo;
	}
}
