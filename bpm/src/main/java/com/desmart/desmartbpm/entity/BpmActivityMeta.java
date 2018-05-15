package com.desmart.desmartbpm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BpmActivityMeta implements Serializable {
    private String activityId;  // 主键
    private String noteType;
    private Date createTime;
    private String creator;
    private Date updateTime;
    private String updateBy;
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
    private String handleSignType;  // alone  count  是否多人
    private String activityGroupName;
    private String poId;
    private Integer deepLevel;   // 流程深度，层级
    private String activityAltName;
    private String proAppId;
    private Integer sortNum;  // 记录环节生成的顺序
    
    // 不在表中
    private DhActivityConf dhActivityConf;
    
    public BpmActivityMeta(){
        
    }
    
    
    public BpmActivityMeta(String proAppId, String bpdId, String snapshotId) {
        this.snapshotId = snapshotId;
        this.bpdId = bpdId;
        this.proAppId = proAppId;
    }
    
    public BpmActivityMeta(String proAppId, String bpdId, String snapshotId, String activityBpdId) {
        this.snapshotId = snapshotId;
        this.bpdId = bpdId;
        this.proAppId = proAppId;
        this.activityBpdId = activityBpdId;
    }

    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
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

    public String getHandleSignType() {
        return handleSignType;
    }

    public void setHandleSignType(String handleSignType) {
        this.handleSignType = handleSignType;
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

    public Integer getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(Integer deepLevel) {
        this.deepLevel = deepLevel;
    }

    public String getActivityAltName() {
        return activityAltName;
    }

    public void setActivityAltName(String activityAltName) {
        this.activityAltName = activityAltName;
    }

    public DhActivityConf getDhActivityConf() {
        return dhActivityConf;
    }


    public void setDhActivityConf(DhActivityConf dhActivityConf) {
        this.dhActivityConf = dhActivityConf;
    }


    public Integer getSortNum() {
        return sortNum;
    }


    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }


    @Override
    public String toString() {
        return "BpmActivityMeta [activityId=" + activityId + ", noteType="
                + noteType + ", createTime=" + createTime + ", creator="
                + creator + ", updateTime=" + updateTime + ", updateBy="
                + updateBy + ", nextactivities=" + nextactivities
                + ", activityName=" + activityName + ", snapshotId="
                + snapshotId + ", activityBpdId=" + activityBpdId + ", bpdId="
                + bpdId + ", type=" + type + ", activityType=" + activityType
                + ", activityTo=" + activityTo + ", parentActivityBpdId="
                + parentActivityBpdId + ", externalId=" + externalId
                + ", bpmTaskType=" + bpmTaskType + ", loopType=" + loopType
                + ", miOrder=" + miOrder + ", snapshotUid=" + snapshotUid
                + ", stepId=" + stepId + ", handleSignType=" + handleSignType
                + ", activityGroupName=" + activityGroupName + ", poId=" + poId
                + ", deepLevel=" + deepLevel + ", activityAltName="
                + activityAltName + ", proAppId=" + proAppId + ", sortNum="
                + sortNum + ", dhActivityConf=" + dhActivityConf + "]";
    }
}