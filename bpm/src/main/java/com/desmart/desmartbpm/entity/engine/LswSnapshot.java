package com.desmart.desmartbpm.entity.engine;

import java.util.Date;

/**
 * ibm bpm引擎中的快照
 * @author yaoyunqing
 *
 */
public class LswSnapshot {
    private String snapshotId; // 快照id
    private String repositoryBranchId;
    private Integer createdByUserId;
    private Date createdOn;
    private String name;
    private String acronym;
    private String description;
    private String branchId;
    private String projectId;
    private Integer seqNum;
    private String isSctive;
    private String isBlaActive;
    private String isStarted;
    private String isInstalled;
    private String isBlaInstalled;
    private String isTipDeployed;
    private String isDefault;
    private Integer errorCounter;
    private String changeData;
    private String tipDeploymentInProgress;
    private Date origCreatedOn;
    private Date activatedOn;
    private String hasSuspendedAllInstances;
    private String status;
    private String isActive;
    private String capability;
    private String isArchived;
    private Date archivedOn;
    private Date lastModified;
    public String getSnapshotId() {
        return snapshotId;
    }
    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
    public String getRepositoryBranchId() {
        return repositoryBranchId;
    }
    public void setRepositoryBranchId(String repositoryBranchId) {
        this.repositoryBranchId = repositoryBranchId;
    }
    public Integer getCreatedByUserId() {
        return createdByUserId;
    }
    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAcronym() {
        return acronym;
    }
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getBranchId() {
        return branchId;
    }
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    public Integer getSeqNum() {
        return seqNum;
    }
    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
    public String getIsSctive() {
        return isSctive;
    }
    public void setIsSctive(String isSctive) {
        this.isSctive = isSctive;
    }
    public String getIsBlaActive() {
        return isBlaActive;
    }
    public void setIsBlaActive(String isBlaActive) {
        this.isBlaActive = isBlaActive;
    }
    public String getIsStarted() {
        return isStarted;
    }
    public void setIsStarted(String isStarted) {
        this.isStarted = isStarted;
    }
    public String getIsInstalled() {
        return isInstalled;
    }
    public void setIsInstalled(String isInstalled) {
        this.isInstalled = isInstalled;
    }
    public String getIsBlaInstalled() {
        return isBlaInstalled;
    }
    public void setIsBlaInstalled(String isBlaInstalled) {
        this.isBlaInstalled = isBlaInstalled;
    }
    public String getIsTipDeployed() {
        return isTipDeployed;
    }
    public void setIsTipDeployed(String isTipDeployed) {
        this.isTipDeployed = isTipDeployed;
    }
    public String getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
    public Integer getErrorCounter() {
        return errorCounter;
    }
    public void setErrorCounter(Integer errorCounter) {
        this.errorCounter = errorCounter;
    }
    public String getChangeData() {
        return changeData;
    }
    public void setChangeData(String changeData) {
        this.changeData = changeData;
    }
    public String getTipDeploymentInProgress() {
        return tipDeploymentInProgress;
    }
    public void setTipDeploymentInProgress(String tipDeploymentInProgress) {
        this.tipDeploymentInProgress = tipDeploymentInProgress;
    }
    public Date getOrigCreatedOn() {
        return origCreatedOn;
    }
    public void setOrigCreatedOn(Date origCreatedOn) {
        this.origCreatedOn = origCreatedOn;
    }
    public Date getActivatedOn() {
        return activatedOn;
    }
    public void setActivatedOn(Date activatedOn) {
        this.activatedOn = activatedOn;
    }
    public String getHasSuspendedAllInstances() {
        return hasSuspendedAllInstances;
    }
    public void setHasSuspendedAllInstances(String hasSuspendedAllInstances) {
        this.hasSuspendedAllInstances = hasSuspendedAllInstances;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCapability() {
        return capability;
    }
    public void setCapability(String capability) {
        this.capability = capability;
    }
    public String getIsArchived() {
        return isArchived;
    }
    public void setIsArchived(String isArchived) {
        this.isArchived = isArchived;
    }
    public Date getArchivedOn() {
        return archivedOn;
    }
    public void setArchivedOn(Date archivedOn) {
        this.archivedOn = archivedOn;
    }
    public Date getLastModified() {
        return lastModified;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "LswSnapshot{" +
                "snapshotId='" + snapshotId + '\'' +
                ", repositoryBranchId='" + repositoryBranchId + '\'' +
                ", createdByUserId=" + createdByUserId +
                ", createdOn=" + createdOn +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", description='" + description + '\'' +
                ", branchId='" + branchId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", seqNum=" + seqNum +
                ", isSctive='" + isSctive + '\'' +
                ", isBlaActive='" + isBlaActive + '\'' +
                ", isStarted='" + isStarted + '\'' +
                ", isInstalled='" + isInstalled + '\'' +
                ", isBlaInstalled='" + isBlaInstalled + '\'' +
                ", isTipDeployed='" + isTipDeployed + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", errorCounter=" + errorCounter +
                ", changeData='" + changeData + '\'' +
                ", tipDeploymentInProgress='" + tipDeploymentInProgress + '\'' +
                ", origCreatedOn=" + origCreatedOn +
                ", activatedOn=" + activatedOn +
                ", hasSuspendedAllInstances='" + hasSuspendedAllInstances + '\'' +
                ", status='" + status + '\'' +
                ", isActive='" + isActive + '\'' +
                ", capability='" + capability + '\'' +
                ", isArchived='" + isArchived + '\'' +
                ", archivedOn=" + archivedOn +
                ", lastModified=" + lastModified +
                '}';
    }
}
