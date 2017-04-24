package com.wm.service.notice;

import java.util.List;

import com.wm.entity.notice.NoticeEntity;

public interface NoticeServiceI {
	/**
	 * 获取用户IDs
	 * @param sql
	 * @return
	 */
	List<Integer> getUserIds(String sql);
	
	/**
	 * 根据消息ID获取消息
	 * @param noticeId
	 * @return
	 */
	NoticeEntity getNotice(Integer noticeId);
	
	/**
	 * 推送消息
	 * @param noticeEntity
	 * @param userIds
	 */
	void push(NoticeEntity noticeEntity,List<Integer> userIds);
	
	/**
	 * 发送消息
	 * @param noticeEntity
	 */
	void push(NoticeEntity noticeEntity);
	
	/**
	 * 发送消息
	 * @param noticeId
	 */
	void push(Integer noticeId);
	
	/**
	 * 
	 * @param noticeEntity
	 * @param userIds
	 */
	void afterPush(NoticeEntity noticeEntity, List<Integer> userIds,  String result);
	
	/**
	 * 推送审核商家申请结果消息
	 * @param noticeEntity
	 * @param userIds
	 */
	void pushAudit(NoticeEntity noticeEntity, Integer userIds);
}
