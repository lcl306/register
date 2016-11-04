package quartz.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.quartz.SchedulerException;

import quartz.QuartzJob;

//quartz and commons-collections
public class ScheduleServer {
	
	 private SchedulerUtil su = new SchedulerUtil();
	 private static final String FILE_NAME = "C:/work/eclipse_workspace/register/src/quartz/schedule.properties";
	 private static final String PACKAGE_NAME = "quartz";
	 
	 void task()throws FileNotFoundException, IOException, ClassNotFoundException,
	 	 SchedulerException, ParseException{
		 Map tasks = su.getTaskMap(FILE_NAME);
		 Set keySet = tasks.keySet();
		 int count = 0;
		 for(Iterator it = keySet.iterator(); it.hasNext();){
			 count++;
			 String key = (String)it.next();
			 String[] info = (String[])tasks.get(key);
			 Class jobClass = Class.forName(PACKAGE_NAME +"." +info[0]);
			 String timeFormat = su.getTimeFormat(info[1]);
			 su.task(timeFormat, jobClass, count);
		 }
	 }
	 
	 public static void main(String[] args)throws Exception{
		 //new ScheduleServer().minuteTask();
		 new ScheduleServer().task();
	 }
	 
	 public void minuteTask()throws SchedulerException, ParseException{
		 su.task("0 0/1 * * * ?", QuartzJob.class, 1);
	 }
	 
	 public void fiveSecondTask()throws SchedulerException, ParseException{
		 su.task("0/5 * * * * ?", QuartzJob.class, 2);
	 }
	 
	 public void hourTask()throws SchedulerException, ParseException{
		 su.task("0 0 0/1 * * ?", QuartzJob.class, 3);
	 }
	 
	 public void task1506()throws SchedulerException, ParseException{
		 su.task("0 6 15 * * ?", QuartzJob.class, 4);
	 }

}
