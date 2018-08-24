package com.desmart.desmartsystem.entity;

import java.util.List;

public class SysTeamMember {
    public static final String TYPE_ROLE = "role";
    public static final String TYPE_USER = "user";

    private String memberUid;
 
    private String teamUid;

    private String userUid;   // ！！保存的是角色Uid或者人员Uid
    
    private String userName;

    private String memberType; // role  / user 常量维护

    private Integer orderIndex;
    
    private String companyCode;
    private String departUid;
    
    private String roleName;

    //不在表内
    List<SysDepartment> sysDepartmentList;
    List<String> teamUidList;
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(String memberUid) {
        this.memberUid = memberUid == null ? null : memberUid.trim();
    }

    public String getTeamUid() {
        return teamUid;
    }

    public void setTeamUid(String teamUid) {
        this.teamUid = teamUid == null ? null : teamUid.trim();
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid == null ? null : userUid.trim();
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getDepartUid() {
		return departUid;
	}

	public void setDepartUid(String departUid) {
		this.departUid = departUid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<SysDepartment> getSysDepartmentList() {
		return sysDepartmentList;
	}

	public void setSysDepartmentList(List<SysDepartment> sysDepartmentList) {
		this.sysDepartmentList = sysDepartmentList;
	}

	public List<String> getTeamUidList() {
		return teamUidList;
	}

	public void setTeamUidList(List<String> teamUidList) {
		this.teamUidList = teamUidList;
	}
    
}