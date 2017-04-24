package com.wm.entity.note;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 短信发送记录表
 * @author wuyong
 * @date 2015-05-07 15:41:13
 * @version V1.0   
 *
 */
@Entity
@Table(name = "note_record", schema = "")
@SuppressWarnings("serial")
public class NoteRecordEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**接收电话*/
	private java.lang.String phone;
	/**短信表ID*/
	private java.lang.Integer noteId;
	/**创建时间*/
	private java.util.Date creationTime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
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
	 *@return: java.lang.String  接收电话
	 */
	@Column(name ="PHONE",nullable=true,length=255)
	public java.lang.String getPhone(){
		return this.phone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接收电话
	 */
	public void setPhone(java.lang.String phone){
		this.phone = phone;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  短信表ID
	 */
	@Column(name ="NOTE_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getNoteId(){
		return this.noteId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  短信表ID
	 */
	public void setNoteId(java.lang.Integer noteId){
		this.noteId = noteId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATION_TIME",nullable=true)
	public java.util.Date getCreationTime(){
		return this.creationTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreationTime(java.util.Date creationTime){
		this.creationTime = creationTime;
	}
}
