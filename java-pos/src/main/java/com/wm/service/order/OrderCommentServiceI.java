package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderCommentEntity;

public interface OrderCommentServiceI extends CommonService {
	
	/**
	 * 
	 * @param orderId
	 * @param userId
	 * @param type //评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 * @param targetId 快递员则传快递员id
	 * @return
	 */
	public OrderCommentEntity getByOrderIdUserId(int orderId, int userId, int type, int targetId);

	/**
	 * 创建或更新评价, 不是自动生成的评价，会添加一条记录到实时统计表中
	 * @param orderId
	 * @param userId
	 * @param type //评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 * @param targetId 快递员则传快递员id
	 * @param comment 评价内容, 如果内容为空，会随机生成好评
	 * @param display  是否显示， S为系统自动确认的时候生成， Y,N为人为，Y为显示，N为不显示 
	 * @param grade 评价的分数
	 */
	public boolean createOrUpdateOrderComment(int orderId, int userId, int type, int targetId, String comment, String display, int grade);

	/**
	 * 创建系统评价
	 * @param orderId
	 * @param userId
	 * @param type //评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 * @param targetId 快递员则传快递员id
	 */
	public void createSystemOrderComment(int orderId, int userId, int type, int targetId);
	

	/**
	 * 创建系统评价,里面根据orderId查找到快递员id和商户id，然后插入两条系统生成的记录
	 * @param orderId
	 */
	public void createSystemOrderComment(int orderId);
	
	/**
	 * 主动评论送积分
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public int orderCommentScore(Integer orderId, Integer userId);
	
}
