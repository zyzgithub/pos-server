package com.life.ucf;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.life.bank.industrial.LifePaymentFlow;
import com.life.bank.industrial.notify.OnlineWithdrawalCallbackManager;
import com.life.commons.CommonUtils;
import com.life.commons.DefineRandom;
import com.ucf.common.BaseParam;
import com.ucf.common.WithdrawParam;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.util.UnRepeatCodeGenerator;
import com.ucf.service.WithdrawService;
import com.wp.ConfigUtil;

@Transactional
@Service("ucfService")
public class UCFServiceImpl extends CommonServiceImpl implements UCFService {
	
	private static final Logger logger = LoggerFactory.getLogger(UCFServiceImpl.class);
	
	@Autowired
	private WithdrawService ucfWithdrawalService;
	
	@Override
	public AjaxJson executorOnlinePayment(int userId, String userName, int bankId, String accountNumber, String accountName, String transAmount, String transRemark, String defineParameter) {
		logger.info("=========================== 发现线上提现【先锋(UCF)】申请：" + userName + "(" + userId + ") ===========================\r\n" 
				+ "本次提现参数(银行ID：" + bankId + ", 卡号：" + accountNumber + ", 账户名：" + accountName + ", 提现额度：" + transAmount + ", 备注内容：" + transRemark + ")；是否测试环境：" + ConfigUtil.isTest);
		String sql = "select count(id) from life_pay_flow where user_id = ? and date(create_time) = curdate()";
		int count = findOneForJdbc(sql, Integer.class, userId);
		if (count > 0) {
			logger.error("=========================== 每天只能进行一次提现操作，user id {} user name {} 已提现次数 {} ===========================", userId, userName, count);
			throw new RuntimeException("您有一笔提现正在处理中，请不要重复提交申请");
		}
		sql = "select count(id) from life_pay_flow where user_id = ? and pay_state = 3";
		count = findOneForJdbc(sql, Integer.class, userId);
		if (count > 0) {
			logger.error("=========================== 用户存在未完成的提现记录，user id {} user name {} 提现中的记录数量 {} ===========================", userId, userName, count);
			throw new RuntimeException("您有一笔提现正在处理中，请不要重复提交申请");
		}
		// 通过银行名获取对应的行号
		sql = "select ifnull(bank_no, 0) from bank where id = (select bank_id from bank_card where id = ?)";
		String bankNo = findOneForJdbc(sql, String.class, bankId);
		logger.info("银行ID：" + bankId + ", 对应的银行标志：" + bankNo);
		
		// 执行一次插入的操作
		sql = "insert into life_pay_flow(id, user_id, user_name, create_time, bank_code, account_number, account_name, account_type, currency, trans_amount, trans_remark, pay_state, bank_card_id, define_parameter) "
				+ "values(?, ?, ?, ?, ?, ?, ?, '1', '156', ?, ?, 3, ?, ?)";
		// 生成简单的唯一性ID(我方订单号)
		String orderNO = simpleUUID();
		// 执行语句，并返回受影响的记录数量
		int numberOfAffectedRows  = executeSql(sql, new Object[]{orderNO, userId, userName, new Timestamp(System.currentTimeMillis()), bankNo, accountNumber, accountName, (int)(Double.parseDouble(transAmount) * 100), transRemark, bankId, CommonUtils.nullToEmpty(defineParameter)});
		if (numberOfAffectedRows <= 0) {
			logger.info("\r\n=========================== " + userName + "("+userId+")的提现失败(原因：无法新增提现记录) ===========================");
			return error("提现失败");
		}
		if ("0".equals(bankNo)) {
			JSONObject paymentResult = new JSONObject();
			paymentResult.put("tradeNo", "");
			paymentResult.put("merchantNo", orderNO);
			paymentResult.put("resMessage", "银行卡不支持");
			updatePaymentFlow(paymentResult, 2, false);
			return error("暂不支持该银行进行线上提现！");
		}
		return executor(orderNO, userId, userName, bankNo, accountNumber, accountName, transAmount, transRemark);
	}
	
