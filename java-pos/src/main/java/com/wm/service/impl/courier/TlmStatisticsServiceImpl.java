package com.wm.service.impl.courier;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.courier.TlmStatisticsServiceI;
@Service
@Transactional
public class TlmStatisticsServiceImpl extends CommonServiceImpl implements TlmStatisticsServiceI{

	@Override
	public void findFromTlmStatisticsByCourierId(Integer courierId) {
		String sql = "SELECT COUNT(*) AS count FROM `tlm_statistics_realtime` WHERE courier_id = ?";
		List<Map<String, Object>>  list = this.findForJdbc(sql, courierId);
		Integer count = Integer.parseInt(list.get(0).get("count").toString());
		if(count == 0){
			String sql1 = "insert tlm_statistics_realtime(courier_id, total_pushed_times,start_time) values(?,1,NOW())";
			this.executeSql(sql1, courierId);
		}else if(count == 1){
			String sql1 = "UPDATE `tlm_statistics_realtime` SET total_pushed_times =(total_pushed_times+1),update_time=NOW() WHERE courier_id=?";
			this.executeSql(sql1, courierId);
		}
	}

	@Override
	public void updateTotalOrder(Integer courierId,Integer completeMin,Integer money) {
		String selectSql = "SELECT COUNT(*) AS count FROM `tlm_statistics_realtime` WHERE courier_id = ?";
		List<Map<String, Object>>  list = this.findForJdbc(selectSql, courierId);
		Integer count = Integer.parseInt(list.get(0).get("count").toString());
		if(count == 0){
			String sql = "insert tlm_statistics_realtime(courier_id,start_time) values(?,NOW())";
			this.executeSql(sql,courierId);
			String sql2 = "UPDATE `tlm_statistics_realtime` SET total_order =(total_order+1),total_order_time=(total_order_time+?),total_order_money=(total_order_money+?),update_time=NOW() WHERE courier_id=?";
			this.executeSql(sql2,completeMin,money,courierId);
		}else if(count == 1){
			String sql = "UPDATE `tlm_statistics_realtime` SET total_order =(total_order+1),total_order_time=(total_order_time+?),total_order_money=(total_order_money+?),update_time=NOW() WHERE courier_id=?";
			this.executeSql(sql,completeMin,money,courierId);
		}
	}

}
