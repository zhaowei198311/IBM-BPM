package com.desmart.desmartbpm.entity;

public class DatRuleCondition {
    public static final String RIGHT_VALUE_TYPE_STRING = "String";
    public static final String RIGHT_VALUE_TYPE_NUMBER = "Number";
    
	private String result;//不在表内
	
	private String conditionId;//条件id
	private String conditionGroupName;//分组
	private String conditionOperator;//条件组合运算符
	private String conditionOperatorName;//
	private String conditionType;//CONDITION_TYPE
	private String createTime;//CREATE_TIME
	private String creator;//创建人id CREATOR
	private String leftName;//字段名称  LEFT_NAME
	private String leftValue;//字段 LEFT_VALUE
	private String leftValueType;//字段类型  LEFT_VALUE_TYPE
	private String returnType;//返回类型  RETURN_TYPE
	private String rightValue;//条件判断值  RIGHT_VALUE
	private String rightValueType;//条件判断值类型  RIGHT_VALUE_TYPE
	private String ruleId;//规则id  RULE_ID
	private String ruleStatus;//规则状态  RULE_STATUS
	private Integer ruleVersion;//规则优先级   RULE_VERSION
	private Integer sortNum;//SORT_NUM 网关序号
	private String valueOperator;//值运算操作符   VALUE_OPERATOR
	//不在表内
	private String ruleName;//规则名称
	public String getConditionId() {
		return conditionId;
	}
	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}
	public String getConditionGroupName() {
		return conditionGroupName;
	}
	public void setConditionGroupName(String conditionGroupName) {
		this.conditionGroupName = conditionGroupName;
	}
	public String getConditionOperator() {
		return conditionOperator;
	}
	public void setConditionOperator(String conditionOperator) {
		this.conditionOperator = conditionOperator;
	}
	public String getConditionOperatorName() {
		return conditionOperatorName;
	}
	public void setConditionOperatorName(String conditionOperatorName) {
		this.conditionOperatorName = conditionOperatorName;
	}
	public String getConditionType() {
		return conditionType;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
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
	public String getLeftName() {
		return leftName;
	}
	public void setLeftName(String leftName) {
		this.leftName = leftName;
	}
	public String getLeftValue() {
		return leftValue;
	}
	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}
	public String getLeftValueType() {
		return leftValueType;
	}
	public void setLeftValueType(String leftValueType) {
		this.leftValueType = leftValueType;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getRightValue() {
		return rightValue;
	}
	public void setRightValue(String rightValue) {
		this.rightValue = rightValue;
	}
	public String getRightValueType() {
		return rightValueType;
	}
	public void setRightValueType(String rightValueType) {
		this.rightValueType = rightValueType;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleStatus() {
		return ruleStatus;
	}
	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}
	public Integer getRuleVersion() {
		return ruleVersion;
	}
	public void setRuleVersion(Integer ruleVersion) {
		this.ruleVersion = ruleVersion;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	public String getValueOperator() {
		return valueOperator;
	}
	public void setValueOperator(String valueOperator) {
		this.valueOperator = valueOperator;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	@Override
	public String toString() {
		return "DatRuleCondition [result=" + result + ", conditionId=" + conditionId + ", conditionGroupName="
				+ conditionGroupName + ", conditionOperator=" + conditionOperator + ", conditionOperatorName="
				+ conditionOperatorName + ", conditionType=" + conditionType + ", createTime=" + createTime
				+ ", creator=" + creator + ", leftName=" + leftName + ", leftValue=" + leftValue + ", leftValueType="
				+ leftValueType + ", returnType=" + returnType + ", rightValue=" + rightValue + ", rightValueType="
				+ rightValueType + ", ruleId=" + ruleId + ", ruleStatus=" + ruleStatus + ", ruleVersion=" + ruleVersion
				+ ", sortNum=" + sortNum + ", valueOperator=" + valueOperator + ", ruleName=" + ruleName + "]";
	}
	
}
