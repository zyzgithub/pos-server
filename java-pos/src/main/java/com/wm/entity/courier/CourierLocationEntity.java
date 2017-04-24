package com.wm.entity.courier;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

@Entity
@Table(name = "0085_courier_location")
public class CourierLocationEntity implements Serializable {
	private static final long serialVersionUID = 2236499027268488807L;

	private Integer id;
	private Integer userId;
	private Double longitude;
	private Double latitude;
	private Integer addtime;

	CourierLocationEntity(){
		
	}
	
	public CourierLocationEntity(Integer userId, Double longitude, Double latitude){
		this.userId = userId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.addtime = DateUtils.getSeconds();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name ="USER_ID",nullable=false,precision=11,scale=0)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name ="LONGITUDE",nullable=true,precision=15,scale=6)
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name ="LATITUDE",nullable=true,precision=15,scale=6)
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name ="ADDTIME",nullable=true,precision=11,scale=0)
	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

}
