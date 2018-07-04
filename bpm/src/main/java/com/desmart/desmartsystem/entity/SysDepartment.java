package com.desmart.desmartsystem.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
public class SysDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID   
     */
	private String departUid; 
    /**
     * 上级组织ID 
     */
	private String departParent;
    /**
     * 组织名称   
     */
	private String departName;
    /**
     * 排序     
     */
	private String orderIndex;
    /**
     * 组织描述   
     */
	private String departDesc;
    /**
     * 组织代码   
     */
	private String departNo;
    /**
     * 组织层级   
     */
	private String departLayer;
    /**
     * 组织管理人  
     */
	private String departAdmins;
    /**
     * 组织时区   
     */
	private String departZone;
    /**
     * 公司ID   
     */
	private String companyUid;
    /**
     * 备用字段   
     */
	private String ext1;
    /**
     * 备用字段   
     */
	private String ext2;
    /**
     * 工作日历   
     */
	private String workCalendar;
    /**
     * 备用字段   
     */
	private String ext3;
    /**
     * 备用字段   
     */
	private String ext4;
    /**
     * 备用字段   
     */
	private String ext5;
    /**
     * 组织类型   
     */
	private String departType;
    /**
     * 是否禁用   
     */
	private String isClosed;
    /**
     * 禁用日期   
     */
	private Date closeDate;
    /**
     * 开始日期   
     */
	private Date beginDate;
    /**
     * 结束日期   
     */
	private Date endDate;
    /**
     * 生成日期   
     */
	private Date createDate;
    /**
     * 更新日期   
     */
	private Date updateDate;
	
	private String displayName;
	
	private String departParentName;
	
	private List<SysUserDepartment> sysUserDepartmentList;


	public String getDepartUid() {
		return departUid;
	}

	public void setDepartUid(String departUid) {
		this.departUid = departUid;
	}

	public String getDepartParent() {
		return departParent;
	}

	public void setDepartParent(String departParent) {
		this.departParent = departParent;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getDepartDesc() {
		return departDesc;
	}

	public void setDepartDesc(String departDesc) {
		this.departDesc = departDesc;
	}

	public String getDepartNo() {
		return departNo;
	}

	public void setDepartNo(String departNo) {
		this.departNo = departNo;
	}

	public String getDepartLayer() {
		return departLayer;
	}

	public void setDepartLayer(String departLayer) {
		this.departLayer = departLayer;
	}

	public String getDepartAdmins() {
		return departAdmins;
	}

	public void setDepartAdmins(String departAdmins) {
		this.departAdmins = departAdmins;
	}

	public String getDepartZone() {
		return departZone;
	}

	public void setDepartZone(String departZone) {
		this.departZone = departZone;
	}

	public String getCompanyUid() {
		return companyUid;
	}

	public void setCompanyUid(String companyUid) {
		this.companyUid = companyUid;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getWorkCalendar() {
		return workCalendar;
	}

	public void setWorkCalendar(String workCalendar) {
		this.workCalendar = workCalendar;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public List<SysUserDepartment> getSysUserDepartmentList() {
		return sysUserDepartmentList;
	}

	public void setSysUserDepartmentList(List<SysUserDepartment> sysUserDepartmentList) {
		this.sysUserDepartmentList = sysUserDepartmentList;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDepartParentName() {
		return departParentName;
	}

	public void setDepartParentName(String departParentName) {
		this.departParentName = departParentName;
	}

	
}
