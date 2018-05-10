/**
 * 
 */
package com.desmart.desmartportal.entity;

import java.util.Date;

/**  
* <p>Title: 任务实例实体类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public class TaskInstance {
	
	private String taskUid; // 任务id(主键)

	private String insUid; // 流程实例主键
	
	private int taskId; // 任务实例序号(流程引擎)
	
	private String usrUid; // 处理人

	private String activityBpdId; // 流程图上元素id

	private String taskType; // 任务实例类型：SIGN；NORMAL；TRANSFER
	
	private String taskStatus; // 任务实例状态

	private String taskTitle; // 流程任务标题

	private Date insUpdateDate; // 流程实例更新日期

	private String taskPreviousUsrUid; // 前一个用户ID

	private String taskPreviousUsrUsername; // 前一个用户名称

	private Date  taskDelegateDate; // 授权日期

	private Date taskInitDate; // 初始日期

	private Date taskFinishDate; // 任务实例完成日期

	private Date taskDueDate; // 截至日期

	private Date taskRiskDate; // 风险日期

	private String taskPriority; // 优先级

	private String taskData; // 任务提交的数据

	/**
	 * @return the taskUid
	 */
	public String getTaskUid() {
		return taskUid;
	}

	/**
	 * @param taskUid the taskUid to set
	 */
	public void setTaskUid(String taskUid) {
		this.taskUid = taskUid;
	}

	/**
	 * @return the insUid
	 */
	public String getInsUid() {
		return insUid;
	}

	/**
	 * @param insUid the insUid to set
	 */
	public void setInsUid(String insUid) {
		this.insUid = insUid;
	}

	/**
	 * @return the taskId
	 */
	public int getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the usrUid
	 */
	public String getUsrUid() {
		return usrUid;
	}

	/**
	 * @param usrUid the usrUid to set
	 */
	public void setUsrUid(String usrUid) {
		this.usrUid = usrUid;
	}

	/**
	 * @return the activityBpdId
	 */
	public String getActivityBpdId() {
		return activityBpdId;
	}

	/**
	 * @param activityBpdId the activityBpdId to set
	 */
	public void setActivityBpdId(String activityBpdId) {
		this.activityBpdId = activityBpdId;
	}

	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}

	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	/**
	 * @return the taskStatus
	 */
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the taskTitle
	 */
	public String getTaskTitle() {
		return taskTitle;
	}

	/**
	 * @param taskTitle the taskTitle to set
	 */
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	/**
	 * @return the insUpdateDate
	 */
	public Date getInsUpdateDate() {
		return insUpdateDate;
	}

	/**
	 * @param insUpdateDate the insUpdateDate to set
	 */
	public void setInsUpdateDate(Date insUpdateDate) {
		this.insUpdateDate = insUpdateDate;
	}

	/**
	 * @return the taskPreviousUsrUid
	 */
	public String getTaskPreviousUsrUid() {
		return taskPreviousUsrUid;
	}

	/**
	 * @param taskPreviousUsrUid the taskPreviousUsrUid to set
	 */
	public void setTaskPreviousUsrUid(String taskPreviousUsrUid) {
		this.taskPreviousUsrUid = taskPreviousUsrUid;
	}

	/**
	 * @return the taskPreviousUsrUsername
	 */
	public String getTaskPreviousUsrUsername() {
		return taskPreviousUsrUsername;
	}

	/**
	 * @param taskPreviousUsrUsername the taskPreviousUsrUsername to set
	 */
	public void setTaskPreviousUsrUsername(String taskPreviousUsrUsername) {
		this.taskPreviousUsrUsername = taskPreviousUsrUsername;
	}

	/**
	 * @return the taskDelegateDate
	 */
	public Date getTaskDelegateDate() {
		return taskDelegateDate;
	}

	/**
	 * @param taskDelegateDate the taskDelegateDate to set
	 */
	public void setTaskDelegateDate(Date taskDelegateDate) {
		this.taskDelegateDate = taskDelegateDate;
	}

	/**
	 * @return the taskInitDate
	 */
	public Date getTaskInitDate() {
		return taskInitDate;
	}

	/**
	 * @param taskInitDate the taskInitDate to set
	 */
	public void setTaskInitDate(Date taskInitDate) {
		this.taskInitDate = taskInitDate;
	}

	/**
	 * @return the taskFinishDate
	 */
	public Date getTaskFinishDate() {
		return taskFinishDate;
	}

	/**
	 * @param taskFinishDate the taskFinishDate to set
	 */
	public void setTaskFinishDate(Date taskFinishDate) {
		this.taskFinishDate = taskFinishDate;
	}

	/**
	 * @return the taskDueDate
	 */
	public Date getTaskDueDate() {
		return taskDueDate;
	}

	/**
	 * @param taskDueDate the taskDueDate to set
	 */
	public void setTaskDueDate(Date taskDueDate) {
		this.taskDueDate = taskDueDate;
	}

	/**
	 * @return the taskRiskDate
	 */
	public Date getTaskRiskDate() {
		return taskRiskDate;
	}

	/**
	 * @param taskRiskDate the taskRiskDate to set
	 */
	public void setTaskRiskDate(Date taskRiskDate) {
		this.taskRiskDate = taskRiskDate;
	}

	/**
	 * @return the taskPriority
	 */
	public String getTaskPriority() {
		return taskPriority;
	}

	/**
	 * @param taskPriority the taskPriority to set
	 */
	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}

	/**
	 * @return the taskData
	 */
	public String getTaskData() {
		return taskData;
	}

	/**
	 * @param taskData the taskData to set
	 */
	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}
	
	public TaskInstance() {
		
	}
	
	/**
	 * @param taskUid
	 * @param insUid
	 * @param taskId
	 * @param usrUid
	 * @param activityBpdId
	 * @param taskType
	 * @param taskStatus
	 * @param taskTitle
	 * @param insUpdateDate
	 * @param taskPreviousUsrUid
	 * @param taskPreviousUsrUsername
	 * @param taskDelegateDate
	 * @param taskInitDate
	 * @param taskFinishDate
	 * @param taskDueDate
	 * @param taskRiskDate
	 * @param taskPriority
	 * @param taskData
	 */
	public TaskInstance(String taskUid, String insUid, int taskId, String usrUid, String activityBpdId, String taskType,
			String taskStatus, String taskTitle, Date insUpdateDate, String taskPreviousUsrUid,
			String taskPreviousUsrUsername, Date taskDelegateDate, Date taskInitDate, Date taskFinishDate,
			Date taskDueDate, Date taskRiskDate, String taskPriority, String taskData) {
		super();
		this.taskUid = taskUid;
		this.insUid = insUid;
		this.taskId = taskId;
		this.usrUid = usrUid;
		this.activityBpdId = activityBpdId;
		this.taskType = taskType;
		this.taskStatus = taskStatus;
		this.taskTitle = taskTitle;
		this.insUpdateDate = insUpdateDate;
		this.taskPreviousUsrUid = taskPreviousUsrUid;
		this.taskPreviousUsrUsername = taskPreviousUsrUsername;
		this.taskDelegateDate = taskDelegateDate;
		this.taskInitDate = taskInitDate;
		this.taskFinishDate = taskFinishDate;
		this.taskDueDate = taskDueDate;
		this.taskRiskDate = taskRiskDate;
		this.taskPriority = taskPriority;
		this.taskData = taskData;
	}
}
