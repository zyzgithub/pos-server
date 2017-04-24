package com.wm.service.impl.withdrawals;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.life.bank.industrial.LifePaymentFlow;
import com.life.bank.industrial.notify.OnlineWithdrawalCallback;
import com.life.commons.CommonUtils;
import com.sun.star.uno.RuntimeException;
import com.wm.entity.flow.FlowEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.entity.withdrawals.WithdrawalsEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.service.withdrawals.WithdrawalsServiceI;

import jeecg.system.listener.InitListener;

@Service("onlineWithdrawl")
@Transactional
public class OnlineWithdrawlImpl implements OnlineWithdrawalCallback {
	
	private static final Logger logger = LoggerFactory.getLogger(OnlineWithdrawlImpl.class);

	@Override
	public void success(LifePaymentFlow lifePaymentFlow) {
		addWithdrawl(lifePaymentFlow, "done");
	}

	@Override
	public void fail(LifePaymentFlow lifePaymentFlow) {
		addWithdrawl(lifePaymentFlow, "apply");
	}
	
	@SuppressWarnings("deprecation")
	private void addWithdrawl(LifePaymentFlow lifePaymentFlow, String applyState) {
		WebApplicationContext applicationContext = InitListener.getApplicationContext();
		try {
			WUserServiceI wuserService = (WUserServiceI)applicationContext.getBean("wUserService");
			
			long userId = lifePaymentFlow.getUserId();
			WUserEntity wuser = wuserService.get(WUserEntity.class, (int) userId);
			Double money = lifePaymentFlow.getTransAmount() / 100d;
			logger.info("userId:{}, lifePayId:{}, money:{}", userId, lifePaymentFlow.getId(), money);
			// 提现申请表
			WithdrawalsEntity withdrawals = new WithdrawalsEntity();
			withdrawals.setWuser(wuser);
			withdrawals.setUserType(wuser.getUserType());
			withdrawals.setMoney(money);
			withdrawals.setSubmitTime(DateUtils.getSeconds());
			withdrawals.setState(applyState);
			if ("done".equals(applyState)) {
				withdrawals.setCompleteTime(DateUtils.getSeconds());
			}
			withdrawals.setBeforeMoney(wuser.getMoney() + money);
			withdrawals.setAfterMoney(wuser.getMoney());
			withdrawals.setBankcardId(lifePaymentFlow.getBankcardId());
			
			int takeMode = 2;
			TakeCashRule takeCashRule = getTakeCashRule(0, takeMode);
			
			long time = takeCashRule.getHolidayDelay() == 1 ? CommonUtils.nextWorkingDay(System.currentTimeMillis()) : System.currentTimeMillis();
			// 期望到账时间
			long expectArrivalTime = time + TimeUnit.HOURS.toMillis(takeCashRule.getDelayHour());
			expectArrivalTime = (takeCashRule.getHolidayDelay() == 1) ? CommonUtils.nextStepsWorkingDay(expectArrivalTime, 2) : expectArrivalTime;
			
			withdrawals.setTakeMode(takeMode);
			withdrawals.setExpectArrivalTime(new Date(expectArrivalTime));
			
			String defineParameter = lifePaymentFlow.getDefineParameter();
			
			JSONObject bankInfo = new JSONObject();
			bankInfo.put("account_name", lifePaymentFlow.getAccountName());
			bankInfo.put("bank_name", defineParameter.split(",")[0]);
			bankInfo.put("bank_number", lifePaymentFlow.getAccountNumber());
			bankInfo.put("bank_code", lifePaymentFlow.getBankCode());
			withdrawals.setBankInfo(bankInfo.toString());
			
			WithdrawalsServiceI withdrawalsService = (WithdrawalsServiceI)applicationContext.getBean("withdrawalsService");
			withdrawalsService.save(withdrawals);
			
			String flowId = defineParameter.split(",")[1];
			if(StringUtils.isNotEmpty(flowId) && !"0".equals(flowId)){
				Integer withId = withdrawals.getId();
				logger.info("关联提现申请ID:{} 到流水表flowId:{}", withId, flowId);
				FlowServiceI flowService = (FlowServiceI)applicationContext.getBean("flowService");
				FlowEntity flow = flowService.getEntity(FlowEntity.class, Integer.parseInt(flowId));
				flow.setDetailId(withId);
				flowService.updateEntitie(flow);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("addWithdrawl failed !!! lifePaymentFlow:{}", lifePaymentFlow.getId());
		}
	}
	
	private TakeCashRule getTakeCashRule(int targetType, int takeMode) {
		WebApplicationContext applicationContext = InitListener.getApplicationContext();
		try {
			WUserServiceI wuserService = (WUserServiceI) applicationContext.getBean("wUserService");
			String sql = "select id, take_mode, take_desc, target_type, low_cost, high_cost, rate, single_amount_limit, day_amount_limit, day_count_limit, holiday_delay, delay_hour, rule_effect_time, rule_invalid_time, show_image, default_option"
					+ " from take_rule where take_mode = " + takeMode + " and target_type = " + targetType;

			List<Map<String, Object>> rules = wuserService.findForJdbc(sql);
			if (rules == null || rules.isEmpty()) {
				return null;
			}
			return new TakeCashRule(rules.get(0));
		} catch (Exception ex) {
			throw new RuntimeException("");
		}
	}
}