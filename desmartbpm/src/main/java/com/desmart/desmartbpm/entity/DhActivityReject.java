package com.desmart.desmartbpm.entity;

/**
 * 允许驳回的活动信息
 */
public class DhActivityReject {
    private String actrUid;  // 主键

    private String activityId;  // 活动id

    private String actrRejectActivity;  // 可以驳回的活动id

    public String getActrUid() {
        return actrUid;
    }

    public void setActrUid(String actrUid) {
        this.actrUid = actrUid;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActrRejectActivity() {
        return actrRejectActivity;
    }

    public void setActrRejectActivity(String actrRejectActivity) {
        this.actrRejectActivity = actrRejectActivity;
    }

    @Override
    public String toString() {
        return "DhActivityReject [actrUid=" + actrUid + ", activityId=" + activityId + ", actrRejectActivity="
                + actrRejectActivity + "]";
    }
    
    
}
