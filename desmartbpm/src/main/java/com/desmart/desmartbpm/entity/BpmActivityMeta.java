package com.desmart.desmartbpm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BpmActivityMeta implements Serializable {
    private String activityId;  // 主键
    private String allowReject;
    private String allowRollback;
    private String allowTransfer;
    private String allowOversign;
    private String allowPassby;
    private String allowDelfile;
    private String allowUpload;
    private String noteType;
    private Integer submitManCount;
    private String defaultOwnerType;
    private String defaultOwners;
    private String defaultOwnersName;
    private String uploadFileLabel;
    private String downloadFileLabel;
    private Date createTime;
    private String creator;
    private Date updateTime;
    private String updateBy;
    private Integer sortNum;
    private String formPath;
    private String nextactivities;
    private String activityName;   // 环节名
    private String snapshotId;   // bpm引擎中快照id
    private String activityBpdId;  // 流程图上的元素id
    private String bpdId;   // 流程图id
    private String type;    // 类型
    private String activityType;  // 活动类型 start  end gateway  gatewayAnd getwayOr
    private String activityTo;  // 下个元素  用, 分隔多个元素
    private String parentActivityBpdId;  // 父元素id
    private String externalId;  // 外接流程的bpdId
    private String bpmTaskType;  // UserTask SubProcess ServiceTask
    private String loopType;  // simpleLoop MultiInstanceLoop none  null
    private String miOrder; 
    private String snapshotUid;  // 版本主键
    private String stepId;
    private String assignVarname;
    private String handleSignType;  // alone  count  是否多人
    private String emailNotification;
    private String limitCandidate;
    private String signCountVarname;
    private String completeRate;
    private String defaultPassBy;
    private String allowCancel;
    private Double activityCostPrehour;
    private Integer activityRuntime;
    private String activityRuntimeUnit;
    private String overtimeTemplateId;
    private String overtimeNotifyIsLoop;
    private String overtimeNotifyPeople;
    private Map<String, String> overtimeNotifyPeopleMap = new HashMap();
    private String overtimeLoopTplId;
    private String overtimeLoopNotifyPeople;
    private Map<String, String> overtimeLoopNotifyPeopleMap = new HashMap();
    private Integer loopStartTimeSpan;
    private String loopStartTimeUnit;
    private Integer loopTimeSpan;
    private String loopTimeUnit;
    private String overtimeNotifyEvent;
    private String loopNotifyEvent;
    private String routeVarname;
    private Integer overtimeDeadline;
    private String overtimeDeadlineUnit;
    private String overtimeDeadlineEvent;
    private String activityGroupName;
    private String poId;
    private Map<String, String> informed = new HashMap<>();
    //private BpmEmailNotification bpmEmailNotification;
    //private List<BpmActivityBizPanel> bpmActivityBizPanel;
    private String rollbackGroupStrategy;
    private Integer deepLevel;   // 流程深度，层级
    private String autoCommit;
    private String customRuleParams;
    private String onTimeTemplateId;
    private String onTimeNotify;
    private String activityAltName;
    private String commitTaskEventCls;
    private String commitDelayCls;


    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getAllowReject() {
        return allowReject;
    }

    public void setAllowReject(String allowReject) {
        this.allowReject = allowReject;
    }

    public String getAllowRollback() {
        return allowRollback;
    }

    public void setAllowRollback(String allowRollback) {
        this.allowRollback = allowRollback;
    }

    public String getAllowTransfer() {
        return allowTransfer;
    }

    public void setAllowTransfer(String allowTransfer) {
        this.allowTransfer = allowTransfer;
    }

    public String getAllowOversign() {
        return allowOversign;
    }

    public void setAllowOversign(String allowOversign) {
        this.allowOversign = allowOversign;
    }

    public String getAllowPassby() {
        return allowPassby;
    }

    public void setAllowPassby(String allowPassby) {
        this.allowPassby = allowPassby;
    }

    public String getAllowDelfile() {
        return allowDelfile;
    }

    public void setAllowDelfile(String allowDelfile) {
        this.allowDelfile = allowDelfile;
    }

    public String getAllowUpload() {
        return allowUpload;
    }

    public void setAllowUpload(String allowUpload) {
        this.allowUpload = allowUpload;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public Integer getSubmitManCount() {
        return submitManCount;
    }

    public void setSubmitManCount(Integer submitManCount) {
        this.submitManCount = submitManCount;
    }

    public String getDefaultOwnerType() {
        return defaultOwnerType;
    }

    public void setDefaultOwnerType(String defaultOwnerType) {
        this.defaultOwnerType = defaultOwnerType;
    }

    public String getDefaultOwners() {
        return defaultOwners;
    }

    public void setDefaultOwners(String defaultOwners) {
        this.defaultOwners = defaultOwners;
    }

    public String getDefaultOwnersName() {
        return defaultOwnersName;
    }

    public void setDefaultOwnersName(String defaultOwnersName) {
        this.defaultOwnersName = defaultOwnersName;
    }

    public String getUploadFileLabel() {
        return uploadFileLabel;
    }

    public void setUploadFileLabel(String uploadFileLabel) {
        this.uploadFileLabel = uploadFileLabel;
    }

    public String getDownloadFileLabel() {
        return downloadFileLabel;
    }

    public void setDownloadFileLabel(String downloadFileLabel) {
        this.downloadFileLabel = downloadFileLabel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getFormPath() {
        return formPath;
    }

    public void setFormPath(String formPath) {
        this.formPath = formPath;
    }

    public String getNextactivities() {
        return nextactivities;
    }

    public void setNextactivities(String nextactivities) {
        this.nextactivities = nextactivities;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getActivityBpdId() {
        return activityBpdId;
    }

    public void setActivityBpdId(String activityBpdId) {
        this.activityBpdId = activityBpdId;
    }

    public String getBpdId() {
        return bpdId;
    }

    public void setBpdId(String bpdId) {
        this.bpdId = bpdId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityTo() {
        return activityTo;
    }

    public void setActivityTo(String activityTo) {
        this.activityTo = activityTo;
    }

    public String getParentActivityBpdId() {
        return parentActivityBpdId;
    }

    public void setParentActivityBpdId(String parentActivityBpdId) {
        this.parentActivityBpdId = parentActivityBpdId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getBpmTaskType() {
        return bpmTaskType;
    }

    public void setBpmTaskType(String bpmTaskType) {
        this.bpmTaskType = bpmTaskType;
    }

    public String getLoopType() {
        return loopType;
    }

    public void setLoopType(String loopType) {
        this.loopType = loopType;
    }

    public String getMiOrder() {
        return miOrder;
    }

    public void setMiOrder(String miOrder) {
        this.miOrder = miOrder;
    }

    public String getSnapshotUid() {
        return snapshotUid;
    }

    public void setSnapshotUid(String snapshotUid) {
        this.snapshotUid = snapshotUid;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getAssignVarname() {
        return assignVarname;
    }

    public void setAssignVarname(String assignVarname) {
        this.assignVarname = assignVarname;
    }

    public String getHandleSignType() {
        return handleSignType;
    }

    public void setHandleSignType(String handleSignType) {
        this.handleSignType = handleSignType;
    }

    public String getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(String emailNotification) {
        this.emailNotification = emailNotification;
    }

    public String getLimitCandidate() {
        return limitCandidate;
    }

    public void setLimitCandidate(String limitCandidate) {
        this.limitCandidate = limitCandidate;
    }

    public String getSignCountVarname() {
        return signCountVarname;
    }

    public void setSignCountVarname(String signCountVarName) {
        this.signCountVarname = signCountVarName;
    }

    public String getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(String completeRate) {
        this.completeRate = completeRate;
    }

    public String getDefaultPassBy() {
        return defaultPassBy;
    }

    public void setDefaultPassBy(String defaultPassBy) {
        this.defaultPassBy = defaultPassBy;
    }

    public String getAllowCancel() {
        return allowCancel;
    }

    public void setAllowCancel(String allowCancel) {
        this.allowCancel = allowCancel;
    }

    public Double getActivityCostPrehour() {
        return activityCostPrehour;
    }

    public void setActivityCostPrehour(Double activityCostPrehour) {
        this.activityCostPrehour = activityCostPrehour;
    }

    public Integer getActivityRuntime() {
        return activityRuntime;
    }

    public void setActivityRuntime(Integer activityRuntime) {
        this.activityRuntime = activityRuntime;
    }

    public String getActivityRuntimeUnit() {
        return activityRuntimeUnit;
    }

    public void setActivityRuntimeUnit(String activityRuntimeUnit) {
        this.activityRuntimeUnit = activityRuntimeUnit;
    }

    public String getOvertimeTemplateId() {
        return overtimeTemplateId;
    }

    public void setOvertimeTemplateId(String overtimeTemplateId) {
        this.overtimeTemplateId = overtimeTemplateId;
    }

    public String getOvertimeNotifyIsLoop() {
        return overtimeNotifyIsLoop;
    }

    public void setOvertimeNotifyIsLoop(String overtimeNotifyIsLoop) {
        this.overtimeNotifyIsLoop = overtimeNotifyIsLoop;
    }

    public String getOvertimeNotifyPeople() {
        return overtimeNotifyPeople;
    }

    public void setOvertimeNotifyPeople(String overtimeNotifyPeople) {
        this.overtimeNotifyPeople = overtimeNotifyPeople;
    }

    public Map<String, String> getOvertimeNotifyPeopleMap() {
        return overtimeNotifyPeopleMap;
    }

    public void setOvertimeNotifyPeopleMap(Map<String, String> overtimeNotifyPeopleMap) {
        this.overtimeNotifyPeopleMap = overtimeNotifyPeopleMap;
    }

    public String getOvertimeLoopTplId() {
        return overtimeLoopTplId;
    }

    public void setOvertimeLoopTplId(String overtimeLoopTplId) {
        this.overtimeLoopTplId = overtimeLoopTplId;
    }

    public String getOvertimeLoopNotifyPeople() {
        return overtimeLoopNotifyPeople;
    }

    public void setOvertimeLoopNotifyPeople(String overtimeLoopNotifyPeople) {
        this.overtimeLoopNotifyPeople = overtimeLoopNotifyPeople;
    }

    public Map<String, String> getOvertimeLoopNotifyPeopleMap() {
        return overtimeLoopNotifyPeopleMap;
    }

    public void setOvertimeLoopNotifyPeopleMap(Map<String, String> overtimeLoopNotifyPeopleMap) {
        this.overtimeLoopNotifyPeopleMap = overtimeLoopNotifyPeopleMap;
    }

    public Integer getLoopStartTimeSpan() {
        return loopStartTimeSpan;
    }

    public void setLoopStartTimeSpan(Integer loopStartTimeSpan) {
        this.loopStartTimeSpan = loopStartTimeSpan;
    }

    public String getLoopStartTimeUnit() {
        return loopStartTimeUnit;
    }

    public void setLoopStartTimeUnit(String loopStartTimeUnit) {
        this.loopStartTimeUnit = loopStartTimeUnit;
    }

    public Integer getLoopTimeSpan() {
        return loopTimeSpan;
    }

    public void setLoopTimeSpan(Integer loopTimeSpan) {
        this.loopTimeSpan = loopTimeSpan;
    }

    public String getLoopTimeUnit() {
        return loopTimeUnit;
    }

    public void setLoopTimeUnit(String loopTimeUnit) {
        this.loopTimeUnit = loopTimeUnit;
    }

    public String getOvertimeNotifyEvent() {
        return overtimeNotifyEvent;
    }

    public void setOvertimeNotifyEvent(String overtimeNotifyEvent) {
        this.overtimeNotifyEvent = overtimeNotifyEvent;
    }

    public String getLoopNotifyEvent() {
        return loopNotifyEvent;
    }

    public void setLoopNotifyEvent(String loopNotifyEvent) {
        this.loopNotifyEvent = loopNotifyEvent;
    }

    public String getRouteVarname() {
        return routeVarname;
    }

    public void setRouteVarname(String routeVarname) {
        this.routeVarname = routeVarname;
    }

    public Integer getOvertimeDeadline() {
        return overtimeDeadline;
    }

    public void setOvertimeDeadline(Integer overTimeDeadline) {
        this.overtimeDeadline = overTimeDeadline;
    }

    public String getOvertimeDeadlineUnit() {
        return overtimeDeadlineUnit;
    }

    public void setOvertimeDeadlineUnit(String overTimeDeadlineUnit) {
        this.overtimeDeadlineUnit = overTimeDeadlineUnit;
    }

    public String getOvertimeDeadlineEvent() {
        return overtimeDeadlineEvent;
    }

    public void setOvertimeDeadlineEvent(String overtimeDeadlineEvent) {
        this.overtimeDeadlineEvent = overtimeDeadlineEvent;
    }

    public String getActivityGroupName() {
        return activityGroupName;
    }

    public void setActivityGroupName(String activityGroupName) {
        this.activityGroupName = activityGroupName;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public Map<String, String> getInformed() {
        return informed;
    }

    public void setInformed(Map<String, String> informed) {
        this.informed = informed;
    }

    public String getRollbackGroupStrategy() {
        return rollbackGroupStrategy;
    }

    public void setRollbackGroupStrategy(String rollbackGroupStrategy) {
        this.rollbackGroupStrategy = rollbackGroupStrategy;
    }

    public Integer getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(Integer deepLevel) {
        this.deepLevel = deepLevel;
    }

    public String getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(String autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getCustomRuleParams() {
        return customRuleParams;
    }

    public void setCustomRuleParams(String customRuleParams) {
        this.customRuleParams = customRuleParams;
    }

    public String getOnTimeTemplateId() {
        return onTimeTemplateId;
    }

    public void setOnTimeTemplateId(String onTimeTemplateId) {
        this.onTimeTemplateId = onTimeTemplateId;
    }

    public String getOnTimeNotify() {
        return onTimeNotify;
    }

    public void setOnTimeNotify(String onTimeNotify) {
        this.onTimeNotify = onTimeNotify;
    }

    public String getActivityAltName() {
        return activityAltName;
    }

    public void setActivityAltName(String activityAltName) {
        this.activityAltName = activityAltName;
    }

    public String getCommitTaskEventCls() {
        return commitTaskEventCls;
    }

    public void setCommitTaskEventCls(String commitTaskEventCls) {
        this.commitTaskEventCls = commitTaskEventCls;
    }

    public String getCommitDelayCls() {
        return commitDelayCls;
    }

    public void setCommitDelayCls(String commitDelayCls) {
        this.commitDelayCls = commitDelayCls;
    }
}