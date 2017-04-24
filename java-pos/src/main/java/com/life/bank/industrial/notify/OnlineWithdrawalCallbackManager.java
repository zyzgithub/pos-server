package com.life.bank.industrial.notify;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dianba.supplychain.service.threads.ScheduledThreadHandler;
import com.life.bank.industrial.LifePaymentFlow;

public class OnlineWithdrawalCallbackManager {

	private static final OnlineWithdrawalCallbackManager instance = new OnlineWithdrawalCallbackManager();

	private static final Map<String, OnlineWithdrawalCallback> onlineWithdrawalCallbacks = new HashMap<String, OnlineWithdrawalCallback>();

	private OnlineWithdrawalCallbackManager() {
	}

	public static OnlineWithdrawalCallbackManager getInstance() {
		return instance;
	}

	public void registerOnlineWithdrawalCallback(String key, OnlineWithdrawalCallback value) {
		onlineWithdrawalCallbacks.put(key, value);
	}

	public void success(final LifePaymentFlow lifePaymentFlow) {
		for (Iterator<OnlineWithdrawalCallback> iter = onlineWithdrawalCallbacks.values().iterator(); iter.hasNext();) {

			final OnlineWithdrawalCallback onlineWithdrawalCallback = iter.next();

			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					onlineWithdrawalCallback.success(lifePaymentFlow);
				}
			};
			ScheduledThreadHandler.submitImmediateTask(runnable);
		}
	}

	public void fail(final LifePaymentFlow lifePaymentFlow) {
		for (Iterator<OnlineWithdrawalCallback> iter = onlineWithdrawalCallbacks.values().iterator(); iter.hasNext();) {

			final OnlineWithdrawalCallback onlineWithdrawalCallback = iter.next();

			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					onlineWithdrawalCallback.fail(lifePaymentFlow);
				}
			};
			ScheduledThreadHandler.submitImmediateTask(runnable);
		}
	}
}