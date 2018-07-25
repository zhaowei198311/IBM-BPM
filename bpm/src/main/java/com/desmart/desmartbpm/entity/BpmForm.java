package com.desmart.desmartbpm.entity;

import java.sql.Timestamp;
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
	private String dynContent;
	private String proUid;
	private String proVersion;
	private String creator;
	private String creatorFullName;//不是本表，连接查询得到
	private Date createTime;
	private String formNoExpression;   // 表单编号生成表达式
	private String formNoStatic;
	
	private String dynWebpage;
	
	private String proMetaName;//流程元名，前台显示
	private String proVerName;//流程快照名
	
	public BpmForm() {}

	public BpmForm(String dynUid, String dynTitle, String dynDescription, String dynContent, String proUid,
			String proVersion, String creator, String creatorFullName, Date createTime, String dynWebpage,
			String proMetaName, String proVerName) {
		super();
		this.dynUid = dynUid;
		this.dynTitle = dynTitle;
		this.dynDescription = dynDescription;
		this.dynContent = dynContent;
		this.proUid = proUid;
		this.proVersion = proVersion;
		this.creator = creator;
		this.creatorFullName = creatorFullName;
		this.createTime = createTime;
		this.dynWebpage = dynWebpage;
		this.proMetaName = proMetaName;
		this.proVerName = proVerName;
	}

	public String getFormNoExpression() {
		return formNoExpression;
	}

	public void setFormNoExpression(String formNoExpression) {
		this.formNoExpression = formNoExpression;
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
	public String getProMetaName() {
		return proMetaName;
	}
	public void setProMetaName(String proMetaName) {
		this.proMetaName = proMetaName;
	}
	public String getProVerName() {
		return proVerName;
	}
	public void setProVerName(String proVerName) {
		this.proVerName = proVerName;
	}
	public String getDynWebpage() {
		return dynWebpage;
	}
	public void setDynWebpage(String dynWebpage) {
		this.dynWebpage = dynWebpage;
	}

	public String getFormNoStatic() {
		return formNoStatic;
	}

	public void setFormNoStatic(String formNoStatic) {
		this.formNoStatic = formNoStatic;
	}

	@Override
	public String toString() {
		return "BpmForm [dynUid=" + dynUid + ", dynTitle=" + dynTitle + ", dynDescription=" + dynDescription
				+ ", dynContent=" + dynContent + ", proUid=" + proUid + ", proVersion=" + proVersion + ", creator="
				+ creator + ", creatorFullName=" + creatorFullName + ", createTime=" + createTime + ", dynWebpage="
				+ dynWebpage + ", proMetaName=" + proMetaName + ", proVerName=" + proVerName + "]";
	}
}
