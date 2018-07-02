package com.desmart.desmartsystem.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartbpm.mongo.InsDataDao;

/**
 * 
 * @ClassName: SynchronizeProcessInstance  
 * @Description: 将查询出的流程实例数据同步到MongoDB insData集合  
 * @author: WUZHUANG  
 * @date: 2018年7月2日  
 *
 */
public class SynchronizeProcessInstance implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		InsDataDao insDataDao = wac.getBean(InsDataDao.class);
		insDataDao.insertInsData();
	}
	
}
