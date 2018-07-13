package com.desmart.desmartbpm.service.impl;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.BpmFormRelePublicFormMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.dao.DhActivityRejectMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.enginedao.LswSnapshotMapper;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.enums.DhTriggerType;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.datatransfer.DataTransferer;

import javax.xml.transform.TransformerFactory;
import java.util.*;

@Service
public class DhTransferServiceImpl implements DhTransferService {
    private static final Logger LOG = LoggerFactory.getLogger(DhTransferServiceImpl.class);

    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private DhProcessMetaMapper dhProcessMetaMapper;
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    @Autowired
    private DhObjectPermissionService dhObjectPermissionService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private DhGatewayLineService dhGatewayLineService;
    @Autowired
    private DatRuleService datRuleService;
    @Autowired
    private DatRuleConditionService datRuleConditionService;
    @Autowired
    private BpmFormManageService bpmFormManageService;
    @Autowired
    private BpmFormFieldService bpmFormFieldService;
    @Autowired
    private BpmFormRelePublicFormMapper bpmFormRelePublicFormMapper;
    @Autowired
    private BpmPublicFormService bpmPublicFormService;
    @Autowired
    private DhStepService dhStepService;
    @Autowired
    private DhActivityAssignService dhActivityAssignService;
    @Autowired
    private DhActivityRejectMapper dhActivityRejectMapper;
    @Autowired
    private DhTriggerService dhTriggerService;
    @Autowired
    private DhInterfaceService dhInterfaceService;
    @Autowired
    private DhInterfaceParameterService dhInterfaceParameterService;
    @Autowired
    private DhTriggerInterfaceService dhTriggerInterfaceService;
    @Autowired
    private DhNotifyTemplateService dhNotifyTemplateService;
    @Autowired
    private LswSnapshotMapper lswSnapshotMapper;


