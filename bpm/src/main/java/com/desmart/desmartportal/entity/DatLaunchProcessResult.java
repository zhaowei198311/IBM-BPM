package com.desmart.desmartportal.entity;

/**
 * 触发一个流程成功返回的数据
 */
public class DatLaunchProcessResult {
    private DhProcessInstance mainProcessInstance; // 主流程
    private DhProcessInstance processContainsFirstTask; // 包含第一个任务的流程
    private DhTaskInstance firstTaskInstance; // 流程的第一个任务

    public DatLaunchProcessResult() {}

    public DatLaunchProcessResult(DhProcessInstance mainProcessInstance, DhProcessInstance processContainsFirstTask, DhTaskInstance firstTaskInstance) {
        this.mainProcessInstance = mainProcessInstance;
        this.processContainsFirstTask = processContainsFirstTask;
        this.firstTaskInstance = firstTaskInstance;
    }

    public DhProcessInstance getMainProcessInstance() {
        return mainProcessInstance;
    }

    public void setMainProcessInstance(DhProcessInstance mainProcessInstance) {
        this.mainProcessInstance = mainProcessInstance;
    }

    public DhProcessInstance getProcessContainsFirstTask() {
        return processContainsFirstTask;
    }

    public void setProcessContainsFirstTask(DhProcessInstance processContainsFirstTask) {
        this.processContainsFirstTask = processContainsFirstTask;
    }

    public DhTaskInstance getFirstTaskInstance() {
        return firstTaskInstance;
    }

    public void setFirstTaskInstance(DhTaskInstance firstTaskInstance) {
        this.firstTaskInstance = firstTaskInstance;
    }
}