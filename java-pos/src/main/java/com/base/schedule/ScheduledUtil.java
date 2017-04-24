package com.base.schedule;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledUtil {
	
	public static final String JPUSH_POOL = "jpushPool";
	public static final String SENDWXMSG_POOL = "sendWxMsgPool";
	public static final String PRINT_POOL = "printPool";
	public static final String OTHER_POOL = "otherPool";
	
	public static final Integer POOL_SIZE = 4;

	private static ScheduledThreadPoolExecutor jpushPoolExecutor = new ScheduledThreadPoolExecutor(
			POOL_SIZE, new WaimaiThreadFactory(JPUSH_POOL),
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	private static ScheduledThreadPoolExecutor sendWxMsgPoolExecutor = new ScheduledThreadPoolExecutor(
			POOL_SIZE, new WaimaiThreadFactory(SENDWXMSG_POOL),
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	private static ScheduledThreadPoolExecutor printPoolExecutor = new ScheduledThreadPoolExecutor(
			POOL_SIZE, new WaimaiThreadFactory(PRINT_POOL),
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	private static ScheduledThreadPoolExecutor otherPoolExecutor = new ScheduledThreadPoolExecutor(
			POOL_SIZE, new WaimaiThreadFactory(OTHER_POOL),
			new ThreadPoolExecutor.CallerRunsPolicy());


	/**
	 * 执行零延迟任务
	 * @param command
	 */
	public static void runNodelayTask(Runnable command, String poolName) {
		if(JPUSH_POOL.equals(poolName)){
			jpushPoolExecutor.schedule(command, 0, TimeUnit.SECONDS);
		} else if (SENDWXMSG_POOL.equals(poolName)){
			sendWxMsgPoolExecutor.schedule(command, 0, TimeUnit.SECONDS);
		} else if (PRINT_POOL.equals(poolName)){
			printPoolExecutor.schedule(command, 0, TimeUnit.SECONDS);
		} else {
			otherPoolExecutor.schedule(command, 0, TimeUnit.SECONDS);
		}
	}
	
}

