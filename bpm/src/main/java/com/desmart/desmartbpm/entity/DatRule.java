package com.desmart.desmartbpm.entity;


public class DatRule {
    
    private String ruleId; // 规则id
    
    private String ruleName; // 规则名 eg: gateway_rule_bpm_actymeta:5494a548-d5b9-44eb-b017-4372ace67adc_result_0
    
    private String parentRuleId;  // 父规则id
    
    private String startTime; // 开始时间
   
    private String endTime;  // 结束时间
    
    private String ruleType; // 规则类型   "PARAMS"
    
    private String ruleProcess;  // 规则内容 Map ( (this['amount'] > 444   &&  this['amount'] >= 10000 ) || (this['amount'] == '12'    ||  this['amount'] > 1 )  )
    
    private String ruleStatus; // 规则状态
    
    private String returnType; // 返回类型 text
    
    private int ruleVersion;   // 规则版本
    
    private String createTime; // 创建时间
    
    private String creator;  // 创建人
     
    private String isActivate; // 是否启用
    
    private String editMode; // "STD"
    
    private String bizType;  // "wfCondCtrl"  "advAuditAuth"  高级授权

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
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
