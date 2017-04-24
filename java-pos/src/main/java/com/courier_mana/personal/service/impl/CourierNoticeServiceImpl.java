package com.courier_mana.personal.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.courier_mana.personal.service.CourierNoticeService;


@Service
public class CourierNoticeServiceImpl extends CommonServiceImpl implements CourierNoticeService {
	private static final Logger logger = LoggerFactory.getLogger(CourierNoticeServiceImpl.class);
	
	@Override
	public List<Map<String,Object>> getNoticeList(Integer page, Integer rows) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select id, title, content, create_time ");
		sql.append(" from tsm_notice ");
		sql.append(" where status = 1 and notice_object = 3 ");
		return findForJdbc(sql.toString(), page, rows);
	} 
}
