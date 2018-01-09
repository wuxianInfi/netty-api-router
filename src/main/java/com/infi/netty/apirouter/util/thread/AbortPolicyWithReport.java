package com.infi.netty.apirouter.util.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月9日 下午4:30:08
 * @since JDK 1.8
 */
@Slf4j
@RequiredArgsConstructor
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

  private final String threadName;

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    String msg = String.format(
        "HttpServer["
            + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
            + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]",
        threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(),
        e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(),
        e.isShutdown(), e.isTerminated(), e.isTerminating());
    log.warn(msg);
    throw new RejectedExecutionException(msg);
  }
}