    public ServerResponse<DhTransferData> exportData(String proAppId, String proUid, String proVerUid) {
        // 基础校验
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        // 获得流程定义基本数据
        DhProcessDefinition dhProcessDefinition = dhProcessDefinitionService.getDhProcessDefinition(proAppId, proUid, proVerUid);
        if (dhProcessDefinition == null) {
            return ServerResponse.createByErrorMessage("找不到此流程定义");
        }
        DhTransferData transferData = new DhTransferData();
        transferData.addToProcessDefinitionList(dhProcessDefinition);

        // 获得流程元数据信息
        DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProAppIdAndProUid(proAppId, proUid);
        if (dhProcessMeta == null) {
            return ServerResponse.createByErrorMessage("流程元数据不存在");
        }
        transferData.addToProcessMetaList(dhProcessMeta);

        // 获得分类信息
        List<DhProcessCategory> categoryList = dhProcessCategoryService.getCategoryAndAllParentCategory(dhProcessMeta.getCategoryUid());
        if (categoryList.isEmpty() && !dhProcessMeta.getCategoryUid().equals("rootCategory")) {
            return ServerResponse.createByErrorMessage("流程分类信息异常");
        }
        transferData.addAllToCategoryList(categoryList);

        // 记录需要引入的触发器的主键
        Set<String> triggerIdSetToInclude = new HashSet<>();
        triggerIdSetToInclude.addAll(getTriggerIdListOnDhProcessDefinition(transferData.getFirstProcessDefinition()));

        // 获得此流程定义的发起权限
        List<DhObjectPermission> permissionListOfStartProcess = dhObjectPermissionService.getPermissionListOfStartProcess(proAppId, proUid, proVerUid);
        transferData.addAllToObjectPermissionList(permissionListOfStartProcess);

        // 查找对应的表单
        List<BpmForm> bpmForms = bpmFormManageService.listAllFormsOfProcessDefinition(proUid, proVerUid);
        List<String> formUidList = this.getIdentityListOfObjectList(bpmForms);
        transferData.setFormList(bpmForms);
        // 根据formUid集合获得 公共子表单 BPM_FORM_RELE_PUBLIC_FORM
        List<String> pubFormUidList = bpmFormRelePublicFormMapper.listPublicFormUidByFormUidList(formUidList);
        List<BpmPublicForm> bpmPublicForms = bpmPublicFormService.listByPublicFormUidList(pubFormUidList);
        transferData.setPublicFormList(bpmPublicForms);
        // 获得表单字段集合(包括普通表单的字段和公共表单的字段)
        formUidList.addAll(pubFormUidList);
        List<BpmFormField> bpmFormFields = bpmFormFieldService.listByFormUidList(formUidList);
        transferData.setFormFieldList(bpmFormFields);


        // 获得所有环节
        List<BpmActivityMeta> activityMetaList = bpmActivityMetaService.listAllBpmActivityMeta(proAppId, proUid, proVerUid);
        if (activityMetaList.isEmpty()) {
            return ServerResponse.createByErrorMessage("流程环节信息异常");
        }
        transferData.addAllToBpmActivityMetaList(activityMetaList);
        List<String> sourceUserActivityIdList = this.getSourceUserActivityIdList(activityMetaList); // 所有环节的activity_id集合
        List<String> sourceGatewayActiivtyIdList = this.getSourceGatewayActivityIdList(activityMetaList);

        // 获得所有的网关连接线
        List<DhGatewayLine> dhGatewayLines = dhGatewayLineService.listByGatewayActivityIdList(sourceGatewayActiivtyIdList);
        transferData.addAllToGatewayLineList(dhGatewayLines);
        // 获得所有规则
        List<String> ruleIdList = getRuleIdListByDhGatewayLines(dhGatewayLines);
        List<DatRule> ruleList = datRuleService.listByRuleIdList(ruleIdList);
        transferData.setRuleList(ruleList);
        // 获得规则对应的条件
        List<DatRuleCondition> ruleConditionList = datRuleConditionService.listByRuleIdList(ruleIdList);
        transferData.setRuleConditionList(ruleConditionList);

        // 获得超时任务默认处理人、可选处理人、超时通知人信息
        List<DhActivityAssign> activityAssignList = dhActivityAssignService.listByActivityIdList(sourceUserActivityIdList);
        transferData.setActivityAssignList(activityAssignList);

        // 获得可回退环节信息
        List<DhActivityReject> activityRejectList = this.getAllActivityRejectByActiivtyIdList(sourceUserActivityIdList);
        transferData.setActivityRejectList(activityRejectList);

        // 获得流程环节的配置
        List<DhActivityConf> activityConfList = this.getActivityConfListByActivityMetaList(activityMetaList);
        transferData.setActivityConfList(activityConfList);

        // 记录需要的所有模版id
        Set<String> notifyTemplateIdSet = new HashSet<>();
        // 获得环节配置有关的模版id
        List<String> notifyTemplateIdListFromActivityConfList = this.getNotifyTemplateIdListFromActivityConfList(activityConfList);
        notifyTemplateIdSet.addAll(notifyTemplateIdListFromActivityConfList);
        // 获得环节配置上的触发器id
        List<String> triggerIdListByActivityConfList = this.getTriggerIdListByActivityConfList(activityConfList);
        triggerIdSetToInclude.addAll(triggerIdListByActivityConfList);

        // 获得所有的步骤
        List<DhStep> dhStepList = dhStepService.listAllStepsOfProcessDefinition(proAppId, proUid, proVerUid);
        transferData.setStepList(dhStepList);
        List<String> stepUidList = this.getIdentityListOfObjectList(dhStepList);
        // 获得Step相关的 权限信息， 字段，区块可见性
        List<DhObjectPermission> permissionListOfField = dhObjectPermissionService.listByStepUidList(stepUidList);
        transferData.addAllToObjectPermissionList(permissionListOfField);

        // 汇总处理trigger
        this.assembleTriggerAndInterface(triggerIdSetToInclude, transferData);
        // 汇总处理template
        this.assembleNotifyTemplateList(notifyTemplateIdSet, transferData);
        return ServerResponse.createBySuccess(transferData);
    }



