package com.wm.entity.networkDevelop;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 网点商家照片
 * @author wuyong
 * @date 2016-04-13 11:43:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_network_photos", schema = "")
@SuppressWarnings("serial")
public class NetworkPhotosEntity implements java.io.Serializable {
	/**photoId*/
	private java.lang.Integer photoId;
	/**网点开拓记录id*/
	private java.lang.Integer networkdDevid;
	/**照片类型(中餐，超市...)*/
	private java.lang.Integer photoType;
	/**照片url*/
	private java.lang.String url;
	/**创建时间*/
	private java.lang.Integer photoOrder;
	/**创建时间*/
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  photoId
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="PHOTO_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getPhotoId(){
		return this.photoId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  photoId
	 */
	public void setPhotoId(java.lang.Integer photoId){
		this.photoId = photoId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  网点开拓记录id
	 */
	@Column(name ="NETWORKD_DEVID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getNetworkdDevid(){
		return this.networkdDevid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  网点开拓记录id
	 */
	public void setNetworkdDevid(java.lang.Integer networkdDevid){
		this.networkdDevid = networkdDevid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  照片类型(中餐，超市...)
	 */
	@Column(name ="PHOTO_TYPE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPhotoType(){
		return this.photoType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  照片类型(中餐，超市...)
	 */
	public void setPhotoType(java.lang.Integer photoType){
		this.photoType = photoType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  照片url
	 */
	@Column(name ="URL",nullable=true,length=64)
	public java.lang.String getUrl(){
		return this.url;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  照片url
	 */
	public void setUrl(java.lang.String url){
		this.url = url;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer   照片顺序
	 */
	@Column(name ="PHOTO_ORDER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPhotoOrder(){
		return this.photoOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  照片顺序
	 */
	public void setPhotoOrder(java.lang.Integer photoOrder){
		this.photoOrder = photoOrder;
	}
}
