/** 
 * 文件名：ThreadPoolFactory.java
 * 版本信息�?
 * 日期�?013-3-22
 */
package com.lis99.mobile.engine.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * android 线程�?
 * 
 * 项目名称：framework 类名称：ThreadPoolFactory 类描述： 创建人：吴安�?创建时间�?013-3-22 上午9:50:10
 * 
 * @version
 * 
 */
public final class ThreadPoolFactory {

    private static ThreadPoolFactory factory = new ThreadPoolFactory();

    private static final int POOL_SIZE = 10;

    private final ExecutorService threadPool;

    private ThreadPoolFactory() {
	threadPool = Executors.newFixedThreadPool(Runtime.getRuntime()
		.availableProcessors() * POOL_SIZE);
	// threadPool = new ThreadPoolExecutor(1, 3, 60,TimeUnit.SECONDS,new
	// LinkedBlockingQueue<Runnable>());
    }

    public static ThreadPoolFactory getInstance() {
	return factory;
    }

    public void execute(Runnable run) {
	threadPool.submit(run);
    }

    public void stopAll() {
	threadPool.shutdownNow();
    }
}