	private AjaxJson executor(String orderNO, int userId, String userName, String bankNo, String accountNumber, String accountName, String transAmount, String transRemark) {
		WithdrawParam param = new WithdrawParam();
		String merId = EnvConfig.ucf.merId;
		logger.info("merchant id -- " + merId + " trade sequence number " + orderNO);
		param.setService(BaseParam.Service.WITHDRAW);
		param.setMerchantId(merId);
		String merchantNo = DateTime.now().toString("yyyyMMddhhmmssSSS");// 商户订单号
		param.setMerchantNo(orderNO);
		param.setAmount(String.valueOf((int)(Double.parseDouble(transAmount) * 100)));
		param.setBankNo(bankNo);
		param.setAccountNo(accountNumber);
		param.setAccountName(accountName);
		param.setNoticeUrl(ConfigUtil.UCF_NOTICE_URL);
		param.setMemo(transRemark);
		String reqSn = "";
		try {
			reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merId, BaseParam.Service.WITHDRAW, merchantNo);
		} catch (CoderException e) {
			e.printStackTrace();
			reqSn = String.valueOf(System.currentTimeMillis() + DefineRandom.randomNumber(6));
		}
		param.setReqSn(reqSn);
		
		JSONObject result = ucfWithdrawalService.withdraw(param);
		if (result == null) {
			logger.error("withdraw error, param:" + JSONObject.toJSON(param));
			
			JSONObject paymentResult = new JSONObject();
			paymentResult.put("tradeNo", "");
			paymentResult.put("merchantNo", orderNO);
			paymentResult.put("resMessage", "提现失败");

			updatePaymentFlow(paymentResult, 2, false);
			// 转线下
			return error("提现失败");
		}
		if ("01".equals(result.get("status"))) {
			logger.error("withdraw error, param:" + JSONObject.toJSON(param) + "  result " + result);
			result.put("tradeNo", "");
			result.put("merchantNo", orderNO);
			result.put("resMessage", "提现失败");
			updatePaymentFlow(result, 2, false);
			return error("提现失败");
		}
		if ("S".equals(result.get("status"))) {
			logger.info("merchantId:" + result.get("merchantId") + " withdraw amount:" + result.get("amount") + " success ! merchantNo:" + result.get("merchantNo") + ", tradeNo:" + result.get("tradeNo"));
			updatePaymentFlow(result, 1);
		} else if ("I".equals(result.get("status"))) {
			logger.info("merchantId:" + result.get("merchantId") + " withdraw amount:" + result.get("amount") + " resolving ... ! merchantNo:" + result.get("merchantNo") + ", tradeNo:" + result.get("tradeNo"));
			updatePaymentFlow(result, 3);
		} else {
			logger.warn("withdraw failed. msg:" + result.get("resMessage"));
			result.put("merchantNo", orderNO);
			updatePaymentFlow(result, 2, false);
			// 失败转线下
			return error("提现失败");
		}
		return success("提现成功");
	}
	
	private String simpleUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
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
	public void updatePaymentFlow(JSONObject paymentResult, int transStatus) {
		updatePaymentFlow(paymentResult, transStatus, true);
	}
	
	private void updatePaymentFlow(JSONObject paymentResult, int transStatus, boolean notify) {
		String sql = "update life_pay_flow set bank_serial_no = ?, trans_fee = ?, callback_time = ?, callback_remark = ?, pay_state = ? where id = ?";
		// 银行处理流水号
		String tradeNo = nullToEmpty(paymentResult.getString("tradeNo"));
		// 我方生成的订单号
		String orderNO = nullToEmpty(paymentResult.getString("merchantNo"));
		// 银行给出的描述 当为错误时有效
		String remark = nullToEmpty(paymentResult.getString("resMessage"));
		
		executeSql(sql, new Object[]{tradeNo, 0, new Timestamp(System.currentTimeMillis()), remark, String.valueOf(transStatus), orderNO});
		
		if (notify) {
			// 封装支付流水对象
			LifePaymentFlow lifePaymentFlow = fillLifePaymentFlow(orderNO);
			if (transStatus == 2) {
				OnlineWithdrawalCallbackManager.getInstance().fail(lifePaymentFlow);
			} else if (transStatus == 1) {
				OnlineWithdrawalCallbackManager.getInstance().success(lifePaymentFlow);
			}
		}
	}
	
	private String nullToEmpty(String value) {
		return value == null ? "" : value;
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
	
	public static void main(String[] args) {
		String url = "https://mapi.ucfpay.com/gateway.do";
		
		Map<String, String> parameter = new HashMap<String, String>();
	}
}