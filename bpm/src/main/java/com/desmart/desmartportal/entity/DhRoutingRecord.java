package com.desmart.desmartportal.entity;

import java.sql.Timestamp;
import java.util.Date;

public class DhRoutingRecord {
    /**流转类型 发起流程操作 */
	public static final String ROUTE_TYPE_START_PROCESS = "startProcess";
	/**流转类型 提交任务 */
	public static final String ROUTE_TYPE_SUBMIT_TASK = "submitTask";
	/**流转类型 取回任务 */
	public static final String ROUTE_TYPE_REVOKE_TASK = "revokeTask";
	/**流转类型 抄送 */
	public static final String ROUTE_TYPE_TRANSFER_TASK = "transferTask";
	/**流传类型 驳回 */
	public static final String ROUTE_TYPE_REJECT_TASK = "rejectTask";
	/**流转类型 加签 */
	public static final String ROUTE_TYPE_ADD_TASK = "addTask";
	/**流转类型 加签完成 */
	public static final String ROUTE_TYPE_FINISH_ADDTASK = "finishAddTask";
	/**流转类型 管理员撤转*/
	public static final String ROUTE_TYPE_TRUN_OFF_TASK = "trunOffTask";
	/**  流转类型，自动提交  */
	public static final String ROUTE_TYPE_AUTOCOMMIT = "autoCommit";
	/**  流转类型，完成系统任务  */
	public static final String ROUTE_TYPE_FINISH_SYSTEM_TASK = "finishSystemTask";

    
	private String routeUid;//流转id
	private String insUid;//流程实例uid
	private String activityName;//环节名
	private String routeType;//操作类型
	private String userUid;//操作人uid 
	private Date createTime;//操作时间
	private String activityId;//环节id
	private String activityTo;//流转到的节点activityId  逗号分隔
	private String taskUid;  // 产生这条流转记录的任务

	//不在表内，关联查询
	private String userName;//操作人姓名
	private String station;//操作人岗位身份
	
	private String taskHandleUserId;//任务实例处理人id
	private String taskHandleUserName;//任务实例处理人姓名
	private String agentUserUid;//任务实例代理人id
	private String agentUserName;//任务实例代理人姓名
	
	public String getRouteUid() {
		return routeUid;
	}
	public void setRouteUid(String routeUid) {
		this.routeUid = routeUid;
	}
	public String getInsUid() {
		return insUid;
	}
	public void setInsUid(String insUid) {
		this.insUid = insUid;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityTo() {
		return activityTo;
	}
	public void setActivityTo(String activityTo) {
		this.activityTo = activityTo;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTaskUid() {
		return taskUid;
	}

	public void setTaskUid(String taskUid) {
		this.taskUid = taskUid;
	}
	public String getAgentUserUid() {
		return agentUserUid;
	}
	public void setAgentUserUid(String agentUserUid) {
		this.agentUserUid = agentUserUid;
	}
	public String getAgentUserName() {
		return agentUserName;
	}
	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}
	public String getTaskHandleUserId() {
		return taskHandleUserId;
	}
	public void setTaskHandleUserId(String taskHandleUserId) {
		this.taskHandleUserId = taskHandleUserId;
	}
	public String getTaskHandleUserName() {
		return taskHandleUserName;
	}
	public void setTaskHandleUserName(String taskHandleUserName) {
		this.taskHandleUserName = taskHandleUserName;
	}
}
