package com.jimbolix.april.mq.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Description 发送消息的线程池
 * @Author lrh
 * @date 2020.04.18 20:13
 */
@Slf4j
public class AsyncBaseQueue {

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int QUEUE_SIZE = 1000;

    private static ExecutorService senderAsync = new ThreadPoolExecutor(THREAD_SIZE, THREAD_SIZE,
            60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_SIZE), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("rabbit client send message");
            return t;
        }
    }, new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("rabbit client send message error,Runnable {},executor{}",r,executor);
        }
    });

    public static void submit(Runnable runnable){
        senderAsync.submit(runnable);
    }
}
