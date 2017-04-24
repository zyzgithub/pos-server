package com.wm.service.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Maps;
import com.jpush.SoundFile;
import com.wm.dao.order.OrderReminderDao;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderReminderEntity;
import com.wm.service.order.OrderReminderServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.jpush.JpushServiceI;

@Service("orderReminderService")
@Transactional
public class OrderReminderServiceImpl extends CommonServiceImpl implements OrderReminderServiceI {

	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private OrderReminderDao orderReminderDao;
	@Autowired
    private JpushServiceI jpushService;

	@Override
	public Map<String, Object> newReminder(int orderId, String remindDesc) {
		List<OrderReminderEntity> list = orderReminderDao.queryListByOrderId(orderId, 0, 1);
		Map<String, Object> hm = Maps.newHashMap();
		if (list != null && list.size() > 0) {
			OrderReminderEntity entity = list.get(0);
			if (StringUtils.isBlank(entity.getResolver())) {
				// 有未处理的，表示不可以新建催单
				hm.put("success", "false");
				hm.put("msg", "不可以新建催单, 还有未处理的催单");
				return hm;
			}
		}
		// 表示可以新建催单
		OrderReminderEntity entity = new OrderReminderEntity();
		entity.setCreateTime(DateUtils.getSeconds());
		entity.setOrderId(orderId);
		entity.setRemindDesc(remindDesc);
		orderReminderDao.save(entity);
		hm.put("success", "true");
		hm.put("msg", "新建催单成功");
		OrderEntity order = orderService.getEntity(OrderEntity.class, orderId);
		Map<String, String> pushMap = new HashMap<String, String>();
		pushMap.put("orderId", orderId+"");
		String title = "您有一条新的催单";
		pushMap.put("title", title);
		pushMap.put("content", title);
		pushMap.put("voiceFile", SoundFile.SOUND_URGENT_ORDER);
		if(order.getCourierId().equals(0)){ // 提醒商家
			jpushService.push(order.getMerchant().getId(), pushMap);
		} else {
			jpushService.push(order.getCourierId(), pushMap);
		}
		return hm;
	}

	@Override
	public Map<String, Object> resolverReminder(int orderId, String resolver, String resolverDesc) {
		List<OrderReminderEntity> list = orderReminderDao.queryListByOrderId(orderId, 0, 10);
		Map<String, Object> hm = Maps.newHashMap();
		if (list != null && list.size() > 0) {
			OrderReminderEntity entity = list.get(0);
			if (StringUtils.isBlank(entity.getResolver())) {
				// 更新催单
				entity.setResolver(resolver);
				entity.setResolverDesc(resolverDesc);
				entity.setResolverTime(DateUtils.getSeconds());
				orderReminderDao.saveOrUpdate(entity);
				hm.put("success", "true");
				hm.put("msg", "更新催单成功");
				return hm;
			}
		}
		hm.put("success", "false");
		hm.put("msg", "没有未处理的催单");
		return hm;
	}

	@Override
	public List<Map<String, Object>> queryByUserIdUserTypeAndMinute(
			Integer userId, Integer userType, Integer minuteBefore, int page,
			int rows) {
		return orderReminderDao.queryByUserIdUserTypeAndMinute(userId,
				userType, minuteBefore, page, rows);
	}

	@Override
	public List<Map<String, Object>> queryByUserIdUserTypeAndDateTime(
			Integer userId, Integer userType, String dtStart, String dtEnd,
			int page, int rows) {
		return orderReminderDao.queryByUserIdUserTypeAndDateTime(userId,
				userType, dtStart, dtEnd, page, rows);
	}
}