package com.desmart.desmartbpm.entity;

import com.desmart.desmartportal.entity.*;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

/**
 * 提交任务需要的信息，用以完成任务，或推送到MQ
 */
public class DataForSubmitTask {

    private CommonBusinessObject pubBo;
    private BpmRoutingData bpmRoutingData;
    private DhRoutingRecord dhRoutingRecord; // 流转信息
    private DhProcessInstance currentProcessInstance;
    private DhStep nextStep;  // 下一个步骤
    private DhTaskInstance currTaskInstance;
    private BpmGlobalConfig bpmGlobalConfig;
    private String applyUser; // 完成任务前尝试将任务分配给谁，为null则不分配


    public CommonBusinessObject getPubBo() {
        return pubBo;
    }

    public void setPubBo(CommonBusinessObject pubBo) {
        this.pubBo = pubBo;
    }

    public BpmRoutingData getBpmRoutingData() {
        return bpmRoutingData;
    }

    public void setBpmRoutingData(BpmRoutingData bpmRoutingData) {
        this.bpmRoutingData = bpmRoutingData;
    }

    public DhRoutingRecord getDhRoutingRecord() {
        return dhRoutingRecord;
    }

    public void setDhRoutingRecord(DhRoutingRecord dhRoutingRecord) {
        this.dhRoutingRecord = dhRoutingRecord;
    }

    public DhProcessInstance getCurrentProcessInstance() {
        return currentProcessInstance;
    }

    public void setCurrentProcessInstance(DhProcessInstance currentProcessInstance) {
        this.currentProcessInstance = currentProcessInstance;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public DhStep getNextStep() {
        return nextStep;
    }

    public void setNextStep(DhStep nextStep) {
        this.nextStep = nextStep;
    }

    public DhTaskInstance getCurrTaskInstance() {
        return currTaskInstance;
    }

    public void setCurrTaskInstance(DhTaskInstance currTaskInstance) {
        this.currTaskInstance = currTaskInstance;
    }

    public BpmGlobalConfig getBpmGlobalConfig() {
        return bpmGlobalConfig;
    }

    public void setBpmGlobalConfig(BpmGlobalConfig bpmGlobalConfig) {
        this.bpmGlobalConfig = bpmGlobalConfig;
    }
}