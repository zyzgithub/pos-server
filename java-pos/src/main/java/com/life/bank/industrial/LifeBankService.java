package com.life.bank.industrial;

import org.jeecgframework.core.common.model.json.AjaxJson;

/**
 * <pre>
 * 1号生活 -- 银行相关服务
 * 
 * 接口开始编写时间 2016-06-05
 * </pre>
 * @author chinahuangxc
 */
public interface LifeBankService {

	/**
	 * <pre>
	 * 执行线上代付操作
	 * 
	 * <b>注意：</b>使用线上代付的操作记录，请不要再次提交给财务系统，避免出现双重支付的情况
	 * </pre>
	 * 
	 * @param userId 用户ID 主要是用于处理用户投诉、核算等操作
	 * @param userName 用户名 冗余数据 方便统计、查询
	 * @param bankId 银行ID，通过银行ID获取相应的行号
	 * @param accountNumber 收款账户卡号
	 * @param accountName 收款账户户名
	 * @param transAmount 收款金额，单位：元
	 * @param transRemark 本次付款的标志，请按财务要求的规范填写
	 * @param defineParameter 可存放使用者自定义的参数内容，一般为逻辑相关的流水号，不能超过200位
	 * 
	 * @return 返回前端的数据
	 * 
	 * @author chinahuangxc
	 */
	public AjaxJson executorOnlinePayment(int userId, String userName, int bankId, String accountNumber, String accountName, String transAmount, String transRemark, String defineParameter);
	
	/**
	 * <pre>
	 * 执行支付状态的修复任务
	 * 
	 * <b>注意：</b>此方法涉及到批量操作；但方法内部是使用循环方式处理每条记录；
	 * 因为中间有较大的网络消耗（到银行接口的消耗），该线程可能造成较大的性能损耗，也可能对任务造成阻塞
	 * </pre>
	 * @author chinahuangxc
	 */
	public void executorRecoverTask();
}