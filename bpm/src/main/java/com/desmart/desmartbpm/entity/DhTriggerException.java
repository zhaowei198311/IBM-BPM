package com.desmart.desmartbpm.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * mq执行触发器产生的异常类
 * @author zbw
 *
 */
public class DhTriggerException implements Serializable{
	
	private static final long serialVersionUID = -7803852253465310676L;

	private String id;  //id主键
	
	private String taskId;   //任务id
	
	private String stepId;   //步骤id
	
	private String insId;    //流程实例id
	
	private Date  createTime; //异常产生的时间
	
	private String mqMessage;   //mq内容
	
	private String errorMessage;  //错误内容
	
	private String requestParam;  //请求参数
	
	private String status;//异常数据状态
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getInsId() {
		return insId;
	}

	public void setInsId(String insId) {
		this.insId = insId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMqMessage() {
		return mqMessage;
	}

	public void setMqMessage(String mqMessage) {
		this.mqMessage = mqMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
