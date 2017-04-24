package com.execontent.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "C_EXECONTENT", schema = "")
@SuppressWarnings("serial")
public class ExeContent implements java.io.Serializable {
	/**code*/
	private java.lang.String code;
	/**名称*/
	private java.lang.String name;
	
	private java.lang.String id;

	/**type*/
	private java.lang.String type;
	/**path*/
	private java.lang.String sqlStatement;
	
	private java.lang.String message;
	
	private java.lang.String errorMessage;
	
	private java.lang.String descrition;
	
	private java.lang.Integer level;

	private ExeContent exeContent;  //父接口
	
	private List<ExeContent> exeContents = new ArrayList<ExeContent>();
	
	@OneToMany(mappedBy = "exeContent")
	public List<ExeContent> getExeContents() {
		return exeContents;
	}

	public void setExeContents(List<ExeContent> exeContents) {
		this.exeContents = exeContents;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_CODE")
	public ExeContent getExeContent() {
		return exeContent;
	}

	public void setExeContent(ExeContent exeContent) {
		this.exeContent = exeContent;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	
	@Column(name ="id",nullable=true,length=32)	
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
	
	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator") 
	@Column(name ="code")
	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  type
	 */

	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  type
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}

	@Column(name ="SQL_LEVEL",nullable=true)
	public java.lang.Integer getLevel() {
		return level;
	}

	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}
	
	
	@Column(name ="sql_statement",nullable=true,length=2000)
	public java.lang.String getSqlStatement() {
		return sqlStatement;
	}

	public void setSqlStatement(java.lang.String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	@Column(name ="message",nullable=true,length=200)
	public java.lang.String getMessage() {
		return message;
	}

	public void setMessage(java.lang.String message) {
		this.message = message;
	}

	@Column(name ="error_message",nullable=true,length=200)
	public java.lang.String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(java.lang.String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Column(name ="name",nullable=true,length=32)
	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	@Column(name ="SQL_DESC",nullable=true,length=200)
	public java.lang.String getDescrition() {
		return descrition;
	}

	public void setDescrition(java.lang.String descrition) {
		this.descrition = descrition;
	}
	
}
