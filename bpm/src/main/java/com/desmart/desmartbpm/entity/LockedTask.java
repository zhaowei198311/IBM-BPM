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
    public static final String LOCKED_TASK_COLLECTION_NAME = "lockedTaskCollection";

    @Id
    private Integer taskId;  // 任务编号
    private Date lockTime;  // 锁的时间

    public LockedTask() {

    }

    @PersistenceConstructor
    public LockedTask(Integer taskId, Date lockTime) {
        this.taskId = taskId;
        this.lockTime = lockTime;
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
                '}';
    }
}