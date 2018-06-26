package com.desmart.desmartbpm.entity;

import com.rabbitmq.client.AMQP;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 描述被打开过的任务
 */
@Document
public class OpenedTask {
    /** 存放打开过的任务的集合 */
    public static final String OPENED_TASK_COLLECTION_NAME = "openedTaskCollection";

    @Id
    private String taskUid;  // 任务主键
    private int taskId;   // 任务编号
    private Date openTime;  // 任务打开时间

    public OpenedTask() {

    }

    @PersistenceConstructor
    public OpenedTask(String taskUid, int taskId, Date openTime) {
        this.taskUid = taskUid;
        this.taskId = taskId;
        this.openTime = openTime;
    }

    public String getTaskUid() {
        return taskUid;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTaskUid(String taskUid) {
        this.taskUid = taskUid;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    @Override
    public String toString() {
        return "OpenedTask{" +
                "taskUid='" + taskUid + '\'' +
                ", taskId=" + taskId +
                ", openTime=" + openTime +
                '}';
    }
}