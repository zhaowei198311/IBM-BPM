package com.desmart.desmartbpm.entity;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.desmartportal.entity.DhTaskInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * mq执行触发器产生的异常类
 * @author zbw
 *
 */
public class DhTaskException implements Serializable {
	private static final long serialVersionUID = -7803852253465310676L;

	/** 状态：提交前校验出错 */
	public static final String STATUS_CHECK_EXCEPTION = "checkEx";
	/** 状态：步骤执行出错 */
	public static final String STATUS_STEP_EXCEPTION = "stepEx";
	/** 状态：提交出错  */
	public static final String STATUS_COMMIT_EXCEPTION = "commitEx";
	/** 状态：已修复 */
	public static final String STATUS_DONE = "done";

	private String id;            // id主键
	private String taskUid;       // 任务实例主键
	private String stepUid;       // 步骤主键
	private String insUid;        // 流程实例主键
	private String dataForSubmitTask;     // 内容
	private String errorMessage;  // 错误内容
	private String dilUid;        // 接口调用日志主键
	private String status;        // 异常状态 对应常量类使用
	private Integer retryCount;   // 重试次数 初始为0
	private Date createTime;      // 异常产生的时间
	private Date lastRetryTime;   // 最后重试时间

	public DhTaskException(){}


	public static DhTaskException createCommitTaskrException(DhTaskInstance dhTaskInstance, String dataForSubmitTask, String errorMessage) {
		DhTaskException dhTriggerException = new DhTaskException();
		dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + String.valueOf(UUID.randomUUID()))
				.setTaskUid(dhTaskInstance.getTaskUid())
				.setStepUid(null)
				.setInsUid(dhTaskInstance.getInsUid())
				.setDataForSubmitTask(dataForSubmitTask)
				.setErrorMessage(errorMessage)
				.setRetryCount(0)
				.setStatus(DhTaskException.STATUS_COMMIT_EXCEPTION);
		return dhTriggerException;
	}

	public static DhTaskException createCheckTaskException(DhTaskInstance dhTaskInstance, String errorMessage) {
		DhTaskException dhTriggerException = new DhTaskException();
		dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + String.valueOf(UUID.randomUUID()))
				.setTaskUid(dhTaskInstance.getTaskUid())
				.setStepUid(null)
				.setInsUid(dhTaskInstance.getInsUid())
				.setDataForSubmitTask(null)
				.setErrorMessage(errorMessage)
				.setRetryCount(0)
				.setStatus(DhTaskException.STATUS_CHECK_EXCEPTION);
		return dhTriggerException;
	}




	public String getId() {
		return id;
	}

	public DhTaskException setId(String id) {
		this.id = id;
		return this;
	}

	public String getTaskUid() {
		return taskUid;
	}

	public DhTaskException setTaskUid(String taskUid) {
		this.taskUid = taskUid;
		return this;
	}

	public String getStepUid() {
		return stepUid;
	}

	public DhTaskException setStepUid(String stepUid) {
		this.stepUid = stepUid;
		return this;
	}

	public String getInsUid() {
		return insUid;
	}

	public DhTaskException setInsUid(String insUid) {
		this.insUid = insUid;
		return this;
	}

	public String getDataForSubmitTask() {
		return dataForSubmitTask;
	}

	public DhTaskException setDataForSubmitTask(String dataForSubmitTask) {
		this.dataForSubmitTask = dataForSubmitTask;
		return this;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public DhTaskException setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public String getDilUid() {
		return dilUid;
	}

	public DhTaskException setDilUid(String dilUid) {
		this.dilUid = dilUid;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public DhTaskException setStatus(String status) {
		this.status = status;
		return this;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public DhTaskException setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public DhTaskException setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getLastRetryTime() {
		return lastRetryTime;
	}

	public DhTaskException setLastRetryTime(Date lastRetryTime) {
		this.lastRetryTime = lastRetryTime;
		return this;
	}


	public static void main(String[] args){
	    DhTaskInstance dhTaskInstance = new DhTaskInstance();
	    dhTaskInstance.setTaskUid("uid");
	    dhTaskInstance.setInsUid("insUid");
		DhTaskException commitTriggerException = DhTaskException.createCommitTaskrException(dhTaskInstance, "{dfddf}", "ERROR-MESSAGE");
		System.out.println("done");
	}

}
