/**
 * 
 */
package com.desmart.desmartportal.entity;

import java.util.Date;

import com.desmart.desmartsystem.entity.SysUser;

/**  
* <p>Title: 任务实例实体类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public class DhTaskInstance {
    /** 普通任务 */
	public static final String TYPE_NORMAL = "normal";
	/** 简单循环任务 */
    public static final String TYPE_SIMPLE_LOOP = "simpleLoop";
    /** 多实例循环任务 */
    public static final String TYPE_MULT_IINSTANCE_LOOP = "multiInstanceLoop";
    /** 传阅任务 */
    public static final String TYPE_TRANSFER = "transfer";
    /** 加签任务 */
    public static final String TYPE_ADD = "add";
    /** 普通加签任务，一人完成即可 */
    public static final String TYPE_NORMAL_ADD = "normalAdd";
    /** 循环加签任务，顺序完成  */
    public static final String TYPE_SIMPLE_LOOPADD = "simpleLoopAdd";
    /** 并行加签任务 */
    public static final String TYPE_MULTI_INSTANCE_LOOPADD = "multiInstanceLoopAdd";
    /** 暂停  */
    public static final String STATUS_WAIT_ADD = "-2";
    
    
	/**
	 *  新任务
	 */
	public static final String 	STATUS_NEW = "11";
	
	/**
	 *  接收到任务
	 */
	public static final String 	STATUS_RECEIVED = "12";
	
	/**
	 *  回复任务
	 */
	public static final String 	STATUS_REPLIED = "13";
	
	/**
	 *  转发任务
	 */
	public static final String 	STATUS_FORWARDED = "14";
	
	/**
	 *  传送任务
	 */
	public static final String 	STATUS_SENT = "21";
	
	/**
	 *  
	 */
	public static final String 	STATUS_ACTIONED = "31";
	
	/**
	 *  任务关闭
	 */
	public static final String 	STATUS_CLOSED = "32";
	
	/**
	 *  任务废弃
	 */
	public static final String 	STATUS_DISCARD = "-1";
	
	/**
	 * 
	 */
	public static final String 	STATUS_SPECIAL = "41";
	
	/**
	 *  删除任务
	 */
	public static final String 	STATUS_DELETED = "91";
	
	/**
	 *  警报任务
	 */
	public static final String 	STATUS_ALERT = "61";
	
	/**
	 *  帮助请求任务
	 */
	public static final String 	STATUS_HELP_REQUEST = "62";
	
	/**
	 *  说明任务
	 */
	public static final String 	STATUS_COMMENT = "63";
	
	/**
	 *  已应答帮助请求任务
	 */
	public static final String 	STATUS_ANSWERED_HELP_REQUEST = "65";
	
	/**
	 *  忽略帮助请求任务
	 */
	public static final String 	STATUS_IGNORED_HELP_REQUEST = "66";
	
	/**
	 *  继承任务
	 */
	public static final String 	STATUS_FOLLOWED = "70";
	
	/**
	 *  标记任务
	 */
	public static final String 	STATUS_TAGGED = "71";
	
	/**
	 *  
	 */
	public static final String 	STATUS_COLLABORATION = "72";
	
	
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
	
	private String taskDelegateUser; // 任务代理人
	
	private Integer synNumber; // 同步编号，记录任务同步
	
	private DhProcessInstance dhProcessInstance; // 流程实例
	
	private SysUser sysUser; // 用户实例
	
	private String fromTaskUid; // 从属哪个任务id

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
	
	public DhTaskInstance() {
		
	}
	
	public String getTaskDelegateUser() {
        return taskDelegateUser;
    }

    public void setTaskDelegateUser(String taskDelegateUser) {
        this.taskDelegateUser = taskDelegateUser;
    }

    public Integer getSynNumber() {
        return synNumber;
    }

    public void setSynNumber(Integer synNumber) {
        this.synNumber = synNumber;
    }

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
	public String getFromTaskUid() {
		return fromTaskUid;
	}

	public void setFromTaskUid(String fromTaskUid) {
		this.fromTaskUid = fromTaskUid;
	}

	@Override
	public String toString() {
		return "DhTaskInstance [taskUid=" + taskUid + ", insUid=" + insUid + ", taskId=" + taskId + ", usrUid=" + usrUid
				+ ", activityBpdId=" + activityBpdId + ", taskType=" + taskType + ", taskStatus=" + taskStatus
				+ ", taskTitle=" + taskTitle + ", insUpdateDate=" + insUpdateDate + ", taskPreviousUsrUid="
				+ taskPreviousUsrUid + ", taskPreviousUsrUsername=" + taskPreviousUsrUsername + ", taskDelegateDate="
				+ taskDelegateDate + ", taskInitDate=" + taskInitDate + ", taskFinishDate=" + taskFinishDate
				+ ", taskDueDate=" + taskDueDate + ", taskRiskDate=" + taskRiskDate + ", taskPriority=" + taskPriority
				+ ", taskData=" + taskData + ", taskDelegateUser=" + taskDelegateUser + ", synNumber=" + synNumber
				+ ", fromTaskUid=" + fromTaskUid + "]";
	}

	public DhProcessInstance getDhProcessInstance() {
		return dhProcessInstance;
	}

	public void setDhProcessInstance(DhProcessInstance dhProcessInstance) {
		this.dhProcessInstance = dhProcessInstance;
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
	 * @param taskDelegateUser
	 * @param synNumber
	 * @param dhProcessInstance
	 * @param sysUser
	 */
	public DhTaskInstance(String taskUid, String insUid, int taskId, String usrUid, String activityBpdId,
			String taskType, String taskStatus, String taskTitle, Date insUpdateDate, String taskPreviousUsrUid,
			String taskPreviousUsrUsername, Date taskDelegateDate, Date taskInitDate, Date taskFinishDate,
			Date taskDueDate, Date taskRiskDate, String taskPriority, String taskData, String taskDelegateUser,
			Integer synNumber, DhProcessInstance dhProcessInstance, SysUser sysUser, String fromTaskUid) {
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
		this.taskDelegateUser = taskDelegateUser;
		this.synNumber = synNumber;
		this.dhProcessInstance = dhProcessInstance;
		this.sysUser = sysUser;
		this.fromTaskUid = fromTaskUid;
	}

}
