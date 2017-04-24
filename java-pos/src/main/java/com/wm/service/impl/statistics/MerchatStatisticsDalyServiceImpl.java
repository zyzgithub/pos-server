package com.wm.service.impl.statistics;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.statistics.MerchatStatisticsDalyEntity;
import com.wm.service.statistics.MerchatStatisticsDalyServiceI;

@Service("merchatStatisticsDalyService")
@Transactional
public class MerchatStatisticsDalyServiceImpl extends CommonServiceImpl implements MerchatStatisticsDalyServiceI {
	
	public void saveDayly(MerchatStatisticsDalyEntity entity) {
		String sql = "insert into 0085_merchant_statistics_dayly(merchant_id, dayly_total_order, dayly_total_comment, ";
		sql += " dayly_total_comment_score, dayly_total_saled_quantity, dayly_total_saled_money, update_date) values(?,?,?,?,?,?,?)";
		this.executeSql(sql, new Object[]{entity.getMerchantId(), entity.getDaylyTotalOrder(), entity.getDaylyTotalComment(), 
				entity.getDaylyTotalCommentScore(), entity.getDaylyTotalSaledQuantity(), entity.getDaylyTotalSaledMoney(), entity.getUpdateDate()});
	}

	public List<Map<String, Object>> findOrderStati(Integer merchantId, String statiDay) {
		String sql = "select * from (select distinct state, count(id) c, sum(origin) s from `order` ";
		sql	+= " where merchant_id=? and date(from_unixtime(create_time))=? group by state) st ";
		sql	+= " where st.state='confirm'";
		return this.findForJdbc(sql, new Object[]{merchantId, statiDay});
	}
	
}