package com.desmart.desmartsystem.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-04-27
 */
public class SysUserDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

	private String uduid;
	private String userUid;
	private String isManager;
	private String departUid;
	private String userName;
	private String departName;
	
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
}
