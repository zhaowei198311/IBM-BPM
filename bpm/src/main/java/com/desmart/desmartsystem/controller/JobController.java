package com.desmart.desmartsystem.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.JobEntity;
import com.desmart.desmartsystem.entity.QrtzJobDetails;
import com.desmart.desmartsystem.service.DhInterfaceService;
import com.desmart.desmartsystem.service.QrtzJobDetailsService;
import com.desmart.desmartsystem.service.QuartzService;
import com.desmart.desmartsystem.util.DatagridForLayUI;
import com.desmart.desmartsystem.util.Json;

//@Controller
@RequestMapping("/quarz")
public class JobController {

	@Autowired
	private QuartzService quartzService;

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private QrtzJobDetailsService qrtzJobDetailsService;

	@RequestMapping(value = "/quarzList")
	public String quarzList() {
		return "desmartsystem/usermanagement/Quarz/QuarzList";
	}

	@RequestMapping(value = "/quarzCron")
	public String QuarzCron() {
		return "desmartsystem/usermanagement/Quarz/QuarzCron";
	}

	@RequestMapping(value = "/quarzAdd")
	public String quarzAdd() {
		return "desmartsystem/usermanagement/Quarz/QuarzAdd";
	}

	@RequestMapping(value = "/quarzDetail")
	public String quarzDetail() {
		return "desmartsystem/usermanagement/Quarz/QuarzDetail";
	}

	@RequestMapping(value = "/quarzEdit")
	public String quarzEdit() {
		return "desmartsystem/usermanagement/Quarz/QuarzEdit";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Json addJob(JobEntity job, HttpServletResponse response) {
		Json j = new Json();

		try {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpr());
		} catch (Exception e) {
			j.setSuccess(false);
			e.printStackTrace();
			j.setMsg("cron表达式有误，不能被解析！");
			return j;
		}

		QrtzJobDetails qrtzJobDetails = new QrtzJobDetails();
		qrtzJobDetails.setJobGroup(job.getJobGroupName());
		qrtzJobDetails.setJobName(job.getJobName());

		QrtzJobDetails qrtzJobDetail = qrtzJobDetailsService.select(qrtzJobDetails);
		if (qrtzJobDetail != null) {
			j.setSuccess(false);
			j.setMsg("同一个类型下的任务名称必须唯一!");
			return j;
		}

		try {
			quartzService.addJob(job, response, new JobController());
			j.setSuccess(true);
			j.setMsg("新增成功");
			j.setObj(job);
		} catch (Exception e) {
			j.setSuccess(false);
			e.printStackTrace();
			j.setMsg("添加失败:" + e.getMessage());
		}
		return j;
	}

