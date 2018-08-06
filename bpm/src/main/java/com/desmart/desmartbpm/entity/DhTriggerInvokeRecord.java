package com.desmart.desmartbpm.entity;

import java.util.Date;

// 触发器调用记录
public class DhTriggerInvokeRecord {
    private String invokeUid;

    private String insUid;

    private String stepUid;

    private Date invokeTime;

    public String getInvokeUid() {
        return invokeUid;
    }

    public void setInvokeUid(String invokeUid) {
        this.invokeUid = invokeUid == null ? null : invokeUid.trim();
    }

    public String getInsUid() {
        return insUid;
    }

    public void setInsUid(String insUid) {
        this.insUid = insUid == null ? null : insUid.trim();
    }

    public String getStepUid() {
        return stepUid;
    }

    public void setStepUid(String stepUid) {
        this.stepUid = stepUid == null ? null : stepUid.trim();
    }

    public Date getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(Date invokeTime) {
        this.invokeTime = invokeTime;
    }
}