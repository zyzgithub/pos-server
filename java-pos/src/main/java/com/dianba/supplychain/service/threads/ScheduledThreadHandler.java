package com.dianba.supplychain.service.threads;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadHandler {

	private static ScheduledThreadPoolExecutor defineThreadPoolExecutor = new ScheduledThreadPoolExecutor(16, new DefineThreadFactory("ayncTask"), new ThreadPoolExecutor.CallerRunsPolicy());

	public static void submitImmediateTask(Runnable command) {
		submitAsyncTask(command, 0, TimeUnit.MILLISECONDS);
	}
	
	public static void submitAsyncTask(Runnable command, long delay, TimeUnit timeUnit) {
		defineThreadPoolExecutor.schedule(command, delay, timeUnit);
	}
}