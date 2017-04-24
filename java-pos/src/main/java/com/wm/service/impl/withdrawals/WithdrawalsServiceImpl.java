package com.wm.service.impl.withdrawals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.life.bank.industrial.LifeBankService;
import com.life.commons.CommonUtils;
import com.life.ucf.UCFService;
import com.sun.star.uno.RuntimeException;
import com.wm.controller.takeout.vo.BankCardVo;
import com.wm.entity.bank.BankcardEntity;
import com.wm.entity.flow.FlowEntity;
import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.entity.withdrawals.WithdrawalsEntity;
import com.wm.service.bank.BankcardServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.service.withdrawals.WithdrawalsServiceI;
import com.wm.util.IDistributedLock;
import com.wm.util.MemcachedDistributedLock;
import com.wm.util.StringUtil;

@Service("withdrawalsService")
@Transactional
public class WithdrawalsServiceImpl extends CommonServiceImpl implements WithdrawalsServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(WithdrawalsServiceImpl.class);
	
	private static final String getLatestWithdraw = "select * from withdrawals where user_id = ? and state<>'cancel' order by submit_time desc";
	private static final String findLast2Withdraw = "select w.* from withdrawals w left join withdrawals w1 on w1.user_id=w.user_id where w1.id=? "
			+ " and w.state<>'cancel' and w.submit_time<=w1.submit_time order by w.submit_time desc limit 2";
	
	private static final String getMulAccount = "select count(0) from 0085_merchant_multiaccount mm "
		+ " where EXISTS (SELECT * from merchant where id=mm.mainstore_id and user_id=?)"
		+ " and EXISTS (SELECT * from merchant where id=mm.branchstore_id and user_id=?)";
	
	private static final Double RATE = 0.45;
	
	@Autowired
	private BankcardServiceI bankcardService;
	@Autowired
	private LifeBankService lifeBankService;
	@Autowired
	private UCFService ucfService;
	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private WUserServiceI userService;
	

	@Override
	public List<WithdrawalsEntity> findWithdrLog(Integer userId) {
		return this.findHql("select w from WithdrawalsEntity w left join w.wuser u where u.id=? ", new Object[]{userId});
	}
	
	@Override
	public List<Map<String, Object>> queryLatest(Integer userId,int page, int rows) {
		return this.findForJdbcParam(getLatestWithdraw, page, rows, userId);
	}

	@Override
	public void saveWithdrawals(WUserEntity wuser, WithdrawalsEntity withdrawals,FlowEntity flow) {
		this.saveOrUpdate(wuser);
		this.save(withdrawals);
		this.save(flow);
	}
	
	public AjaxJson oldWithDrawApply(WUserEntity wuser, String money){
		AjaxJson j = new AjaxJson();
		WithdrawalsEntity withdrawals = new WithdrawalsEntity();
		withdrawals.setWuser(wuser);
		List<BankCardVo> bankcards = bankcardService.queryByDefault(wuser.getId(), "Y", 1, 5);
		if (bankcards != null && !bankcards.isEmpty()) {
			BankCardVo bankcard = bankcards.get(0);
			if (bankcard != null) {
				withdrawals.setBankcardId(bankcard.getBankId());
			} else {
				j.setSuccess(false);
				j.setMsg("抱歉，该用户没有绑定银行卡，无法提现");
				j.setStateCode("01");
				return j;
			}
		} else {
			j.setSuccess(false);
			j.setMsg("抱歉，该用户没有绑定银行卡，无法提现");
			j.setStateCode("01");
			return j;
		}
		withdrawals.setUserType(wuser.getUserType());
		withdrawals.setMoney(Double.parseDouble(money));
		withdrawals.setSubmitTime(DateUtils.getSeconds());
		withdrawals.setState("apply");
		withdrawals.setBeforeMoney(wuser.getMoney());
		withdrawals.setAfterMoney(wuser.getMoney() - Double.parseDouble(money));
		FlowEntity flow = new FlowEntity();
		flow.setAction("withdraw");
		flow.setCreateTime(DateUtils.getSeconds());
		flow.setDetail(wuser.getUsername() + "申请提现");
		flow.setMoney(Double.parseDouble(money));
		flow.setType("pay");
		flow.setUserId(wuser.getId());
		flow.setPreMoney(wuser.getMoney());
		flow.setPostMoney(wuser.getMoney() - Double.parseDouble(money));
		wuser.setMoney(wuser.getMoney()- Double.parseDouble(money));
		saveWithdrawals(wuser, withdrawals, flow);
		j.setSuccess(true);
		j.setMsg("申请提现成功");
		j.setStateCode("00");
		return j;
	}
	
	@Override
	public AjaxJson takeCashRuleLoader(int userId) {
		WUserEntity user = userService.getEntity(WUserEntity.class, userId);
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}
		int targetType = 0;
		if ("merchant".equals(user.getUserType())) {
			// 获取商家信息
			MerchantEntity merchant = findUniqueByProperty(MerchantEntity.class, "wuser.id", user.getId());
			if (merchant.getCategory().getId() == 1013) {
				targetType = 1;
			}
		}
		
		// 加载提现规则
		String sql = "select take_mode, take_desc name, low_cost, high_cost, rate, show_image, default_option from take_rule where target_type = " + targetType;
		List<Map<String, Object>> rules = findForJdbc(sql);
		
		JSONArray array = new JSONArray();
		for (Map<String, Object> map : rules) {
			array.add(map);
		}
		return success(array.toString());
	}
	
	@Override
	public AjaxJson beforeTakeCash(int userId, int takeMode) {
		WUserEntity user = userService.getEntity(WUserEntity.class, userId);
		if (user == null) {
			return fail("用户不存在","");
		}
		TakeCashRule takeCashRule = getTakeCashRule(null, user, takeMode);
		// price[0] -- 为今次可提现的最大额度     price[1] -- 为今次要扣取的服务费   price[2] -- 费率   price[3] -- 冻结额度
		double[] price = calculationCommissionAmount(userId, user.getMoney(), takeCashRule);
		if (user.getMoney() < (price[1] + price[3])) {
			return fail("您的余额不足支付本次提现的服务费，请选择其他到账方式", "");
		}
		if (price[0] == user.getMoney()) {
			// 如果可提现的最大额度为用户余额 那么重设可提现的最大额度
			price[0] = user.getMoney() - price[1] - price[3];
		}
		int remainTakeCashCount = remainTakeCashCount(userId, takeCashRule);

		JSONObject response = new JSONObject();
		response.put("rateValue", "提现服务费率" + CommonUtils.unRounding((price[2] * 100)) + "%");
		response.put("maxTakeCash", "本次最多可提现：" + CommonUtils.unRounding(price[0]) + "元");
		response.put("maxTakeCashValue", CommonUtils.unRounding(price[0]));
		response.put("remainTakeCashCount", remainTakeCashCount);
		if (remainTakeCashCount <= 0) {
			response.put("tip", "今天剩余提现次数为0次，请选择其他到账方式");
			response.put("tipMode", 1);
		} else {
			if (takeCashRule.getDayCountLimit() == 99999) {
				response.put("tip", "当前到账方式会产生额外服务费扣点，是否确认提现？");
			} else {
				response.put("tip", "今天剩余提现次数为" + remainTakeCashCount + "次，是否确认提现？");
			}
			response.put("tipMode", 2);
		}
		return success(response.toString());
	}
	
	private final static int EXPIRE = 60;
	private final static int TRY_TIME = 30;
	
	@Override
	public double tryTakeCash(int userId, double takeAmount, Integer bindCardId, int takeMode) {
		WUserEntity user = userService.getEntity(WUserEntity.class, userId);
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}
		
		// 检查用户是否在黑名单中
		String sql = "SELECT descr from 0085_user_withdraw_blacklist where user_id=? and status=0";
		List<Map<String, Object>> blackList = this.findForJdbc(sql, userId);
		if(!CollectionUtils.isEmpty(blackList)){
			throw new RuntimeException(blackList.get(0).get("descr").toString());
		}

		// 检查用户是否是分店
		MerchantEntity merchantEntity = this.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId);
		if (merchantEntity != null) {
			String sql_multiaccount = "select * from 0085_merchant_multiaccount where branchstore_id = ?  limit 1";
			List<Map<String, Object>> multAcc = this.findForJdbc(sql_multiaccount, merchantEntity.getId());
			if (!CollectionUtils.isEmpty(multAcc)) {
				throw new RuntimeException("分店不能直接提现");
			}
		}
		
		String lockName = "money_user_" + userId;
		
		IDistributedLock lock = new MemcachedDistributedLock();
		String uuid = null;
		try {
			uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
			if (uuid == null) {
				logger.error("同时存在其它进程在对该用户的余额进行更新， 无法获取分布式锁，用户：", user.getUsername() + "(" + user.getId() + ")");
				throw new RuntimeException("系统繁忙");
			}
			return securityTakeCash(user, takeAmount, bindCardId, takeMode);
		} finally {
			if (uuid != null) {
				lock.releaseLock(lockName, uuid);
			}
		}
	}
	
	@Override
	public int remainTakeCashCount(int userId, int takeMode) {
		WUserEntity user = userService.getEntity(WUserEntity.class, userId);
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}
		TakeCashRule takeCashRule = getTakeCashRule(null, user, takeMode);
		return remainTakeCashCount(userId, takeCashRule);
	}
	
	private int remainTakeCashCount(int userId, TakeCashRule takeCashRule) {
		if (takeCashRule == null) {
			return 0;
		}
		// 获取该用户今天的提现数据 只控制同种提现规则 String[0] -- 提现次数 String[1] -- 提现额度
		String[] todayTakeCashRecord = todayTakeCashCount(userId, takeCashRule.getTakeMode());
		return takeCashRule.getDayCountLimit() - Integer.parseInt(todayTakeCashRecord[0]);
	}
	
	private double securityTakeCash(WUserEntity user, double takeAmount, Integer bindCardId, int takeMode) {
		if (takeAmount <= 0) {
			// 提现金额异常
			throw new RuntimeException("提现金额必须大于0");			
		}
		if (takeAmount > user.getMoney()) {
			// 余额不足
			throw new RuntimeException("您的余额不足");
		}
		BankCardVo bankCard = new BankCardVo();
		if (StringUtil.isEmpty(bindCardId) || bindCardId.intValue() == 0) {
			// 获取默认的银行卡
			bankCard = bankcardService.getDefaultBankCard(user.getId());
			if (bankCard == null) {
				throw new RuntimeException("抱歉，该用户没有绑定银行卡，无法提现");
			}
		} else {
			BankcardEntity card = get(BankcardEntity.class, bindCardId);
			if (card == null) {
				throw new RuntimeException("请检查银行卡是否有效");
			}
			if (card.getWuser().getId() != user.getId()) {
				throw new RuntimeException("请不要提取他人的余额");
			}
			bankCard.setBankId(bindCardId);
			bankCard.setUserId(card.getWuser().getId());
			bankCard.setCardNo(card.getCardNo());
			bankCard.setName(card.getName());
			bankCard.setSourceBank(card.getSourceBank());
			
			bankCard.setBankName(card.getBank().getName());
			bankCard.setBankCode(card.getBank().getBankCode());
		}
		// 原来的额度
		double beforeAmount = user.getMoney();

		// 系统分切,终止代理商提现服务
		MerchantInfoEntity infoEntity = findUniqueByProperty(MerchantInfoEntity.class, "merchantId", user.getId());
		if(infoEntity != null && infoEntity.getPlatformType() == 2){
			throw new RuntimeException("服务已停止!");
		}

		// 提现流水 同时返回流水ID
		long primary = takeCashFlow(user.getId(), 0, ("提现￥" + String.valueOf(takeAmount) + "元"), "withdraw", String.valueOf(takeAmount), String.valueOf(beforeAmount),
				String.valueOf((beforeAmount - takeAmount)));
		// 获取商家信息
		MerchantEntity merchantEntity = findUniqueByProperty(MerchantEntity.class, "wuser.id", user.getId());
//
//		if (infoEntity.getPlatformType() == 2 && takeAmount < 100) {
//			throw new RuntimeException("提现金额必须大于100元");
//		}
		boolean isOnlinePayment = false;
		// 直接线上打款 失败时执行线下打款方式
		if (merchantEntity != null && onlinePayment(merchantEntity, takeAmount)) {
			if (takeAmount < 100) {
				throw new RuntimeException("提现金额必须大于100元");
			}
			if (!CommonUtils.isPassTime()) { // 放通时段允许提现，否则需要判断是否在节假日
				// if (CommonUtils.isWeekend() || CommonUtils.currentHours() < 10 || CommonUtils.isSpecialTime()) {
				// throw new RuntimeException("10点前或节假日不能进行提现操作");
				// }
				// if (CommonUtils.currentHours() < 12) {
				// throw new RuntimeException("因银行系统升级导致12点后方可进行提现操作，如时间提前将另行通知");
				// }
				if (CommonUtils.isWeekend() || CommonUtils.currentHours() < 12 || CommonUtils.isSpecialTime()) {
					throw new RuntimeException("请于工作日12:00-24:00发起提现操作");
				}
			}
			// 更新余额
			boolean result = modifyBalance(user.getId(), beforeAmount, -(takeAmount));
			if (!result) {
				throw new RuntimeException("提现失败，请确认您的帐号是否在其他手机同时执行提现");
			}
			if (isWhiteList(user.getId().longValue())) {
				AjaxJson response = ucfService.executorOnlinePayment(user.getId(), user.getUsername(), bankCard.getBankId(), bankCard.getCardNo(), bankCard.getName(), String.valueOf(takeAmount),
						merchantEntity.getTitle(), bankCard.getBankName() + "," + String.valueOf(primary));
				// 执行线上提现操作
				if (response.isSuccess()) {
					return 0.0;
				}
			}
			isOnlinePayment = true;
		}
		TakeCashRule takeCashRule = getTakeCashRule(merchantEntity, user, takeMode);
		// 计算费用 主要获取服务费
		double[] price = calculationCommissionAmount(user.getId(), takeAmount, takeCashRule);
		int holidayDelay = takeCashRule.getHolidayDelay();
		int currentHours = CommonUtils.currentHours();
		if (takeCashRule.getTakeMode() == 0 && (currentHours < CommonUtils.currentHours(takeCashRule.getRuleEffectTime()) || currentHours >= CommonUtils.currentHours(takeCashRule.getRuleInvalidTime()))) {
			// 超过有效时间
			throw new RuntimeException("当前时间不能进行提现");
		}
		long startTime = System.currentTimeMillis();
		long time = holidayDelay == 1 ? CommonUtils.nextWorkingDay(startTime) : startTime;
		// 期望到账时间
		long expectArrivalTime = time + TimeUnit.HOURS.toMillis(takeCashRule.getDelayHour());
		expectArrivalTime = holidayDelay == 1 ? CommonUtils.nextStepsWorkingDay(expectArrivalTime, 2) : expectArrivalTime;
		// 获取该用户今天的提现数据 只控制同种提现规则 String[0] -- 提现次数  String[1] -- 提现额度
		String[] todayTakeCashRecord = todayTakeCashCount(user.getId(), takeMode);
		if (Integer.parseInt(todayTakeCashRecord[0]) >= takeCashRule.getDayCountLimit()) {
			throw new RuntimeException("该提现类型每天只能执行" + takeCashRule.getDayCountLimit() + "次");
		}
		if (takeAmount > takeCashRule.getSingleAmountLimit()) {
			String msg = "提现金额不能超过：" + takeCashRule.getSingleAmountLimit() + "元";
			logger.warn(msg);
			throw new RuntimeException(msg);
		}
		if ((Double.parseDouble(todayTakeCashRecord[1]) + takeAmount) > takeCashRule.getDayAmountLimit()) {
			throw new RuntimeException("您今天的提现额度已超过系统的上限，每天只能提现：" + takeCashRule.getDayAmountLimit() + "元");
		}
		// 提现金额 + 服务费 + 冻结金额 是否超过余额
		if ((takeAmount + price[1]) + price[3] > beforeAmount) {
			// 余额不足
			throw new RuntimeException("剩余额度不足以支付服务费");
		}

		boolean result = true;

		logger.info("提现2次扣除前置日志 isOnlinePayment : %s ,price is %s ", isOnlinePayment + "", price[1] + "");

		if (!isOnlinePayment) {
			// 更新余额
			result = modifyBalance(user.getId(), beforeAmount, -(takeAmount + price[1]));
		} else if (isOnlinePayment && price[1] > 0) {
			// 如果本来是可以执行线上直接付款的 那么程序执行到此处 说明线上支付失败了 但已经扣取了用户的提现额度 此处只需再扣取手续费即可
			BigDecimal afterMoney = BigDecimal.valueOf(beforeAmount).subtract(BigDecimal.valueOf(takeAmount));
			afterMoney.setScale(4, BigDecimal.ROUND_HALF_UP);
			logger.info("如果本来是可以执行线上直接付款的 那么程序执行到此处 说明线上支付失败了 但已经扣取了用户的提现额度 此处只需再扣取手续费即可 afterMoney : " + afterMoney + ", beforeAmount : " + beforeAmount + ", takeAmount : " + takeAmount);
			result = modifyBalance(user.getId(), afterMoney.doubleValue(), -(price[1]));
		}

		if (!result) {
			throw new RuntimeException("提现失败，请确认您的帐号是否在其他手机同时执行提现.");
		}

		if (price[1] > 0) {
			// 服务费流水 -- 与提现流水关联
			takeCashFlow(user.getId(), primary, ("手续费￥" + String.valueOf(price[1]) + "元"), "takeCashCost", String.valueOf(price[1]), String.valueOf((beforeAmount - takeAmount)), 
					String.valueOf((beforeAmount - takeAmount - price[1])));
		}
		// 记录财务使用的流水
		takeCashFinanceFlow(primary, user, takeAmount, beforeAmount, (beforeAmount - takeAmount - price[1]), takeMode, bankCard.getBankId(), bankCard.getBankCode(), bankCard.getName(), bankCard.getBankName(), bankCard.getCardNo(), expectArrivalTime);
	
		return price[1];
	}
	
	private boolean modifyBalance(int userId, double remainAmount, double offsetAmount) {
		// 更新余额的语句 必须验证余额是否为本次判断的额度
		String sql = "update user set money = ? where id = ? and money = ? ";
		
		int affectRows = executeSql(sql, remainAmount + offsetAmount, userId, remainAmount);
		return affectRows > 0;
	}
	
	private long takeCashFinanceFlow(long primary, WUserEntity user, double money, double beforeMoney, double afterMoney, int takeMode, int bankId, String bankCode, String accountName, String bankName, String bankNumber, long expectArrivalTime) {
		// 提现申请表
		WithdrawalsEntity withdrawals = new WithdrawalsEntity();
		withdrawals.setWuser(user);
		withdrawals.setUserType(user.getUserType());
		withdrawals.setMoney(money);
		withdrawals.setSubmitTime((int) (System.currentTimeMillis() / 1000));
		withdrawals.setState("apply");
		withdrawals.setBeforeMoney(beforeMoney);
		withdrawals.setAfterMoney(afterMoney);
		withdrawals.setBankcardId(bankId);
		
		JSONObject bankInfo = new JSONObject();
		bankInfo.put("account_name", accountName);
		bankInfo.put("bank_name", bankName);
		bankInfo.put("bank_number", bankNumber);
		bankInfo.put("bank_code", bankCode);
		
		withdrawals.setBankInfo(bankInfo.toString());
		
		withdrawals.setTakeMode(takeMode);
		withdrawals.setExpectArrivalTime(new Date(expectArrivalTime));
		save(withdrawals);

		Integer withId = withdrawals.getId();
		logger.info("关联提现申请ID:{} 到流水表flowId:{}", withId, primary);
		FlowEntity flow = flowService.getEntity(FlowEntity.class, (int) primary);
		flow.setDetailId(withId);
		flowService.updateEntitie(flow);
		
		return withId.longValue();
	}
	
	private boolean onlinePayment(MerchantEntity merchantEntity, double takeAmount) {
		MerchantInfoEntity merchantInfoEntity = findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantEntity.getId());
		if (merchantInfoEntity == null || merchantInfoEntity.getPlatformType() != 2) {
			return false;
		}
		// throw new RuntimeException("系统升级，预计24点前恢复提现功能");
		String sql = "select value from system_config where code = 'open_payment'";
		String openPayment = findOneForJdbc(sql, String.class);
		if (!"true".equals(openPayment)) {
			return false;
		}
		sql = "select value from system_config where code = 'max_pay_amount'";
		String maxPayAmount = findOneForJdbc(sql, String.class);
		if (CommonUtils.isNullString(maxPayAmount)) {
			maxPayAmount = "50000";
		}
		if (takeAmount > Double.parseDouble(maxPayAmount)) {
			return false;
		}
		return true;
	}
	
	private long takeCashFlow(int userId, long relationshipId, String detail, String action, String takeAmount, String beforeAmount, String afterAmount) {
		String sql = "insert into flow(user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) values (?, ?, ?, ?, ?, 'pay', ?, ?, unix_timestamp(now()))";
		Long primaryKey = insertBySql(sql, userId, relationshipId, detail, Double.parseDouble(takeAmount), action, Double.parseDouble(beforeAmount), Double.parseDouble(afterAmount));
		if (primaryKey == null) {
			// 异常
			throw new RuntimeException("无法记录提现流水");
		}
		return primaryKey.longValue();
	}
	
	/**
	 * <pre>
	 * 计算佣金
	 * </pre>
	 * 
	 * @param amount 提现额度
	 * @param takeMode 提现模式
	 * 
	 * @return double[] 
	 * <pre>
	 * <b>0</b> 最多可提现的额度 
	 * <b>1</b> 收取的费用(服务费) 
	 * <b>2</b> 费率
	 * </pre>
	 */
	private double[] calculationCommissionAmount(int userId, double amount, TakeCashRule takeCashRule) {
		// 配置表中的费率为万份比
		double rate = takeCashRule.getRate() / 10000f;
		// 本次提现额度可能需要支付的手续费
		double commissionAmount = amount * rate;
		if (commissionAmount < takeCashRule.getLowCost()) {
			// 最低限制
			commissionAmount = takeCashRule.getLowCost();
		}
		if (commissionAmount > takeCashRule.getHighCost()) {
			// 最高限制
			commissionAmount = takeCashRule.getHighCost();
		}
		// 只取小数点后两位 非四舍五入
		commissionAmount = ((int) (commissionAmount * 100)) / 100f;
		double maxTakeCash = amount;
		if (maxTakeCash > takeCashRule.getSingleAmountLimit()) {
			// 最多只能提现的额度
			maxTakeCash = takeCashRule.getSingleAmountLimit();
		}
		String sql = "select sum(paymoney) m from post_buyout_log where user_id = ? and pay_state = 0 and need_pay_time <= unix_timestamp(now())";
		List<Map<String, Object>> list = findForJdbc(sql, userId);
		double freezeAmount = 0.0;
		if (list != null && !list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			
			Object o = map.get("m");
			freezeAmount = (o == null ? 0.0 : ((BigDecimal) o).doubleValue());
		}
		return new double[] { maxTakeCash, commissionAmount, rate , freezeAmount};
	}
	
	private TakeCashRule getTakeCashRule(MerchantEntity merchant, WUserEntity user, int takeMode) {
		int targetType = 0;
		if ("merchant".equals(user.getUserType())) {
			if(merchant == null) {
				// 获取商家信息
				merchant = findUniqueByProperty(MerchantEntity.class, "wuser.id", user.getId());
			}
			if (merchant.getCategory().getId() == 1013) {
				targetType = 1;
			}
		}
		String sql = "select id, take_mode, take_desc, target_type, low_cost, high_cost, rate, single_amount_limit, day_amount_limit, day_count_limit, holiday_delay, delay_hour, rule_effect_time, rule_invalid_time, show_image, default_option"
				+ " from take_rule where take_mode = " + takeMode + " and target_type = " + targetType;
		
		List<Map<String, Object>> rules = findForJdbc(sql);
		if (rules == null || rules.isEmpty()) {
			return null;
		}
		return new TakeCashRule(rules.get(0));
	}
	
	private String[] todayTakeCashCount(int userId, int takeMode) {
		String sql = "select count(id) c, sum(money) s from withdrawals where user_id = " + userId + " and take_mode = " + takeMode + " and date(from_unixtime(submit_time)) = date(now()) and state in ('apply','done')";
		List<Map<String, Object>> record = findForJdbc(sql);
		if (record == null || record.isEmpty()) {
			return new String[] { "0", "0" };
		}
		Map<String, Object> r = record.get(0);
		return new String[] { r.get("c").toString(), r.get("s") == null ?  "0" : r.get("s").toString() };
	}
	
	private AjaxJson success(String response) {
		AjaxJson success = new AjaxJson();
		success.setObj(response);
		return success;
	}
	
	private AjaxJson fail(String message, String response) {
		AjaxJson fail = new AjaxJson();
		fail.setStateCode(AjaxJson.STATE_CODE_FAIL);
		fail.setSuccess(false);
		fail.setMsg(message);
		fail.setObj(response);
		return fail;
	}
	
	/**
	 * 调用线上支付接口 返回成功时不处理 返回失败时走回原接口 
	 * @param wuser
	 * @param money
	 * @param cardId
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public AjaxJson newWithDrawApply(WUserEntity wuser, Double money, Integer cardId) throws NumberFormatException, Exception {
		tryTakeCash(wuser.getId(), money, cardId, 2);
		AjaxJson j = new AjaxJson();
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("您的提现申请已被接受，请耐心等待！");
		return j;
	}
	

	@Override
	public boolean merchantChildWithdraw(Integer merchantUserId, Integer childMerchUserId, BigDecimal money) throws Exception {
		Integer count = findOneForJdbc(getMulAccount, Integer.class, merchantUserId, childMerchUserId);
		if(count <= 0){
			logger.error("不存在子账户{}和总账户{}的关系", childMerchUserId, merchantUserId);
			return false;
		}
		logger.info("子账户{}的转到总账户{}余额共:{}", childMerchUserId, merchantUserId, money);
		flowService.branchStoreAccountOut(childMerchUserId, money.setScale(2, RoundingMode.HALF_UP)); //分店转出余额并插入流水
		flowService.mainStoreAccountIn(merchantUserId, money.setScale(2, RoundingMode.HALF_UP));  //主店转入余额并插入流水
		return true;
	}
	
	public AjaxJson agentWithdrawApply(AgentInfoEntity agent, WUserEntity wuser, Double money, Integer cardId) throws Exception{
		AjaxJson j = validateAgent(agent, money, cardId);
		if (!j.isSuccess()) {
			return j;
		}
		
		BigDecimal beforeMoney = new BigDecimal(agent.getIncome()).divide(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_DOWN);
		BigDecimal afterMoney = beforeMoney.subtract(new BigDecimal(money)).setScale(4, BigDecimal.ROUND_DOWN);
		// 提现申请表
		WithdrawalsEntity withdrawals = new WithdrawalsEntity();
		withdrawals.setWuser(wuser);
		withdrawals.setUserType(wuser.getUserType());
		withdrawals.setMoney(money);
		withdrawals.setSubmitTime(DateUtils.getSeconds());
		withdrawals.setState("apply");
		withdrawals.setBeforeMoney(beforeMoney.doubleValue());
		withdrawals.setAfterMoney(afterMoney.doubleValue());
		
		BankCardVo bankcard = new BankCardVo();
		if(StringUtil.isEmpty(cardId)){
			List<BankCardVo> bankcards = bankcardService.queryByDefault(wuser.getId(), "Y", 1, 5);
			if(bankcards == null || bankcards.isEmpty()) {
				j.setSuccess(false);
				j.setMsg("抱歉，该用户没有绑定银行卡，无法提现");
				j.setStateCode("01");
				return j;
			}
			bankcard = bankcards.get(0);
		} else {
			BankcardEntity card = this.get(BankcardEntity.class, cardId);
			if (card == null) {
				j.setStateCode("01");
				j.setMsg("请检查银行卡是否有效");
				j.setSuccess(false);
				return j;
			}
			if(card.getWuser().getId()!=wuser.getId()){
				logger.info("用户userId:{}所持银行卡cardId:{}非本人绑定", new Object[]{wuser.getId(), cardId}); 
				j.setStateCode("01");
				j.setMsg("请检查所绑银行卡是否正确");
				j.setSuccess(false);
				return j;
			}
			bankcard.setBankId(cardId);
			bankcard.setCardNo(card.getCardNo());
			bankcard.setName(card.getName());
		}
		withdrawals.setBankcardId(bankcard.getBankId());
		this.save(withdrawals);
		// 记录流水
		flowService.agentWithdraw(wuser.getId(), money);
		String sql = "select value from system_config where code = 'max_pay_amount'";
		String maxPayAmount = findOneForJdbc(sql, String.class);
		if (money > Double.parseDouble(maxPayAmount)) {
			// 提现金额超过5万人民币，不直接线上转款
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("由于您本次提现金额超过" + maxPayAmount + "元，我司会对您的提现申请进行审核，审核通过后将为您打款！");
			return j;
		}
		sql = "select value from system_config where code = 'open_payment'";
		String openPayment = findOneForJdbc(sql, String.class);
		if (!"true".equals(openPayment)) {
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("您的提现申请已被接受，请耐心等待！");
			return j;
		}
		String agentName = agent.getAttractName();
		j = lifeBankService.executorOnlinePayment(wuser.getId(), wuser.getUsername(), bankcard.getBankId(), bankcard.getCardNo(), bankcard.getName(), money.toString(), agentName, "");
		if(j.isSuccess()){
			logger.info("{}提现{}成功！", wuser.getId(), money);
			withdrawals.setCompleteTime(DateUtils.getSeconds());
			withdrawals.setState("done");
			this.updateEntitie(withdrawals);
		} else {
			logger.error("{}线上提现{}失败！", wuser.getId(), money);
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("您的提现申请已被接受，请耐心等待！");
		}
		return j;
	}
	
	private AjaxJson validateAgent(AgentInfoEntity agent, Double money, Integer cardId){
		AjaxJson j = new AjaxJson();
		
		if (money==null || money <= 0) {
			j.setStateCode("01");
			j.setMsg("请输入正确的提现金额");
			j.setSuccess(false);
			return j;
		}
		if(agent.getIncome() < money*100){
			j.setStateCode("01");
			j.setMsg("提现金额不能大于可用余额");
			j.setSuccess(false);
			return j;
		}
		String sql = "select count(id) times from withdrawals where user_id=? and date(from_unixtime(submit_time))=date(now()) and state in ('apply','done') ";
		Integer times = this.findOneForJdbc(sql, Integer.class, agent.getUserId());
		if (times > 0) {
			j.setStateCode("01");
			j.setMsg("一天只能提现一次");
			j.setSuccess(false);
			return j;
		}
		return j;
	}
	
	/**
	 * @param userId
	 */
	public String checkWithdrawByUser(Integer userId) {
		String retMsg = "";
		List<Map<String, Object>> list = this.queryLatest(userId, 1, 2);
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			logger.info("user:{} withdraw size:{}", userId, size);
			if(size == 1){
				logger.info("期间只有一笔提现记录，暂定为安全的");
				Map<String, Object> wd1 = list.get(0);
				Double money1 = Double.parseDouble(wd1.get("money").toString());
				// 大金额,大于5W为预警金额
				if(money1 > 50000){
					logger.warn("该笔提现存在风险:提现金额超过5W ！！！withdrawId:{}, money:{}", wd1.get("id"), money1);
					retMsg += "提现金额超过5W;";
				}
			} else {
				for(int i = 0; i < size - 1; i++){
					Map<String, Object> wd1 = list.get(i);
					Double money1 = Double.parseDouble(wd1.get("money").toString());
					Integer withId1 = Integer.parseInt(wd1.get("id").toString());
					Integer bankcardId1 = Integer.parseInt(wd1.get("bankcard_id").toString());
					Integer submitTime1 = Integer.parseInt(wd1.get("submit_time").toString());
					String submitDate1 = new DateTime(1000L * submitTime1).toString("yyyy-MM-dd");
					// 大金额,大于5W为预警金额
					if(money1 > 50000){
						logger.warn("该笔提现存在风险:提现金额超过5W ！！！withdrawId:{}, money:{}", withId1, money1);
						retMsg += "提现金额超过5W;";
					}
					
					Map<String, Object> wd2 = list.get(i + 1);
					Integer withId2 = Integer.parseInt(wd2.get("id").toString());
					Double money2 = Double.parseDouble(wd2.get("money").toString());
					Integer bankcardId2 =Integer.parseInt(wd2.get("bankcard_id").toString());
					Integer submitTime2 = Integer.parseInt(wd2.get("submit_time").toString());
					String submitDate2 = new DateTime(1000L * submitTime2).toString("yyyy-MM-dd");
					
					// 校验银行卡变更
					if(!bankcardId1.equals(bankcardId2)){
						BankcardEntity card1 = bankcardService.get(BankcardEntity.class, bankcardId1);
						BankcardEntity card2 = bankcardService.get(BankcardEntity.class, bankcardId2);
						if(!card1.getCardNo().equals(card2.getCardNo()) || !card1.getPhone().equals(card2.getPhone()) || card1.getName().equals(card2.getName())){
							logger.warn("提现存在风险:提现银行卡已更换！！！ bankcardId1:{}, bankcardId2:{}", bankcardId1, bankcardId2);
							logger.warn("cur name:{}, phone:{}, cardno:{}", 
									card1.getName(), card1.getPhone(), card1.getCardNo());
							logger.warn("old name:{}, phone:{}, cardno:{}",
									card2.getName(), card2.getPhone(), card2.getCardNo());
							retMsg += "提现银行卡有变更;";
						}
					}
					
					// 时间：一天只能提现一笔
					Period p = new Period(new DateTime(submitDate2), new DateTime(submitDate1), PeriodType.days());
					int periodDays = p.getDays();
					if(periodDays == 0){
						logger.warn("该笔提现存在风险:同一天提现多次 ！！！cur withId:{}, old withId:{}, submitDate:{}", withId1, withId2, submitDate1);
						retMsg += "同一天提现多次;";
					} else {
						// 提现增幅偏大
						if(money1 > money2){
							double increment = Math.pow(money1 / money2, 1 / periodDays); // 实际增幅
							Double targetMoney = 0d;// 预期最高提现金额
							for (int j = 1; j < periodDays + 1; j++) {
								targetMoney += money2 * Math.pow((1 + RATE), j);
							}
							if(money1 > targetMoney){
								logger.info("targetMoney:{}, realMoney:{}, periodDays:{}, increment:{}", targetMoney, money1, periodDays, increment);
								logger.warn("提现存在风险:提现金额相对上次有较大增幅！！！\r\ncur withId:{}, money:{}\r\nold withId:{}, money:{}", withId1, money1, withId2, money2);
								retMsg += "提现金额相对上次有较大增幅;";
							} else {
								logger.info("普通增量：money1:{}, money2:{}, periodDays:{}, increment:{}", money1, money2, periodDays, increment);
							}
						}
					}
					
					// 核对期间收入是否大于提现金额
					Map<String, Object> flow2 = flowService.findWithDrawFlow(userId, withId2);
					if(flow2 != null){
						Double lastMoney = Double.parseDouble(flow2.get("post_money").toString());
						Double incomeMoney = flowService.findTotalMoney(userId, "income", withId2, withId1);
						Double payMoney = flowService.findTotalMoney(userId, "pay", withId2, withId1);
						Double maxWithdwMoney = lastMoney + incomeMoney - payMoney; // 最大提现金额
						if(money1 > maxWithdwMoney){
							logger.error("提现有异常：提现金额包含非法收入!!! maxWithdwMoney:{}, money1:{}", maxWithdwMoney, money1);
							retMsg += "提现金额包含非法收入;";
						}
					}
				}
			}
		} else {
			logger.info("期间不存在提现记录, userId:{}", userId);
		}
		if(StringUtils.isNotEmpty(retMsg)){
			retMsg = "提现存在风险：" + retMsg;
			logger.info(retMsg);
		}
		return retMsg;
	}

	@Override
	public String checkWithdraw(Integer withdrawId) {
		String retMsg = "";
		List<Map<String, Object>> list = this.findForJdbc(findLast2Withdraw, withdrawId);
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			logger.info("withdrawId:{} withdraw size:{}", withdrawId, size);
			if(size == 1){
				logger.info("期间只有一笔提现记录，暂定为安全的");
				Map<String, Object> wd1 = list.get(0);
				Double money1 = Double.parseDouble(wd1.get("money").toString());
				// 大金额,大于5W为预警金额
				if(money1 > 50000){
					logger.warn("该笔提现存在风险:提现金额超过5W ！！！withdrawId:{}, money:{}", wd1.get("id"), money1);
					retMsg += "提现金额超过5W;";
				}
			} else {
				for(int i = 0; i < size - 1; i++){
					Map<String, Object> wd1 = list.get(i);
					Double money1 = Double.parseDouble(wd1.get("money").toString());
					Integer withId1 = Integer.parseInt(wd1.get("id").toString());
					Integer bankcardId1 = Integer.parseInt(wd1.get("bankcard_id").toString());
					Integer submitTime1 = Integer.parseInt(wd1.get("submit_time").toString());
					String submitDate1 = new DateTime(1000L * submitTime1).toString("yyyy-MM-dd");
					// 大金额,大于5W为预警金额
					if(money1 > 50000){
						logger.warn("该笔提现存在风险:提现金额超过5W ！！！withdrawId:{}, money:{}", withId1, money1);
						retMsg += "提现金额超过5W;";
					}

					Map<String, Object> wd2 = list.get(i + 1);
					Integer withId2 = Integer.parseInt(wd2.get("id").toString());
					Double money2 = Double.parseDouble(wd2.get("money").toString());
					Integer bankcardId2 = Integer.parseInt(wd2.get("bankcard_id").toString());
					Integer submitTime2 = Integer.parseInt(wd2.get("submit_time").toString());
					String submitDate2 = new DateTime(1000L * submitTime2).toString("yyyy-MM-dd");
					
					// 校验银行卡变更
					if (!bankcardId1.equals(bankcardId2)) {
						BankcardEntity card1 = bankcardService.get(BankcardEntity.class, bankcardId1);
						BankcardEntity card2 = bankcardService.get(BankcardEntity.class, bankcardId2);
						if (!card1.getCardNo().equals(card2.getCardNo()) || !card1.getPhone().equals(card2.getPhone()) || card1.getName().equals(card2.getName())) {
							logger.warn("提现存在风险:提现银行卡已更换！！！ bankcardId1:{}, bankcardId2:{}", bankcardId1, bankcardId2);
							logger.warn("cur name:{}, phone:{}, cardno:{}", card1.getName(), card1.getPhone(), card1.getCardNo());
							logger.warn("old name:{}, phone:{}, cardno:{}", card2.getName(), card2.getPhone(), card2.getCardNo());
							retMsg += "提现银行卡有变更;";
						}
					}
					
					// 时间：一天只能提现一笔
					Period p = new Period(new DateTime(submitDate2), new DateTime(submitDate1), PeriodType.days());
					int periodDays = p.getDays();
					if(periodDays == 0){
						logger.warn("该笔提现存在风险:同一天提现多次 ！！！cur withId:{}, old withId:{}, submitDate:{}", withId1, withId2, submitDate1);
						retMsg += "同一天提现多次;";
					} else {
						// 提现增幅偏大
						if(money1 > money2){
							double increment = Math.pow(money1 / money2, 1 / periodDays); // 实际增幅
							Double targetMoney = 0d;// 预期最高提现金额
							for (int j = 1; j < periodDays + 1; j++) {
								targetMoney += money2 * Math.pow((1 + RATE), j);
							}
							if(money1 > targetMoney){
								logger.info("targetMoney:{}, realMoney:{}, periodDays:{}, increment:{}", targetMoney, money1, periodDays, increment);
								logger.warn("提现存在风险:提现金额相对上次有较大增幅！！！\r\ncur withId:{}, money:{}\r\nold withId:{}, money:{}", withId1, money1, withId2, money2);
								retMsg += "提现金额相对上次有较大增幅;";
							} else {
								logger.info("普通增量：money1:{}, money2:{}, periodDays:{}, increment:{}", money1, money2, periodDays, increment);
							}
						}
					}
					
					// 核对期间收入是否大于提现金额
					Integer userId = this.findOneForJdbc("select user_id from withdrawals where id=?", Integer.class, withdrawId);
					Map<String, Object> flow2 = flowService.findWithDrawFlow(userId, withId2);
					if(flow2 != null){
						Double lastMoney = Double.parseDouble(flow2.get("post_money").toString());
						Double incomeMoney = flowService.findTotalMoney(userId, "income", withId2, withId1);
						Double payMoney = flowService.findTotalMoney(userId, "pay", withId2, withId1);
						Double maxWithdwMoney = lastMoney + incomeMoney - payMoney; // 最大提现金额
						if(money1 > maxWithdwMoney){
							logger.error("提现有异常：提现金额包含非法收入!!! maxWithdwMoney:{}, money1:{}", maxWithdwMoney, money1);
							retMsg += "提现金额包含非法收入;";
						}
					}
				}
			}
		} else {
			logger.info("期间不存在提现记录, withdrawId:{}", withdrawId);
		}
		if(StringUtils.isNotEmpty(retMsg)){
			retMsg = "提现存在风险：" + retMsg;
			logger.info(retMsg);
		}
		return retMsg;
	}
	
	
	private boolean isWhiteList(long userId) {
		return !OnlinePaymentBlackList.isBlackList(userId);
	}
}
