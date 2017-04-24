package com.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.order.OrderServiceI;
import com.wm.util.AliOcs;

import com.courier_mana.common.Constants;

/**
 * 定时任务-缓存商家ID与商家来源
 * @author Roar
 */

public class MerchantSourceJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(MerchantSourceJob.class);

	@Autowired
	private OrderServiceI orderService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务-缓存商家ID与商家来源");
        String sql = "select merchant_id,merchant_source from 0085_merchant_info";
        List<Map<String, Object>> sourceMapList = orderService.findForJdbc(sql);
        for(Map<String,Object> map : sourceMapList){
            AliOcs.set(Constants.MERCHANT_SOURCE_KEY+map.get("merchant_id"),  map.get("merchant_source"));
        }
	}
	
}
