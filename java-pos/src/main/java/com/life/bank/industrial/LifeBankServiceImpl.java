package com.life.bank.industrial;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cib.epay.sdk.EPay;
import com.dianba.supplychain.service.threads.ScheduledThreadHandler;
import com.life.bank.industrial.notify.OnlineWithdrawalCallbackManager;
import com.life.commons.CommonUtils;
import com.sms.service.SmsServiceI;
import com.wp.ConfigUtil;

@Service("lifeBankService")
public class LifeBankServiceImpl extends CommonServiceImpl implements LifeBankService {
	
	private static final Logger logger = LoggerFactory.getLogger(LifeBankServiceImpl.class);
	
	@Autowired
	private SmsServiceI smsService;

	@Override
	public AjaxJson executorOnlinePayment(int userId, String userName, int bankId, String accountNumber, String accountName, String transAmount, String transRemark, String defineParameter) {
		logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 发现线上提现申请：" + userName + "(" + userId + ") ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓\r\n" 
				+ "本次提现参数(银行ID：" + bankId + ", 卡号：" + accountNumber + ", 账户名：" + accountName + ", 提现额度：" + transAmount + ", 备注内容：" + transRemark + ")；是否测试环境：" + ConfigUtil.isTest);
		String sql = "select count(id) from life_pay_flow where user_id = ? and date(create_time) = curdate()";
		int count = findOneForJdbc(sql, Integer.class, userId);
		if (count > 0) {
			logger.error("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 每天只能进行一次提现操作，user id {} user name {} 已提现次数 {} ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", userId, userName, count);
			throw new RuntimeException("您有一笔提现正在处理中，请不要重复提交申请");
		}
		// 通过银行名获取对应的行号
		sql = "select bank_code from bank where id = (select bank_id from bank_card where id = ?)";
		String bankCode = findOneForJdbc(sql, String.class, bankId);
		if ("0".equals(bankCode)) {
			return error("暂不支持该银行进行线上提现！");
		}
		logger.info("银行ID：" + bankId + ", 对应的银行行号：" + bankCode);
		
		// 执行一次插入的操作
		sql = "insert into life_pay_flow(id, user_id, user_name, create_time, bank_code, account_number, account_name, account_type, currency, trans_amount, trans_remark, pay_state, bank_card_id, define_parameter) "
				+ "values(?, ?, ?, ?, ?, ?, ?, '0', 'CNY', ?, ?, 0, ?, ?)";
		// 生成简单的唯一性ID(我方订单号)
		String orderNO = simpleUUID();
		// 执行语句，并返回受影响的记录数量
		int numberOfAffectedRows  = executeSql(sql, new Object[]{orderNO, userId, userName, new Timestamp(System.currentTimeMillis()), bankCode, accountNumber, accountName, (int)(Double.parseDouble(transAmount) * 100), transRemark, bankId, CommonUtils.nullToEmpty(defineParameter)});
		if (numberOfAffectedRows <= 0) {
			logger.info("\r\n↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ " + userName + "("+userId+")的提现失败(原因：无法新增提现记录) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
			return error("提现失败");
		}
		// 第一次为即时任务
		immediateOnlinePaymentTask(orderNO, userId, userName, bankCode, accountNumber, accountName, transAmount, transRemark);
		return success("提现申请成功，请等待银行返回处理结果！");
	}
	
