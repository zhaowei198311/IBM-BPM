package com.desmart.desmartbpm.entity;

/**
 * 分派关系
 * 如：任务处理人指定为某角色，超时通知指定某人
 * 1. 指定任务默认处理人 activity_id -> role1...
 * 2. 指定任务可选处理人 activity_id -> user1...
 * 3. 指定环节超时通知人 activity_id -> user1...
 *
 */
public class DhActivityAssign {
    
    private String actaUid;  // 主键

    private String activityId; // 活动id

    private String actaType;  // 分配哪种事项   process 处理人； timeoutNotify超时提醒

    private String actaAssignType; // 被分配的对象是什么类型   USER, ROLE, TEAM

    private String actaAssignId;  // 被分配的对象ID
    
    private String userName;
    private String roleName;
    private String teamName;
    

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "DhActivityAssign [actaUid=" + actaUid + ", activityId="
                + activityId + ", actaType=" + actaType + ", actaAssignType="
                + actaAssignType + ", actaAssignId=" + actaAssignId
                + ", userName=" + userName + ", roleName=" + roleName
                + ", teamName=" + teamName + "]";
    }

    

}
