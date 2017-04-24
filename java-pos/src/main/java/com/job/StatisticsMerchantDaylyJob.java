package com.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.statistics.MerchatStatisticsDalyEntity;
import com.wm.service.comment.CommentServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.statistics.MerchatStatisticsDalyServiceI;

/**
 * 定时任务-商家日统计
 * @author Simon
 */
public class StatisticsMerchantDaylyJob extends QuartzJobBean {
	
	private static final Logger logger = Logger.getLogger(StatisticsMerchantDaylyJob.class);

	@Autowired
	OrderServiceI orderService;
	@Autowired
	CommentServiceI commentService;
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	private MerchatStatisticsDalyServiceI merchatStatisticsDalyService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-商家日统计");
		
		DateTime statisticsDate = DateTime.now().minusDays(1);
		
		List<MerchantEntity> merchantList = merchantService.findHql("from MerchantEntity ");
		
		for (MerchantEntity merchant : merchantList) {
			Integer merchantId = merchant.getId();
			try {
				MerchatStatisticsDalyEntity msde = new MerchatStatisticsDalyEntity();
				msde.setMerchantId(merchantId);
				
				// 统计订单数
				String sql = "select * from (select distinct state, count(id) c, sum(origin) s from `order` ";
				sql	+= " where merchant_id=? and date(from_unixtime(create_time))=? group by state) st ";
				sql	+= " where st.state='confirm'";
				List<Map<String, Object>> list = merchatStatisticsDalyService.findForJdbc(sql, new Object[]{merchantId, statisticsDate.toString("yyyy-MM-dd")});
				if(list != null && list.size() > 0){
					Map<String, Object> map = list.get(0);
					Integer totalOrder = Integer.parseInt(map.get("c").toString());
					msde.setDaylyTotalOrder(totalOrder);
					Object saledMoney = map.get("s");
					if(saledMoney != null){
						Double totalSaledMoney = Double.parseDouble(saledMoney.toString());
						msde.setDaylyTotalSaledMoney(totalSaledMoney);
					} else {
						msde.setDaylyTotalSaledMoney(0.0);
					}
				}
				
				// 统计总份数
				Long yestodayMenus = orderService.getMerchantMenus(merchantId, statisticsDate.toString("yyyy-MM-dd"), statisticsDate.toString("yyyy-MM-dd"));
				msde.setDaylyTotalSaledQuantity(yestodayMenus.intValue());
				
				// 统计评论
				sql= "select count(*) c, sum(grade) s from 0085_order_comment where comment_target=1 and comment_target_id=? and date(from_unixtime(comment_time))=?";
				list = commentService.findForJdbc(sql, new Object[]{merchantId, statisticsDate.toString("yyyy-MM-dd")});
				if(list != null && list.size() > 0){
					Map<String, Object> map = list.get(0);
					Integer totalComm = Integer.parseInt(map.get("c").toString());
					msde.setDaylyTotalComment(totalComm);
					Object grade = map.get("s");
					if(grade != null){
						Integer totalScore = Integer.parseInt(grade.toString());
						msde.setDaylyTotalCommentScore(totalScore);
					} else {
						msde.setDaylyTotalCommentScore(0);
					}
				}
				
				msde.setUpdateDate((int)(statisticsDate.getMillis()/1000));
				merchatStatisticsDalyService.saveDayly(msde);
			
			} catch (Exception e) {
				logger.error("商家【" + merchantId + "】日统计失败！", e);
			}
		}
		
	}
	
}
