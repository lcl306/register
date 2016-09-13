package async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FuturePool {
	
	static final int FUTURE_THREAD_SIZE = 5;
	
	static final int timeout = 60;
	
	//ExecutorService executor = Executors.newScheduledThreadPool(FUTURE_THREAD_SIZE);  //按schedule依次delay执行，或周期执行
	//ExecutorService executor = Executors.newSingleThreadExecutor();  //只有1个线程
	//ExecutorService executor = Executors.newFixedThreadPool(FUTURE_THREAD_SIZE);  //只有固定的线程
	static ExecutorService executor = Executors.newCachedThreadPool();  //执行一些生存期很短的异步型任务，池中有idle线程reuse，没有则创建，使用后idle，idle线程60s删除
	
	public static <T> Future<T> submit(Callable<T> task){
		return executor.submit(task);
	}
	
	/**
	 * get相当于Thead.join，会阻塞当前主线程
	 * */
	public static <T> T get(Future<T> future) throws TimeoutException, ExecutionException, InterruptedException{
		return future.get(timeout, TimeUnit.SECONDS);
	}

}
