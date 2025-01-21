package com.qihui.sun.config;

import java.util.concurrent.*;

public class WishTreeThreadPool {
    public static ExecutorService getExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors(); // 获取CPU核心数
        int maximumPoolSize = corePoolSize + 1; // 最大线程数略大于核心数
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS, workQueue, new ThreadPoolExecutor.AbortPolicy());
    }

    public static ExecutorService getVirtualExecutor() {
        //经测试cpu密集型任务使用效果比较糟糕
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
