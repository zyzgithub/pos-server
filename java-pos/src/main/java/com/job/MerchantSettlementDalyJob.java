package com.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.MerchantSettlementServiceI;

/**
 * 定时任务-商家日结算统计表
 * @author Simon
 */
public class MerchantSettlementDalyJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(MerchantSettlementDalyJob.class);

	@Autowired
	MerchantServiceI merchantService;
	@Autowired
	MerchantSettlementServiceI merchantSettlementService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-商家日结算统计表");
		
		List<MerchantEntity> Merchants = merchantService.findHql("from MerchantEntity ");
		if(Merchants != null && Merchants.size() > 0){
			for(MerchantEntity merchant : Merchants){
				try {
					merchantSettlementService.settleDayly(merchant.getId());
				} catch (Exception e) {
					logger.error("商家【" + merchant.getId() + "】日结算统计失败！", e);
				}
			}
		}
	}
	
}
