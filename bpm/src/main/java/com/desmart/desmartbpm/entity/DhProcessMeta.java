package com.desmart.desmartbpm.entity;

import java.util.Date;
import java.util.Objects;

/**
 * 流程定义元数据
 *
 */
public class DhProcessMeta {
    /** 正常状态 */
    public static final String STATUS_ON = "on";
    /** 隐藏状态，菜单界面不提供发起 */
    public static final String STATUS_HIDE = "hide";
    /** 关闭状态，切换为关闭菜单时，删除所有的流程实例，任务， 已办等信息  */
    public static final String STATUS_CLOSED = "closed";
    
    private String proMetaUid;  // 主键
    private String proAppId;  // 应用库id
    private String proUid; // 流程图id
    private String proName; // 流程名
    private String categoryUid;  // 流程分类
    private String proDisplay; // 流程引擎中流程名
    private String proMetaStatus; // 元数据状态
    private String proNo;  // 流程编号

    private String creator;
    private Date createTime;
    private String updateUser;
    private Date lastUpdateTime;
    
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

    public String getProNo() {
        return proNo;
    }

    public void setProNo(String proNo) {
        this.proNo = proNo;
    }

    public String getProMetaStatus() {
        return proMetaStatus;
    }
    public void setProMetaStatus(String proMetaStatus) {
        this.proMetaStatus = proMetaStatus;
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
                + ", proDisplay=" + proDisplay + ", proMetaStatus="
                + proMetaStatus + ", creatorFullName=" + creatorFullName
                + ", updatorFullName=" + updatorFullName + "]";
    }


    /**
     * 比较规则， proAppId与proUid都相等，则视为相等
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DhProcessMeta that = (DhProcessMeta) o;
        return Objects.equals(proAppId, that.proAppId) &&
                Objects.equals(proUid, that.proUid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(proAppId, proUid);
    }



}
