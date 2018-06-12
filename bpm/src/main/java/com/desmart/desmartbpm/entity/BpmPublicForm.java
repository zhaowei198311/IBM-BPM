package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 公共表单实体类
 * @author loser_wu
 * @since 2018年5月24日
 */
public class BpmPublicForm {
	private String publicFormUid;
	private String publicFormName;
	private String publicFormDescription;
	private String publicFormContent;
	private String publicFormWebpage;
	private String publicFormCode;
	private String creator;
	private Date createTime;
	
	private String creatorName;//用于前端显示用
	
	public BpmPublicForm() {}

	public BpmPublicForm(String publicFormUid, String publicFormName, String publicFormDescription,
			String publicFormContent, String publicFormWebpage, String publicFormCode, String creator, Date createTime,
			String creatorName) {
		super();
		this.publicFormUid = publicFormUid;
		this.publicFormName = publicFormName;
		this.publicFormDescription = publicFormDescription;
		this.publicFormContent = publicFormContent;
		this.publicFormWebpage = publicFormWebpage;
		this.publicFormCode = publicFormCode;
		this.creator = creator;
		this.createTime = createTime;
		this.creatorName = creatorName;
	}

	public String getPublicFormUid() {
		return publicFormUid;
	}

	public void setPublicFormUid(String publicFormUid) {
		this.publicFormUid = publicFormUid;
	}

	public String getPublicFormName() {
		return publicFormName;
	}

	public void setPublicFormName(String publicFormName) {
		this.publicFormName = publicFormName;
	}

	public String getPublicFormDescription() {
		return publicFormDescription;
	}

	public void setPublicFormDescription(String publicFormDescription) {
		this.publicFormDescription = publicFormDescription;
	}

	public String getPublicFormContent() {
		return publicFormContent;
	}

	public void setPublicFormContent(String publicFormContent) {
		this.publicFormContent = publicFormContent;
	}

	public String getPublicFormWebpage() {
		return publicFormWebpage;
	}

	public void setPublicFormWebpage(String publicFormWebpage) {
		this.publicFormWebpage = publicFormWebpage;
	}

	public String getPublicFormCode() {
		return publicFormCode;
	}

	public void setPublicFormCode(String publicFormCode) {
		this.publicFormCode = publicFormCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	@Override
	public String toString() {
		return "BpmPublicForm [publicFormUid=" + publicFormUid + ", publicFormName=" + publicFormName
				+ ", publicFormDescription=" + publicFormDescription + ", publicFormContent=" + publicFormContent
				+ ", publicFormWebpage=" + publicFormWebpage + ", publicFormCode=" + publicFormCode + ", creator="
				+ creator + ", createTime=" + createTime + ", creatorName=" + creatorName + "]";
	}
}
