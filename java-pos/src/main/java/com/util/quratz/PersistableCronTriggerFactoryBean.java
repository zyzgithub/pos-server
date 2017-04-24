package com.util.quratz;

import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {
	
	@Override
	public void afterPropertiesSet() throws ParseException {

		super.afterPropertiesSet();
		
	}
	
}
