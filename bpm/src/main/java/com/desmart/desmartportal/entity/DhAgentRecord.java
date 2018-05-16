package com.desmart.desmartportal.entity;

public class DhAgentRecord {
    private String agentRecordId;

    private String agentId;

    private String proName;

    private String taskUid;

    private String taskTitle;

    private String agentUser;

    public String getAgentDetailId() {
        return agentRecordId;
    }

    public void setAgentDetailId(String agentRecordId) {
        this.agentRecordId = agentRecordId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getTaskUid() {
        return taskUid;
    }

    public void setTaskUid(String taskUid) {
        this.taskUid = taskUid;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getAgentUser() {
        return agentUser;
    }

    public void setAgentUser(String agentUser) {
        this.agentUser = agentUser;
    }

    @Override
    public String toString() {
        return "DhAgentRecord [agentDetailId=" + agentRecordId + ", agentId="
                + agentId + ", proName=" + proName + ", taskUid=" + taskUid
                + ", taskTitle=" + taskTitle + ", agentUser=" + agentUser + "]";
    }
    
    
}
