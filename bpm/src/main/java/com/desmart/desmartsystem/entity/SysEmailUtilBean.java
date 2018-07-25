package com.desmart.desmartsystem.entity;

import java.util.List;

import com.desmart.desmartportal.entity.DhTaskInstance;
/**
 * 系统邮件参数
 * @author lys
 * 
 */
public class SysEmailUtilBean {
 	private List<String> toUserNoList;//发送目标-用户工号
	private List<String> toEmailList; //发送目标-用户邮箱
	private DhTaskInstance dhTaskInstance;//当前任务
	private String notifyTemplateUid;//通知模板
	private String bpmformsHost;//平台host
	
	public List<String> getToUserNoList() {
		return toUserNoList;
	}
	public void setToUserNoList(List<String> toUserNoList) {
		this.toUserNoList = toUserNoList;
	}
	public List<String> getToEmailList() {
		return toEmailList;
	}
	public void setToEmailList(List<String> toEmailList) {
		this.toEmailList = toEmailList;
	}
	public DhTaskInstance getDhTaskInstance() {
		return dhTaskInstance;
	}
	public void setDhTaskInstance(DhTaskInstance dhTaskInstance) {
		this.dhTaskInstance = dhTaskInstance;
	}
	public String getNotifyTemplateUid() {
		return notifyTemplateUid;
	}
	public void setNotifyTemplateUid(String notifyTemplateUid) {
		this.notifyTemplateUid = notifyTemplateUid;
	}
	
	public String getBpmformsHost() {
		return bpmformsHost;
	}
	public void setBpmformsHost(String bpmformsHost) {
		this.bpmformsHost = bpmformsHost;
	}
	@Override
	public String toString() {
		return "SysEmailUtilBean [toUserNoList=" + toUserNoList + ", toEmailList=" + toEmailList + ", dhTaskInstance="
				+ dhTaskInstance + ", notifyTemplateUid=" + notifyTemplateUid + ", bpmformsHost=" + bpmformsHost + "]";
	}
	
	
}
