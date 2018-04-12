package com.desmart.desmartbpm.entity;

import java.math.BigDecimal;

public class SysPositionUser {
    private String mapUid;

    private String userUid;

    private String departUid;

    private String positionUid;

    private BigDecimal isShow;

    public String getMapUid() {
        return mapUid;
    }

    public void setMapUid(String mapUid) {
        this.mapUid = mapUid == null ? null : mapUid.trim();
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

    public String getPositionUid() {
        return positionUid;
    }

    public void setPositionUid(String positionUid) {
        this.positionUid = positionUid == null ? null : positionUid.trim();
    }

    public BigDecimal getIsShow() {
        return isShow;
    }

    public void setIsShow(BigDecimal isShow) {
        this.isShow = isShow;
    }
}