package com.wm.entity.merchantinfo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家最低提成价格设置表
 * @author wuyong
 * @date 2015-09-17 15:49:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_info", schema = "")
@SuppressWarnings("serial")
public class MerchantInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**单件商品时，价格超过该值时，快递员才能拿到提成*/
	private BigDecimal courierMinDeductMoney;
	/**扣点类型，默认1=为门店扣点，2=商品扣点*/
	private java.lang.Integer merchantDeductType;
	/**门店扣点数，20=20%*/
	private BigDecimal deductPercent;
	/**merchantId*/
	private java.lang.Integer merchantId;
	/**外卖是否需要回扣（0否，1是）*/
	private java.lang.Integer isTakeout;
	/**堂食是否需要回扣（0否，1是）*/
	private java.lang.Integer isHallFood;
	/**配送范围*/
	private BigDecimal deliveryScope; 
	/**配送范围修改时间*/
	private Date deliveryScopeTime; 
	/**商家来源，0=默认商家，1=乡村基商家，2=私厨 , 3=我要洗衣,4=代购*/
	private Integer merchantSource;
	/**是否开启堂食折扣，默认=0表示不开启，1=开启*/
	private Integer isDineInDiscount;
	/**堂食折扣，1=1%*/
	private Integer dineInDiscount;
	/**商家平摊，1=1%*/
	private Integer merchantSharePercent;
	/**平台分摊比例，如1=1%*/
	private Integer platformSharePercent;
	/** 茶位费 100=1元 */
	private Integer coverChargeFee;
	/** 平台类型，默认1=平台商家，2=合作商商家 */
	private Integer platformType;
	/** 堂食订单打印小票张数  */
	private java.lang.Integer inPrintPiece;
	/** 外卖订单打印小票张数  */
	private java.lang.Integer outPrintPiece;
	/** 扫码是否需要回扣（0否，1是）  */
	private Integer isScan;
	/** 商家自动开店功能：0未申请开通外卖 1已申请未回访 2已申请回访中 3已开通外卖 4拒绝开通外卖  */
	private Integer applyWaimaiStatus;
	/** 商家自动开店功能：申请开通外卖时间  */
	private Date applyWaimaiTime;
	/** 商家自助注册邀请码,对应业务员工号  */
	private String inviteCode; 
	/** 商家加盟类型，1加盟店，2直营店  */
	private Integer shopFromType; 
	/** 合作商商家扣点类型 例如 A,B,C等类型 */
	private String deductionType;
	


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
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  单件商品时，价格超过该值时，快递员才能拿到提成
	 */
	@Column(name ="COURIER_MIN_DEDUCT_MONEY",nullable=true,precision=6,scale=2)
	public BigDecimal getCourierMinDeductMoney(){
		return this.courierMinDeductMoney;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  单件商品时，价格超过该值时，快递员才能拿到提成
	 */
	public void setCourierMinDeductMoney(BigDecimal courierMinDeductMoney){
		this.courierMinDeductMoney = courierMinDeductMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  扣点类型，默认1=为门店扣点，2=商品扣点
	 */
	@Column(name ="MERCHANT_DEDUCT_TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getMerchantDeductType(){
		return this.merchantDeductType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  扣点类型，默认1=为门店扣点，2=商品扣点
	 */
	public void setMerchantDeductType(java.lang.Integer merchantDeductType){
		this.merchantDeductType = merchantDeductType;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  门店扣点数，20=20%
	 */
	@Column(name ="DEDUCT_PERCENT",nullable=false,precision=4,scale=2)
	public BigDecimal getDeductPercent(){
		return this.deductPercent;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  门店扣点数，20=20%
	 */
	public void setDeductPercent(BigDecimal deductPercent){
		this.deductPercent = deductPercent;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  merchantId
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  merchantId
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	
	@Column(name ="IS_TAKEOUT",nullable=false,precision=10,scale=0)
	public java.lang.Integer getIsTakeout() {
		return isTakeout;
	}

	public void setIsTakeout(java.lang.Integer isTakeout) {
		this.isTakeout = isTakeout;
	}
	@Column(name ="IS_HALL_FOOD",nullable=false,precision=10,scale=0)
	public java.lang.Integer getIsHallFood() {
		return isHallFood;
	}

	public void setIsHallFood(java.lang.Integer isHallFood) {
		this.isHallFood = isHallFood;
	}
	
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  配送距离
	 */
	@Column(name ="delivery_scope",nullable=true,precision=8,scale=2)
	public BigDecimal getDeliveryScope(){
		return this.deliveryScope;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  配送距离
	 */
	public void setDeliveryScope(BigDecimal deliveryScope){
		this.deliveryScope = deliveryScope;
	}
	
	/**
	 * 配送修改时间
	 * @return
	 */
	@Column(name ="delivery_scope_time",nullable=true)
	public Date getDeliveryScopeTime() {
		return deliveryScopeTime;
	}

	public void setDeliveryScopeTime(Date deliveryScopeTime) {
		this.deliveryScopeTime = deliveryScopeTime;
	}
	
	/**
	 * 商家来源，0=默认商家，1=乡村基商家，2=私厨 , 3=我要洗衣,4=代购
	 * @return
	 */
	@Column(name = "merchant_source",nullable=false,precision=4,scale=0)
	public Integer getMerchantSource() {
		return merchantSource;
	}
	
	public void setMerchantSource(Integer merchantSource) {
		this.merchantSource = merchantSource;
	}

	@Column(name="is_dine_in_discount")
	public Integer getIsDineInDiscount() {
		return isDineInDiscount;
	}

	public void setIsDineInDiscount(Integer isDineInDiscount) {
		this.isDineInDiscount = isDineInDiscount;
	}

	@Column(name="dine_in_discount")
	public Integer getDineInDiscount() {
		return dineInDiscount;
	}

	public void setDineInDiscount(Integer dineInDiscount) {
		this.dineInDiscount = dineInDiscount;
	}

	@Column(name="merchant_share_percent")
	public Integer getMerchantSharePercent() {
		return merchantSharePercent;
	}

	public void setMerchantSharePercent(Integer merchantSharePercent) {
		this.merchantSharePercent = merchantSharePercent;
	}

	@Column(name="platform_share_percent")
	public Integer getPlatformSharePercent() {
		return platformSharePercent;
	}

	public void setPlatformSharePercent(Integer platformSharePercent) {
		this.platformSharePercent = platformSharePercent;
	}

	@Column(name="cover_charge_fee")
	public Integer getCoverChargeFee() {
		return coverChargeFee;
	}

	public void setCoverChargeFee(Integer coverChargeFee) {
		this.coverChargeFee = coverChargeFee;
	}

	/**
	 * @return 1=平台商家;2=合作商商家
	 */
	@Column(name="platform_type")
	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	@Column(name="in_print_piece")
	public java.lang.Integer getInPrintPiece() {
		return inPrintPiece;
	}

	public void setInPrintPiece(java.lang.Integer inPrintPiece) {
		this.inPrintPiece = inPrintPiece;
	}

	@Column(name="out_print_piece")
	public java.lang.Integer getOutPrintPiece() {
		return outPrintPiece;
	}

	public void setOutPrintPiece(java.lang.Integer outPrintPiece) {
		this.outPrintPiece = outPrintPiece;
	}

	@Column(name="is_scan")
	public Integer getIsScan() {
		return isScan;
	}

	public void setIsScan(Integer isScan) {
		this.isScan = isScan;
	}
	
	@Column(name="apply_waimai_status")
	public Integer getApplyWaimaiStatus() {
		return applyWaimaiStatus;
	}

	public void setApplyWaimaiStatus(Integer applyWaimaiStatus) {
		this.applyWaimaiStatus = applyWaimaiStatus;
	}

	@Column(name="apply_waimai_time")
	public Date getApplyWaimaiTime() {
		return applyWaimaiTime;
	}

	public void setApplyWaimaiTime(Date applyWaimaiTime) {
		this.applyWaimaiTime = applyWaimaiTime;
	}

	@Column(name="invite_code")
	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	@Column(name ="SHOP_FROM_TYPE",nullable=false,precision=2,scale=0)
	public Integer getShopFromType() {
		return shopFromType;
	}

	public void setShopFromType(Integer shopFromType) {
		this.shopFromType = shopFromType;
	}

	@Column(name="deduction_type")
	public String getDeductionType() {
		return deductionType;
	}

	public void setDeductionType(String deductionType) {
		this.deductionType = deductionType;
	}
	
	
}
