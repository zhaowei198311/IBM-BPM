package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 短信模版、邮件模版
 * 1. 任务到达通知模版（短信、邮件）
 * 2. 任务超时通知模版（短信、邮件）
 * 3. ...
 */
public class DhNotifyTemplate {
	public static final String MAIL_NOTIFY_TEMPLATE = "MAIL_NOTIFY_TEMPLATE";//邮件类型
	public static final String MESSAGE_NOTIFY_TEMPLATE = "MESSAGE_NOTIFY_TEMPLATE";//短信类型
	private String templateUid;
	private String templateName;
	private String templateType;
	private String templateContent;//模板内容
	private String createUserUid;
	private Date createTime;
	private String updateUserUid;
	private Date updateTime;
	
	//不在表内
	private String userName;
	private String updateUserName;
	
	public String getTemplateUid() {
		return templateUid;
	}
	public void setTemplateUid(String templateUid) {
		this.templateUid = templateUid;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getTemplateContent() {
		return templateContent;
	}
	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	public String getCreateUserUid() {
		return createUserUid;
	}
	public void setCreateUserUid(String createUserUid) {
		this.createUserUid = createUserUid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUserUid() {
		return updateUserUid;
	}
	public void setUpdateUserUid(String updateUserUid) {
		this.updateUserUid = updateUserUid;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	@Override
	public String toString() {
		return "DhNotifyTemplate [templateUid=" + templateUid + ", templateName=" + templateName + ", templateType="
				+ templateType + ", templateContent=" + templateContent + ", createUserUid=" + createUserUid
				+ ", createTime=" + createTime + ", updateUserUid=" + updateUserUid + ", updateTime=" + updateTime
				+ ", userName=" + userName + ", updateUserName=" + updateUserName + "]";
	}
	
}
