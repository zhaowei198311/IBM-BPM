package com.desmart.desmartbpm.entity;

/**
 * 从排他网关流出的连接线
 *
 */
public class DhGatewayLine {
    private String gatewayLineUid;  // 主键

    private String activityId; // 连接线起始点的排他网关的activity_id

    private String toActivityId; // 连接线结束点相连的节点activity_id

    private String routeResult; // 路由条件对应的值

    private String isDefault;  // 是否默认 路径 "TRUE" / "FALSE"

    private String ruleId;  // 规则主键
    
    private String activityBpdId;  // 此线在流程图上的元素id
    
    //不在表上
    private String activityName; // 连接线起始点的排他网关的activity_name
    private String gatewayActivityBpdId; // 连接线起始点的排他网关的activityBpdId
    private String toActivityName;// 连接线结束点相连的节点activity_name
    private String toActivityBpdId; // 连接线结束点相连的节点的activity_bpd_id
    
    public String getGatewayLineUid() {
        return gatewayLineUid;
    }

    public void setGatewayLineUid(String gatewayLineUid) {
        this.gatewayLineUid = gatewayLineUid;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getGatewayActivityBpdId() {
        return gatewayActivityBpdId;
    }

    public void setGatewayActivityBpdId(String gatewayActivityBpdId) {
        this.gatewayActivityBpdId = gatewayActivityBpdId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getToActivityId() {
        return toActivityId;
    }

    public void setToActivityId(String toActivityId) {
        this.toActivityId = toActivityId;
    }

    public String getRouteResult() {
        return routeResult;
    }

    public void setRouteResult(String routeResult) {
        this.routeResult = routeResult;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public String getToActivityBpdId() {
        return toActivityBpdId;
    }

    public void setToActivityBpdId(String toActivityBpdId) {
        this.toActivityBpdId = toActivityBpdId;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getActivityBpdId() {
        return activityBpdId;
    }

    public void setActivityBpdId(String activityBpdId) {
        this.activityBpdId = activityBpdId;
    }

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getToActivityName() {
		return toActivityName;
	}

	public void setToActivityName(String toActivityName) {
		this.toActivityName = toActivityName;
	}

	@Override
	public String toString() {
		return "DhGatewayLine [gatewayLineUid=" + gatewayLineUid + ", activityId=" + activityId + ", toActivityId="
				+ toActivityId + ", routeResult=" + routeResult + ", isDefault=" + isDefault + ", ruleId=" + ruleId
				+ ", activityBpdId=" + activityBpdId + ", activityName=" + activityName + ", toActivityName="
				+ toActivityName + "]";
	}
    
    
    
}
