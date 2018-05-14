package com.desmart.desmartsystem.entity;

import java.math.BigDecimal;
import java.util.Date;

public class SysPosition {
    private String positionUid;

    private String positionName;

    private Integer orderIndex;

    private String positionNo;

    private String positionLayer;

    private String positionDesc;

    private String ext1;

    private String ext2;

    private String ext3;

    private String ext4;

    private String ext5;

    private BigDecimal isClosed;

    private Date closeDate;

    private Date beginDate;

    private Date endDate;

    private Date createDate;

    private Date updateDate;

    public String getPositionUid() {
        return positionUid;
    }

    public void setPositionUid(String positionUid) {
        this.positionUid = positionUid == null ? null : positionUid.trim();
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName == null ? null : positionName.trim();
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo == null ? null : positionNo.trim();
    }

    public String getPositionLayer() {
        return positionLayer;
    }

    public void setPositionLayer(String positionLayer) {
        this.positionLayer = positionLayer == null ? null : positionLayer.trim();
    }

    public String getPositionDesc() {
        return positionDesc;
    }

    public void setPositionDesc(String positionDesc) {
        this.positionDesc = positionDesc == null ? null : positionDesc.trim();
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4 == null ? null : ext4.trim();
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5 == null ? null : ext5.trim();
    }

    public BigDecimal getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(BigDecimal isClosed) {
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
}