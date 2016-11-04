package log4j;

import java.util.concurrent.atomic.AtomicInteger;

public class LogControl {
	
	//计数周期
	private static final long INTERVAL = 1000;

	//不写日志的时间
	private static final long PUNISH_TIME = 5000;
	
	//阈值
	private static final long ERROR_THRESHOLD = 100;
	
	private static AtomicInteger count;
	
	private static long beginTime;
	
	private static long punishTimeEnd;
	
	public static boolean isLog(){
		boolean rtn = true;
		if(punishTimeEnd>0 && punishTimeEnd<System.currentTimeMillis()){
			rtn = false;
		}else{
			if(count.getAndIncrement()==0){
				beginTime = System.currentTimeMillis();
				rtn = true;
			}else{
				if(count.get()>ERROR_THRESHOLD){
					count.set(0);
					punishTimeEnd = PUNISH_TIME+System.currentTimeMillis();
					rtn = false;
				}else if(System.currentTimeMillis()>(beginTime+INTERVAL)){
					count.set(0);
				}
			}
		}
		return rtn;
	}
}