    /**
     * 获得所有在流程定义表上绑定的触发器id（去重）
     * @param dhProcessDefinition  流程定义
     * @return
     */
    private List<String> getTriggerIdListOnDhProcessDefinition(DhProcessDefinition dhProcessDefinition) {
        List<String> triggerIdList = new ArrayList<>();
        // 取消触发器
        if (StringUtils.isNotBlank(dhProcessDefinition.getProTriCanceled())
                && !triggerIdList.contains(dhProcessDefinition.getProTriCanceled())) {
            triggerIdList.add(dhProcessDefinition.getProTriCanceled());
        }
        // 删除触发器
        if (StringUtils.isNotBlank(dhProcessDefinition.getProTriDeleted())
                && !triggerIdList.contains(dhProcessDefinition.getProTriDeleted())) {
            triggerIdList.add(dhProcessDefinition.getProTriDeleted());
        }
        // 暂停触发器
        if (StringUtils.isNotBlank(dhProcessDefinition.getProTriPaused())
                && !triggerIdList.contains(dhProcessDefinition.getProTriPaused())) {
            triggerIdList.add(dhProcessDefinition.getProTriPaused());
        }
        // 重新分配触发器
        if (StringUtils.isNotBlank(dhProcessDefinition.getProTriReassigned())
                && !triggerIdList.contains(dhProcessDefinition.getProTriReassigned())) {
            triggerIdList.add(dhProcessDefinition.getProTriReassigned());
        }
        // 恢复触发器
        if (StringUtils.isNotBlank(dhProcessDefinition.getProTriUnpaused())
                && !triggerIdList.contains(dhProcessDefinition.getProTriUnpaused())) {
            triggerIdList.add(dhProcessDefinition.getProTriUnpaused());
        }
        // 发起触发器
        if (StringUtils.isNotBlank(dhProcessDefinition.getProTriStart())
                && !triggerIdList.contains(dhProcessDefinition.getProTriStart())) {
            triggerIdList.add(dhProcessDefinition.getProTriStart());
        }
        return triggerIdList;
    }

    /**
     * 获得环节配置中的绑定触发器id集合（去重）
     * @param confList
     * @return
     */
    private List<String> getTriggerIdListByActivityConfList(List<DhActivityConf> confList) {
        List<String> result = new ArrayList<>();
        if (confList == null || confList.isEmpty()) {
            return result;
        }
        for (DhActivityConf conf : confList) {
            if (StringUtils.isNotBlank(conf.getActcOuttimeTrigger())
                    && !result.contains(conf.getActcOuttimeTrigger())) {
                result.add(conf.getActcOuttimeTrigger());
            }
        }
        return result;
    }



    /**
     * 根据网关连接线集合获得关联的规则id集合
     * @param dhGatewayLines
     * @return
     */
    private List<String> getRuleIdListByDhGatewayLines(List<DhGatewayLine> dhGatewayLines) {
        List<String> ruleIdList = new ArrayList<>();
        for (DhGatewayLine line : dhGatewayLines) {
            String ruleId = line.getRuleId();
            if (StringUtils.isNotBlank(ruleId) && !ruleIdList.contains(ruleId)) {
                ruleIdList.add(ruleId);
            }
        }
        return ruleIdList;
    }

    /**
     * 在环节列表中找出非外链节点且是人员任务节点的环节，
     * 返回它们的activity_id集合
     * @param bpmActivityMetaList
     * @return
     */
    private List<String> getSourceUserActivityIdList(List<BpmActivityMeta> bpmActivityMetaList) {
        List<String> result = new ArrayList<>();
        if (bpmActivityMetaList == null || bpmActivityMetaList.isEmpty()) {
            return result;
        }
        for (BpmActivityMeta activityMeta : bpmActivityMetaList) {
            if (activityMeta.getActivityId().equals(activityMeta.getSourceActivityId())
                    && BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(activityMeta.getBpmTaskType())) {
                result.add(activityMeta.getActivityId());
            }
        }
        return result;
    }

