package com.desmart.desmartsystem.entity;

import java.util.Date;

/**
 * 
 * @ClassName: SysDictionary  
 * @Description: 数据字典  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
public class SysDictionary {
	/**
	 * 数据字典主键ID
	 */
	private String dicUid;
	/**
	 * 数据字典code
	 */
	private String dicCode;
	/**
	 * 分类
	 */
	private String dicName;
	/**
	 * 分类描述
	 */
	private String dicDescription;
	/**
	 * 状态
	 */
	private String dicStatus;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 更新人
	 */
	private String updateBy;

	public String getDicUid() {
		return dicUid;
	}

	public void setDicUid(String dicUid) {
		this.dicUid = dicUid;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getDicDescription() {
		return dicDescription;
	}

	public void setDicDescription(String dicDescription) {
		this.dicDescription = dicDescription;
	}

	public String getDicStatus() {
		return dicStatus;
	}

	public void setDicStatus(String dicStatus) {
		this.dicStatus = dicStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Override
	public String toString() {
		return "SysDictionary [dicUid=" + dicUid + ", dicCode=" + dicCode + ", dicName=" + dicName + ", dicDescription="
				+ dicDescription + ", dicStatus=" + dicStatus + ", createDate=" + createDate + ", createBy=" + createBy
				+ ", updateDate=" + updateDate + ", updateBy=" + updateBy + "]";
	}
	
	
}
