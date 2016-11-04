package quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("In SimpleQuartzJob - executing its JOB at " 
                + new Date() + " by " + context.getTrigger().getName() +" and " 
                + context.getJobDetail().getName());
		throw new JobExecutionException("throws exception in QuartzJob");
	}

}
