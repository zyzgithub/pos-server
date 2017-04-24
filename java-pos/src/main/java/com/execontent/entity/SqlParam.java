package com.execontent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "c_sqlparam", schema = "")
@SuppressWarnings("serial")
public class SqlParam implements java.io.Serializable{
	private String id;
	private String exeid;
	private String name;
	private int cannull;
	private String dataType;   //数据类型
	private String descrition;
	private int paramType;  //参数类型，0：请求参数，1：返回参数
	
	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator") 
	@Column(name ="id")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name ="exeid",length=32,nullable=true)
	public String getExeid() {
		return exeid;
	}
	
	public void setExeid(String exeid) {
		this.exeid = exeid;
	}
	
	@Column(name ="name",length=32,nullable=true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name ="cannull",nullable=true)
	public int getCannull() {
		return cannull;
	}
	
	public void setCannull(int cannull) {
		this.cannull = cannull;
	}
	
	@Column(name ="descrition",length=200,nullable=true)
	public String getDescrition() {
		return descrition;
	}
	public void setDescrition(String descrition) {
		this.descrition = descrition;
	}
	
	@Column(name ="data_type",length=32,nullable=true)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Column(name ="param_type")
	public int getParamType() {
		return paramType;
	}

	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

}
