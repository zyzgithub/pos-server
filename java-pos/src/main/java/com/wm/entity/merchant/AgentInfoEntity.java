package com.wm.entity.merchant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: agent_info
 * @author jiangpingmei
 * @date 2016-03-17 09:12:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "agent_info", schema = "")
@SuppressWarnings("serial")
public class AgentInfoEntity implements Serializable {
	
	/** id*/
	private Integer id;
	/** user_id */
	private Integer userId;
	/** 市id */
	private Integer orgId;
	/** 代理商名称 */
	private String attractName;
	/** 签约账号名额 */
	private Integer places;
	/** 商务电话 */
	private String businessTelephone;
	/**T+0 直营返点率，1.00相当于1.00% */
	private BigDecimal points;
	/**T+0 分销返点率，1.00相当于1.00% */
	private BigDecimal rebate;
	/** 关联账号 */
	private String relatedAccount;
	/** 关联账号id */
	private Integer relatedAccountId;
	/** 收入 1元=100 */
	private Double income;
	/** 保证金 1元=100 */
	private Integer bond;
	/** 创建人id */
	private Integer createId;
	/** 创建时间 */
	private Timestamp createTime;
	/** 合同图片url */
	private String contractImgUrl;
	/** 路径(_x_x_) */
	private String pPath;
	/** 微信手续费 100=1%   */
	private Integer wxCost;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column( name="user_id", nullable=false, precision=11, scale=0 )
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column( name="org_id", nullable=false, precision=11, scale=0 )
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Column( name="attract_name", nullable=false, length=255 )
	public String getAttractName() {
		return attractName;
	}
	public void setAttractName(String attractName) {
		this.attractName = attractName;
	}

	@Column( name="places", nullable=false, precision=8, scale=0 )
	public Integer getPlaces() {
		return places;
	}
	public void setPlaces(Integer places) {
		this.places = places;
	}

	@Column( name="business_telephone", nullable=false, length=15 )
	public String getBusinessTelephone() {
		return businessTelephone;
	}
	public void setBusinessTelephone(String businessTelephone) {
		this.businessTelephone = businessTelephone;
	}

	@Column( name="points", nullable=false )
	public BigDecimal getPoints() {
		return points;
	}
	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	@Column( name="rebate", nullable=false )
	public BigDecimal getRebate() {
		return rebate;
	}
	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}

	@Column( name="related_account", nullable=false, length=255 )
	public String getRelatedAccount() {
		return relatedAccount;
	}
	public void setRelatedAccount(String relatedAccount) {
		this.relatedAccount = relatedAccount;
	}

	@Column( name="related_account_id", nullable=true, precision=11, scale=0 )
	public Integer getRelatedAccountId() {
		return relatedAccountId;
	}
	public void setRelatedAccountId(Integer relatedAccountId) {
		this.relatedAccountId = relatedAccountId;
	}

	@Column( name="income", nullable=false, precision=11, scale=0 )
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}

	@Column( name="bond", nullable=false, precision=11, scale=0 )
	public Integer getBond() {
		return bond;
	}
	public void setBond(Integer bond) {
		this.bond = bond;
	}

	@Column( name="create_id", nullable=false, precision=11, scale=0 )
	public Integer getCreateId() {
		return createId;
	}
	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	@Column( name="create_time", nullable=false )
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column( name="contract_img_url", nullable=false )
	public String getContractImgUrl() {
		return contractImgUrl;
	}
	public void setContractImgUrl(String contractImgUrl) {
		this.contractImgUrl = contractImgUrl;
	}

	@Column( name="p_path", nullable=true)
	public String getpPath() {
		return pPath;
	}
	public void setpPath(String pPath) {
		this.pPath = pPath;
	}
	
	@Column( name="wx_cost")
	public Integer getWxCost() {
		return wxCost;
	}
	public void setWxCost(Integer wxCost) {
		this.wxCost = wxCost;
	}
	
	
}
