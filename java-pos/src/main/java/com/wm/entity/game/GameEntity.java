package com.wm.entity.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 游戏分数积分兑换规则表
 * @author wuyong
 * @date 2015-05-11 23:52:15
 * @version V1.0   
 *
 */
@Entity
@Table(name = "game", schema = "")
@SuppressWarnings("serial")
public class GameEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**游戏名字*/
	private java.lang.String gameName;
	/**游戏openId*/
	private java.lang.String gameOpenId;
	/**兑换游戏分数*/
	private java.lang.Integer gameScore;
	/**兑换积分数*/
	private java.lang.Integer score;
	/**是否启用:0未启用;1已启用*/
	private java.lang.String enabled;
	/**游戏说明*/
	private java.lang.String remark;
	/**游戏进入地址*/
	private java.lang.String url; 
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  游戏openId
	 */
	@Column(name ="GAME_OPEN_ID",nullable=false,length=64)
	public java.lang.String getGameOpenId(){
		return this.gameOpenId;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  游戏名字
	 */
	@Column(name ="GAME_NAME",nullable=false,length=100)
	public java.lang.String getGameName() {
		return gameName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  游戏名字
	 */
	public void setGameName(java.lang.String gameName) {
		this.gameName = gameName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  游戏openId
	 */
	public void setGameOpenId(java.lang.String gameOpenId){
		this.gameOpenId = gameOpenId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  兑换游戏分数
	 */
	@Column(name ="GAME_SCORE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getGameScore(){
		return this.gameScore;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  兑换游戏分数
	 */
	public void setGameScore(java.lang.Integer gameScore){
		this.gameScore = gameScore;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  兑换积分数
	 */
	@Column(name ="SCORE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getScore(){
		return this.score;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  兑换积分数
	 */
	public void setScore(java.lang.Integer score){
		this.score = score;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否启用:0未启用;1已启用
	 */
	@Column(name ="ENABLED",nullable=false,length=1)
	public java.lang.String getEnabled(){
		return this.enabled;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否启用:0未启用;1已启用
	 */
	public void setEnabled(java.lang.String enabled){
		this.enabled = enabled;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  游戏说明
	 */
	@Column(name ="REMARK",nullable=true,length=300)
	public java.lang.String getRemark() {
		return remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  游戏说明
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  游戏地址
	 */
	@Column(name ="URL",nullable=true,length=300)
	public java.lang.String getUrl() {
		return url;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  游戏地址
	 */
	public void setUrl(java.lang.String url) {
		this.url = url;
	}
}
