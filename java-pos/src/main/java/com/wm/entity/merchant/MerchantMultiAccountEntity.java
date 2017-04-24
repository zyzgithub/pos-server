package com.wm.entity.merchant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 */
@Entity
@Table(name = "0085_merchant_multiaccount", schema = "")
@SuppressWarnings("serial")
public class MerchantMultiAccountEntity implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
	
	private Integer mainstoreId;
	private Integer branchstoreId;
	
	
	@Column(name ="mainstore_id", nullable=false)
	public Integer getMainstorId() {
		return mainstoreId;
	}
	public void setMainstoreId(Integer mainstoreId) {
		this.mainstoreId = mainstoreId;
	}
	
	@Column(name ="branchstore_id", nullable=false)
	public Integer getBranchstoreId() {
		return branchstoreId;
	}
	public void setBranchstoreId(Integer branchstoreId) {
		this.branchstoreId = branchstoreId;
	}

	
	
}
