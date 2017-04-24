package com.life.bank.industrial;

import javax.servlet.ServletContextEvent;

import com.life.bank.industrial.notify.OnlineWithdrawalCallback;
import com.life.bank.industrial.notify.OnlineWithdrawalCallbackManager;
import com.life.commons.LifeInitialEventListener;
import com.wm.service.impl.withdrawals.OnlineWithdrawlImpl;

public class WithdrawalInitialEventListenerImpl implements LifeInitialEventListener {

	/**
	 * 必须在系统初始化时调用
	 */
	@Override
	public void notifySystemInitialComplate(ServletContextEvent event) {
		// 初始化财务监听回调器
		OnlineWithdrawalCallback onlineWithdrawalCallback = new OnlineWithdrawlImpl();
		// 注册到提现回调管理器
		OnlineWithdrawalCallbackManager.getInstance().registerOnlineWithdrawalCallback("Finance callback", onlineWithdrawalCallback);
	}
}