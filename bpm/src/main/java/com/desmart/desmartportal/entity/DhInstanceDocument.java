package com.desmart.desmartportal.entity;

import java.sql.Timestamp;
import java.util.Date;

public class DhInstanceDocument {
    public DhInstanceDocument() {  
          
    }  
    public static final String DOC_TAGS_PROCESS="process";//流程附件
    public static final String DOC_TAGS_DATAFORM="dataform";//数据表格文件
    
	private String appDocUid; //流程实例文件UID 
	private String appDocFileName; //文件名
	private String appDocFileUrl;//文件ftp存储路径
	private String appDocTitle;//文件标题---
	private String appDocComment; //文件说明-----
	private Integer docVersion; //文件版本
	private String appUid; // 流程实例insUID ===== 发起流程时，存储草稿UID  
	private String taskId; //环节id ---activity_id 
	private String userUid; // 用户UID
	private String appDocType; //文档类型
	private Date appDocCreateDate;//文档创建日期
	private Integer appDocIndex;//文档排序
	private String appDocTags;//文档标签----
	private String appDocStatus;//文档状态 是否被删除 normal/del
	private String appDocIsHistory;//是否为历史文件TRUE/FALSE
	private Date appDocUpdateDate;//文档修改时间
	private String updateUserUid;//修改用户id
	private String appDocIdCard;//不同版本中的文件唯一标识
	
	private String appUserName;//不在表内
	private String updateUserName;
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
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
	public void setAppDocCreateDate(Timestamp appDocCreateDate) {
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
	public String getAppDocIsHistory() {
		return appDocIsHistory;
	}
	public void setAppDocIsHistory(String appDocIsHistory) {
		this.appDocIsHistory = appDocIsHistory;
	}
	public Date getAppDocUpdateDate() {
		return appDocUpdateDate;
	}
	public void setAppDocUpdateDate(Date appDocUpdateDate) {
		this.appDocUpdateDate = appDocUpdateDate;
	}
	public String getUpdateUserUid() {
		return updateUserUid;
	}
	public void setUpdateUserUid(String updateUserUid) {
		this.updateUserUid = updateUserUid;
	}
	public String getAppDocIdCard() {
		return appDocIdCard;
	}
	public void setAppDocIdCard(String appDocIdCard) {
		this.appDocIdCard = appDocIdCard;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	@Override
	public String toString() {
		return "DhInstanceDocument [appDocUid=" + appDocUid + ", appDocFileName=" + appDocFileName + ", appDocFileUrl="
				+ appDocFileUrl + ", appDocTitle=" + appDocTitle + ", appDocComment=" + appDocComment + ", docVersion="
				+ docVersion + ", appUid=" + appUid + ", taskId=" + taskId + ", userUid=" + userUid + ", appDocType="
				+ appDocType + ", appDocCreateDate=" + appDocCreateDate + ", appDocIndex=" + appDocIndex
				+ ", appDocTags=" + appDocTags + ", appDocStatus=" + appDocStatus + ", appDocIsHistory="
				+ appDocIsHistory + ", appDocUpdateDate=" + appDocUpdateDate + ", updateUserUid=" + updateUserUid
				+ ", appDocIdCard=" + appDocIdCard + ", appUserName=" + appUserName + ", updateUserName="
				+ updateUserName + "]";
	}
	
}
