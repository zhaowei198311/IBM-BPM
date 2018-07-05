package com.desmart.desmartsystem.entity;

import java.math.BigDecimal;
import java.util.List;

public class SysRoleUser {
    private String mapUid;

    private Integer mapType;

    private String userUid;

    private String departUid;

    private String roleUid;

    private BigDecimal isManager;

    private BigDecimal isShow;
    
    private List<SysUser> users;
    
    private String userName;
    
    private String companyCode;
    
    private String roleName;
    
    private String isClosed;
    
    

    public List<SysUser> getUsers() {
		return users;
	}

	public void setUsers(List<SysUser> users) {
		this.users = users;
	}

	public String getMapUid() {
        return mapUid;
    }

    public void setMapUid(String mapUid) {
        this.mapUid = mapUid == null ? null : mapUid.trim();
    }

    public Integer getMapType() {
        return mapType;
    }

    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid == null ? null : userUid.trim();
    }

    public String getDepartUid() {
        return departUid;
    }

    public void setDepartUid(String departUid) {
        this.departUid = departUid == null ? null : departUid.trim();
    }

    public String getRoleUid() {
        return roleUid;
    }

    public void setRoleUid(String roleUid) {
        this.roleUid = roleUid == null ? null : roleUid.trim();
    }

    public BigDecimal getIsManager() {
        return isManager;
    }

    public void setIsManager(BigDecimal isManager) {
        this.isManager = isManager;
    }

    public BigDecimal getIsShow() {
        return isShow;
    }

    public void setIsShow(BigDecimal isShow) {
        this.isShow = isShow;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}
	
	
    
}