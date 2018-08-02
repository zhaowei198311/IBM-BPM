package com.desmart.desmartsystem.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.controller.JobController;
import com.desmart.desmartsystem.entity.JobEntity;
import com.desmart.desmartsystem.service.QuartzService;

//@Service
public class QuartzServiceImpl implements QuartzService{
	
	private static Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);


	@Autowired
	private Scheduler scheduler;// 获取调度器
	
	public void addJob(JobEntity jobEntity, HttpServletResponse response,JobController jobController) {
		try {
			Class s=Class.forName(jobEntity.getClazz());
			// 创建一项作业
			JobDetail job = JobBuilder.newJob(s)
			.withIdentity(jobEntity.getJobName(), jobEntity.getJobGroupName())
			.build();
			
			/*==========Start===========================================================*/
			//测试传参
			String msg="枭雄";
			//方法一
			job.getJobDataMap().put("message", msg);
			//方法二
			scheduler.getContext().put("com.test.controller.JobController", jobController);
			/*===========End==========================================================*/
			
			// 创建一个触发器
			CronTrigger trigger = TriggerBuilder.newTrigger()
			.withIdentity(jobEntity.getJobName(), jobEntity.getJobGroupName())
			.withSchedule(CronScheduleBuilder.cronSchedule(jobEntity.getCronExpr()))
			.build();
			// 告诉调度器使用该触发器来安排作业
			scheduler.scheduleJob(job, trigger);
			
			// 启动
			if(!scheduler.isShutdown()){
				scheduler.start();
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
