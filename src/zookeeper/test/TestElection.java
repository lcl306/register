package zookeeper.test;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;

import zookeeper.util.BaseWatcher;
import zookeeper.util.Election;

public class TestElection {
	
	static int POOL_SIZE = 5;
	
	static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE); 

	public static void main(String[] args) {
		test(false);
	}
	
	static void test(boolean order){
		new TestElection.MyElection().clear();
		for(int i=0; i<POOL_SIZE; i++){
			if(order){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			executor.execute(new Runnable(){
				@Override
				public void run() {
					MyElection my = new TestElection.MyElection();
					my.findLeader();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					my.delete();
				}
			});
		}
		executor.shutdown();
	}
	
	static class MyElection extends Election{
		
		static String E_ROOT = "/election";
		
		String LEADER = "leader"; 
		
		static String HOST  =  "127.0.0.1:2181";
		
		public MyElection(){
			super(HOST, E_ROOT);
		}

		@Override
		public void following(byte[] host) {
			try {
				System.out.println(Thread.currentThread().getName() +" following..." +new String(host,BaseWatcher.CODING));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void leading() {
			System.out.println(Thread.currentThread().getName() +" leading...");
		}
		
		public void findLeader(){
			try {
				super.findLeader(LEADER);
			} catch (UnknownHostException | InterruptedException | KeeperException e) {
				e.printStackTrace();
			}
		}
		
		public void delete(){
			try {
				super.delete("/"+LEADER);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
