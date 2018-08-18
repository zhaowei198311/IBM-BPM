package com.desmart.desmartbpm.entity;

import com.desmart.common.constant.EntityIdPrefix;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * mq执行触发器产生的异常类
 * @author zbw
 *
 */
public class DhTriggerException implements Serializable{
	private static final long serialVersionUID = -7803852253465310676L;
	/** 状态：步骤执行出错 */
	public static final String STATUS_ERROR_IN_STEP = "inStep";
	/** 状态：提交出错 */
	public static final String STATUS_ERROR_WHEN_SUBMIT = "whenSubmit";
	/** 状态：提交成功后出错  */
	public static final String STATUS_ERROR_AFTER_SUBMIT = "afterSubmit";
	/** 状态：已修复 */
	public static final String STATUS_DONE = "done";

	private String id;  //id主键
	private String taskUid;  // 任务主键
	private String stepUid;   //步骤id
	private String insUid;    //流程实例主键
	private String mqMessage;   //mq内容
	private String errorMessage;  //错误内容
	private String dilUid;   // 接口调用日志主键
	private String status; //异常数据状态
	private Integer retryCount; // 重试次数 初始为0
	private Date createTime; //异常产生的时间
	private Date lastRetryTime; // 最后重试时间

	public DhTriggerException(){}



	public String getId() {
		return id;
	}

	public DhTriggerException setId(String id) {
		this.id = id;
		return this;
	}

	public String getTaskUid() {
		return taskUid;
	}

	public DhTriggerException setTaskUid(String taskUid) {
		this.taskUid = taskUid;
		return this;
	}

	public String getStepUid() {
		return stepUid;
	}

	public DhTriggerException setStepUid(String stepUid) {
		this.stepUid = stepUid;
		return this;
	}

	public String getInsUid() {
		return insUid;
	}

	public DhTriggerException setInsUid(String insUid) {
		this.insUid = insUid;
		return this;
	}

	public String getMqMessage() {
		return mqMessage;
	}

	public DhTriggerException setMqMessage(String mqMessage) {
		this.mqMessage = mqMessage;
		return this;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public DhTriggerException setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public String getDilUid() {
		return dilUid;
	}

	public DhTriggerException setDilUid(String dilUid) {
		this.dilUid = dilUid;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public DhTriggerException setStatus(String status) {
		this.status = status;
		return this;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public DhTriggerException setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public DhTriggerException setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getLastRetryTime() {
		return lastRetryTime;
	}

	public DhTriggerException setLastRetryTime(Date lastRetryTime) {
		this.lastRetryTime = lastRetryTime;
		return this;
	}

}
