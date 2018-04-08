package com.desmart.desmartbpm.entity;

/**
 * 环节步骤
 * @author yaoyunqing
 *
 */
public class DhStep {
    
    private String stepUid;// 步骤主键

    private String proUid;// 流程id

    private String proVerUid;// 流程版本id

    private String activityBpdId;// 流程图上元素id

    private String stepTypeObj;// 步骤类型对象

    private String stepUidObj;// 步骤用户对象

    private String stepCondition;// 步骤条件（针对模式的条件）

    private Short stepSort;// 步骤顺序

    private String stepMode;// 步骤模式：EDIT；VIEW

    public String getStepUid() {
        return stepUid;
    }

    public void setStepUid(String stepUid) {
        this.stepUid = stepUid;
    }

    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public String getProVerUid() {
        return proVerUid;
    }

    public void setProVerUid(String proVerUid) {
        this.proVerUid = proVerUid;
    }

    public String getActivityBpdId() {
        return activityBpdId;
    }

    public void setActivityBpdId(String activityBpdId) {
        this.activityBpdId = activityBpdId;
    }

    public String getStepTypeObj() {
        return stepTypeObj;
    }

    public void setStepTypeObj(String stepTypeObj) {
        this.stepTypeObj = stepTypeObj;
    }

    public String getStepUidObj() {
        return stepUidObj;
    }

    public void setStepUidObj(String stepUidObj) {
        this.stepUidObj = stepUidObj;
    }

    public String getStepCondition() {
        return stepCondition;
    }

    public void setStepCondition(String stepCondition) {
        this.stepCondition = stepCondition;
    }

    public Short getStepSort() {
        return stepSort;
    }

    public void setStepSort(Short stepSort) {
        this.stepSort = stepSort;
    }

    public String getStepMode() {
        return stepMode;
    }

    public void setStepMode(String stepMode) {
        this.stepMode = stepMode;
    }

    @Override
    public String toString() {
        return "DhStep [stepUid=" + stepUid + ", proUid=" + proUid
                + ", proVerUid=" + proVerUid + ", activityBpdId="
                + activityBpdId + ", stepTypeObj=" + stepTypeObj
                + ", stepUidObj=" + stepUidObj + ", stepCondition="
                + stepCondition + ", stepSort=" + stepSort + ", stepMode="
                + stepMode + "]";
    }
    
    
}
