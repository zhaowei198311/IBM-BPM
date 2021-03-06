package com.desmart.desmartsystem.service;

import javax.servlet.http.HttpServletResponse;

import com.desmart.desmartsystem.controller.JobController;
import com.desmart.desmartsystem.entity.JobEntity;

public interface QuartzService {

	/**
	 * 添加job
	 * @param dhInterface job
	 * @return
	 */
	public void addJob(JobEntity job, HttpServletResponse response,JobController jobController);
}
