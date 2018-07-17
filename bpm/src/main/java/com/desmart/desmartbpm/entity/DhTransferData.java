package com.desmart.desmartbpm.entity;

import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护迁移一个流程定义需要的数据
 */
public class DhTransferData {
    public static final String ATTRIBUTE_IN_SESSION = "processDefinitionTransferData";

    private List<DhProcessCategory> categoryList = new ArrayList<>();
    private List<DhProcessMeta> processMetaList = new ArrayList<>();
    private List<DhProcessDefinition> processDefinitionList = new ArrayList<>();
    private List<DhTrigger> triggerList = new ArrayList<>();
    private List<DhObjectPermission> objectPermissionList = new ArrayList<>();
    private List<BpmActivityMeta> bpmActivityMetaList = new ArrayList<>();
    private List<DhActivityConf> activityConfList = new ArrayList<>();

    private List<DhGatewayLine> gatewayLineList = new ArrayList<>();
    private List<DatRule> ruleList = new ArrayList<>();
    private List<DatRuleCondition> ruleConditionList = new ArrayList<>();

    private List<BpmForm> formList = new ArrayList<>();
    private List<BpmFormRelePublicForm> formRelePublicFormList = new ArrayList<>();
    private List<BpmPublicForm> publicFormList = new ArrayList<>();
    private List<BpmFormField> formFieldList = new ArrayList<>();

    private List<DhStep> stepList = new ArrayList<>();
    private List<DhActivityAssign> activityAssignList = new ArrayList<>();
    private List<DhActivityReject> activityRejectList = new ArrayList<>();
    private List<DhNotifyTemplate> notifyTemplateList = new ArrayList<>();
    private List<DhTriggerInterface> triggerInterfaceList = new ArrayList<>();
    private List<DhInterface> interfaceList = new ArrayList<>();
    private List<DhInterfaceParameter> interfaceParameterList = new ArrayList<>();


    public void addToCategoryList(DhProcessCategory category) {
        this.categoryList.add(category);
    }

    public void addAllToCategoryList(List<DhProcessCategory> categoryList) {
        this.categoryList.addAll(categoryList);
    }

    public void addToProcessMetaList(DhProcessMeta processMeta) {
        this.processMetaList.add(processMeta);
    }
    public void addToProcessDefinitionList(DhProcessDefinition processDefinition) {
        this.processDefinitionList.add(processDefinition);
    }
    public void addToTriggerList(DhTrigger trigger) {
        this.triggerList.add(trigger);
    }
    public void addToObjectPermissionList(DhObjectPermission objectPermission) {
        this.objectPermissionList.add(objectPermission);
    }
    public void addAllToObjectPermissionList(List<DhObjectPermission> objectPermissionList) {
        this.objectPermissionList.addAll(objectPermissionList);
    }

    public void addToBpmActivityMetaList(BpmActivityMeta bpmActivityMeta) {
        this.bpmActivityMetaList.add(bpmActivityMeta);
    }
    public void addAllToBpmActivityMetaList(List<BpmActivityMeta> bpmActivityMetaList) {
        this.bpmActivityMetaList.addAll(bpmActivityMetaList);
    }
    public void addToActivityConfList(DhActivityConf activityConf) {
        this.activityConfList.add(activityConf);
    }

    public void addToGatewayLineList(DhGatewayLine gatewayLine) {
        this.gatewayLineList.add(gatewayLine);
    }
    public void addAllToGatewayLineList(List<DhGatewayLine> gatewayLineList) {
        this.gatewayLineList.addAll(gatewayLineList);
    }

    public void addToRuleList(DatRule rule) {
        this.ruleList.add(rule);
    }
    public void addToRuleConditionList(DatRuleCondition ruleCondition) {
        this.ruleConditionList.add(ruleCondition);
    }

    public void addToFormList(BpmForm form) {
        this.formList.add(form);
    }
    public void addToFormRelePublicFormList(BpmFormRelePublicForm formRelePublicForm) {
        this.formRelePublicFormList.add(formRelePublicForm);
    }
    public void addToPublicFormList(BpmPublicForm publicForm) {
        this.publicFormList.add(publicForm);
    }
    public void addToFormFieldList(BpmFormField formField) {
        this.formFieldList.add(formField);
    }

    public void addToStepList(DhStep step) {
        this.stepList.add(step);
    }
    public void addToActivityAssignList(DhActivityAssign activityAssign) {
        this.activityAssignList.add(activityAssign);
    }
    public void addToActivityRejectList(DhActivityReject activityReject) {
        this.activityRejectList.add(activityReject);
    }
    public void addToNotifyTemplateList(DhNotifyTemplate notifyTemplate) {
        this.notifyTemplateList.add(notifyTemplate);
    }
    public void addToTriggerInterfaceList(DhTriggerInterface triggerInterface) {
        this.triggerInterfaceList.add(triggerInterface);
    }
    public void addToInterfaceList(DhInterface dhInterface) {
        this.interfaceList.add(dhInterface);
    }
    public void addToInterfaceParameterList(DhInterfaceParameter interfaceParameter) {
        this.interfaceParameterList.add(interfaceParameter);
    }


