package zookeeper.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zookeeper.util.Lock;

public class TestLock {
	
	static int POOL_SIZE = 5;
	
	static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE); 
	
	public static void main(String[] args)throws Exception{
		new TestLock.MyLock().clear();
		for(int i=0; i<POOL_SIZE; i++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					new TestLock.MyLock().init();
				}
			});
		}
		executor.shutdown();
	}
	
	static class MyLock extends Lock{
		
		static String LOCK_ROOT = "/lock";
		
		static String HOST  =  "127.0.0.1:2181";

		public MyLock() {
			super(HOST, LOCK_ROOT);
		}

		@Override
		public void doAction() {
			System.out.println(Thread.currentThread().getName()+": 执行任务开始");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+": 执行任务结束");
			this.release();
		}
	}

}
