package com.desmart.desmartbpm.entity;

import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程定义升级需要的信息
 */
public class DhUpdateData {
    private Map<String, String> oldNewFieldUidMap;
    private Map<String, String> oldNewStepUidMap;
    private Map<String, String> oldNewFormUidMap;
    private Map<String, String> oldNewActivityIdMap;

    private String type;
    private List<DhProcessCategory> categoryList;
    private List<DhProcessMeta> processMetaList;
    private List<DhProcessDefinition> processDefinitionList;
    private List<DhTrigger> triggerList;
    private List<DhObjectPermission> objectPermissionList;
    private List<BpmActivityMeta> bpmActivityMetaList;
    private List<DhActivityConf> activityConfList;

    private List<DhGatewayLine> gatewayLineList;
    private List<DatRule> ruleList;
    private List<DatRuleCondition> ruleConditionList;

    private List<BpmForm> formList;
    private List<BpmFormRelePublicForm> formRelePublicFormList;
    private List<BpmPublicForm> publicFormList;
    private List<BpmFormField> formFieldList;

    private List<DhStep> stepList;
    private List<DhActivityAssign> activityAssignList;
    private List<DhActivityReject> activityRejectList;
    private List<DhNotifyTemplate> notifyTemplateList;
    private List<DhTriggerInterface> triggerInterfaceList;
    private List<DhInterface> interfaceList;
    private List<DhInterfaceParameter> interfaceParameterList;


    public void addToCategoryList(DhProcessCategory category) {
        if (this.categoryList == null) {
            this.categoryList = new ArrayList<>();
        }
        this.categoryList.add(category);
    }

    public void addAllToCategoryList(List<DhProcessCategory> categoryList) {
        if (this.categoryList == null) {
            this.categoryList = new ArrayList<>();
        }
        this.categoryList.addAll(categoryList);
    }
    public void addAllToInterfaceParameterList(List<DhInterfaceParameter> interfaceParameterList) {
        if (this.interfaceParameterList == null) {
            this.interfaceParameterList = new ArrayList<>();
        }
        this.interfaceParameterList.addAll(interfaceParameterList);
    }

    public void addToProcessMetaList(DhProcessMeta processMeta) {
        if (this.processMetaList == null) {
            this.processMetaList = new ArrayList<>();
        }
        this.processMetaList.add(processMeta);
    }
    public void addToProcessDefinitionList(DhProcessDefinition processDefinition) {
        if (this.processDefinitionList == null) {
            this.processDefinitionList = new ArrayList<>();
        }
        this.processDefinitionList.add(processDefinition);
    }
    public void addToTriggerList(DhTrigger trigger) {
        if (this.triggerList == null) {
            this.triggerList = new ArrayList<>();
        }
        this.triggerList.add(trigger);
    }
    public void addToObjectPermissionList(DhObjectPermission objectPermission) {
        if (this.objectPermissionList == null) {
            this.objectPermissionList = new ArrayList<>();
        }
        this.objectPermissionList.add(objectPermission);
    }
    public void addAllToObjectPermissionList(List<DhObjectPermission> objectPermissionList) {
        if (this.objectPermissionList == null) {
            this.objectPermissionList = new ArrayList<>();
        }
        this.objectPermissionList.addAll(objectPermissionList);
    }

    public void addToBpmActivityMetaList(BpmActivityMeta bpmActivityMeta) {
        if (this.bpmActivityMetaList == null) {
            this.bpmActivityMetaList = new ArrayList<>();
        }
        this.bpmActivityMetaList.add(bpmActivityMeta);
    }
    public void addAllToBpmActivityMetaList(List<BpmActivityMeta> bpmActivityMetaList) {
        if (this.bpmActivityMetaList == null) {
            this.bpmActivityMetaList = new ArrayList<>();
        }
        this.bpmActivityMetaList.addAll(bpmActivityMetaList);
    }
    public void addToActivityConfList(DhActivityConf activityConf) {
        if (this.activityConfList == null) {
            this.activityConfList = new ArrayList<>();
        }
        this.activityConfList.add(activityConf);
    }

