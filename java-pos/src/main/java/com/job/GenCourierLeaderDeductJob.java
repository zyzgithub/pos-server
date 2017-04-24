package com.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.statistics.CourierStatisticsDalyServiceI;

/**
 * 定时任务-生成上个月快递员组长子网点提成
 * @author Simon
 */
public class GenCourierLeaderDeductJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(GenCourierLeaderDeductJob.class);

	@Autowired
	CourierStatisticsDalyServiceI courierStatisticsDalyService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-生成上个月快递员组长子网点提成");
		
		String sql = "select so.*,u.username,o.org_name,cp.position_id from 0085_courier_sub_org so ";
		sql += " left join `user` u on u.id=so.courier_id ";
		sql += " left join 0085_courier_position cp on cp.courier_id=so.courier_id ";
		sql += " left join 0085_org o on o.id=so.sub_org_id ";
		sql += " where u.is_delete=0 and u.user_type='courier' ";
		sql += " and o.`status`=1 and o.`level`=6 ";
		sql += " and cp.position_id in (3,5) "; // 商务经理、片区经理
		
		List<Map<String, Object>> leaders = courierStatisticsDalyService.findForJdbc(sql);
		if(leaders != null && leaders.size() > 0){
			for (Map<String, Object> map : leaders) {
				Integer leaderId = Integer.parseInt(map.get("courier_id").toString());
				Integer subOrgId = Integer.parseInt(map.get("sub_org_id").toString()); // 开辟的子网点
				try {
					courierStatisticsDalyService.statisticsMonthly(leaderId, subOrgId);
				} catch (Exception e) {
					logger.error("生成上个月快递员组长【" + leaderId + "】子网点【" + subOrgId + "】提成失败！", e);
				}
			}
		}
	}
	
}