    /**
     * 在环节列表中找出非外链节点且是排他网关的环节，
     * 返回它们的activity_id集合
     * @param bpmActivityMetaList
     * @return
     */
    private List<String> getSourceGatewayActivityIdList(List<BpmActivityMeta> bpmActivityMetaList) {
        List<String> result = new ArrayList<>();
        if (bpmActivityMetaList == null || bpmActivityMetaList.isEmpty()) {
            return result;
        }
        for (BpmActivityMeta activityMeta : bpmActivityMetaList) {
            if (activityMeta.getActivityId().equals(activityMeta.getSourceActivityId())
                    && BpmActivityMeta.ACTIVITY_TYPE_GATEWAY.equals(activityMeta.getActivityType())) {
                result.add(activityMeta.getActivityId());
            }
        }
        return result;
    }

    /**
     * 获得对象集合的标识符id 集合
     * @param objList  对象集合
     * @param <T>
     * @return
     */
    private <T> List<String>  getIdentityListOfObjectList(List<T> objList) {
        List<String> result = new ArrayList<>();
        if (objList == null || objList.isEmpty()) {
            return result;
        }
        for (T t : objList) {
            String identity = null;
            if (t instanceof BpmForm) {
                identity = ((BpmForm) t).getDynUid();
            } else if (t instanceof DhStep) {
                identity = ((DhStep) t).getStepUid();
            } else if (t instanceof BpmActivityMeta) {
                identity = ((BpmActivityMeta)t).getActivityId();
            } else if (t instanceof DhTrigger) {
                identity = ((DhTrigger)t).getTriUid();
            }
            if (StringUtils.isNotBlank(identity) && !result.contains(identity)) {
                result.add(identity);
            }
        }
        return result;
    }


    /**
     * 根据环节id集合，获得这些环节的可供回退的信息
     * @param activityIdList 环节id集合
     * @return
     */
    private List<DhActivityReject> getAllActivityRejectByActiivtyIdList(List<String> activityIdList) {
        if (activityIdList == null || activityIdList.isEmpty()) {
            return new ArrayList<>();
        }
        return dhActivityRejectMapper.listByActivityIdList(activityIdList);
    }


    /**
     * 从环节配置中获得需要的模版id集合
     * @return
     */
    private List<String> getNotifyTemplateIdListFromActivityConfList(List<DhActivityConf> activityConfList) {
        List<String> result = new ArrayList<>();
        if (activityConfList == null || activityConfList.isEmpty()) {
            return result;
        }
        for (DhActivityConf conf : activityConfList) {
            if (StringUtils.isNotBlank(conf.getActcMailNotifyTemplate())) {
                result.add(conf.getActcMailNotifyTemplate());
            }
            if (StringUtils.isNotBlank(conf.getActcMessageNotifyTemplate())) {
                result.add(conf.getActcMessageNotifyTemplate());
            }
            if (StringUtils.isNotBlank(conf.getActcOuttimeTemplate())) {
                result.add(conf.getActcOuttimeTemplate());
            }
        }
        return result;
    }



    /**
     * 根据环节列表获得配置列表
     * @param activityMetaList
     * @return
     */
    private List<DhActivityConf> getActivityConfListByActivityMetaList(List<BpmActivityMeta> activityMetaList) {
        List<DhActivityConf> confList = new ArrayList<>();
        if (activityMetaList == null || activityMetaList.isEmpty()) {
            return confList;
        }
        for (BpmActivityMeta bpmActivityMeta : activityMetaList) {
            if (bpmActivityMeta.getDhActivityConf() != null) {
                confList.add(bpmActivityMeta.getDhActivityConf());
            }
        }
        return confList;
    }

