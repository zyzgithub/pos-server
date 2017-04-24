package com.wm.service.impl.order;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.credit.CreditEntity;
import com.wm.entity.order.OrderCommentEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.comment.util.CommentUtil;
import com.wm.service.order.OrderCommentServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.statistics.CourierStatisticsRealtimeServiceI;
import com.wm.service.statistics.MerchatStatisticsRealtimeServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.service.user.WUserServiceI;

@Service("orderCommentService")
@Transactional
public class OrderCommentServiceImpl extends CommonServiceImpl implements OrderCommentServiceI {

	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private MerchatStatisticsRealtimeServiceI merchatStatisticsRealtimeService;
	
	@Autowired
	private CourierStatisticsRealtimeServiceI courierStatisticsRealtimeService;
	
	@Autowired
	private SystemconfigServiceI systemConfigService;
	
	@Autowired
	private WUserServiceI userService;
	
	@Override
	public OrderCommentEntity getByOrderIdUserId(int orderId, int userId,
			int commentTarget, int commentTargetId) {
		String hql = "from OrderCommentEntity where orderId = " + orderId
				+ " and userId = " + userId + " and commentTarget = "
				+ commentTarget + " and commentTargetId = " + commentTargetId;
		return this.singleResult(hql);
	}

	@Override
	public boolean createOrUpdateOrderComment(int orderId, int userId, int type,
			int targetId, String comment, String display, int grade) {
		boolean flag = true;
		
		OrderCommentEntity en = this.getByOrderIdUserId(orderId, userId, type,
				targetId);
		if (en == null) {
			en = new OrderCommentEntity();
		} else if (en.getCommentDisplay().equalsIgnoreCase(display)) {
			flag = false;
		}
		en.setOrderId(orderId);
		if (StringUtils.isBlank(comment)) {
			comment = CommentUtil.getRandomCommentByType(type);
		}
	
		en.setCommentContent(comment);
		en.setCommentDisplay(display);
		en.setCommentTime(DateUtils.getSeconds());
		en.setGrade(grade);
		en.setUserId(userId);
		en.setCommentTargetId(targetId);
		en.setCommentTarget(type);
		this.saveOrUpdate(en);
		
		return flag;
	}

	@Override
	public void createSystemOrderComment(int orderId, int userId, int type,
			int targetId) {
		OrderCommentEntity en = new OrderCommentEntity();
		en.setOrderId(orderId);
		String comment = CommentUtil.getRandomCommentByType(type);
		// S为系统自动生成评价
		String display = "S";
		int grade = 5;
		en.setCommentContent(comment);
		en.setCommentDisplay(display);
		en.setCommentTime(DateUtils.getSeconds());
		en.setGrade(grade);
		en.setUserId(userId);
		en.setCommentTargetId(targetId);
		en.setCommentTarget(type);
		this.save(en);
	}

	@Override
	public void createSystemOrderComment(int orderId) {
		OrderEntity orderEntity = orderService.get(OrderEntity.class, orderId);
		if(orderEntity != null){
			int userId = orderEntity.getWuser().getId();
			//创建快递员的评价
			int type = 0;
			int targetId = orderEntity.getCourierId();
			this.createSystemOrderComment(orderId, userId, type, targetId);
			//创建商家的评价
			type = 1;
			targetId = orderEntity.getMerchant().getId();
			this.createSystemOrderComment(orderId, userId, type, targetId);
		}
	}

	@Override
	public int orderCommentScore(Integer orderId, Integer userId) {
		//主动评分添加积分， 先读取system_config,然后取comment_score
		String commentScoreCode = "comment_score";
		String valByCode = systemConfigService.getValByCode(commentScoreCode);
		if(StringUtils.isEmpty(valByCode)){
			valByCode = "10";//默认送10分
		}
		Integer score = Integer.parseInt(valByCode);
		
		//给用户添加主动评论的积分
		WUserEntity userEntity = userService.get(WUserEntity.class , userId);
		userEntity.setScore(userEntity.getScore() + score);
		this.updateEntitie(userEntity);
		
		//记录积分的流水
		CreditEntity creditEntity = new CreditEntity();
		creditEntity.setAction(commentScoreCode);
		creditEntity.setCreateTime(DateUtils.getSeconds());
		creditEntity.setDetailId(orderId);
		creditEntity.setDetail("主动评论送积分");
		creditEntity.setScore(score);
		creditEntity.setWuser(userEntity);
		this.save(creditEntity);
		return score;
	}
}