package com.wm.entity.courierbuilding;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 0085_courier_building
 * @author 0085.com
 * @date 2015-09-18 16:47:56
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_building", schema = "")
@SuppressWarnings("serial")
public class CourierBuildingEntity implements java.io.Serializable {
	
	/**快递员*/
	private java.lang.Integer courierId;
	/**负责配送的建筑*/
	private java.lang.Integer buildingId;
	/**负责起始楼层*/
	private java.lang.Integer firstFloor;
	/**负责终止楼层*/
	private java.lang.Integer lastFloor;
	/**楼层列表，用逗号分隔，如果为0表示按first,last的数值*/
	private java.lang.String floors;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员
	 */
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  负责配送的建筑
	 */
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="BUILDING_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getBuildingId(){
		return this.buildingId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  负责配送的建筑
	 */
	public void setBuildingId(java.lang.Integer buildingId){
		this.buildingId = buildingId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  负责起始楼层
	 */
	@Column(name ="FIRST_FLOOR",nullable=true,precision=10,scale=0)
	public java.lang.Integer getFirstFloor(){
		return this.firstFloor;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  负责起始楼层
	 */
	public void setFirstFloor(java.lang.Integer firstFloor){
		this.firstFloor = firstFloor;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  负责终止楼层
	 */
	@Column(name ="LAST_FLOOR",nullable=true,precision=10,scale=0)
	public java.lang.Integer getLastFloor(){
		return this.lastFloor;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  负责终止楼层
	 */
	public void setLastFloor(java.lang.Integer lastFloor){
		this.lastFloor = lastFloor;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  楼层列表，用逗号分隔，如果为0表示按first,last的数值
	 */
	@Column(name ="FLOORS",nullable=true,length=255)
	public java.lang.String getFloors(){
		return this.floors;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  楼层列表，用逗号分隔，如果为0表示按first,last的数值
	 */
	public void setFloors(java.lang.String floors){
		this.floors = floors;
	}
}
