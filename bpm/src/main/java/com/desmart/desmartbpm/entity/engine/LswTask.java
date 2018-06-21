package com.desmart.desmartbpm.entity.engine;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

public class LswTask {

    private Integer taskId;

    private String tip;

    private String snapshotId;

    private Long userId;

    private Long groupId;

    private String participantId;

    private Long managersGroupId;

    private BigDecimal expertParticipantRef;

    private String status;

    private Long priorityId;

    private Date dueDate;

    private Date dueTime;

    private Date atRiskDate;

    private String subject;

    private Date rcvdDatetime;

    private Long rcvdFrom;

    private Long rcvdTaskId;

    private String collaboration;

    private Date sentDatetime;

    private Date readDatetime;

    private Date closeDatetime;

    private Long closeBy;

    private Long origTaskId;

    private BigDecimal startProcessRef;

    private String cachedProcessVersionId;

    private Long groupIdType;

    private Long executionStatus;

    private Long bpdInstanceId;

    private BigDecimal createdByBpdRef;

    private String cachedCbbVersionId;

    private String createdByBpdFlowObjectId;

    private Long assumerId;

    private BigDecimal attachedFormRef;

    private String cachedFormVersionId;

    private BigDecimal attachedExtActivityRef;

    private String cachedExtactVersionId;

    private Object sharepointDiscussionUrl;

    private Object activityName;

    private Long activityTaskType;
    
