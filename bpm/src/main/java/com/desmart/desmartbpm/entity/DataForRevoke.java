package com.desmart.desmartbpm.entity;

import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;

/**
 * 支持取回的信息
 */
public class DataForRevoke {
    private int insId;  // 流程实例编号
    private String tokenId;  // 需要移动的token
    private DhTaskInstance currTaskInstance;  // 现在激活的任务
    private String targetActivityBpdId;  // token移动的目标位置
    private DhRoutingRecord routingRecordToRemove; // 待删除的流转信息
    private DhTaskInstance finishedTask; // 完成的任务

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

    public DhTaskInstance getCurrTaskInstance() {
        return currTaskInstance;
    }

    public void setCurrTaskInstance(DhTaskInstance currTaskInstance) {
        this.currTaskInstance = currTaskInstance;
    }

    public String getTargetActivityBpdId() {
        return targetActivityBpdId;
    }

    public void setTargetActivityBpdId(String targetActivityBpdId) {
        this.targetActivityBpdId = targetActivityBpdId;
    }

    public DhRoutingRecord getRoutingRecordToRemove() {
        return routingRecordToRemove;
    }

    public void setRoutingRecordToRemove(DhRoutingRecord routingRecordToRemove) {
        this.routingRecordToRemove = routingRecordToRemove;
    }

    public DhTaskInstance getFinishedTask() {
        return finishedTask;
    }

    public void setFinishedTask(DhTaskInstance finishedTask) {
        this.finishedTask = finishedTask;
    }
}