package com.desmart.desmartsystem.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartbpm.service.AutoCommitSystemTaskService;

public class AutoCommitSystemDelayTask implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		AutoCommitSystemTaskService autoCommitSystemTaskService = wac.getBean(AutoCommitSystemTaskService.class);
		autoCommitSystemTaskService.startAutoCommitSystemDelayTask();

	}

}
