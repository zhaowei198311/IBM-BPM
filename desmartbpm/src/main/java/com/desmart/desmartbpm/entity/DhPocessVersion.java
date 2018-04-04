package com.desmart.desmartbpm.entity;

import java.util.Date;

public class DhPocessVersion {
    private String proVerUid;// 流程版本id（引擎中流程快照id）

    private String proUid;// 流程id（引擎中流程图id）
    
    private Date verCreateDate;// 版本产生时间

    private String verCreateUser;// 创建人

    private String verName;// 版本名称

    private String verAcronym;// 版本短缩名

    private String verDescription;// 版本描述

    private String isActive;// 是否激活

    private String isDefault;// 是否默认

    private String isDeployed;// 是否已发布

    private String isArchived;// 是否已归档

    private Date archivedDate;// 归档日期

    private String verStatus;// 版本状态

    private Date lastModifiedDate;// 最后更新日期

    private String lastModifiedUser;// 最后修改人

    public DhPocessVersion() {
        
    }

    public String getProVerUid() {
        return proVerUid;
    }

    public void setProVerUid(String proVerUid) {
        this.proVerUid = proVerUid;
    }

    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public Date getVerCreateDate() {
        return verCreateDate;
    }

    public void setVerCreateDate(Date verCreateDate) {
        this.verCreateDate = verCreateDate;
    }

    public String getVerCreateUser() {
        return verCreateUser;
    }

    public void setVerCreateUser(String verCreateUser) {
        this.verCreateUser = verCreateUser;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getVerAcronym() {
        return verAcronym;
    }

    public void setVerAcronym(String verAcronym) {
        this.verAcronym = verAcronym;
    }

    public String getVerDescription() {
        return verDescription;
    }

    public void setVerDescription(String verDescription) {
        this.verDescription = verDescription;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDeployed() {
        return isDeployed;
    }

    public void setIsDeployed(String isDeployed) {
        this.isDeployed = isDeployed;
    }

    public String getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(String isArchived) {
        this.isArchived = isArchived;
    }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getVerStatus() {
        return verStatus;
    }

    public void setVerStatus(String verStatus) {
        this.verStatus = verStatus;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    @Override
    public String toString() {
        return "DhPocessVersion [proVerUid=" + proVerUid + ", proUid=" + proUid
                + ", verCreateDate=" + verCreateDate + ", verCreateUser="
                + verCreateUser + ", verName=" + verName + ", verAcronym="
                + verAcronym + ", verDescription=" + verDescription
                + ", isActive=" + isActive + ", isDefault=" + isDefault
                + ", isDeployed=" + isDeployed + ", isArchived=" + isArchived
                + ", archivedDate=" + archivedDate + ", verStatus=" + verStatus
                + ", lastModifiedDate=" + lastModifiedDate
                + ", lastModifiedUser=" + lastModifiedUser + "]";
    }
    
    
}