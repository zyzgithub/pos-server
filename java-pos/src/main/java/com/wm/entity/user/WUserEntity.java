package com.wm.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

/**   
 * @Title: Entity
 * @Description: user
 * @author wuyong
 * @date 2015-01-07 09:12:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "user", schema = "")
@SuppressWarnings("serial")
public class WUserEntity implements java.io.Serializable {
	
	public static final int LEAVE_STATE = 1;	//离职
	public static final int SERVING_STATE= 0;  //在职
	/**id*/
	private java.lang.Integer id;
	/**username*/
	private java.lang.String username;
	/**nickname*/
	private java.lang.String nickname;
	/**password*/
	private java.lang.String password;
	/**gender*/
	private java.lang.String gender = "M";
	/**photoUrl*/
	private java.lang.String photoUrl;
	/**mobile*/
	private java.lang.String mobile;
	/**money*/
	private java.lang.Double money = 0.00;
	/**score*/
	private java.lang.Integer score = 0;
	/**用户类型*/
	private java.lang.String userType = "user";
	/**sns*/
	private java.lang.String sns;
	/**ip*/
	private java.lang.String ip;
	/**loginTime*/
	private java.lang.Integer loginTime =  DateUtils.getSeconds();
	/**createTime*/
	private java.lang.Integer createTime = DateUtils.getSeconds();
	/**payPassword*/
	private java.lang.String payPassword;
	/**微信公众号用户对应的openid，各个公众号唯一不相同 **/
	private java.lang.String openId;
	/**微信公众号用户的unionId，同一个开放平台帐号下的unionId一样**/
	private java.lang.String unionId;
	
	private java.lang.Integer firstOrderTime = 0;
	
	/**是否禁用,0-启用，1-禁用*/
	private java.lang.Integer isDelete;
	
	/**用户状态*/
	private java.lang.Integer userState;
	
	/** 创建者id，关联user表的id */
	private Integer creator;
	
	/** 用户的支付宝账号 */
	private String aliAcNo;

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
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
	 *@return: java.lang.String  username
	 */
	@Column(name ="USERNAME",nullable=true,length=32)
	public java.lang.String getUsername(){
		return this.username;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  username
	 */
	public void setUsername(java.lang.String username){
		this.username = username;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  nickname
	 */
	@Column(name ="NICKNAME",nullable=true,length=32)
	public java.lang.String getNickname() {
		return nickname;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  nickname
	 */
	public void setNickname(java.lang.String nickname) {
		this.nickname = nickname;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  password
	 */
	@Column(name ="PASSWORD",nullable=true,length=32)
	public java.lang.String getPassword(){
		return this.password;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  password
	 */
	public void setPassword(java.lang.String password){
		this.password = password;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  gender
	 */
	@Column(name ="GENDER",nullable=false,length=1)
	public java.lang.String getGender(){
		return this.gender;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  gender
	 */
	public void setGender(java.lang.String gender){
		this.gender = gender;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  photoUrl
	 */
	@Column(name ="PHOTOURL",nullable=true,length=100)
	public java.lang.String getPhotoUrl() {
		return photoUrl;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  photoUrl
	 */
	public void setPhotoUrl(java.lang.String photoUrl) {
		this.photoUrl = photoUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  mobile
	 */
	@Column(name ="MOBILE",nullable=true,length=16)
	public java.lang.String getMobile(){
		return this.mobile;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  mobile
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  money
	 */
	@Column(name ="MONEY",nullable=false,precision=10,scale=2)
	public java.lang.Double getMoney(){
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  money
	 */
	public void setMoney(java.lang.Double money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  score
	 */
	@Column(name ="SCORE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getScore(){
		return this.score;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  score
	 */
	public void setScore(java.lang.Integer score){
		this.score = score;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用户类型'courier'快递员,'merchant'商家,'user'普通用户,'manage'管理员
	 */
	@Column(name ="USER_TYPE",nullable=true,length=8)
	public java.lang.String getUserType(){
		return this.userType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用户类型
	 */
	public void setUserType(java.lang.String userType){
		this.userType = userType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  sns
	 */
	@Column(name ="SNS",nullable=true,length=64)
	public java.lang.String getSns(){
		return this.sns;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  sns
	 */
	public void setSns(java.lang.String sns){
		this.sns = sns;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ip
	 */
	@Column(name ="IP",nullable=true,length=16)
	public java.lang.String getIp(){
		return this.ip;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ip
	 */
	public void setIp(java.lang.String ip){
		this.ip = ip;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  loginTime
	 */
	@Column(name ="LOGIN_TIME",nullable=false)
	public java.lang.Integer getLoginTime(){
		return this.loginTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  loginTime
	 */
	public void setLoginTime(java.lang.Integer loginTime){
		this.loginTime = loginTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  createTime
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  payPassword
	 */
	@Column(name ="PAY_PASSWORD",nullable=true,length=32)
	public java.lang.String getPayPassword(){
		return this.payPassword;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  payPassword
	 */
	public void setPayPassword(java.lang.String payPassword){
		this.payPassword = payPassword;
	}
	
	/**
	 * @return 微信公众号用户对应的openid，各个公众号唯一不相同
	 */
	@Column(name ="openid")
	public java.lang.String getOpenId() {
		return openId;
	}

	public void setOpenId(java.lang.String openId) {
		this.openId = openId;
	}
	
	/**
	 * @return 获取微信公众号用户的unionId，同一个开放平台帐号下的unionId一样
	 */
	@Column(name ="unionid")
	public java.lang.String getUnionId() {
		return unionId;
	}

	public void setUnionId(java.lang.String unionId) {
		this.unionId = unionId;
	}
	
	@Column(name ="FIRST_ORDER_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getFirstOrderTime() {
		return firstOrderTime;
	}

	public void setFirstOrderTime(java.lang.Integer firstOrderTime) {
		this.firstOrderTime = firstOrderTime;
	}

	@Column(name ="IS_DELETE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(java.lang.Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	@Column(name ="USER_STATE",nullable=false, precision = 10,scale = 0)
	public java.lang.Integer getUserState() {
		return userState;
	}

	public void setUserState(java.lang.Integer userState) {
		this.userState = userState;
	}

	@Column(name ="CREATOR",nullable=true, precision = 11,scale = 0)
	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	@Column(name ="ALIACNO",nullable=true, precision = 64,scale = 0)
	public String getAliAcNo() {
		return aliAcNo;
	}

	public void setAliAcNo(String aliAcNo) {
		this.aliAcNo = aliAcNo;
	}

	public WUserEntity(Integer id) {
		super();
		this.id = id;
	}

	public WUserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
