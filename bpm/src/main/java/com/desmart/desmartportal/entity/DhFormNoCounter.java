package com.desmart.desmartportal.entity;

import java.util.Date;

/**
 * 描述表单号规则对应的表单号状态
 */
public class DhFormNoCounter {
    private String formNoExpression;

    private Integer currentNo;

    private String isDaily;

    private Date createTime;

    private Date updateTime;

    public String getFormNoExpression() {
        return formNoExpression;
    }

    public void setFormNoExpression(String formNoExpression) {
        this.formNoExpression = formNoExpression == null ? null : formNoExpression.trim();
    }

    public Integer getCurrentNo() {
        return currentNo;
    }

    public void setCurrentNo(Integer currentNo) {
        this.currentNo = currentNo;
    }

    public String getIsDaily() {
        return isDaily;
    }

    public void setIsDaily(String isDaily) {
        this.isDaily = isDaily == null ? null : isDaily.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}