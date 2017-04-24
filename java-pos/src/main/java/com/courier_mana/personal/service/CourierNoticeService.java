package com.courier_mana.personal.service;

import java.util.List;
import java.util.Map;

import com.wm.entity.notice.NoticeEntity;

/**
 * 
 * 快递员个人中心-系统消息接口
 *
 */
public interface CourierNoticeService {

	/**
	 * 获取系统消息列表
	 * @param courierId
	 * @return
	 */
	public List<Map<String,Object>> getNoticeList(Integer page, Integer rows);
	
}
