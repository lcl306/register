package async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TestFuturePool {
	
	public static void main(String[] args) throws Exception{
		Future<Integer> f = FuturePool.submit(new TestFuturePool.Task());
		Future<Integer> f2 = FuturePool.submit(new TestFuturePool.Task());
		Future<Integer> f3 = FuturePool.submit(new TestFuturePool.Task());
		System.out.println("计算开始");
		Integer sum = FuturePool.get(f);
		sum +=  FuturePool.get(f2);
		sum +=  FuturePool.get(f3);
		System.out.println("计算结果： " +sum);
	}
	
	static class Task implements Callable<Integer>{
		
		@Override
		public Integer call() throws Exception{
			Integer sum = 0; 
			for(int i=0; i<10; i++){
				sum += i;
			}
			Thread.sleep(3000);
			return sum;
		}
	}

}
