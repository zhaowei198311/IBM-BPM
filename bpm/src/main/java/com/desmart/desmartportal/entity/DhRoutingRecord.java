package com.desmart.desmartportal.entity;

import java.util.Date;

public class DhRoutingRecord {
	private String routeUid;//流转id
	private String insUid;//流程实例uid
	private String activityName;//环节名
	private String routeType;//操作类型
	private String userUid;//操作人uid 
	private Date createTime;//操作时间
	//不在表内，关联查询
	private String userName;//操作人姓名
	private String station;//操作人岗位身份
	public String getRouteUid() {
		return routeUid;
	}
	public void setRouteUid(String routeUid) {
		this.routeUid = routeUid;
	}
	public String getInsUid() {
		return insUid;
	}
	public void setInsUid(String insUid) {
		this.insUid = insUid;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	@Override
	public String toString() {
		return "DhRoutingRecord [routeUid=" + routeUid + ", insUid=" + insUid + ", activityName=" + activityName
				+ ", routeType=" + routeType + ", userUid=" + userUid + ", createTime=" + createTime + ", userName="
				+ userName + ", station=" + station + "]";
	}
	
	
}
