/*package com.wm.service.impl.order;

import java.util.Map;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





import com.wm.service.order.OrderKitchenServiceI;

@Service("orderKitchentService")
@Transactional
public class OrderKitchenServiceImpl extends CommonServiceImpl implements OrderKitchenServiceI{

	@Autowired
	private SystemService systemService;
	
	*//**
	 * 获得公告信息和上一次发布公告的时间
	 *//*
	@Override
	public Map<String, Object> getNotice(Long id) {
		
		String sql = "select notice,notice_time from merchant where id=?";
		
		return this.findOneForJdbc(sql, id);
	}

	*//**
	 * 添加或更新公告
	 *//*
	@Override
	public void createOrUpdateNotice( Long id, String notice) {

		String sql = "update merchant set notice=?,notice_time=NOW() where id=? "
				+ "and DATE(notice_time)<>DATE(NOW()) "
				+ "OR notice_time is null ";

			this.executeSql(sql, notice,id);
	}
	
}
*/