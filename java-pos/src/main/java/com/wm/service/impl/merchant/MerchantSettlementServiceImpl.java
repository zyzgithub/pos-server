package com.wm.service.impl.merchant;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantSettlementEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.MerchantSettlementServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.service.withdrawals.WithdrawalsServiceI;

@Service("merchantSettlementService")
@Transactional
public class MerchantSettlementServiceImpl extends CommonServiceImpl implements MerchantSettlementServiceI {
	
	private static final Logger logger = Logger.getLogger(MerchantSettlementServiceImpl.class);
	
	@Autowired
	OrderServiceI orderService;
	@Autowired
	WUserServiceI wUserService;
	@Autowired
	MerchantServiceI merchantService;
	@Autowired
	WithdrawalsServiceI withdrawalsService;
	
	public void settleDayly(Integer merchantId){
		MerchantEntity merchant = merchantService.get(MerchantEntity.class, merchantId);
		if(merchant == null){
			logger.error("未找到商家【" + merchantId + "】信息");
			return ;
		}
		DateTime yestoday = new DateTime().minusDays(1);
		MerchantSettlementEntity settle = new MerchantSettlementEntity();
		settle.setMerchantId(merchantId);
		// 统计昨天商家的所有订单总金额
		String sql = "select sum(o.origin) sum from `order` o ";
		sql += " where o.merchant_id=? ";
		sql += " and o.state='confirm' ";
		sql += " and date(from_unixtime(o.complete_time))=? ";
		Map<String, Object> originMap = orderService.findOneForJdbc(sql, new Object[]{merchantId, yestoday.toString("yyyy-MM-dd")});
		Double origin = 0.0;
		if(originMap != null){
			Object o = originMap.get("sum");
			if(o != null){
				origin = Double.parseDouble(o.toString());
			}
		}
		settle.setDaylyOrderIncome(origin);
		
		// 统计昨天商家的预收入总金额
		int deduct = 100;
		if(merchant.getDeduction() != null) {
			deduct = (int)(Math.rint(100*(1 - merchant.getDeduction()))); // 平台扣点
		}
		Double preOrderIncome = ((int)(Math.rint(((int)100*origin)*deduct/100)))/100.0; // 保留2位小数,四舍五入
		settle.setDaylyPreIncome(preOrderIncome);
		
		// 计算当前商家余额
		sql = "select * from 0085_merchant_settlement_dayly where merchant_id=? and date(from_unixtime(update_date))=? ";
		WUserEntity user = merchant.getWuser();
		if(null == user){
			logger.error("未找到商家【" + merchantId + "】对应的用户信息");
			return ;
		}
		Integer delayDays = merchant.getIncomeDate(); // 预收入兑现天数
		if(delayDays == null){
			delayDays = 0;
		}
		if(delayDays == 0){
			settle.setDaylyBalance(preOrderIncome);
		} else if (delayDays == 1){
			List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{merchantId, yestoday.minusDays(1).toString("yyyy-MM-dd")});
			if(list != null && list.size() > 0){
				Map<String, Object> balanceMap = list.get(0);
				int income = (int)(Math.rint(100*Double.parseDouble(balanceMap.get("dayly_pre_income").toString())));
				int balance = (int)(Math.rint(100*Double.parseDouble(balanceMap.get("dayly_balance").toString())));
				settle.setDaylyBalance((income + balance)/100.0);
			} else {
				// 直接从商家的余额复制过来
				settle.setDaylyBalance(user.getMoney());
			}
		} else {
			List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{merchantId, yestoday.minusDays(delayDays).toString("yyyy-MM-dd")});
			if(list != null && list.size() > 0){
				Map<String, Object> incomeMap = list.get(0);
				int income = (int)(Math.rint(100*Double.parseDouble(incomeMap.get("dayly_pre_income").toString())));
				int balance = 0;
				list = this.findForJdbc(sql, new Object[]{merchantId, yestoday.minusDays(1).toString("yyyy-MM-dd")});
				if(list != null && list.size() > 0){
					Map<String, Object> balanceMap = list.get(0);
					balance = (int)(Math.rint(100*Double.parseDouble(balanceMap.get("dayly_balance").toString())));
				} else {
					// 不应该会出现
					balance = (int)(Math.rint(100*user.getMoney()));
				}
				settle.setDaylyBalance((income + balance)/100.0);
			} else {
				settle.setDaylyBalance(user.getMoney());
			}
		}
		
		// 查询商家当天提现记录
		sql = "select * from withdrawals where state='done' and user_type='merchant' and user_id=? and date(from_unixtime(submit_time))=?";
		Double withdrawMoney = 0.0; // 提款申请成功
		List<Map<String, Object>> withdrawList = withdrawalsService.findForJdbc(sql, new Object[]{user.getId(), yestoday.toString("yyyy-MM-dd")});
		if(withdrawList != null && withdrawList.size() > 0){
			for(Map<String, Object> map : withdrawList){
				withdrawMoney += Double.parseDouble(map.get("money").toString());
			}
		}
		settle.setDaylyBalance(settle.getDaylyBalance() - withdrawMoney);
		sql = "select * from withdrawals where state='cancel' and user_type='merchant' and user_id=? and date(from_unixtime(complete_time))=?";
		withdrawMoney = 0.0; // 提款申请失败
		withdrawList = withdrawalsService.findForJdbc(sql, new Object[]{user.getId(), yestoday.toString("yyyy-MM-dd")});
		if(withdrawList != null && withdrawList.size() > 0){
			for(Map<String, Object> map : withdrawList){
				withdrawMoney += Double.parseDouble(map.get("money").toString());
			}
		}
		settle.setDaylyBalance(settle.getDaylyBalance() + withdrawMoney);
		
		settle.setUpdateDate((int)(yestoday.getMillis()/1000));
		this.save(settle);
	}
	
}