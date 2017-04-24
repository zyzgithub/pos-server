package com.courier_mana.statistics.vo;

/**
 * 为WarehouseReportDetail服务的courier_mana_call_log表vo
 * @author hyj
 */
public class CourierManaCallLogVo4WarehouseReportDetail {
	/**
	 * 商家ID
	 */
	private long merchantId;
	/**
	 * 拨打电话(回访)次数
	 */
	private int dials;
	
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public int getDials() {
		return dials;
	}
	public void setDials(int dials) {
		this.dials = dials;
	}
}
