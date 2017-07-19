package com.dianba.pos.base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkStealingPool {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newWorkStealingPool();

    public static Future<?> submit(Runnable runnable) {
        return EXECUTOR_SERVICE.submit(runnable);
    }
}
