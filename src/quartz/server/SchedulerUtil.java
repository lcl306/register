package quartz.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerUtil {
	
	void task(String timeFormat, Class job, int jobPosition)throws SchedulerException, ParseException{
		 SchedulerFactory factory = new StdSchedulerFactory();
		 Scheduler sd = factory.getScheduler();
		 JobDetail detail = new JobDetail("jobDetail" +jobPosition, "jobDetailGroup1", job);
		 CronTrigger trigger = new CronTrigger("cronTrigger", "triggerGroup" +jobPosition);
		 CronExpression expression = new CronExpression(timeFormat);
		 trigger.setCronExpression(expression);
		 sd.scheduleJob(detail, trigger);
		 sd.start();
	 }
	
	
	Map getTaskMap(String fileName)throws FileNotFoundException, IOException{
		Map<String, String[]> taskMap = new HashMap<String, String[]>();
		FileInputStream in = null;
		BufferedReader reader = null;
		InputStreamReader isr = null;
		try{
			in = new FileInputStream(fileName);
			isr = new InputStreamReader(in);
			reader = new BufferedReader(isr);
			String line = "";
			while((line=reader.readLine())!=null){
				String[] info = regexLine(line);
				taskMap.put(line, info);
			}
		}finally{
			if(reader!=null)reader.close();
			if(isr!=null)isr.close();
			if(in!=null)in.close();
		}
		return taskMap;
	}
	
	private String[] regexLine(String line){
		String[] rtn = line.split("=");
		return rtn;
	}
	
	String getTimeFormat(String str){
		return str;
	}

}