    public void addToGatewayLineList(DhGatewayLine gatewayLine) {
        if (this.gatewayLineList == null) {
            this.gatewayLineList = new ArrayList<>();
        }
        this.gatewayLineList.add(gatewayLine);
    }
    public void addAllToGatewayLineList(List<DhGatewayLine> gatewayLineList) {
        if (this.gatewayLineList == null) {
            this.gatewayLineList = new ArrayList<>();
        }
        this.gatewayLineList.addAll(gatewayLineList);
    }

    public void addToRuleList(DatRule rule) {
        if (this.ruleList == null) {
            this.ruleList = new ArrayList<>();
        }
        this.ruleList.add(rule);
    }
    public void addToRuleConditionList(DatRuleCondition ruleCondition) {
        if (this.ruleConditionList == null) {
            this.ruleConditionList = new ArrayList<>();
        }
        this.ruleConditionList.add(ruleCondition);
    }

    public void addToFormList(BpmForm form) {
        if (this.formList == null) {
            this.formList = new ArrayList<>();
        }
        this.formList.add(form);
    }
    public void addToFormRelePublicFormList(BpmFormRelePublicForm formRelePublicForm) {
        if (this.formRelePublicFormList == null) {
            this.formRelePublicFormList = new ArrayList<>();
        }
        this.formRelePublicFormList.add(formRelePublicForm);
    }
    public void addToPublicFormList(BpmPublicForm publicForm) {
        if (this.publicFormList == null) {
            this.publicFormList = new ArrayList<>();
        }
        this.publicFormList.add(publicForm);
    }
    public void addToFormFieldList(BpmFormField formField) {
        if (this.formFieldList == null) {
            this.formFieldList = new ArrayList<>();
        }
        this.formFieldList.add(formField);
    }

    public void addToStepList(DhStep step) {
        if (this.stepList == null) {
            this.stepList = new ArrayList<>();
        }
        this.stepList.add(step);
    }
    public void addToActivityAssignList(DhActivityAssign activityAssign) {
        if (this.activityAssignList == null) {
            this.activityAssignList = new ArrayList<>();
        }
        this.activityAssignList.add(activityAssign);
    }
    public void addToActivityRejectList(DhActivityReject activityReject) {
        if (this.activityRejectList == null) {
            this.activityRejectList = new ArrayList<>();
        }
        this.activityRejectList.add(activityReject);
    }
    public void addToNotifyTemplateList(DhNotifyTemplate notifyTemplate) {
        if (this.notifyTemplateList == null) {
            this.notifyTemplateList = new ArrayList<>();
        }
        this.notifyTemplateList.add(notifyTemplate);
    }
    public void addToTriggerInterfaceList(DhTriggerInterface triggerInterface) {
        if (this.triggerInterfaceList == null) {
            this.triggerInterfaceList = new ArrayList<>();
        }
        this.triggerInterfaceList.add(triggerInterface);
    }
    public void addToInterfaceList(DhInterface dhInterface) {
        if (this.interfaceList == null) {
            this.interfaceList = new ArrayList<>();
        }
        this.interfaceList.add(dhInterface);
    }
    public void addToInterfaceParameterList(DhInterfaceParameter interfaceParameter) {
        if (this.interfaceParameterList == null) {
            this.interfaceParameterList = new ArrayList<>();
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getOldNewFieldUidMap() {
        return oldNewFieldUidMap;
    }

    public void setOldNewFieldUidMap(Map<String, String> oldNewFieldUidMap) {
        this.oldNewFieldUidMap = oldNewFieldUidMap;
    }

    public Map<String, String> getOldNewStepUidMap() {
        return oldNewStepUidMap;
    }

    public void setOldNewStepUidMap(Map<String, String> oldNewStepUidMap) {
        this.oldNewStepUidMap = oldNewStepUidMap;
    }

    public Map<String, String> getOldNewFormUidMap() {
        return oldNewFormUidMap;
    }

    public void setOldNewFormUidMap(Map<String, String> oldNewFormUidMap) {
        this.oldNewFormUidMap = oldNewFormUidMap;
    }

    public Map<String, String> getOldNewActivityIdMap() {
        return oldNewActivityIdMap;
    }

    public void setOldNewActivityIdMap(Map<String, String> oldNewActivityIdMap) {
        this.oldNewActivityIdMap = oldNewActivityIdMap;
    }
}