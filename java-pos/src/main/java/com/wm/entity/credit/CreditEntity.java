package com.wm.entity.credit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: credit
 * @author wuyong
 * @date 2015-01-07 09:56:32
 * @version V1.0   
 *
 */
@Entity
@Table(name = "credit", schema = "")
@SuppressWarnings("serial")
public class CreditEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**detailId*/
//	private MenuEntity menu;
	
	private java.lang.Integer detailId;
	/**detail*/
	private java.lang.String detail;
	/**score*/
	private java.lang.Integer score;
	/**action 积分变动方式
	 * comment   评价送积分
	 * buy		  购物送积分
	 * charge	 
	 * refund	
	 * game		游戏送积分
	 * signin	签到送积分
	 * */
	private java.lang.String action;
	/**createTime*/
	private java.lang.Integer createTime = DateUtils.getSeconds();
	
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  userId
	 */
	/*@Column(name ="USER_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	/*public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  detailId
	 */
	@Column(name ="DETAIL_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getDetailId(){
		return this.detailId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  detailId
	 */
	public void setDetailId(java.lang.Integer detailId){
		this.detailId = detailId;
	}
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DETAIL_ID", nullable = true)
	public MenuEntity getMenu() {
		return menu;
	}

	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}*/
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  detail
	 */
	@Column(name ="DETAIL",nullable=true,length=255)
	public java.lang.String getDetail(){
		return this.detail;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  detail
	 */
	public void setDetail(java.lang.String detail){
		this.detail = detail;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  score
	 */
	@Column(name ="SCORE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getScore(){
		return this.score;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  score
	 */
	public void setScore(java.lang.Integer score){
		this.score = score;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  action
	 */
	@Column(name ="ACTION",nullable=false,length=16)
	public java.lang.String getAction(){
		return this.action;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  action
	 */
	public void setAction(java.lang.String action){
		this.action = action;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  createTime
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
}
