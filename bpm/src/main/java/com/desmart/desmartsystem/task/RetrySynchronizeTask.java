package com.desmart.desmartsystem.task;


import com.desmart.desmartbpm.service.SynchronizeTaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 拉取未同步成功的任务
 */
public class RetrySynchronizeTask implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SynchronizeTaskService synchronizeTaskService = wac.getBean(SynchronizeTaskService.class);
		synchronizeTaskService.retrySynchronizeTask();
	}
	
}
