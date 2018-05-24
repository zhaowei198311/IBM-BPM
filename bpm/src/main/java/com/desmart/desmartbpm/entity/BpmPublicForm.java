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
	private String publicFormFilename;
	private String creator;
	private Date createTime;
	
	private String creatorName;//用于前端显示用
	
	public BpmPublicForm() {}

	public BpmPublicForm(String publicFormUid, String publicFormName, String publicFormDescription,
			String publicFormContent, String publicFormFilename, String creator, Date createTime, String creatorName) {
		super();
		this.publicFormUid = publicFormUid;
		this.publicFormName = publicFormName;
		this.publicFormDescription = publicFormDescription;
		this.publicFormContent = publicFormContent;
		this.publicFormFilename = publicFormFilename;
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

	public String getPublicFormFilename() {
		return publicFormFilename;
	}

	public void setPublicFormFilename(String publicFormFilename) {
		this.publicFormFilename = publicFormFilename;
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
				+ ", publicFormFilename=" + publicFormFilename + ", creator=" + creator + ", createTime=" + createTime
				+ ", creatorName=" + creatorName + "]";
	}
}
