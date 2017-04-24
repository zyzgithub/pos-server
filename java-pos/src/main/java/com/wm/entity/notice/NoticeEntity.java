package com.wm.entity.notice;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员管理端通知公告记录表
 * @author suyuqiang
 * @date 2015-12-26
 * @version V1.8
 *
 */
@Entity
@Table(name = "tsm_notice", schema = "")
@SuppressWarnings("serial")
public class NoticeEntity implements java.io.Serializable {
	/**id*/
	private Integer id;
	/**通知对象ID,关联user.id*/
	private Integer noticeId;
	/**通知对象：1商家，2用户，3快递员*/
	private Integer noticeObject=3;
	/**标题*/
	private String title;
	/**内容*/
	private String content;
	/**状态 0无效 1有效*/
	private String status="1";
	/**创建时间*/
	private Date createTime;
	/**更新时间*/
	private Date updateTime;
	/**网点ID*/
	private Integer orgId;
	/**1.系统通知2.系统公告3.其他*/
	private Integer type;
	/**默认1为正常状态，0为撤销状态*/
	private Integer sendStatus = 1;
	/**关联user的id，表示此消息的操作员*/
	private Integer operatorId;
	/**快递员类型*/
	private Integer courierType;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public Integer getId(){
		return this.id;
	}
	public void setId(Integer id){
		this.id = id;
	}
	
	@Column(name ="NOTICE_ID",nullable=true)
	public Integer getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}
	
	@Column(name ="NOTICE_OBJECT",nullable=true)
	public Integer getNoticeObject() {
		return noticeObject;
	}
	public void setNoticeObject(Integer noticeObject) {
		this.noticeObject = noticeObject;
	}
	
	@Column(name ="TITLE",nullable=true)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name ="CONTENT",nullable=true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name ="STATUS",nullable=true)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name ="update_time",nullable=true)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name ="ORG_ID",nullable=true)
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	@Column(name ="TYPE",nullable=true)
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name ="SEND_STATUS",nullable=true)
	public Integer getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}
	
	@Column(name ="OPERATOR_ID",nullable=true)
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	
	@Column(name ="COURIER_TYPE",nullable=true)
	public Integer getCourierType() {
		return courierType;
	}
	public void setCourierType(Integer courierType) {
		this.courierType = courierType;
	}
}
