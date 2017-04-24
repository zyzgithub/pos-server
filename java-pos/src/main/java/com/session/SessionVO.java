package com.session;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "c_session", schema = "")
@SuppressWarnings("serial")
public class SessionVO implements java.io.Serializable {
	private String sessionKey;		//会话KEY
	private String startTime;		//会话开始时间
	private String endTime;		//话会截止时间
	private String userId;		//用户ID
	private Date createTime;		//建创时间
	private Date exitTime;		//退出时间
	private String userInfo;      //用户信息
	private int isDebug;        //是否进入调试模式，0为否，1为是
	
	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator") 
	@Column(name ="SESSION_KEY",nullable=false,length=50)
	public String getSessionKey() {
		return sessionKey;
	}
	
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	@Column(name ="START_TIME",nullable=false)
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Column(name ="END_TIME",nullable=false)
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Column(name ="USER_ID",nullable=false)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name ="CREATE_TIME",nullable=false)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name ="EXIT_TIME",nullable=false)
	public Date getExitTime() {
		return exitTime;
	}
	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}
	
	@Column(name ="IS_DEBUG",nullable=false)
	public int getIsDebug() {
		return isDebug;
	}

	public void setIsDebug(int isDebug) {
		this.isDebug = isDebug;
	}
	
	@Column(name ="USER_INFO",nullable=false)
	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
}