	private void immediateOnlinePaymentTask(final String orderNO, final int userId, final String userName, final String bankCode, final String accountNumber, final String accountName, final String transAmount, final String transRemark) {
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				if (Double.parseDouble(transAmount) > 50000) {
					logger.error("提现数据异常，user id {}, payment amount {}", userId, transAmount);
					LifePaymentFlow lifePaymentFlow = fillLifePaymentFlow(orderNO);
					// 失败时回调监听者 由监听者处理后续操作
					OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
					return;
				}
				String result = (!ConfigUtil.isTest) ?
						EPay.pyPay(orderNO, bankCode, accountNumber, accountName, "0", transAmount, transRemark)
						// "{\"errcode\":\"EPAY_29001\",\"errmsg\":\"[EPAY_29001]通讯错误或超时，交易未决\"}"
						: "{\"orderNo\":\"" + orderNO + "\",\"sno\":\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\",\"transDate\":\"" + new SimpleDateFormat("yyyyMMdd").format(new Date())
								+ "\",\"transFee\":\"0.50\",\"transStatus\":\"1\"," + "\"transTime\":\"" + new SimpleDateFormat("HHmmss").format(new Date()) + "\",\"transUsage\":\"" + transRemark + "\"}";
				logger.info(result +"\r\n↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ " + userName + "("+userId+")的提现结果 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
				
				if (result.contains("errcode")) {
					// sendWarnningSMS("第一次提现失败，进入重试任务，流水ID为：" + orderNO + "，用户：" + userName + "("+userId+")，提现金额：" + transAmount + "，收款帐号为：" + accountNumber);
					logger.error("第一次提现失败，进入重试任务，流水ID为：" + orderNO + "，用户：" + userName + "("+userId+")，提现金额：" + transAmount + "，收款帐号为：" + accountNumber);
					// 执行失败 重新发起该任务
					delayOnlinePayment(orderNO);
					return;
				}
				JSONObject bankPaymentResult = JSONObject.parseObject(result);
				// 银行返回的状态
				int transStatus = bankPaymentResult.getIntValue("transStatus");
				// 更新本地流水状态
				try {
					updatePaymentFlow(bankPaymentResult, transStatus);
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
				}
				// 封装支付流水对象
				LifePaymentFlow lifePaymentFlow = fillLifePaymentFlow(orderNO);
				if (transStatus == 2) {
					// 失败时回调监听者 由监听者处理后续操作
					OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
					return;
				}
				if (transStatus == 1) {
					// 银行返回成功时
					OnlineWithdrawalCallbackManager.getInstance().success(lifePaymentFlow);
				}
			}
		};
		ScheduledThreadHandler.submitImmediateTask(runnable);
	}
	
	public void delayOnlinePayment(final String orderNO) {
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				String sql = "update life_pay_flow set error_count = error_count + 1 where id = ?";
				// 更新重试的次数 当重试次数到达指定的值时 停止任务
				executeSql(sql, new Object[] { orderNO });

				LifePaymentFlow lifePaymentFlow = fillLifePaymentFlow(orderNO);
				
				logger.info("[Reset online payment task] order no " + orderNO + ", user id " + lifePaymentFlow.getUserId() + ", retry count " + lifePaymentFlow.getErrorCount() + ", pay state " + lifePaymentFlow.getPayState());
				 
				if (lifePaymentFlow.getPayState() != 0) {
					// 只能执行未处理的任务
					return;
				}
				double transAmount = lifePaymentFlow.getTransAmount() / 100f;
				if (transAmount > 50000) {
					logger.error("提现数据异常，user id {}, payment amount {}", lifePaymentFlow.getUserId(), transAmount);
					// 失败时回调监听者 由监听者处理后续操作
					OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
					return;
				}
				int random = CommonUtils.random(1, 6);
				logger.error("Random value {}, error count {}", random, lifePaymentFlow.getErrorCount());
				String result = (!ConfigUtil.isTest) ?
						EPay.pyPay(orderNO, lifePaymentFlow.getBankCode(), lifePaymentFlow.getAccountNumber(), lifePaymentFlow.getAccountName(), "0", String.valueOf(transAmount), lifePaymentFlow.getTransRemark())
						: "{\"errcode\":\"EPAY_29001\",\"errmsg\":\"[EPAY_29001]通讯错误或超时，交易未决\"}";
						// : "{\"orderNo\":\"" + orderNO + "\",\"sno\":\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\",\"transDate\":\"" + new SimpleDateFormat("yyyyMMdd").format(new Date())
						// + "\",\"transFee\":\"0.50\",\"transStatus\":\"1\"," + "\"transTime\":\"" + new SimpleDateFormat("HHmmss").format(new Date()) + "\",\"transUsage\":\"" + lifePaymentFlow.getTransRemark() + "\"}";
				if (result.contains("errcode")) {
					if(lifePaymentFlow.getErrorCount() > 3){
						sendWarnningSMS("重新发起提现申请，本次操作依然失败，总的失败次数：" + lifePaymentFlow.getErrorCount() + "，流水ID为：" + orderNO + "，用户：" + lifePaymentFlow.getUserName() 
								+ "(" + lifePaymentFlow.getUserId() + ")，提现金额：" + String.valueOf(transAmount) + "，收款帐号为：" + lifePaymentFlow.getAccountNumber());
					}
					logger.error("重新发起提现申请，本次操作依然失败，总的失败次数：" + lifePaymentFlow.getErrorCount() + "，流水ID为：" + orderNO + "，用户：" + lifePaymentFlow.getUserName() 
							+ "(" + lifePaymentFlow.getUserId() + ")，提现金额：" + String.valueOf(transAmount) + "，收款帐号为：" + lifePaymentFlow.getAccountNumber());					
					if (lifePaymentFlow.getErrorCount() < 5) {
						// 错误时 未到达指定次数 再次发起请求
						ScheduledThreadHandler.submitAsyncTask(this, 10, TimeUnit.SECONDS);
						return;
					}
					// 失败时回调监听者 由监听者处理后续操作
					OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
					// 更新错误的内容 不再发起重试
					errorPaymentFlow(orderNO, result);
					return;
				}
				JSONObject bankPaymentResult = JSONObject.parseObject(result);
				// 银行返回的状态
				int transStatus = bankPaymentResult.getIntValue("transStatus");
				// 更新本地流水状态
				try {
					updatePaymentFlow(bankPaymentResult, transStatus);
				} catch (Exception ex) {
					ex.printStackTrace();
					// return error("处理银行数据异常，请联系我司客服！");
					return;
				}
				if (transStatus == 2) {
					// 失败时回调监听者 由监听者处理后续操作
					OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
					return;
				}
				if (transStatus == 1) {
					// 银行返回成功时
					OnlineWithdrawalCallbackManager.getInstance().success(lifePaymentFlow);
				}
			}
		};
		ScheduledThreadHandler.submitAsyncTask(runnable, 10, TimeUnit.SECONDS);
	}
	
	private LifePaymentFlow fillLifePaymentFlow(String orderNO) {
		String sql = "select id, user_id, user_name, create_time, bank_code, account_number, account_name, account_type, currency, trans_amount, trans_remark, bank_serial_no, trans_fee, callback_time, callback_remark, pay_state, bank_card_id, error_count, define_parameter"
				+ " from life_pay_flow where id = ?";
		
		List<Map<String, Object>> lifePayFlows = findForJdbc(sql, orderNO);
		if (lifePayFlows == null || lifePayFlows.isEmpty()) {
			return null;
		}
		Map<String, Object> lifePayFlow = lifePayFlows.get(0);
		
		LifePaymentFlow lifePaymentFlow = new LifePaymentFlow();
		lifePaymentFlow.setId(orderNO);
		lifePaymentFlow.setUserId(Long.parseLong(lifePayFlow.get("user_id").toString()));
		lifePaymentFlow.setUserName(lifePayFlow.get("user_name").toString());
		lifePaymentFlow.setCreateTime(CommonUtils.dateFormatToLong(lifePayFlow.get("create_time").toString()));
		lifePaymentFlow.setBankCode(lifePayFlow.get("bank_code").toString());
		lifePaymentFlow.setAccountNumber(lifePayFlow.get("account_number").toString());
		lifePaymentFlow.setAccountName(lifePayFlow.get("account_name").toString());
		lifePaymentFlow.setAccountType(Integer.parseInt(lifePayFlow.get("account_type").toString()));
		lifePaymentFlow.setCurrency(lifePayFlow.get("currency").toString());
		lifePaymentFlow.setTransAmount(Long.parseLong(lifePayFlow.get("trans_amount").toString()));
		lifePaymentFlow.setTransRemark(lifePayFlow.get("trans_remark").toString());
		Object bankSerialNo = lifePayFlow.get("bank_serial_no");
		lifePaymentFlow.setBankSserialNo(bankSerialNo == null ? "" : bankSerialNo.toString());
		Object transFee = lifePayFlow.get("trans_fee");
		lifePaymentFlow.setTransFee(Long.parseLong(transFee == null ? "0" : transFee.toString()));
		Object callbackTime = lifePayFlow.get("callback_time");
		lifePaymentFlow.setCallbackTime(CommonUtils.dateFormatToLong(callbackTime == null ? "2000-01-01 00:00:00" : callbackTime.toString()));
		Object callbackRemark = lifePayFlow.get("callback_remark");
		lifePaymentFlow.setCallbackRemark(callbackRemark == null ? "" : callbackRemark.toString());
		lifePaymentFlow.setPayState(Integer.parseInt(lifePayFlow.get("pay_state").toString()));
		lifePaymentFlow.setBankcardId(Integer.parseInt(lifePayFlow.get("bank_card_id").toString()));
		lifePaymentFlow.setErrorCount(Integer.parseInt(lifePayFlow.get("error_count").toString()));
		Object defineParameter = lifePayFlow.get("define_parameter");
		lifePaymentFlow.setDefineParameter(defineParameter == null ? "" : defineParameter.toString());
		return lifePaymentFlow;
	}
	
	@Override
	public void executorRecoverTask() {
		if (ConfigUtil.isTest) {
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 测试环境，不对银行流水进行数据同步 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			return;
		}
		// 10分钟之前的时间
		Timestamp beforeTenMinuteTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10));
		logger.info("↓↓↓↓↓↓↓↓↓↓ 执行支付状态修复任务，本次修复的数据为：" + beforeTenMinuteTime + "前的所有未决记录 ↓↓↓↓↓↓↓↓↓↓");
		String sql = "select id from life_pay_flow where pay_state = 3 and create_time < '" + beforeTenMinuteTime + "' and account_type = 0";

		List<Map<String, Object>> queryResults = findForJdbc(sql);

		logger.info("时间结点(" + beforeTenMinuteTime + "), 查询未同步状态的记录数量：" + queryResults.size());
		int successCount = 0;
		for (Map<String, Object> queryResult : queryResults) {
			String orderNO = queryResult.get("id").toString();

			String paymentStatusQueryValue = EPay.pyQuery(orderNO);
			JSONObject paymentStatusQueryResult = JSONObject.parseObject(paymentStatusQueryValue);
			if (paymentStatusQueryResult.toString().contains("error")) {
				continue;
			}
			// 状态是否已经变化
			int transStatus = paymentStatusQueryResult.getIntValue("transStatus");
			if (transStatus == 3) {
				continue;
			}
			try {
				updatePaymentFlow(paymentStatusQueryResult, transStatus);
				successCount++;
				
				LifePaymentFlow lifePaymentFlow = fillLifePaymentFlow(orderNO);
				if (transStatus == 1) {
					// 通知监听器修改为成功状态
					OnlineWithdrawalCallbackManager.getInstance().success(lifePaymentFlow);
				} else if (transStatus == 2) {
					// 通知监听器修改为失败状态
					OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("↑↑↑↑↑↑↑↑↑↑ 时间结点(" + beforeTenMinuteTime + ")前的未决记录修复任务完成，本次修复数量：" + successCount + " ↑↑↑↑↑↑↑↑↑↑");
	}
	
	private void updatePaymentFlow(JSONObject bankPaymentResult, int transStatus) throws NumberFormatException, ParseException {
		String sql = "update life_pay_flow set bank_serial_no = ?, trans_fee = ?, callback_time = ?, callback_remark = ?, pay_state = ? where id = ?";
		// 银行处理流水号
		String sno = nullToEmpty(bankPaymentResult.getString("sno"));
		// 我方生成的订单号
		String orderNO = nullToEmpty(bankPaymentResult.getString("orderNo"));
		// 交易手续费
		String transFee = nullToEmpty(bankPaymentResult.getString("transFee"));
		// 银行给出的描述 当为错误时有效
		String remark = nullToEmpty(bankPaymentResult.getString("remark"));
		// 时间
		String time = bankPaymentResult.getString("transDate") + bankPaymentResult.getString("transTime");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		executeSql(sql, new Object[]{sno, (int)(Double.parseDouble(transFee) * 100), new Timestamp(dateFormat.parse(time).getTime()), remark, String.valueOf(transStatus), orderNO});
	}
	
	private void errorPaymentFlow(String orderNO, String errorRemark) {
		String sql = "update life_pay_flow set callback_time = ?, callback_remark = ?, pay_state = ? where id = ?";
		
		executeSql(sql, new Object[]{new Timestamp(System.currentTimeMillis()), errorRemark, "2", orderNO});
	}
	
	private String simpleUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
	private AjaxJson error(String msg) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(false);
		ajaxJson.setStateCode("01");
		ajaxJson.setMsg(msg);
		
		return ajaxJson;
	}
	
	private AjaxJson success(String msg) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setStateCode("00");
		ajaxJson.setMsg(msg);
		
		return ajaxJson;
	}
	
	private String nullToEmpty(String value) {
		return value == null ? "" : value;
	}
	
	private void sendWarnningSMS(String content) {
		smsService.sendSms("18680233352", content);
		smsService.sendSms("13189011530", content);
	}
}