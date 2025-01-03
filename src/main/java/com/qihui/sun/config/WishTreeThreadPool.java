package com.qihui.sun.config;

import lombok.Getter;

import java.util.concurrent.*;

public class WishTreeThreadPool {
    @Getter
    private static final ExecutorService executor;

    static {
        int corePoolSize = Runtime.getRuntime().availableProcessors(); // 获取CPU核心数
        int maximumPoolSize = corePoolSize + 1; // 最大线程数略大于核心数
        long keepAliveTime = 60L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100); // 使用有界队列
        ThreadFactory virtualThreadFactory = r -> Thread.ofVirtual().name("virtual-thread-", 0).start(r);

        executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                virtualThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
