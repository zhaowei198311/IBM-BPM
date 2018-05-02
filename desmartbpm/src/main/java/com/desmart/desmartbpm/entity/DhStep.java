package com.desmart.desmartbpm.entity;

/**
 * 环节步骤
 * @author yaoyunqing
 *
 */
public class DhStep {
    private String stepUid;
    private String proAppId;
    private String proUid;
    private String proVerUid;
    private String activityBpdId;
    private Integer stepSort;
    private String stepBusinessKey;
    private String stepType;
    private String stepObjectUid;
    private String stepCondition;
    private String stepMode;
    
    // 非表字段
    private String triTitle;
    private String formName;
    
    public DhStep() {
        
    }
    
    public DhStep(String proAppId, String proUid, String proVerUid) {
        super();
        this.proAppId = proAppId;
        this.proUid = proUid;
        this.proVerUid = proVerUid;
    }

    public String getStepUid() {
        return stepUid;
    }
    public void setStepUid(String stepUid) {
        this.stepUid = stepUid;
    }
    public String getProAppId() {
        return proAppId;
    }
    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
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
    public Integer getStepSort() {
        return stepSort;
    }
    public void setStepSort(Integer stepSort) {
        this.stepSort = stepSort;
    }
    public String getStepBusinessKey() {
        return stepBusinessKey;
    }
    public void setStepBusinessKey(String stepBusinessKey) {
        this.stepBusinessKey = stepBusinessKey;
    }
    public String getStepType() {
        return stepType;
    }
    public void setStepType(String stepType) {
        this.stepType = stepType;
    }
    public String getStepObjectUid() {
        return stepObjectUid;
    }
    public void setStepObjectUid(String stepObjectUid) {
        this.stepObjectUid = stepObjectUid;
    }
    public String getStepCondition() {
        return stepCondition;
    }
    public void setStepCondition(String stepCondition) {
        this.stepCondition = stepCondition;
    }
    public String getStepMode() {
        return stepMode;
    }
    public void setStepMode(String stepMode) {
        this.stepMode = stepMode;
    }
    @Override
    public String toString() {
        return "DhStep [stepUid=" + stepUid + ", proAppId=" + proAppId
                + ", proUid=" + proUid + ", proVerUid=" + proVerUid
                + ", activityBpdId=" + activityBpdId + ", stepSort=" + stepSort
                + ", stepBusinessKey=" + stepBusinessKey + ", stepType="
                + stepType + ", stepObjectUid=" + stepObjectUid
                + ", stepCondition=" + stepCondition + ", stepMode=" + stepMode
                + ", triTitle=" + triTitle + ", formName=" + formName + "]";
    }
    
    

    
    
}
