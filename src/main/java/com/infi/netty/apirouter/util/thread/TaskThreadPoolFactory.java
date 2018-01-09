package com.infi.netty.apirouter.util.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 下午4:30:08
 * @since JDK 1.8
 */
public final class TaskThreadPoolFactory {

  public static Executor createExecutor(int threads, int queues) {
    String name = "netty-task-pool";
    return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
        queues == 0 ? new SynchronousQueue<>()
            : (queues < 0 ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queues)),
        new CustomizableThreadFactory(name), new AbortPolicyWithReport(name));
  }
}
