package com.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.order.OrderServiceI;

public class MonitorMerchantOrderStatisticsJob extends QuartzJobBean {
	private static final Logger logger = Logger.getLogger(MonitorMerchantOrderStatisticsJob.class);
	
	@Autowired
	private OrderServiceI orderService;
	

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		String sql = "SELECT DATE(FROM_UNIXTIME(create_time)) date,COUNT(id) quantity ,merchant_id  "
				+ "FROM `order` "
				+ "WHERE  state='confirm' "
				+ "AND DATE(FROM_UNIXTIME(create_time))=DATE_ADD(DATE(NOW()),INTERVAL -1 DAY) "
				+ "AND NOT from_type='crowdsourcing' AND rstate='normal' "
				+ "GROUP BY merchant_id ";
		List<Map<String, Object>> list = orderService.findForJdbc(sql);
		logger.info("自动统计商家昨日订单,共计："+list.size()+"条数据");
		
		if(list!=null && list.size()>0){
			int sum = 0;
			for(int i=0;i<list.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map = list.get(i);
				
				Integer merchantId = Integer.parseInt(map.get("merchant_id").toString());
				Integer quantity = Integer.parseInt(map.get("quantity").toString());
				String time = map.get("date").toString();
				
				//查询商家扣点后当日总营收入
				String osql = "SELECT SUM(oi.money) money FROM order_income oi,`order` o "
						+"WHERE oi.order_id=o.id AND oi.state='pay' "
						+"AND DATE(FROM_UNIXTIME(o.create_time))=? "
						+"AND o.merchant_id=? AND o.state='confirm' "
						+"AND NOT o.from_type='crowdsourcing' AND o.rstate='normal' ";
				Map<String, Object> money = orderService.findOneForJdbc(osql, time, merchantId);
				
				if(money!=null && money.size()>0){
					
					//将数据插入统计表tom_merchant_order_statistics
					String tsql = "INSERT INTO tom_merchant_order_statistics(merchant_id,date,money,quantity) VALUES (?,STR_TO_DATE(?,'%Y-%m-%d'),?,?)";
					
					if(money.get("money")==null){
						logger.info("商家"+merchantId+"－"+time+"统计失败");
					}else{
						orderService.executeSql(tsql, merchantId, time, money.get("money"), quantity);
						sum++;
					}
					
				}else{
					logger.info("商家"+merchantId+"－"+time+"统计失败");
				}
			}
			logger.info("成功统计商家订单信息共计："+sum+"条数据");
		}
	}
}

