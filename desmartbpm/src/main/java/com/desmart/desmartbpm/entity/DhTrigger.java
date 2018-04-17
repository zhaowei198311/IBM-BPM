package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 触发器
 */
public class DhTrigger {
    private String triUid;
    private String triTitle;  // 触发器标题
    private String triDescription; // 触发器执行命令：Java class名；script 内容；SQL内容；接口名称
    private String triType; // 触发器类型：script；javaclass；sql；interface
    private String triWebbot; // 触发器执行命令：java class名；script 内容；sql内容；接口名称
    private String triParam; // 触发器参数
    private String creator;
    private Date createTime;
    private String updator;
    private Date updateTime;


    public String getTriUid() {
        return triUid;
    }

    public void setTriUid(String triUid) {
        this.triUid = triUid;
    }

    public String getTriTitle() {
        return triTitle;
    }

    public void setTriTitle(String triTitle) {
        this.triTitle = triTitle;
    }

    public String getTriDescription() {
        return triDescription;
    }

    public void setTriDescription(String triDescription) {
        this.triDescription = triDescription;
    }

    public String getTriType() {
        return triType;
    }

    public void setTriType(String triType) {
        this.triType = triType;
    }

    public String getTriWebbot() {
        return triWebbot;
    }

    public void setTriWebbot(String triWebbot) {
        this.triWebbot = triWebbot;
    }

    public String getTriParam() {
        return triParam;
    }

    public void setTriParam(String triParam) {
        this.triParam = triParam;
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

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DhTrigger{" +
                "triUid='" + triUid + '\'' +
                ", triTitle='" + triTitle + '\'' +
                ", triDescription='" + triDescription + '\'' +
                ", triType='" + triType + '\'' +
                ", triWebbot='" + triWebbot + '\'' +
                ", triParam='" + triParam + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updator='" + updator + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}