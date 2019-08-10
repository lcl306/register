package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimmerTest {
	
	public static void schedule(){
		Timer timer = new Timer();
		//3秒的task执行完后，比预定完了3秒，period为1秒，之后的task不会追赶3秒，仍有period
		timer.schedule(new TimerTask(){
			int count = 0;	
			@Override
			public void run() {
				count++;
				System.out.println(Thread.currentThread().getName()+"start---"+new Date(System.currentTimeMillis()));
				try {
					if(count==10) Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName()+"end---"+new Date(System.currentTimeMillis()));
			}
		}, 0, 1000);
	}
	
	public static void scheduleFix(){
		Timer timer = new Timer();
		//3秒的task执行完后，比预定完了3秒，period为1秒，之后的task会没有3个period追赶3秒
		timer.scheduleAtFixedRate(new TimerTask(){
			int count = 0;
			@Override
			public void run() {
				count++;
				System.out.println(Thread.currentThread().getName()+"FixStart---"+new Date(System.currentTimeMillis()));
				try {
					if(count==10) Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName()+"FixEnd---"+new Date(System.currentTimeMillis()));
			}
		}, 0, 1000);
	}
	
	public static void multiScheduleFix(){
		//ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		//每period只会从线程池中取1个thread，即便task执行时间超过period，也不会取其他thread
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new Runnable() {
        	int count = 0;
            @Override
            public void run() {
            	count++;
				System.out.println(Thread.currentThread().getName()+"MultiFixStart---"+new Date(System.currentTimeMillis()));
				try {
					if(count==10) Thread.sleep(3000);
					//Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName()+"MultiFixEnd---"+new Date(System.currentTimeMillis()));
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
	}
	
	public static void multiScheduleFuture(){
		try {
            List<Callable> callableList = new ArrayList<>();
            callableList.add(new MyCallableA());
            callableList.add(new MyCallableB());
            //ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
            //MyCallableA和MyCallableB会延迟1秒后，由executorService的2个thread执行
            ScheduledFuture futureA = executorService.schedule(callableList.get(0), 1L, TimeUnit.SECONDS);
            ScheduledFuture futureB = executorService.schedule(callableList.get(1), 1L, TimeUnit.SECONDS);

            System.out.println("            X = " + new Date(System.currentTimeMillis()));
            //主线程会阻塞，至MyCallableA执行完
            System.out.println("返回值A：" + futureA.get());
            System.out.println("返回值B：" + futureB.get());
            System.out.println("            Y = " + new Date(System.currentTimeMillis()));

            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
	}
	
	static class MyCallableA implements Callable<String> {
        @Override
        public String call() throws Exception{
            try {
                System.out.println("callA begin " + Thread.currentThread().getName() + ", " + new Date(System.currentTimeMillis()));
                TimeUnit.SECONDS.sleep(3); // 休眠3秒
                System.out.println("callA end " + Thread.currentThread().getName() + ", " + new Date(System.currentTimeMillis()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "returnA";
        }
    }

    static class MyCallableB implements Callable<String>  {
        @Override
        public String call() throws Exception{
            System.out.println("callB begin " + Thread.currentThread().getName() + ", " + new Date(System.currentTimeMillis()));
            System.out.println("callB end " + Thread.currentThread().getName() + ", " + new Date(System.currentTimeMillis()));
            return "returnB";
        }
    }
	

	public static void main(String[] args) {
		//schedule();
		//scheduleFix();
		//multiScheduleFix();
		multiScheduleFuture();
	}

}
