package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 流程定义元数据
 *
 */
public class DhProcessMeta {
    private String proMetaUid;  // 主键
    private String proAppId;  // 应用库id
    private String proUid; // 流程图id
    private String proName; // 流程名
    private String creator;
    private Date createTime;
    private String updateUser;
    private Date lastUpdateTime;
    private String categoryUid;  // 流程分类
    private String proDisplay; // 流程引擎中流程名
    
    // 不再此表中
    private String creatorFullName;
    private String updatorFullName;
    
    public String getProMetaUid() {
        return proMetaUid;
    }
    public void setProMetaUid(String proMetaUid) {
        this.proMetaUid = proMetaUid;
    }
    public String getProAppId() {
        return proAppId;
    }
    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }
    public String getProUid() {
        return proUid;
    }
    public void setProUid(String proUid) {
        this.proUid = proUid;
    }
    public String getProName() {
        return proName;
    }
    public void setProName(String proName) {
        this.proName = proName;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateUser() {
        return updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getCategoryUid() {
        return categoryUid;
    }
    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }
    
    public String getCreatorFullName() {
        return creatorFullName;
    }
    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }
    public String getUpdatorFullName() {
        return updatorFullName;
    }
    public void setUpdatorFullName(String updatorFullName) {
        this.updatorFullName = updatorFullName;
    }
    
    
    public String getProDisplay() {
        return proDisplay;
    }
    public void setProDisplay(String proDisplay) {
        this.proDisplay = proDisplay;
    }
    @Override
    public String toString() {
        return "DhProcessMeta [proMetaUid=" + proMetaUid + ", proAppId="
                + proAppId + ", proUid=" + proUid + ", proName=" + proName
                + ", creator=" + creator + ", createTime=" + createTime
                + ", updateUser=" + updateUser + ", lastUpdateTime="
                + lastUpdateTime + ", categoryUid=" + categoryUid
                + ", creatorFullName=" + creatorFullName + ", updatorFullName="
                + updatorFullName + "]";
    }

    
   
    
    
    
}