    public List<DhProcessCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<DhProcessCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public List<DhProcessMeta> getProcessMetaList() {
        return processMetaList;
    }

    public void setProcessMetaList(List<DhProcessMeta> processMetaList) {
        this.processMetaList = processMetaList;
    }

    public List<DhProcessDefinition> getProcessDefinitionList() {
        return processDefinitionList;
    }

    public void setProcessDefinitionList(List<DhProcessDefinition> processDefinitionList) {
        this.processDefinitionList = processDefinitionList;
    }

    public List<DhTrigger> getTriggerList() {
        return triggerList;
    }

    public void setTriggerList(List<DhTrigger> triggerList) {
        this.triggerList = triggerList;
    }

    public List<DhObjectPermission> getObjectPermissionList() {
        return objectPermissionList;
    }

    public void setObjectPermissionList(List<DhObjectPermission> objectPermissionList) {
        this.objectPermissionList = objectPermissionList;
    }

    public List<BpmActivityMeta> getBpmActivityMetaList() {
        return bpmActivityMetaList;
    }

    public void setBpmActivityMetaList(List<BpmActivityMeta> bpmActivityMetaList) {
        this.bpmActivityMetaList = bpmActivityMetaList;
    }

    public List<DhActivityConf> getActivityConfList() {
        return activityConfList;
    }

    public void setActivityConfList(List<DhActivityConf> activityConfList) {
        this.activityConfList = activityConfList;
    }

    public List<DhGatewayLine> getGatewayLineList() {
        return gatewayLineList;
    }

    public void setGatewayLineList(List<DhGatewayLine> gatewayLineList) {
        this.gatewayLineList = gatewayLineList;
    }

    public List<DatRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<DatRule> ruleList) {
        this.ruleList = ruleList;
    }

    public List<DatRuleCondition> getRuleConditionList() {
        return ruleConditionList;
    }

    public void setRuleConditionList(List<DatRuleCondition> ruleConditionList) {
        this.ruleConditionList = ruleConditionList;
    }

    public List<BpmForm> getFormList() {
        return formList;
    }

    public void setFormList(List<BpmForm> formList) {
        this.formList = formList;
    }

    public List<BpmFormRelePublicForm> getFormRelePublicFormList() {
        return formRelePublicFormList;
    }

    public void setFormRelePublicFormList(List<BpmFormRelePublicForm> formRelePublicFormList) {
        this.formRelePublicFormList = formRelePublicFormList;
    }

    public List<BpmPublicForm> getPublicFormList() {
        return publicFormList;
    }

    public void setPublicFormList(List<BpmPublicForm> publicFormList) {
        this.publicFormList = publicFormList;
    }

    public List<BpmFormField> getFormFieldList() {
        return formFieldList;
    }

    public void setFormFieldList(List<BpmFormField> formFieldList) {
        this.formFieldList = formFieldList;
    }

    public List<DhStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<DhStep> stepList) {
        this.stepList = stepList;
    }

    public List<DhActivityAssign> getActivityAssignList() {
        return activityAssignList;
    }

    public void setActivityAssignList(List<DhActivityAssign> activityAssignList) {
        this.activityAssignList = activityAssignList;
    }

    public List<DhActivityReject> getActivityRejectList() {
        return activityRejectList;
    }

    public void setActivityRejectList(List<DhActivityReject> activityRejectList) {
        this.activityRejectList = activityRejectList;
    }

    public List<DhNotifyTemplate> getNotifyTemplateList() {
        return notifyTemplateList;
    }

    public void setNotifyTemplateList(List<DhNotifyTemplate> notifyTemplateList) {
        this.notifyTemplateList = notifyTemplateList;
    }

    public List<DhTriggerInterface> getTriggerInterfaceList() {
        return triggerInterfaceList;
    }

    public void setTriggerInterfaceList(List<DhTriggerInterface> triggerInterfaceList) {
        this.triggerInterfaceList = triggerInterfaceList;
    }

    public List<DhInterface> getInterfaceList() {
        return interfaceList;
    }

    public void setInterfaceList(List<DhInterface> interfaceList) {
        this.interfaceList = interfaceList;
    }

    public List<DhInterfaceParameter> getInterfaceParameterList() {
        return interfaceParameterList;
    }

    public void setInterfaceParameterList(List<DhInterfaceParameter> interfaceParameterList) {
        this.interfaceParameterList = interfaceParameterList;
    }



}