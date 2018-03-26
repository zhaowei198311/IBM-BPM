package com.desmart.desmartbpm.entity;

import java.sql.Timestamp;
import java.util.Date;

public class DatRule {
    
    private String ruleId;
    
    private String ruleName;
    
    private String parentRuleId;
    
    private Date startTime;
   
    private Date endTime;
    
    private String ruleType;
    
    private String ruleProcess;
    
    private String ruleStatus;
    
    private String returnType;
    
    private int ruleVersion;
    
    private Timestamp createTime;
    
    private String creator;
    
    private String isActivate;
    
    private String editMode;
    
    private String bizType;

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getParentRuleId() {
		return parentRuleId;
	}

	public void setParentRuleId(String parentRuleId) {
		this.parentRuleId = parentRuleId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleProcess() {
		return ruleProcess;
	}

	public void setRuleProcess(String ruleProcess) {
		this.ruleProcess = ruleProcess;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public int getRuleVersion() {
		return ruleVersion;
	}

	public void setRuleVersion(int ruleVersion) {
		this.ruleVersion = ruleVersion;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getIsActivate() {
		return isActivate;
	}

	public void setIsActivate(String isActivate) {
		this.isActivate = isActivate;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
    
    
}
