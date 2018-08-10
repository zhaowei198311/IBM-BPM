package com.desmart.desmartsystem.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartbpm.service.AutoMailNotifyService;
import com.desmart.desmartbpm.service.SynchronizeTaskService;

public class AutoMailNotifyTask implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		AutoMailNotifyService autoMailNotifyService = wac.getBean(AutoMailNotifyService.class);
		autoMailNotifyService.timeoutMailNotify();
	}

}
