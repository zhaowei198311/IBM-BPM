package com.desmart.desmartbpm.entity;

import com.desmart.desmartportal.entity.DhTaskInstance;

/**
 * 为跳转到驳回环节准备的数据
 */
public class DataForSkipFromReject {
    private int insId;   // 流程实例编号
    private String tokenId; // 需要移动的token
    private BpmActivityMeta targetNode;  // token移动的目标节点
    private String newTaskOwner;  // 新任务处理人
    private String newTaskOwnerName; // 新任务处理人姓名

    private DhTaskInstance currTask; // 当前任务

    public int getInsId() {
        return insId;
    }

    public void setInsId(int insId) {
        this.insId = insId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public BpmActivityMeta getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(BpmActivityMeta targetNode) {
        this.targetNode = targetNode;
    }

    public String getNewTaskOwner() {
        return newTaskOwner;
    }

    public void setNewTaskOwner(String newTaskOwner) {
        this.newTaskOwner = newTaskOwner;
    }

    public DhTaskInstance getCurrTask() {
        return currTask;
    }

    public void setCurrTask(DhTaskInstance currTask) {
        this.currTask = currTask;
    }

    public String getNewTaskOwnerName() {
        return newTaskOwnerName;
    }

    public void setNewTaskOwnerName(String newTaskOwnerName) {
        this.newTaskOwnerName = newTaskOwnerName;
    }
}