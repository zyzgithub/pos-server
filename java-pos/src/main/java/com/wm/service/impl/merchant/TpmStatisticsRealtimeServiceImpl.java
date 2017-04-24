package com.wm.service.impl.merchant;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.merchant.TpmStatisticsRealtimeServiceI;
@Service
@Transactional
public class TpmStatisticsRealtimeServiceImpl extends CommonServiceImpl implements TpmStatisticsRealtimeServiceI{
	
	private static final String getStatCount = "select count(*) from `tpm_statistics_realtime` where merchant_id = ? ";
	private static final String addStat = "insert tpm_statistics_realtime(merchant_id,total_order,total_order_money,total_order_time,org_id) "
			+ " values(?,1,?,?,ifnull((select org_id from 0085_merchant_org where merchant_id=?), 0))";
	private static final String updateStat = "update `tpm_statistics_realtime` set total_order_money=(total_order_money+?),"
			+ " total_order_time=(total_order_time+?),total_order=(total_order+1) where merchant_id=?";

	
	public void updateStat(Integer merchantId, Integer consumeMoney, Integer orderTime){
		Integer statCount = this.findOneForJdbc(getStatCount, Integer.class, merchantId);
		if(statCount.equals(0)){
			this.executeSql(addStat, merchantId, consumeMoney, orderTime, merchantId);
		} else if(statCount.equals(1)){
			this.executeSql(updateStat, consumeMoney, orderTime, merchantId);
		}
	}
	
	/*
	public void createOrUpdateTSR(Integer merchantId,Integer money,Integer orderTime) {
		String sql = "SELECT COUNT(*) AS count FROM `tpm_statistics_realtime` WHERE merchant_id = ?";
		List<Map<String, Object>>  list = this.findForJdbc(sql, merchantId);
		Integer count = Integer.parseInt(list.get(0).get("count").toString());
		if(count == 0){
			String orgSql = "SELECT * FROM `0085_merchant_org` WHERE merchant_id =?";
			Map<String, Object>  orgMap = this.findOneForJdbc(orgSql, merchantId);
			if(StringUtil.isEmpty(orgMap)){
				String sql1 = "insert tpm_statistics_realtime(merchant_id, total_order,total_order_money,total_order_time) values(?,1,?,?)";
				this.executeSql(sql1, merchantId,money,orderTime);
			}else{
				String sql1 = "insert tpm_statistics_realtime(merchant_id, total_order,total_order_money,total_order_time,org_id) values(?,1,?,?,?)";
				this.executeSql(sql1, merchantId,money,orderTime,orgMap.get("org_id").toString());
			}			
		}else if(count == 1){
			String updateSql = "UPDATE `tpm_statistics_realtime` SET total_order_money =(total_order_money+?),total_order_time=(total_order_time+?),total_order=(total_order+1) WHERE merchant_id=?";
			this.executeSql(updateSql, money,orderTime,merchantId);
		}
	}

	public void createOrUpdateTSROnEatIn(Integer merchantId, Integer money, Integer orderTime) {
		String sql = "SELECT COUNT(*) AS count FROM `tpm_statistics_realtime` WHERE merchant_id = ?";
		List<Map<String, Object>>  list = this.findForJdbc(sql, merchantId);
		Integer count = Integer.parseInt(list.get(0).get("count").toString());
		if(count == 0){
			String orgSql = "SELECT * FROM `0085_merchant_org` WHERE merchant_id =?";
			Map<String, Object>  orgMap = this.findOneForJdbc(orgSql, merchantId);
			if(StringUtil.isEmpty(orgMap)){
				String sql1 = "insert tpm_statistics_realtime(merchant_id, total_order,total_order_money,total_order_time) values(?,1,?,?)";
				this.executeSql(sql1, merchantId,money,orderTime);
			}else{
				String sql1 = "insert tpm_statistics_realtime(merchant_id, total_order,total_order_money,total_order_time,org_id) values(?,1,?,?,?)";
				this.executeSql(sql1, merchantId,money,orderTime,orgMap.get("org_id").toString());
			}			
		}else if(count == 1){
			String updateSql = "UPDATE `tpm_statistics_realtime` SET total_order_money =(total_order_money+?),total_order_time=(total_order_time+?),total_order=(total_order+1) WHERE merchant_id=?";
			this.executeSql(updateSql, money,orderTime,merchantId);
		}
	}*/

}
