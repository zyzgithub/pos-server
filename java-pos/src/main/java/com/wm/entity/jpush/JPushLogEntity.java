package com.wm.entity.jpush;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

/**   
 * @Title: Entity
 * @Description: 极光推送日志
 * @author wuyong
 * @date 2015-09-06 18:50:08
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_jpush_log", schema = "")
@SuppressWarnings("serial")
public class JPushLogEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**推送时间*/
	private java.lang.Integer pushTime;
	/**推送标题*/
	private java.lang.String title;
	/**推送内容*/
	private java.lang.String content;
	/**推送的语音文件名*/
	private java.lang.String voiceFile;
	/**推送对象别名*/
	private java.lang.String targets;
	/**自定义参数*/
	private java.lang.String extras;
	/**返回结果*/
	private java.lang.String result;
	/**返回时间*/
	private java.lang.Integer resultTime;
	/**是否反馈*/
	private java.lang.Integer isFeedBack;
	/**客户端反馈时间*/
	private java.lang.Long feedBackTime;
	
	public JPushLogEntity() {
		
	}
	
	public JPushLogEntity(String title, String content, String voiceFile, String targets, String extras) {
		this.title = title;
		this.content = content;
		this.voiceFile = voiceFile;
		this.targets = targets;
		this.extras = extras;
		this.pushTime = DateUtils.getSeconds();
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 *@return: java.lang.Integer  推送时间
	 */
	@Column(name ="PUSH_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPushTime(){
		return this.pushTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  推送时间
	 */
	public void setPushTime(java.lang.Integer pushTime){
		this.pushTime = pushTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  推送标题
	 */
	@Column(name ="TITLE",nullable=true,length=32)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  推送标题
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  推送内容
	 */
	@Column(name ="CONTENT",nullable=true,length=500)
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  推送内容
	 */
	public void setContent(java.lang.String content){
		this.content = content;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  推送的语音文件名
	 */
	@Column(name ="VOICE_FILE",nullable=true,length=16)
	public java.lang.String getVoiceFile(){
		return this.voiceFile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  推送的语音文件名
	 */
	public void setVoiceFile(java.lang.String voiceFile){
		this.voiceFile = voiceFile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  推送对象别名
	 */
	@Column(name ="TARGETS",nullable=true,length=200)
	public java.lang.String getTargets(){
		return this.targets;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  推送对象别名
	 */
	public void setTargets(java.lang.String targets){
		this.targets = targets;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  自定义参数
	 */
	@Column(name ="EXTRAS",nullable=true,length=1000)
	public java.lang.String getExtras(){
		return this.extras;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  自定义参数
	 */
	public void setExtras(java.lang.String extras){
		this.extras = extras;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回结果
	 */
	@Column(name ="RESULT",nullable=true,length=100)
	public java.lang.String getResult(){
		return this.result;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回结果
	 */
	public void setResult(java.lang.String result){
		this.result = result;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  返回时间
	 */
	@Column(name ="RESULT_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getResultTime(){
		return this.resultTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  返回时间
	 */
	public void setResultTime(java.lang.Integer resultTime){
		this.resultTime = resultTime;
	}

	@Column(name ="IS_FEED_BACK",nullable=true,precision=2,scale=0)
	public java.lang.Integer getIsFeedBack() {
		return isFeedBack;
	}

	public void setIsFeedBack(java.lang.Integer isFeedBack) {
		this.isFeedBack = isFeedBack;
	}

	@Column(name ="FEED_BACK_TIME",nullable=true,precision=11,scale=0)
	public java.lang.Long getFeedBackTime() {
		return feedBackTime;
	}

	public void setFeedBackTime(java.lang.Long feedBackTime) {
		this.feedBackTime = feedBackTime;
	}
	
}
