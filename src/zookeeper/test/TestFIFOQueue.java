package zookeeper.test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;

import zookeeper.util.BaseWatcher;
import zookeeper.util.FIFOQueue;

public class TestFIFOQueue {
	
	static int POOL_SIZE = 5;
	
	static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE); 

	public static void main(String[] args) {
		waitTest();
		System.out.println("==================================================");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		noWaitTest();
		executor.shutdown();
	}
	
	public static void noWaitTest(){
		// 先有产品
		new TestFIFOQueue.MyFIFOQueue(false).clear();
		test2(false, 3, 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		test2(false, 1, 3); //最后一个消费线程没有data，直接返回空数据
	}
	
	public static void waitTest(){
		// 先有产品
		new TestFIFOQueue.MyFIFOQueue(false).clear();
		test2(true, 3, 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		test2(true, 2, 3);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------------------------------------------");
		//先消费
		new TestFIFOQueue.MyFIFOQueue(false).clear();
		test1(true, 2, 3);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		test1(true, 3, 2);
	}
	
	public static void test1(final boolean wait, int worker, int consumer){
		for(int i=0; i<consumer; i++){
			executor.execute(new Runnable(){

				@Override
				public void run() {
					String name = new TestFIFOQueue.MyFIFOQueue(wait).consume();
					System.out.println(Thread.currentThread().getName()+": " +name);
				}
			});
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0; i<worker; i++){
			executor.execute(new Runnable(){

				@Override
				public void run() {
					new TestFIFOQueue.MyFIFOQueue(wait).produce("产品-"+Thread.currentThread().getName());
				}
			});
		}
	}
	
	public static void test2(final boolean wait, int worker, int consumer){
		for(int i=0; i<worker; i++){
			executor.execute(new Runnable(){

				@Override
				public void run() {
					new TestFIFOQueue.MyFIFOQueue(wait).produce("产品-"+Thread.currentThread().getName());
				}
			});
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0; i<consumer; i++){
			executor.execute(new Runnable(){

				@Override
				public void run() {
					String name = new TestFIFOQueue.MyFIFOQueue(wait).consume();
					System.out.println(Thread.currentThread().getName()+": " +name);
				}
			});
		}
		
	}
	
	public static class MyFIFOQueue extends FIFOQueue{
		
		static String FIFO_ROOT = "/my_FIFO_queue";
		
		static String HOST  =  "127.0.0.1:2181";
		
		static String ELEMENT = "element_";
		
		protected boolean wait;

		public MyFIFOQueue(boolean wait) {
			super(HOST, FIFO_ROOT);
			this.wait = wait;
		}
		
		public String produce(String name){
			try {
				String node = super.produce(ELEMENT, name.getBytes(BaseWatcher.CODING));
				System.out.println("produce node: " +node +"name: "+name);
				return node;
			} catch (UnsupportedEncodingException | KeeperException| InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public String consume(){
			try {
				byte[] data = super.consume(wait);
				if(data!=null){
					return new String(data, BaseWatcher.CODING);
				}else{
					return "no data";
				}
				
			} catch (UnsupportedEncodingException | InterruptedException | KeeperException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}

}
