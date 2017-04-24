package com.wm.service.impl.statistics;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.statistics.CourierStatisticsRealtimeEntity;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.service.deduct.DeductServiceI;
import com.wm.service.statistics.CourierStatisticsRealtimeServiceI;

@Service("courierStatisticsRealtimeService")
@Transactional
public class CourierStatisticsRealtimeServiceImpl extends CommonServiceImpl implements CourierStatisticsRealtimeServiceI {

	@Autowired
	private DeductServiceI deductService;
	
	@Autowired
	private DeductLogServiceI deductLogService;
	
	@Override
	public void createOrUpdateCourierComment(Integer courierId, Integer totalCommentScore) {
		CourierStatisticsRealtimeEntity en = this.findUniqueByProperty(CourierStatisticsRealtimeEntity.class, "courierId", courierId);
		if( en == null){
			en = new CourierStatisticsRealtimeEntity();
			en.setCourierId(courierId);
		}
		en.setTotalComment(en.getTotalComment() + 1);//每次加1
		en.setTotalCommentScore(en.getTotalCommentScore() + totalCommentScore);
		en.setUpdateTime(DateUtils.getSeconds());
		this.saveOrUpdate(en);
	}
	
	public void statisticsRealtime(OrderEntity order) {
		/*CourierStatisticsRealtimeEntity csre = this.findUniqueByProperty(CourierStatisticsRealtimeEntity.class, "courierId", order.getCourierId());
		if(csre == null){
			csre = new CourierStatisticsRealtimeEntity();
			csre.setCourierId(order.getCourierId());
		}
		Integer totalOrder = csre.getTotalOrder() + 1;
		csre.setTotalOrder(totalOrder);//退单的时候要减去
		Long menus = deductService.getQuantityByOrder(order.getId(), true);
		List<Map<String, Object>> deductRule = deductService.getCourierDeductRule(order.getCourierId());
		Double totalDeduct = deductLogService.getDeductByRule(deductRule, menus.intValue());
		csre.setTotalDeduct(csre.getTotalDeduct() + totalDeduct);
		csre.setUpdateTime(DateUtils.getSeconds());
		this.saveOrUpdate(csre);*/
		return ;
	}

}