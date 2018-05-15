package com.desmart.desmartbpm.entity;

/**
 * 对象权限
 * @author yaoyunqing
 *
 */
public class DhObjectPermission {
    private String opUid; 

    private String proAppId;

    private String proUid;

    private String proVerUid; 

    private String stepUid;  // 活动id

    private String opParticipateType; // 权限所有者类型：USER，TEAM，ROLE

    private String opParticipateUid;  // 权限所有者ID

    private String opObjType; // 对象类型：FORM，PROCESS，FIELD，ATTACHMENT

    private String opObjUid;  // 对象ID

    private String opAction; // 权限类型：EDIT，VIEW，HIDDEN，START，UPLOAD，DOWNLOAD
    

    public String getOpUid() {
        return opUid;
    }

    public void setOpUid(String opUid) {
        this.opUid = opUid;
    }

    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public String getProVerUid() {
        return proVerUid;
    }

    public void setProVerUid(String proVerUid) {
        this.proVerUid = proVerUid;
    }

    public String getStepUid() {
        return stepUid;
    }

    public void setStepUid(String stepUid) {
        this.stepUid = stepUid;
    }

    public String getOpParticipateType() {
        return opParticipateType;
    }

    public void setOpParticipateType(String opParticipateType) {
        this.opParticipateType = opParticipateType;
    }

    public String getOpParticipateUid() {
        return opParticipateUid;
    }

    public void setOpParticipateUid(String opParticipateUid) {
        this.opParticipateUid = opParticipateUid;
    }

    public String getOpObjType() {
        return opObjType;
    }

    public void setOpObjType(String opObjType) {
        this.opObjType = opObjType;
    }

    public String getOpObjUid() {
        return opObjUid;
    }

    public void setOpObjUid(String opObjUid) {
        this.opObjUid = opObjUid;
    }

    public String getOpAction() {
        return opAction;
    }

    public void setOpAction(String opAction) {
        this.opAction = opAction;
    }

    @Override
    public String toString() {
        return "DhObjectPermission [opUid=" + opUid + ", proAppId=" + proAppId
                + ", proUid=" + proUid + ", proVerUid=" + proVerUid
                + ", stepUid=" + stepUid + ", opParticipateType="
                + opParticipateType + ", opParticipateUid=" + opParticipateUid
                + ", opObjType=" + opObjType + ", opObjUid=" + opObjUid
                + ", opAction=" + opAction + "]";
    }
    
    
    
}
