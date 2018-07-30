package com.desmart.desmartbpm.service;

import java.util.Date;

/**
 * 引擎中公开的流程
 */
public class BpmExposedItem {
    private String proAppId;
    private String proAppName;
    private String bpdId;
    private String bpdName;
    private String snapshotId;
    private String snapshotName;
    private Date snapshotCreateTime;
    private String batchId;


    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getProAppName() {
        return proAppName;
    }

    public void setProAppName(String proAppName) {
        this.proAppName = proAppName;
    }

    public String getBpdId() {
        return bpdId;
    }

    public void setBpdId(String bpdId) {
        this.bpdId = bpdId;
    }

    public String getBpdName() {
        return bpdName;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public void setBpdName(String bpdName) {
        this.bpdName = bpdName;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Date getSnapshotCreateTime() {
        return snapshotCreateTime;
    }

    public void setSnapshotCreateTime(Date snapshotCreateTime) {
        this.snapshotCreateTime = snapshotCreateTime;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}