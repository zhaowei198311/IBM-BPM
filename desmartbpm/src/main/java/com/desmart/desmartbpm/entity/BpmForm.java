package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 表单
 * @author loser_wu
 * @since 2018/4/12
 */
public class BpmForm {
	private String dynUid;
	private String dynTitle;
	private String dynDescription;
	private String dynType;
	private String dynFilename;
	private String dynContent;
	private String proUid;
	private String proVersion;
	private String creator;
	private String creatorFullName;//不是本表，连接查询得到
	private Date createTime;
	
	public BpmForm() {}

	public BpmForm(String dynUid, String dynTitle, String dynDescription, String dynType, String dynFilename,
			String dynContent, String proUid, String proVersion, String creator, String creatorFullName,
			Date createTime) {
		super();
		this.dynUid = dynUid;
		this.dynTitle = dynTitle;
		this.dynDescription = dynDescription;
		this.dynType = dynType;
		this.dynFilename = dynFilename;
		this.dynContent = dynContent;
		this.proUid = proUid;
		this.proVersion = proVersion;
		this.creator = creator;
		this.creatorFullName = creatorFullName;
		this.createTime = createTime;
	}

	public String getDynUid() {
		return dynUid;
	}
	public void setDynUid(String dynUid) {
		this.dynUid = dynUid;
	}
	public String getDynTitle() {
		return dynTitle;
	}
	public void setDynTitle(String dynTitle) {
		this.dynTitle = dynTitle;
	}
	public String getDynDescription() {
		return dynDescription;
	}
	public void setDynDescription(String dynDescription) {
		this.dynDescription = dynDescription;
	}
	public String getDynType() {
		return dynType;
	}
	public void setDynType(String dynType) {
		this.dynType = dynType;
	}
	public String getDynFilename() {
		return dynFilename;
	}
	public void setDynFilename(String dynFilename) {
		this.dynFilename = dynFilename;
	}
	public String getDynContent() {
		return dynContent;
	}
	public void setDynContent(String dynContent) {
		this.dynContent = dynContent;
	}
	public String getProUid() {
		return proUid;
	}
	public void setProUid(String proUid) {
		this.proUid = proUid;
	}
	public String getProVersion() {
		return proVersion;
	}
	public void setProVersion(String proVersion) {
		this.proVersion = proVersion;
	}
	public String getCreatorFullName() {
		return creatorFullName;
	}
	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "BpmForm [dynUid=" + dynUid + ", dynTitle=" + dynTitle + ", dynDescription=" + dynDescription
				+ ", dynType=" + dynType + ", dynFilename=" + dynFilename + ", dynContent=" + dynContent + ", proUid="
				+ proUid + ", proVersion=" + proVersion + ", creator=" + creator + ", creatorFullName="
				+ creatorFullName + ", createTime=" + createTime + "]";
	}
}