    // 表外字段 
    private String userName;  // 任务处理人工号

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId == null ? null : snapshotId.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId == null ? null : participantId.trim();
    }

    public Long getManagersGroupId() {
        return managersGroupId;
    }

    public void setManagersGroupId(Long managersGroupId) {
        this.managersGroupId = managersGroupId;
    }

    public BigDecimal getExpertParticipantRef() {
        return expertParticipantRef;
    }

    public void setExpertParticipantRef(BigDecimal expertParticipantRef) {
        this.expertParticipantRef = expertParticipantRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public Date getAtRiskDate() {
        return atRiskDate;
    }

    public void setAtRiskDate(Date atRiskDate) {
        this.atRiskDate = atRiskDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getRcvdDatetime() {
        return rcvdDatetime;
    }

    public void setRcvdDatetime(Date rcvdDatetime) {
        this.rcvdDatetime = rcvdDatetime;
    }

    public Long getRcvdFrom() {
        return rcvdFrom;
    }

    public void setRcvdFrom(Long rcvdFrom) {
        this.rcvdFrom = rcvdFrom;
    }

    public Long getRcvdTaskId() {
        return rcvdTaskId;
    }

    public void setRcvdTaskId(Long rcvdTaskId) {
        this.rcvdTaskId = rcvdTaskId;
    }

    public String getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(String collaboration) {
        this.collaboration = collaboration;
    }

    public Date getSentDatetime() {
        return sentDatetime;
    }

    public void setSentDatetime(Date sentDatetime) {
        this.sentDatetime = sentDatetime;
    }

    public Date getReadDatetime() {
        return readDatetime;
    }

    public void setReadDatetime(Date readDatetime) {
        this.readDatetime = readDatetime;
    }

    public Date getCloseDatetime() {
        return closeDatetime;
    }

    public void setCloseDatetime(Date closeDatetime) {
        this.closeDatetime = closeDatetime;
    }

    public Long getCloseBy() {
        return closeBy;
    }

    public void setCloseBy(Long closeBy) {
        this.closeBy = closeBy;
    }

    public Long getOrigTaskId() {
        return origTaskId;
    }

    public void setOrigTaskId(Long origTaskId) {
        this.origTaskId = origTaskId;
    }

    public BigDecimal getStartProcessRef() {
        return startProcessRef;
    }

    public void setStartProcessRef(BigDecimal startProcessRef) {
        this.startProcessRef = startProcessRef;
    }

    public String getCachedProcessVersionId() {
        return cachedProcessVersionId;
    }

    public void setCachedProcessVersionId(String cachedProcessVersionId) {
        this.cachedProcessVersionId = cachedProcessVersionId == null ? null : cachedProcessVersionId.trim();
    }

    public Long getGroupIdType() {
        return groupIdType;
    }

    public void setGroupIdType(Long groupIdType) {
        this.groupIdType = groupIdType;
    }

    public Long getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(Long executionStatus) {
        this.executionStatus = executionStatus;
    }

    public Long getBpdInstanceId() {
        return bpdInstanceId;
    }

    public void setBpdInstanceId(Long bpdInstanceId) {
        this.bpdInstanceId = bpdInstanceId;
    }

    public BigDecimal getCreatedByBpdRef() {
        return createdByBpdRef;
    }

    public void setCreatedByBpdRef(BigDecimal createdByBpdRef) {
        this.createdByBpdRef = createdByBpdRef;
    }

    public String getCachedCbbVersionId() {
        return cachedCbbVersionId;
    }

    public void setCachedCbbVersionId(String cachedCbbVersionId) {
        this.cachedCbbVersionId = cachedCbbVersionId == null ? null : cachedCbbVersionId.trim();
    }

    public String getCreatedByBpdFlowObjectId() {
        return createdByBpdFlowObjectId;
    }

    public void setCreatedByBpdFlowObjectId(String createdByBpdFlowObjectId) {
        this.createdByBpdFlowObjectId = createdByBpdFlowObjectId;
    }

    public Long getAssumerId() {
        return assumerId;
    }

    public void setAssumerId(Long assumerId) {
        this.assumerId = assumerId;
    }

    public BigDecimal getAttachedFormRef() {
        return attachedFormRef;
    }

    public void setAttachedFormRef(BigDecimal attachedFormRef) {
        this.attachedFormRef = attachedFormRef;
    }

    public String getCachedFormVersionId() {
        return cachedFormVersionId;
    }

    public void setCachedFormVersionId(String cachedFormVersionId) {
        this.cachedFormVersionId = cachedFormVersionId == null ? null : cachedFormVersionId.trim();
    }

    public BigDecimal getAttachedExtActivityRef() {
        return attachedExtActivityRef;
    }

    public void setAttachedExtActivityRef(BigDecimal attachedExtActivityRef) {
        this.attachedExtActivityRef = attachedExtActivityRef;
    }

    public String getCachedExtactVersionId() {
        return cachedExtactVersionId;
    }

    public void setCachedExtactVersionId(String cachedExtactVersionId) {
        this.cachedExtactVersionId = cachedExtactVersionId == null ? null : cachedExtactVersionId.trim();
    }

    public Object getSharepointDiscussionUrl() {
        return sharepointDiscussionUrl;
    }

    public void setSharepointDiscussionUrl(Object sharepointDiscussionUrl) {
        this.sharepointDiscussionUrl = sharepointDiscussionUrl;
    }

    public Object getActivityName() {
        return activityName;
    }

    public void setActivityName(Object activityName) {
        this.activityName = activityName;
    }

    public Long getActivityTaskType() {
        return activityTaskType;
    }

    public void setActivityTaskType(Long activityTaskType) {
        this.activityTaskType = activityTaskType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LswTask [taskId=" + taskId + ", tip=" + tip + ", snapshotId="
                + snapshotId + ", userId=" + userId + ", groupId=" + groupId
                + ", participantId=" + participantId + ", managersGroupId="
                + managersGroupId + ", expertParticipantRef="
                + expertParticipantRef + ", status=" + status + ", priorityId="
                + priorityId + ", dueDate=" + dueDate + ", dueTime=" + dueTime
                + ", atRiskDate=" + atRiskDate + ", subject=" + subject
                + ", rcvdDatetime=" + rcvdDatetime + ", rcvdFrom=" + rcvdFrom
                + ", rcvdTaskId=" + rcvdTaskId + ", collaboration="
                + collaboration + ", sentDatetime=" + sentDatetime
                + ", readDatetime=" + readDatetime + ", closeDatetime="
                + closeDatetime + ", closeBy=" + closeBy + ", origTaskId="
                + origTaskId + ", startProcessRef=" + startProcessRef
                + ", cachedProcessVersionId=" + cachedProcessVersionId
                + ", groupIdType=" + groupIdType + ", executionStatus="
                + executionStatus + ", bpdInstanceId=" + bpdInstanceId
                + ", createdByBpdRef=" + createdByBpdRef
                + ", cachedCbbVersionId=" + cachedCbbVersionId
                + ", createdByBpdFlowObjectId=" + createdByBpdFlowObjectId
                + ", assumerId=" + assumerId + ", attachedFormRef="
                + attachedFormRef + ", cachedFormVersionId="
                + cachedFormVersionId + ", attachedExtActivityRef="
                + attachedExtActivityRef + ", cachedExtactVersionId="
                + cachedExtactVersionId + ", sharepointDiscussionUrl="
                + sharepointDiscussionUrl + ", activityName=" + activityName
                + ", activityTaskType=" + activityTaskType + ", userName="
                + userName + "]";
    }
    
    
}