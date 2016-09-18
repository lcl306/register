package zookeeper.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.zookeeper.KeeperException;

import zookeeper.util.SyncQueue;

public class TestSyncQueue {
	
	static int POOL_SIZE = 5;
	
	static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE); 

	public static void main(String[] args) {
		new TestSyncQueue.MySyncQueue(POOL_SIZE).clear();
		List<Future<Integer>> list = new ArrayList<>();
		for(int i=0; i<POOL_SIZE; i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Future<Integer> f = executor.submit(new Callable<Integer>(){
				@Override
				public Integer call() throws Exception {
					return new TestSyncQueue.MySyncQueue(POOL_SIZE).submit();
				}
			});
			list.add(f);
		}
		int sum = 0;
		for(Future<Integer> f : list){
			 try {
				sum +=f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------total=" +sum);
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
		
		public Integer submit(){
			try {
				return super.submit(MEMBER);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public void prepare() {
			for(int i=0; i<10; i++){
				sum += i;
			}
		}
		
	}

}
