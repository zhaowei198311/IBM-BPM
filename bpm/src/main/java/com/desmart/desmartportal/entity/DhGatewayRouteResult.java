package com.desmart.desmartportal.entity;

import java.util.Date;

/**
 * 网关决策结果
 */
public class DhGatewayRouteResult {
    private String routeResultUid; // 主键
    private Integer insId;         // 流程实例编号
    private String activityBpdId;  // 活动元素id
    private String routeResult;    // 结果
    private String status;         // 状态
    private Date createTime;
    private Date updateTime;
    public String getRouteResultUid() {
        return routeResultUid;
    }
    public void setRouteResultUid(String routeResultUid) {
        this.routeResultUid = routeResultUid;
    }
    public Integer getInsId() {
        return insId;
    }
    public void setInsId(Integer insId) {
        this.insId = insId;
    }
    public String getActivityBpdId() {
        return activityBpdId;
    }
    public void setActivityBpdId(String activityBpdId) {
        this.activityBpdId = activityBpdId;
    }
    public String getRouteResult() {
        return routeResult;
    }
    public void setRouteResult(String routeResult) {
        this.routeResult = routeResult;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "DhGatewayRouteResult [routeResultUid=" + routeResultUid
                + ", insId=" + insId + ", activityBpdId=" + activityBpdId
                + ", routeResult=" + routeResult + ", status=" + status
                + ", createTime=" + createTime + ", updateTime=" + updateTime
                + "]";
    }
    
}
