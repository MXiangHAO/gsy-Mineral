package com.example.chapter_blog.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

    private static final int POOL_SIZE = 5; // 线程池大小，根据需求调整

    private static ExecutorService executorService;

    // 私有构造方法，防止外部实例化
    private ThreadPoolManager() {
    }

    // 初始化线程池
    private static void initThreadPool() {
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    // 获取线程池实例
    public static ExecutorService getThreadPool() {
        if (executorService == null) {
            synchronized (ThreadPoolManager.class) {
                if (executorService == null) {
                    initThreadPool();
                }
            }
        }
        return executorService;
    }

    // 关闭线程池
    public static void shutdownThreadPool() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

