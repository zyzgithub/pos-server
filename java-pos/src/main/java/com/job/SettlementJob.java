package com.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.service.orderincome.OrderIncomeServiceI;

/**
 * 定时任务-商家结算
 * @author Simon
 */
public class SettlementJob extends QuartzJobBean {
	
	private static final Logger logger = Logger.getLogger(SettlementJob.class);

	@Autowired
	OrderIncomeServiceI orderIncomeService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-商家结算");
		
		String sql="select id from order_income o_i where o_i.pay_time<unix_timestamp(now()) and o_i.state='unpay'";
		List<Map<String, Object>> orderIcomeIdMap = orderIncomeService.findForJdbc(sql);
		if(orderIcomeIdMap != null && orderIcomeIdMap.size()>0){
			Map<String, Object> m = new HashMap<String, Object>();
			for (int i = 0; i < orderIcomeIdMap.size(); i++) {
				m = orderIcomeIdMap.get(i);
				Integer orderIncomeId = Integer.valueOf(m.get("id").toString());
				OrderIncomeEntity orderIncome = orderIncomeService.get(OrderIncomeEntity.class, orderIncomeId);
				if(orderIncome!=null){
					Integer orderId = orderIncome.getOrderId();
					OrderEntity order = orderIncomeService.get(OrderEntity.class, orderId);
					if(order!=null){
						MerchantEntity merchant = order.getMerchant();
						if(merchant!=null){
							MerchantInfoEntity merchantInfo = orderIncomeService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchant.getId());
							if(merchantInfo!=null){
								//排除合作商商家订单和闪购订单
								if(merchantInfo.getPlatformType()!=2 && order.getFlashOrderId().equals(-1)){//1=平台商家，2=合作商商家
									
									try {
										//防止错误订单进入结算
										if("confirm".equals(order.getState()) && "normal".equals(order.getRstate())){
											orderIncomeService.unOrderIncome(Integer.parseInt(m.get("id").toString()));
										}else{
											logger.error("订单orderId:["+orderId+"]状态错误不能结算 state:["+order.getState()+"]===rstate:["+order.getRstate()+"]");
										}
									} catch (Exception e) {
										logger.error("商家orderIncomeId【" + m.get("id") + "】结算失败！", e);
										e.printStackTrace();
									}
								}
							}else{
								logger.error("商家merchantId【" + merchant.getId() + "】结算失败！找不到merchantInfo信息");
							}
						}else{
							logger.error("商家orderId【" + order.getId() + "】结算失败！找不到merchant信息");
						}
					}else{
						logger.error("商家orderId【" + orderIncome.getOrderId() + "】结算失败！找不到order信息");
					}
				}else{
					logger.error("商家orderIncomeId【" + m.get("id") + "】结算失败！找不到orderIncome信息");
				}
			}
		}
		
	}
	
}
