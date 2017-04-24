package com.wm.service.impl.statistics;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.statistics.MerchatStatisticsRealtimeEntity;
import com.wm.service.deduct.DeductServiceI;
import com.wm.service.statistics.MerchatStatisticsRealtimeServiceI;

@Service("merchatStatisticsRealtimeService")
@Transactional
public class MerchatStatisticsRealtimeServiceImpl extends CommonServiceImpl implements MerchatStatisticsRealtimeServiceI {

	@Autowired
	private DeductServiceI deductService;

	@Override
	public void createOrUpdateMerchantComment(Integer merchantId, Integer commentScore) {
		MerchatStatisticsRealtimeEntity en = this.findUniqueByProperty(MerchatStatisticsRealtimeEntity.class, "merchantId", merchantId);
		if( en == null){
			en = new MerchatStatisticsRealtimeEntity();
			en.setMerchantId(merchantId);
		}
		en.setTotalComment(en.getTotalComment() + 1);//每次加1
		en.setTotalCommentScore(en.getTotalCommentScore() + commentScore);
		en.setUpdateTime(DateUtils.getSeconds());
		this.saveOrUpdate(en);
	}
	
	public void statisticsRealtime(OrderEntity order) {
		Integer totalOrder;
		Integer merchantId = order.getMerchant().getId();
		MerchatStatisticsRealtimeEntity msre = this.findUniqueByProperty(MerchatStatisticsRealtimeEntity.class, "merchantId", merchantId);
		if(msre == null){
			msre = new MerchatStatisticsRealtimeEntity();
			msre.setMerchantId(merchantId);
		}
		totalOrder = msre.getTotalOrder() + 1;
		msre.setTotalOrder(totalOrder);
		Double totalSaledMoney = msre.getTotalSaledMoney();
		totalSaledMoney += order.getOrigin();
		msre.setTotalSaledMoney(totalSaledMoney);
		Long menus = deductService.getQuantityByOrder(order.getId(), false);
		msre.setTotalSaledQuantity(msre.getTotalSaledQuantity() + menus.intValue());
		msre.setUpdateTime(DateUtils.getSeconds());
		this.saveOrUpdate(msre);
	}

}