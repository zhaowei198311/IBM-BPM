package com.desmart.desmartsystem.entity;

import java.io.Serializable;

/**
 * 描述人员外挂部门
 */
public class SysUserDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String IS_MANAGER_TRUE = "true";
    public static final String IS_MANAGER_FALSE = "false";

	private String uduid;  // 主键
	private String userUid;  // 人员主键
	private String isManager;  // 是否作为经理 "true" / "false"
	private String departUid;  // 外挂部门号
	private String userName;
	private String departName;
	private String departNo;
	private String isDelete;
	private String companyCode;
	private String companyName;
	
	//是否可以删除
	private String canDelete;
	
	private SysCompany sysCompany;
	
	public String getUduid() {
		return uduid;
	}

	public void setUduid(String uduid) {
		this.uduid = uduid;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getDepartUid() {
		return departUid;
	}

	public void setDepartUid(String departUid) {
		this.departUid = departUid;
	}
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	@Override
	public String toString() {
		return "SysUserDepartment{" +
			"uduid=" + uduid +
			", userUid=" + userUid +
			", isManager=" + isManager +
			", departUid=" + departUid +
			"}";
	}
	

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
//
//	public String getCompanyName() {
//		return companyName;
//	}
//
//	public void setCompanyName(String companyName) {
//		this.companyName = companyName;
//	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public SysCompany getSysCompany() {
		return sysCompany;
	}

	public void setSysCompany(SysCompany sysCompany) {
		this.sysCompany = sysCompany;
	}

	public String getDepartNo() {
		return departNo;
	}

	public void setDepartNo(String departNo) {
		this.departNo = departNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
	}
	
}
