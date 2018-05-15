package com.desmart.desmartbpm.entity;

/**
 * 为团队过滤服务提供依据
 *
 */
public class DhTaskHandler {
    private String handleUid;

    private Long insId;

    private String activityBpdId;

    private String userUid;

    private String status;

    public String getHandleUid() {
        return handleUid;
    }

    public void setHandleUid(String handleUid) {
        this.handleUid = handleUid;
    }

    public Long getInsId() {
        return insId;
    }

    public void setInsId(Long insId) {
        this.insId = insId;
    }

    public String getActivityBpdId() {
        return activityBpdId;
    }

    public void setActivityBpdId(String activityBpdId) {
        this.activityBpdId = activityBpdId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DhTaskHandler [handleUid=" + handleUid + ", insId=" + insId
                + ", activityBpdId=" + activityBpdId + ", userUid=" + userUid
                + ", status=" + status + "]";
    }

    
}