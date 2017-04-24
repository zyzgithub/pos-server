package com.wm.entity.order;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 物流信息表
 * @author db
 * @date 2016-04-11
 */

@Entity
@Table(name="logistics", schema="")
@SuppressWarnings("serial")
public class LogisticsEntity implements java.io.Serializable {

	/**  主键  */
	private Long id;
	/**  订单id  */
	private Integer orderId;
	/** 物流信息类型  0：商家发货     1：买家退货   */
	private char type;
	/**  物流名称  */
	private String logisticsName;
	/**  物流单号  */
	private String logisticsNumber;
	/**  物流快照图片  */
	private String logisticsSnapshot;
	/**  创建人id  type=0 时 为商家id,type=1时 为用户id  */
	private Integer createById;
	/**  创建时间  */
	private Timestamp createTime;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false, precision=20, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="order_id", nullable=false, precision=20, scale=0)
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="type")
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	
	@Column(name="logistics_name")
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	
	@Column(name="logistics_number")
	public String getLogisticsNumber() {
		return logisticsNumber;
	}
	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}
	
	@Column(name="logistics_snapshot")
	public String getLogisticsSnapshot() {
		return logisticsSnapshot;
	}
	public void setLogisticsSnapshot(String logisticsSnapshot) {
		this.logisticsSnapshot = logisticsSnapshot;
	}
	
	@Column(name="create_by_id")
	public Integer getCreateById() {
		return createById;
	}
	public void setCreateById(Integer createById) {
		this.createById = createById;
	}
	
	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
