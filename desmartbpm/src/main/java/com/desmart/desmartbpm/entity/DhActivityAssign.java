package com.desmart.desmartbpm.entity;

/**
 * 分派关系
 * 如：任务处理人指定为某角色，超时通知指定某人
 */
public class DhActivityAssign {
    
    private String actaUid;  // 主键

    private String activityId; // 活动id

    private String actaType;  // 分配哪种事项   process 处理人； timeoutNotify超时提醒

    private String actaAssignType; // 被分配的对象是什么类型   USER, ROLE, TEAM

    private String actaAssignId;  // 被分配的对象ID

    public String getActaUid() {
        return actaUid;
    }

    public void setActaUid(String actaUid) {
        this.actaUid = actaUid;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActaType() {
        return actaType;
    }

    public void setActaType(String actaType) {
        this.actaType = actaType;
    }

    public String getActaAssignType() {
        return actaAssignType;
    }

    public void setActaAssignType(String actaAssignType) {
        this.actaAssignType = actaAssignType;
    }

    public String getActaAssignId() {
        return actaAssignId;
    }

    public void setActaAssignId(String actaAssignId) {
        this.actaAssignId = actaAssignId;
    }

    @Override
    public String toString() {
        return "DhActivityAssign [actaUid=" + actaUid + ", activityId=" + activityId + ", actaType=" + actaType
                + ", actaAssignType=" + actaAssignType + ", actaAssignId=" + actaAssignId + "]";
    }

    

}
