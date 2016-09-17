package zookeeper.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;

import zookeeper.util.SyncQueue;

public class TestSyncQueue {
	
	static int POOL_SIZE = 5;
	
	static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE); 

	public static void main(String[] args) {
		new TestSyncQueue.MySyncQueue(POOL_SIZE).clear();
		for(int i=0; i<POOL_SIZE; i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			executor.execute(new Runnable(){
				@Override
				public void run() {
					new TestSyncQueue.MySyncQueue(POOL_SIZE).submit();
				}
			});
		}
		executor.shutdown();
	}
	
	public static class MySyncQueue extends SyncQueue<Integer>{
		
		static String SYNC_ROOT = "/syncQueue";
		
		String MEMBER = "member_"; 
		
		static String START = "start";
		
		static String HOST  =  "127.0.0.1:2181";
		
		int sum = 0;

		public MySyncQueue(int size) {
			super(HOST, SYNC_ROOT, size, START);
		}

		@Override
		public Integer get(int size) {
			System.out.println(Thread.currentThread().getName()+": get ..., size="+size);
			sum += size;
			return new Integer(sum);
		}
		
		public void submit(){
			try {
				super.submit(MEMBER);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void prepare() {
			for(int i=0; i<10; i++){
				sum += i;
			}
		}
		
	}

}