    /**
     * 根据触发器id列表，装配需要的触发器、接口、接口参数、参数映射信息
     * trigger
     * interface
     * dh_interface_parameter
     * triggerInterface
     * @param triggerUidSet
     * @param transferData  封装导出信息的类
     * @return
     */
    private void assembleTriggerAndInterface(Set<String> triggerUidSet, DhTransferData transferData) {
        List<DhTrigger> dhTriggerList = dhTriggerService.listByTriggerUidList(new ArrayList<>(triggerUidSet));
        if (dhTriggerList.isEmpty()) {
            return;
        }
        // 1. 装配触发器
        transferData.setTriggerList(dhTriggerList);
        // 检查是否包含有接口相关的触发器
        List<DhTrigger> interfaceTypeTriggers = getTriggerTypeIsInterface(dhTriggerList);
        if (interfaceTypeTriggers.isEmpty()) {
            return;
        }
        // 如果有接口相关的触发器，遍历步骤，通过步骤来装配具体的触发器
        List<String> interfaceTriggerIdList = this.getIdentityListOfObjectList(interfaceTypeTriggers);
        // 从接口类型的触发器中得到所有被需要的接口
        List<String> intUidList = this.getInterfaceIdListByInterfaceTypeTriggers(interfaceTypeTriggers);
        // 2. 装配接口
        List<DhInterface> interfaceList = dhInterfaceService.listDhInterfaceByIntUidList(intUidList);
        transferData.setInterfaceList(interfaceList);
        // 3. 装配接口参数
        List<DhInterfaceParameter> interfaceParameterList = dhInterfaceParameterService.listByIntUidList(intUidList);
        transferData.setInterfaceParameterList(interfaceParameterList);
        // 4. 装配trigger_interface 接口与表单的映射
        List<String> interfaceStepUidList = new ArrayList<>();
        for (DhStep dhStep : transferData.getStepList()) {
            if (interfaceTriggerIdList.contains(dhStep.getStepObjectUid())) {
                // 如果这个步骤对应的触发器是接口触发器， 装配表单参数和接口参数的映射规则
                interfaceStepUidList.add(dhStep.getStepUid());
            }
        }
        List<DhTriggerInterface> triggerInterfaceList = dhTriggerInterfaceService.listByStepUidList(interfaceStepUidList);
        transferData.setTriggerInterfaceList(triggerInterfaceList);
    }

    /**
     * 从触发器列表中筛选出触发器类型是接口的触发器
     * @param triggerList
     * @return
     */
    private List<DhTrigger> getTriggerTypeIsInterface(List<DhTrigger> triggerList) {
        List<DhTrigger> result = new ArrayList<>();
        for (DhTrigger trigger : triggerList) {
            if (DhTriggerType.INTERFACE.getCode().equals(trigger.getTriType())) {
                result.add(trigger);
            }
        }
        return result;
    }

    /**
     * 从接口类型的触发器集合中得到需要的触发器，并去重
     * @param interfaceTypeTriggers 类型是接口的触发器
     * @return
     */
    private List<String> getInterfaceIdListByInterfaceTypeTriggers(List<DhTrigger> interfaceTypeTriggers) {
        List<String> result = new ArrayList<>();
        if (interfaceTypeTriggers == null || interfaceTypeTriggers.isEmpty()) {
            return result;
        }
        for (DhTrigger trigger : interfaceTypeTriggers) {
            if (StringUtils.isNotBlank(trigger.getTriWebbot())
                    && DhTriggerType.INTERFACE.getCode().equals(trigger.getTriType())
                    && !result.contains(trigger.getTriWebbot())) {
                result.add(trigger.getTriWebbot());
            }
        }
        return result;
    }

    /**
     * 根据模版id集合获得需要的模版
     * @param notifyTemplateIdSet  模版id集合
     * @param transferData 封装导出数据的集合
     */
    private void assembleNotifyTemplateList(Set<String> notifyTemplateIdSet, DhTransferData transferData) {
        if (notifyTemplateIdSet == null || notifyTemplateIdSet.isEmpty()) {
            return;
        }
        List<DhNotifyTemplate> notifyTemplateList = dhNotifyTemplateService.listByTemplateUidList(new ArrayList<>(notifyTemplateIdSet));
        transferData.setNotifyTemplateList(notifyTemplateList);
    }

    public String getExportFileName(DhProcessDefinition dhProcessDefinition) {
        LswSnapshot snapshot = dhProcessDefinitionService.getLswSnapshotBySnapshotId(dhProcessDefinition.getProVerUid());
        return dhProcessDefinition.getProName() + "_" + snapshot.getName() + ".json";
    }

}