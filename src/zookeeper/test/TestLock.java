package zookeeper.test;

import zookeeper.util.Lock;

public class TestLock {
	
	public static void main(String[] args)throws Exception{

		new Thread(new Runnable(){
			@Override
			public void run() {
				new TestLock.MyLock("127.0.0.1:2181").init();
			}
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				new TestLock.MyLock("127.0.0.1:2181").init();
			}
		}).start();

	}
	
	static class MyLock extends Lock{

		public MyLock(String host) {
			super(host);
		}

		@Override
		public void doAction() {
			System.out.println(Thread.currentThread().getName()+": 执行任务");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.release();
		}
	}

}
