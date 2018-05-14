package com.desmart.desmartsystem.entity;

import java.util.Date;

/**
 * 
 * @ClassName: SysDictionaryData  
 * @Description: 数据字典内容  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
public class SysDictionaryData {	
	/**
	 * 数据字典内容ID
	 */
	private String dicDataUid;
	/**
	 * 数据字段内容code
	 */
	private String dicDataCode;
	/**
	 * 数据字典分类内容名
	 */
	private String dicDataName;
	/**
	 * 数据字典ID
	 */
	private String dicUid;
	/**
	 * 数据字典分类内容描述
	 */
	private String dicDataDescription;
	/**
	 * 排序号
	 */
	private Double dicDataSort;
	/**
	 * 状态
	 */
	private String dicDataStatus;
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

	public String getDicDataUid() {
		return dicDataUid;
	}

	public void setDicDataUid(String dicDataUid) {
		this.dicDataUid = dicDataUid;
	}

	public String getDicDataCode() {
		return dicDataCode;
	}

	public void setDicDataCode(String dicDataCode) {
		this.dicDataCode = dicDataCode;
	}

	public String getDicDataName() {
		return dicDataName;
	}

	public void setDicDataName(String dicDataName) {
		this.dicDataName = dicDataName;
	}

	public String getDicUid() {
		return dicUid;
	}

	public void setDicUid(String dicUid) {
		this.dicUid = dicUid;
	}

	public String getDicDataDescription() {
		return dicDataDescription;
	}

	public void setDicDataDescription(String dicDataDescription) {
		this.dicDataDescription = dicDataDescription;
	}

	public Double getDicDataSort() {
		return dicDataSort;
	}

	public void setDicDataSort(Double dicDataSort) {
		this.dicDataSort = dicDataSort;
	}

	public String getDicDataStatus() {
		return dicDataStatus;
	}

	public void setDicDataStatus(String dicDataStatus) {
		this.dicDataStatus = dicDataStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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

	@Override
	public String toString() {
		return "SysDictionaryData [dicDataUid=" + dicDataUid + ", dicDataCode=" + dicDataCode + ", dicDataName="
				+ dicDataName + ", dicUid=" + dicUid + ", dicDataDescription=" + dicDataDescription + ", dicDataSort="
				+ dicDataSort + ", dicDataStatus=" + dicDataStatus + ", createDate=" + createDate + ", createBy="
				+ createBy + ", updateDate=" + updateDate + ", updateBy=" + updateBy + "]";
	}
	
	
}
