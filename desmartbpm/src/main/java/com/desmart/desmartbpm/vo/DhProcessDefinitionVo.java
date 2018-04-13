package com.desmart.desmartbpm.vo;

/**
 * 用于页面上展示流程定义
 */
public class DhProcessDefinitionVo {
    private String proName;   // 流程元数据名
    private String proVerUid; // 快照id
    private String proUid; // 流程id
    private String proAppId; // 应用库id
    private String verName;  // 快照名
    private String isActive;  // 是否激活

    private String verCreateTime; // 快照创建时间
    private String proStatus; // 流程定义状态
    private String updator;   // 流程定义更新人
    private String updateTime; // 流程定义更新时间

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProVerUid() {
        return proVerUid;
    }

    public void setProVerUid(String proVerUid) {
        this.proVerUid = proVerUid;
    }

    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }





    public String getVerCreateTime() {
        return verCreateTime;
    }

    public void setVerCreateTime(String verCreateTime) {
        this.verCreateTime = verCreateTime;
    }

    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "DhProcessDefinitionVo{" +
                "proName='" + proName + '\'' +
                ", proVerUid='" + proVerUid + '\'' +
                ", proUid='" + proUid + '\'' +
                ", proAppId='" + proAppId + '\'' +
                ", verName='" + verName + '\'' +
                ", isActive='" + isActive + '\'' +
                ", verCreateTime='" + verCreateTime + '\'' +
                ", proStatus='" + proStatus + '\'' +
                ", updator='" + updator + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}