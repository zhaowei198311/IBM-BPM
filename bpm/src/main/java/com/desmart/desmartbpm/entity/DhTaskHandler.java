package com.desmart.desmartbpm.entity;

/**
 * 为团队过滤服务提供依据
 *
 */
public class DhTaskHandler {
    private String handleUid;

    private Long insId;

    private String taskActivityId;

    private String userUid;

    private String status;
    
    private String userName;//不在表内，处理人用户姓名

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

    public String getTaskActivityId() {
        return taskActivityId;
    }

    public void setTaskActivityId(String taskActivityId) {
        this.taskActivityId = taskActivityId;
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

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "DhTaskHandler [handleUid=" + handleUid + ", insId=" + insId + ", taskActivityId=" + taskActivityId
				+ ", userUid=" + userUid + ", status=" + status + ", userName=" + userName + "]";
	}

    
}