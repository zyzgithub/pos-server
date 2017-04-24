package com.wm.entity.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: team
 * @author wuyong
 * @date 2015-01-20 19:06:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "team", schema = "")
@SuppressWarnings("serial")
public class TeamEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**组长id*/
	private java.lang.Integer userId;
	/**讨论主题*/
	private java.lang.String teamName;
	/**numMember*/
	private java.lang.Integer numMember;
	/**小组讨论主题分类*/
	private java.lang.String topic;
	/**小组头像*/
	private java.lang.String image;
	/**小组简介*/
	private java.lang.String introduction;
	/**标签*/
	private java.lang.String label;
	
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  组长id
	 */
	@Column(name ="USER_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  组长id
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  讨论主题
	 */
	@Column(name ="TEAM_NAME",nullable=true,length=255)
	public java.lang.String getTeamName(){
		return this.teamName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  讨论主题
	 */
	public void setTeamName(java.lang.String teamName){
		this.teamName = teamName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  numMember
	 */
	@Column(name ="NUM_MEMBER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getNumMember(){
		return this.numMember;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  numMember
	 */
	public void setNumMember(java.lang.Integer numMember){
		this.numMember = numMember;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  小组讨论主题分类
	 */
	@Column(name ="TOPIC",nullable=true,length=255)
	public java.lang.String getTopic(){
		return this.topic;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  小组讨论主题分类
	 */
	public void setTopic(java.lang.String topic){
		this.topic = topic;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  小组头像
	 */
	@Column(name ="IMAGE",nullable=false,length=255)
	public java.lang.String getImage(){
		return this.image;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  小组头像
	 */
	public void setImage(java.lang.String image){
		this.image = image;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  小组简介
	 */
	@Column(name ="INTRODUCTION",nullable=false,length=255)
	public java.lang.String getIntroduction(){
		return this.introduction;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  小组简介
	 */
	public void setIntroduction(java.lang.String introduction){
		this.introduction = introduction;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标签
	 */
	@Column(name ="LABEL",nullable=false,length=2)
	public java.lang.String getLabel(){
		return this.label;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标签
	 */
	public void setLabel(java.lang.String label){
		this.label = label;
	}
}
