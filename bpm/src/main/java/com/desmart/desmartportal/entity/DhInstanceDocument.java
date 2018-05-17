package com.desmart.desmartportal.entity;

import java.util.Date;

public class DhInstanceDocument {
	/** 
     * 默认的构造方法必须不能省，不然不能解析 
     */  
    public DhInstanceDocument() {  
          
    }  
	private String appDocUid; //流程实例文件UID 
	private String appDocFileName; //文件名
	private String appDocFileUrl;//文件ftp存储路径
	private String appDocTitle;//文件标题---
	private String appDocComment; //文件说明-----
	private Integer docVersion; //文件版本
	private String appUid; // 流程实例UID ===== 发起流程时，存储草稿UID  
	private Integer taskId; //环节id -----activity_id 
	private String userUid; // 用户UID
	private String appDocType; //文档类型
	private Date appDocCreateDate;//文档创建日期
	private Integer appDocIndex;//文档排序
	private String appDocTags;//文档标签----
	private String appDocStatus;//文档状态
	private String appUserName;//不在表内
	public String getAppDocUid() {
		return appDocUid;
	}
	public void setAppDocUid(String appDocUid) {
		this.appDocUid = appDocUid;
	}
	public String getAppDocFileName() {
		return appDocFileName;
	}
	public void setAppDocFileName(String appDocFileName) {
		this.appDocFileName = appDocFileName;
	}
	public String getAppDocTitle() {
		return appDocTitle;
	}
	public void setAppDocTitle(String appDocTitle) {
		this.appDocTitle = appDocTitle;
	}
	public String getAppDocComment() {
		return appDocComment;
	}
	public void setAppDocComment(String appDocComment) {
		this.appDocComment = appDocComment;
	}
	public Integer getDocVersion() {
		return docVersion;
	}
	public void setDocVersion(Integer docVersion) {
		this.docVersion = docVersion;
	}
	public String getAppUid() {
		return appUid;
	}
	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	public String getAppDocType() {
		return appDocType;
	}
	public void setAppDocType(String appDocType) {
		this.appDocType = appDocType;
	}
	public Date getAppDocCreateDate() {
		return appDocCreateDate;
	}
	public void setAppDocCreateDate(Date appDocCreateDate) {
		this.appDocCreateDate = appDocCreateDate;
	}
	public Integer getAppDocIndex() {
		return appDocIndex;
	}
	public void setAppDocIndex(Integer appDocIndex) {
		this.appDocIndex = appDocIndex;
	}
	public String getAppDocTags() {
		return appDocTags;
	}
	public void setAppDocTags(String appDocTags) {
		this.appDocTags = appDocTags;
	}
	public String getAppDocStatus() {
		return appDocStatus;
	}
	public void setAppDocStatus(String appDocStatus) {
		this.appDocStatus = appDocStatus;
	}
	public String getAppDocFileUrl() {
		return appDocFileUrl;
	}
	public void setAppDocFileUrl(String appDocFileUrl) {
		this.appDocFileUrl = appDocFileUrl;
	}
	
	public String getAppUserName() {
		return appUserName;
	}
	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}
	@Override
	public String toString() {
		return "DhInstanceDocument [appDocUid=" + appDocUid + ", appDocFileName=" + appDocFileName + ", appDocFileUrl="
				+ appDocFileUrl + ", appDocTitle=" + appDocTitle + ", appDocComment=" + appDocComment + ", docVersion="
				+ docVersion + ", appUid=" + appUid + ", taskId=" + taskId + ", userUid=" + userUid + ", appDocType="
				+ appDocType + ", appDocCreateDate=" + appDocCreateDate + ", appDocIndex=" + appDocIndex
				+ ", appDocTags=" + appDocTags + ", appDocStatus=" + appDocStatus + ", appUserName=" + appUserName
				+ "]";
	}
	
	
}
