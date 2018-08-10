package com.desmart.desmartsystem.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartbpm.service.AutoCommitService;
import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartbpm.service.impl.AutoCommitServiceImpl;

public class AutoCommitTask implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		AutoCommitService AutoCommitService = wac.getBean(AutoCommitService.class);
		AutoCommitService.startAutoCommit();
	}

}