	/**
	 * 获取table
	 * 
	 * @param model
	 * @return
	 * @throws SchedulerException
	 */
	@RequestMapping("datagrid.do")
	@ResponseBody
	public DatagridForLayUI datagrid(JobEntity jobEntity, int page, int limit) throws SchedulerException {

		// page 当前也
		// limit 每页显示记录数

		List<JobEntity> jobInfos = new ArrayList<JobEntity>();

		List<String> triggerGroupNames = scheduler.getTriggerGroupNames();

		for (String triggerGroupName : triggerGroupNames) {
			Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));
			for (TriggerKey triggerKey : triggerKeySet) {
				Trigger t = scheduler.getTrigger(triggerKey);
				if (t instanceof CronTrigger) {
					CronTrigger trigger = (CronTrigger) t;
					JobKey jobKey = trigger.getJobKey();
					JobDetail jd = scheduler.getJobDetail(jobKey);
					JobEntity jobInfo = new JobEntity();
					jobInfo.setJobName(jobKey.getName());
					jobInfo.setJobGroupName(jobKey.getGroup());
					jobInfo.setTriggerName(triggerKey.getName());
					jobInfo.setTriggerGroupName(triggerKey.getGroup());
					jobInfo.setCronExpr(trigger.getCronExpression());
					jobInfo.setNextFireTime(trigger.getNextFireTime());
					jobInfo.setPreviousFireTime(trigger.getPreviousFireTime());
					jobInfo.setStartTime(trigger.getStartTime());
					jobInfo.setEndTime(trigger.getEndTime());
					jobInfo.setJobClass(jd.getJobClass().getCanonicalName());
					jobInfo.setClazz(jd.getJobClass().getCanonicalName());

					if (StringUtils.isNotBlank(jd.getDescription())) {
						jobInfo.setDuration(Long.parseLong(jd.getDescription()));
					}
					;

					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					jobInfo.setJobStatus(triggerState.toString());// NONE无,
																	// NORMAL正常,
																	// PAUSED暂停,
																	// COMPLETE完成,
																	// ERROR错误,
																	// BLOCKED阻塞
					jobInfos.add(jobInfo);
				}
			}
		}
		DatagridForLayUI datagridForLayUI = new DatagridForLayUI();

		// 每页开始数
		int star = (page - 1) * limit;
		// 设置总页数
		int count = jobInfos.size();

		datagridForLayUI.setCount(Long.valueOf(count));
		datagridForLayUI.setData(jobInfos.subList(star, count - star > limit ? star + limit : count));
		return datagridForLayUI;
	}

	/**
	 * 暂停任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @return
	 */
	@RequestMapping("/pauseJob")
	@ResponseBody
	public Json pauseJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName,
			HttpServletRequest request) {
		Json j = new Json();
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			scheduler.pauseJob(jobKey);
			j.setSuccess(true);
			j.setMsg("暂停成功！");
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			j.setSuccess(false);
			j.setMsg("暂停失败:" + e.getMessage());
			e.printStackTrace();
		}
		return j;
	}

	/**
	 * 启动任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @return
	 */
	@RequestMapping("/resumeJob")
	@ResponseBody
	public Json resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName,
			HttpServletRequest request) {
		Json j = new Json();
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			scheduler.resumeJob(jobKey);
			j.setSuccess(true);
			j.setMsg("启动成功！");
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			j.setSuccess(false);
			j.setMsg("启动失败:" + e.getMessage());
			e.printStackTrace();
		}
		return j;
	}

	/**
	 * 删除任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * @return
	 */
	@RequestMapping("/remove")
	@ResponseBody
	public Json remove(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName,
			@RequestParam("triggerName") String triggerName, @RequestParam("triggerGroupName") String triggerGroupName,
			HttpServletRequest request) {
		Json j = new Json();
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 删除任务
			scheduler.deleteJob(jobKey);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("删除失败:" + e.getMessage());
			e.printStackTrace();
		}
		return j;
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEditJob")
	public String toEditJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName,
			Model model, HttpServletRequest request) {
		String method = request.getMethod();
		JobEntity jobEntity = new JobEntity();
		try {
			if ("GET".equals(method)) {
				jobName = new String(jobName.getBytes("ISO-8859-1"), "UTF-8");
				jobGroupName = new String(jobGroupName.getBytes("ISO-8859-1"), "UTF-8");
			}
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			List<CronTrigger> triggers = (List<CronTrigger>) scheduler.getTriggersOfJob(jobKey);
			CronTrigger trigger = triggers.get(0);
			TriggerKey triggerKey = trigger.getKey();
			String cron = trigger.getCronExpression();
			jobEntity.setJobName(jobKey.getName());
			jobEntity.setJobGroupName(jobKey.getGroup());
			jobEntity.setTriggerName(triggerKey.getName());
			jobEntity.setTriggerGroupName(triggerKey.getGroup());
			jobEntity.setCronExpr(cron);
			jobEntity.setClazz(jobDetail.getJobClass().getCanonicalName());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("jobEntity", jobEntity);
		return "forward:quartzs/edit.jsp";
	}

	/**
	 * 编辑
	 * 
	 * @param jobEntity
	 * @param response
	 * @return
	 */
	@RequestMapping("/editJob")
	@ResponseBody
	public Json editJob(JobEntity jobEntity, HttpServletResponse response) {
		Json j = new Json();
		//添加job名称验证
		QrtzJobDetails qrtzJobDetails = new QrtzJobDetails();
		qrtzJobDetails.setJobGroup(jobEntity.getJobGroupName());
		qrtzJobDetails.setJobName(jobEntity.getJobName());
		
		
		String 	oldjobName = jobEntity.getOldjobName();

		QrtzJobDetails qrtzJobDetail = qrtzJobDetailsService.select(qrtzJobDetails);
		if (qrtzJobDetail != null) {
			String jobName =  qrtzJobDetail.getJobName();
			if(!oldjobName.equals(jobName)) {
				j.setSuccess(false);
				j.setMsg("同一个类型下的任务名称必须唯一!");
				return j;
			}
		}
		
		
		TriggerKey oldTriggerKey = TriggerKey.triggerKey(jobEntity.getOldtriggerName(), jobEntity.getOldtriggerGroup());
		JobKey jobKey = JobKey.jobKey(jobEntity.getOldjobName(), jobEntity.getOldjobGroupName());
		
		try {
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(oldTriggerKey);
			if (trigger != null) {
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				// 停止触发器
				scheduler.pauseTrigger(oldTriggerKey);
				// 移除触发器
				scheduler.unscheduleJob(oldTriggerKey);
				// 删除任务
				scheduler.deleteJob(jobKey);
				quartzService.addJob(jobEntity, response, new JobController());
				JobKey jobKey1 = JobKey.jobKey(jobEntity.getJobName(), jobEntity.getJobGroupName());
				if (jobEntity.getJobStatus().equals("PAUSED")) {
					scheduler.pauseJob(jobKey1);// 暂停
				}
				j.setSuccess(true);
				j.setMsg("修改成功！");
			} else {
				j.setSuccess(false);
				j.setMsg("修改失败！");
			}

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return j;
	}

	public void test(String msg) {
		System.out.println(msg + "---------------进入controller--------------------");
	}
}
