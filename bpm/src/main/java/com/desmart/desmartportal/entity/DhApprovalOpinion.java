
package com.desmart.desmartportal.entity;

import java.sql.Timestamp;
import java.util.Date;

public class DhApprovalOpinion {
	private String aprOpiId;//审批id
	private String insUid;//流程实例id--ins_Uid
	private String taskUid;//任务id--activityId
	private Integer aprOpiIndex;//审批序号
	private String aprUserId;//审批用户id
	private Integer aprTimeNumber;//审批回数
	private String aprOpiComment;//审批意见
	private String aprStatus;//审批状态--
	private Date aprDate;//审批时间
	private String activityId;//环节id
	//不在表内
	private String activityName;//环节名称
	private String aprUserName;//审批用户名称
	private String aprStation;//审批用户岗位
	
	private String taskHandleUserId;//任务实例处理人id
	private String taskHandleUserName;//任务实例处理人姓名
	private String agentUserUid;//任务实例代理人id
	private String agentUserName;//任务实例代理人姓名
	public String getAprOpiId() {
		return aprOpiId;
	}
	public void setAprOpiId(String aprOpiId) {
		this.aprOpiId = aprOpiId;
	}
	public String getInsUid() {
		return insUid;
	}
	public void setInsUid(String insUid) {
		this.insUid = insUid;
	}
	public String getTaskUid() {
		return taskUid;
	}
	public void setTaskUid(String taskUid) {
		this.taskUid = taskUid;
	}
	public Integer getAprOpiIndex() {
		return aprOpiIndex;
	}
	public void setAprOpiIndex(Integer aprOpiIndex) {
		this.aprOpiIndex = aprOpiIndex;
	}
	public String getAprUserId() {
		return aprUserId;
	}
	public void setAprUserId(String aprUserId) {
		this.aprUserId = aprUserId;
	}
	public Integer getAprTimeNumber() {
		return aprTimeNumber;
	}
	public void setAprTimeNumber(Integer aprTimeNumber) {
		this.aprTimeNumber = aprTimeNumber;
	}
	public String getAprOpiComment() {
		return aprOpiComment;
	}
	public void setAprOpiComment(String aprOpiComment) {
		this.aprOpiComment = aprOpiComment;
	}
	public String getAprStatus() {
		return aprStatus;
	}
	public void setAprStatus(String aprStatus) {
		this.aprStatus = aprStatus;
	}
	public Date getAprDate() {
		return aprDate;
	}
	public void setAprDate(Timestamp aprDate) {
		this.aprDate = aprDate;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getAprUserName() {
		return aprUserName;
	}
	public void setAprUserName(String aprUserName) {
		this.aprUserName = aprUserName;
	}
	public String getAprStation() {
		return aprStation;
	}
	public void setAprStation(String aprStation) {
		this.aprStation = aprStation;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
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
}
