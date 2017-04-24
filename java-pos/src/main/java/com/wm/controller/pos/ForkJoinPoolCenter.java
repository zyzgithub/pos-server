package com.wm.controller.pos;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by mjorcen on 16/7/11.
 */
public class ForkJoinPoolCenter {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(4);

    public static void submit(RecursiveTask recursiveTask) {
        forkJoinPool.submit(recursiveTask);
    }
}