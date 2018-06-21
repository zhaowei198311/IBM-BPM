package com.desmart.desmartsystem.task;


import com.desmart.desmartbpm.service.SynchronizeTaskService;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

/**
 * 从流程引擎中拉取任务
 */
public class SynchronizeTask implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SynchronizeTaskService synchronizeTaskService = wac.getBean(SynchronizeTaskService.class);
		synchronizeTaskService.synchronizeTaskFromEngine();
	}
	
}
