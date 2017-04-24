package com.wm.entity.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: image
 * @author wuyong
 * @date 2015-01-07 09:58:19
 * @version V1.0   
 *
 */
@Entity
@Table(name = "image", schema = "")
@SuppressWarnings("serial")
public class ImageEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**fileName*/
	private java.lang.String fileName;
	/**filePath*/
	private java.lang.String filePath;
	/**pid*/
	private ImageEntity pImage;
	//private java.lang.Integer pid;
	/**imgType*/
	private java.lang.String imgType;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
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
	 *@return: java.lang.String  fileName
	 */
	@Column(name ="FILE_NAME",nullable=true,length=100)
	public java.lang.String getFileName(){
		return this.fileName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  fileName
	 */
	public void setFileName(java.lang.String fileName){
		this.fileName = fileName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  filePath
	 */
	@Column(name ="FILE_PATH",nullable=true,length=200)
	public java.lang.String getFilePath(){
		return this.filePath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  filePath
	 */
	public void setFilePath(java.lang.String filePath){
		this.filePath = filePath;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  pid
	 */
	/*@Column(name ="PID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPid(){
		return this.pid;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  pid
	 */
	/*public void setPid(java.lang.Integer pid){
		this.pid = pid;
	}*/

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PID", nullable = true)
	public ImageEntity getpImage() {
		return pImage;
	}

	public void setpImage(ImageEntity pImage) {
		this.pImage = pImage;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  imgType
	 */
	@Column(name ="IMG_TYPE",nullable=true,length=8)
	public java.lang.String getImgType(){
		return this.imgType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  imgType
	 */
	public void setImgType(java.lang.String imgType){
		this.imgType = imgType;
	}
}
