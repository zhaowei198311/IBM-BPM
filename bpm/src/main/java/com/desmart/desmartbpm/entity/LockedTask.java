package com.desmart.desmartbpm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 锁住阻止拉取的任务
 */
@Document
public class LockedTask {
    /** 存放锁任务的mongo collection名称  */
    public static final String LOCKED_TASK_COLLECTION_NAME = "lockedTaskCollection";
    /** 主流程的第一个任务  */
    public static final String REASON_FIRST_TASK_OF_PROCESS = "firstTaskOfProcess";
    /** 驳回任务  */
    public static final String REASON_REJECT_TASK = "rejectTask";
    /** 撤转任务 */
    public static final String REASON_TRUN_OFF_TASK = "trunOffTask";

    @Id
    private Integer taskId;  // 任务编号
    private Date lockTime;  // 锁的时间
    private String lockedReason; // 锁任务的原因

    public LockedTask() {

    }

    @PersistenceConstructor
    public LockedTask(Integer taskId, Date lockTime, String lockedReason) {
        this.taskId = taskId;
        this.lockTime = lockTime;
        this.lockedReason = lockedReason;
    }

    public String getLockedReason() {
        return lockedReason;
    }

    public void setLockedReason(String lockedReason) {
        this.lockedReason = lockedReason;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public String toString() {
        return "LockedTask{" +
                "taskId=" + taskId +
                ", lockTime=" + lockTime +
                ", lockedReason='" + lockedReason + '\'' +
                '}';
    }
}