package com.qihui.sun.permission;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SimpleRateLimiter {
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;

    public SimpleRateLimiter(int permits, long period, TimeUnit unit) {
        this.semaphore = new Semaphore(permits);
        this.scheduler = new ScheduledThreadPoolExecutor(1);

        // 定期重置 Semaphore 的许可数
        scheduler.scheduleAtFixedRate(() -> semaphore.release(permits - semaphore.availablePermits()), period, period, unit);
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
   