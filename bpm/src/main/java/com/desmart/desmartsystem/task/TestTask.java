package com.desmart.desmartsystem.task;


import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class TestTask implements Job {
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long ms = System.currentTimeMillis(); 
        System.out.println("测试打印输出" + new Date(ms));  	
	}
	
}
