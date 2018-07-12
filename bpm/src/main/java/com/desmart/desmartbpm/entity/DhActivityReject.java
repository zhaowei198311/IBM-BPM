package com.desmart.desmartbpm.entity;

/**
 * 允许驳回的活动信息
 * activity_id -> activity_bpd_id
 */
public class DhActivityReject {
    private String actrUid;  // 主键

    private String activityId;  // 活动id

    private String actrRejectActivity;  // 可以驳回的活动id
    
    // 非此表字段
    private String activityName;
    private String activityBpdId;
    
    public DhActivityReject() {
        
    }

    public DhActivityReject(String actrUid, String activityId, String actrRejectActivity) {
        this.actrUid = actrUid;
        this.activityId = activityId;
        this.actrRejectActivity = actrRejectActivity;
    }
    
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    
    public String getActivityBpmId() {
		return activityBpdId;
	}

	public void setActivityBpmId(String activityBpdId) {
		this.activityBpdId = activityBpdId;
	}

	@Override
    public String toString() {
        return "DhActivityReject [actrUid=" + actrUid + ", activityId="
                + activityId + ", actrRejectActivity=" + actrRejectActivity
                + ", activityName=" + activityName + "]";
    }
    
    
}
