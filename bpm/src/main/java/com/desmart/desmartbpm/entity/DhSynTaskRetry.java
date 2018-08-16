package com.desmart.desmartbpm.entity;

import java.util.Date;

public class DhSynTaskRetry {
    /** 未完成状态  */
    public static final int STATUS_UNFINISHED = 0;
    /** 已完成 */
    public static final int STATUS_SUCCESS = 1;
    /** 作废  */
    public static final int STATUS_ABANDON = -1;

    private String id;  // 主键

    private Integer taskId;  // 任务id

    private Integer retryCount; // 重试计数

    private Integer status;  // 状态

    private Date lastRetryTime;  // 最后重试时间

    private Date createTime;   // 创建时间

    private String errorMessage;  // 错误信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastRetryTime() {
        return lastRetryTime;
    }

    public void setLastRetryTime(Date lastRetryTime) {
        this.lastRetryTime = lastRetryTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "DhSynTaskRetry{" +
                "id='" + id + '\'' +
                ", taskId=" + taskId +
                ", retryCount=" + retryCount +
                ", status=" + status +
                ", lastRetryTime=" + lastRetryTime +
                ", createTime=" + createTime +
                '}';
    }
}