package com.desmart.desmartsystem.entity;

import java.math.BigDecimal;

public class SysRoleResource {
    private String mapUid;

    private String roleUid;

    private String resourceUid;

    private BigDecimal isShow;

    public String getMapUid() {
        return mapUid;
    }

    public void setMapUid(String mapUid) {
        this.mapUid = mapUid == null ? null : mapUid.trim();
    }

    public String getRoleUid() {
        return roleUid;
    }

    public void setRoleUid(String roleUid) {
        this.roleUid = roleUid == null ? null : roleUid.trim();
    }

    public String getResourceUid() {
        return resourceUid;
    }

    public void setResourceUid(String resourceUid) {
        this.resourceUid = resourceUid == null ? null : resourceUid.trim();
    }

    public BigDecimal getIsShow() {
        return isShow;
    }

    public void setIsShow(BigDecimal isShow) {
        this.isShow = isShow;
    }
}