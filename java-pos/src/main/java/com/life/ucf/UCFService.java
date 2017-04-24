package com.life.ucf;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.alibaba.fastjson.JSONObject;

public interface UCFService {

	public AjaxJson executorOnlinePayment(int userId, String userName, int bankId, String accountNumber, String accountName, String transAmount, String transRemark, String defineParameter);

	public void updatePaymentFlow(JSONObject paymentResult, int transStatus);
}