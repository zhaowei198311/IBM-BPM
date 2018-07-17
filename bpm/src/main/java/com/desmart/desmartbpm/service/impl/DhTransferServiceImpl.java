package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.DateUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.*;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.enums.DhTriggerType;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    private DhProcessDefinitionMapper dhProcessDefinitionMapper;
    @Autowired
    private DhActivityConfService dhActivityConfService;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhGatewayLineMapper dhGatewayLineMapper;
    @Autowired
    private DatRuleMapper datRuleMapper;
    @Autowired
    private DatRuleConditionMapper datRuleConditionMapper;
    @Autowired
    private DhObjectPermissionMapper dhObjectPermissionMapper;



    public ServerResponse<DhTransferData> exportProcessDefinition(String proAppId, String proUid, String proVerUid) {
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
        /*
        DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProAppIdAndProUid(proAppId, proUid);
        if (dhProcessMeta == null) {
            return ServerResponse.createByErrorMessage("流程元数据不存在");
        }
        transferData.addToProcessMetaList(dhProcessMeta);
        */
        // 获得分类信息
        /*
        List<DhProcessCategory> categoryList = dhProcessCategoryService.getCategoryAndAllParentCategory(dhProcessMeta.getCategoryUid());
        if (categoryList.isEmpty() && !dhProcessMeta.getCategoryUid().equals("rootCategory")) {
            return ServerResponse.createByErrorMessage("流程分类信息异常");
        }
        transferData.addAllToCategoryList(categoryList);
        */

        // 记录需要引入的触发器的主键
        // Set<String> triggerIdSetToInclude = new HashSet<>();
        // triggerIdSetToInclude.addAll(getTriggerIdListOnDhProcessDefinition(transferData.getProcessDefinitionList().get(0)));

        // 获得此流程定义的发起权限
        List<DhObjectPermission> permissionListOfStartProcess = dhObjectPermissionService.getPermissionListOfStartProcess(proAppId, proUid, proVerUid);
        transferData.addAllToObjectPermissionList(permissionListOfStartProcess);

        // 查找对应的表单
        List<BpmForm> bpmForms = bpmFormManageService.listAllFormsOfProcessDefinition(proUid, proVerUid);
        transferData.setFormList(bpmForms);
        // 自有的表单主键集合
        List<String> formUidList = this.getIdentityListOfObjectList(bpmForms);
        List<BpmFormRelePublicForm> bpmFormRelePublicFormList = bpmFormRelePublicFormMapper.listByFormUidList(formUidList);
        // 保存中间表记录
        transferData.setFormRelePublicFormList(bpmFormRelePublicFormList);
        // 根据formUid集合获得引用的 公共子表单 BPM_FORM_RELE_PUBLIC_FORM
        // List<String> pubFormUidList = bpmFormRelePublicFormMapper.listPublicFormUidByFormUidList(formUidList);
        // List<BpmPublicForm> bpmPublicForms = bpmPublicFormService.listByPublicFormUidList(pubFormUidList);
        // transferData.setPublicFormList(bpmPublicForms);
        // 获得表单字段集合(包括普通表单的字段和公共表单的字段)
        // formUidList.addAll(pubFormUidList); // 将共用子表单的id和普通表单的id结合
        List<BpmFormField> bpmFormFields = bpmFormFieldService.listByFormUidList(formUidList);
        transferData.setFormFieldList(bpmFormFields);

        // 获得所有环节
        List<BpmActivityMeta> activityMetaList = bpmActivityMetaService.listAllBpmActivityMeta(proAppId, proUid, proVerUid);
        if (activityMetaList.isEmpty()) {
            return ServerResponse.createByErrorMessage("流程环节信息异常");
        }
        transferData.addAllToBpmActivityMetaList(activityMetaList);
        List<String> sourceUserActivityIdList = this.getSourceUserActivityIdList(activityMetaList); // 所有原生流程环节
        List<String> sourceGatewayActiivtyIdList = this.getSourceGatewayActivityIdList(activityMetaList); // 所有原生网关环节

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

        // 获得原生流程环节的配置
        List<DhActivityConf> sourceActivityConfList = this.getSourceActivityConfListByActivityMetaList(activityMetaList);
        transferData.setActivityConfList(sourceActivityConfList);

        // 记录需要的所有模版id
        // Set<String> notifyTemplateIdSet = new HashSet<>();
        // 获得环节配置有关的模版id
        // List<String> notifyTemplateIdListFromActivityConfList = this.getNotifyTemplateIdListFromActivityConfList(sourceActivityConfList);
        // notifyTemplateIdSet.addAll(notifyTemplateIdListFromActivityConfList);
        // 获得环节配置上的触发器id
        // List<String> triggerIdListFromActivityConfList = this.getTriggerIdListFromActivityConfList(sourceActivityConfList);
        // triggerIdSetToInclude.addAll(triggerIdListFromActivityConfList);
        // 获得所有的步骤
        List<DhStep> dhStepList = dhStepService.listAllStepsOfProcessDefinition(proAppId, proUid, proVerUid);
        transferData.setStepList(dhStepList);
        List<String> stepUidList = this.getIdentityListOfObjectList(dhStepList);
        List<DhTriggerInterface> triggerInterfaceList = this.getDhTriggerInterfaceList(dhStepList);
        transferData.setTriggerInterfaceList(triggerInterfaceList);
        // 获得步骤关联的触发器
        // List<String> triggerIdFromStepList = this.getTriggerIdListFromStepList(dhStepList);
        // triggerIdSetToInclude.addAll(triggerIdFromStepList);

        // 获得Step相关的 权限信息， 字段，区块可见性
        List<DhObjectPermission> permissionListOfField = dhObjectPermissionService.listByStepUidList(stepUidList);
        transferData.addAllToObjectPermissionList(permissionListOfField);
        // 汇总处理trigger
        // this.assembleTriggerAndInterface(triggerIdSetToInclude, transferData);
        // 汇总处理template
        // this.assembleNotifyTemplateList(notifyTemplateIdSet, transferData);
        return ServerResponse.createBySuccess(transferData);
    }

    /**
     * 根据步骤列表取得步骤关联的触发器id列表
     * @param dhStepList  步骤列表
     * @return
     */
    private List<String> getTriggerIdListFromStepList(List<DhStep> dhStepList) {
        List<String> result = new ArrayList<>();
        if (dhStepList == null || dhStepList.isEmpty()) return result;
        for (DhStep step : dhStepList) {
            if (DhStep.TYPE_TRIGGER.equals(step.getStepType())) result.add(step.getStepObjectUid());
        }
        return result;
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
    private List<String> getTriggerIdListFromActivityConfList(List<DhActivityConf> confList) {
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
     * 获得对象集合的标识符id 集合(去重)
     * @param objList  对象集合
     * @param <T>
     * @return
     */
    private <T> List<String>  getIdentityListOfObjectList(List<T> objList) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(objList)) {
            return result;
        }
        for (T item : objList) {
            String identity = null;
            if (item instanceof BpmForm) {
                identity = ((BpmForm) item).getDynUid();
            } else if (item instanceof DhStep) {
                identity = ((DhStep) item).getStepUid();
            } else if (item instanceof BpmActivityMeta) {
                identity = ((BpmActivityMeta)item).getActivityId();
            } else if (item instanceof DhTrigger) {
                identity = ((DhTrigger)item).getTriUid();
            } else if (item instanceof DhProcessCategory) {
                identity = ((DhProcessCategory)item).getCategoryUid();
            } else if (item instanceof DhGatewayLine) {
                identity = ((DhGatewayLine)item).getGatewayLineUid();
            } else if (item instanceof DhNotifyTemplate) {
                identity = ((DhNotifyTemplate)item).getTemplateUid();
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
     * 根据环节列表获得所有原生环节的配置列表
     * @param activityMetaList
     * @return
     */
    private List<DhActivityConf> getSourceActivityConfListByActivityMetaList(List<BpmActivityMeta> activityMetaList) {
        List<DhActivityConf> confList = new ArrayList<>();
        if (activityMetaList == null || activityMetaList.isEmpty()) {
            return confList;
        }
        for (BpmActivityMeta activityMeta : activityMetaList) {
            if (activityMeta.getActivityId().equals(activityMeta.getSourceActivityId())
                && activityMeta.getDhActivityConf() != null) {
                confList.add(activityMeta.getDhActivityConf());
            }
        }
        return confList;
    }

    private List<DhTriggerInterface> getDhTriggerInterfaceList(List<DhStep> stepList) {
        List<String> triggerStepIdList = new ArrayList<>();
        for (DhStep step : stepList) {
            if (DhStep.TYPE_TRIGGER.equals(step.getStepType())) {
                triggerStepIdList.add(step.getStepUid());
            }
        }
        if (triggerStepIdList.isEmpty()) {
            return new ArrayList<>();
        }
        return dhTriggerInterfaceService.listByStepUidList(triggerStepIdList);


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

    @Override
    public ServerResponse<DhTransferData> trunFileIntoDhTransferData(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".json")) {
            return ServerResponse.createByErrorMessage("文件格式异常，请检查后重新上传");
        }
        InputStreamReader isr = null;
        BufferedReader br = null;
        InputStream in = null;
        StringBuilder sb = null;
        try {
            in = file.getInputStream();
            isr = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(isr);
            sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            LOG.error("读取上传的文件异常", e);
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (isr != null)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String info = sb.toString();
        DhTransferData dhTransferData = JSONObject.parseObject(info, new TypeReference<DhTransferData>() {});
        return ServerResponse.createBySuccess(dhTransferData);
    }

    @Transactional
    @Override
    public ServerResponse startImportProcessDefinition(DhTransferData transferData) {
        ServerResponse serverResponse = validateTransferDataForImportProcessDefinition(transferData);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        DhProcessDefinition definition = transferData.getProcessDefinitionList().get(0);
        // 检查元数据是否存在
        DhProcessMeta processMeta = dhProcessMetaService.getByProAppIdAndProUid(definition.getProAppId(), definition.getProUid());
        if (processMeta == null) {
            return ServerResponse.createBySuccessMessage("请先添加流程元数据");
        }
        // 删除流程定义即关联数据
        ServerResponse removeResponse = this.removeProcessDefinition(definition.getProAppId(),
                definition.getProUid(), definition.getProVerUid());
        if (!removeResponse.isSuccess()) {
            return removeResponse;
        }
        // 导入流程定义
        ServerResponse insertDefinitionResponse = insertProcessDefinitionRecord(definition);
        if (!insertDefinitionResponse.isSuccess()) {
            return insertDefinitionResponse;
        }

        // 导入环节信息
        importActivityMetaList(transferData);
        // 导入环节配置
        importActivityConfList(transferData);
        // 导入网关连接线
        importGatewayLineList(transferData);
        // 导入规则
        importRuleList(transferData);
        // 导入规则条件
        importRuleConditionList(transferData);
        // 导入步骤
        importStepList(transferData);
        // 导入分派记录(任务、超时处理人)
        importActivityAssignList(transferData);
        // 导入可回退环节配置
        importActivityRejectList(transferData);
        // 导入权限信息
        importObjectPermissionList(transferData);
        // 导入表单相关内容
        // importFormInfo(transferData);

        return ServerResponse.createBySuccess();
    }

    /**
     * 导入流程元数据列表
     * @param processMetaList
     */
    private void importProcessMeta(List<DhProcessMeta> processMetaList) {
        if (CollectionUtils.isEmpty(processMetaList)) return;
        List<DhProcessMeta> processMetaInDb = dhProcessMetaMapper.listByProcessMetaLsit(processMetaList);
        processMetaList.removeAll(processMetaInDb);
        if (processMetaList.isEmpty()) return;
        String currUserUid = getCurrentUserUid();
        for (DhProcessMeta dhProcessMeta : processMetaList) {
            dhProcessMeta.setCreator(currUserUid);
            dhProcessMeta.setCreateTime(new Date());
            dhProcessMeta.setUpdateUser(currUserUid);
            dhProcessMeta.setLastUpdateTime(new Date());
        }
        dhProcessMetaMapper.insertBatch(processMetaList);
    }

    /**
     * 导入流程元数据
     * @param dhTransferData
     */
    private void importProcessMeta(DhTransferData dhTransferData) {
        DhProcessMeta processMeta = dhTransferData.getProcessMetaList().get(0);
        DhProcessMeta metaInDb = dhProcessMetaService.getByProAppIdAndProUid(processMeta.getProAppId(), processMeta.getProUid());
        String currUserUid = getCurrentUserUid();
        Date date = new Date();
        if (metaInDb == null) {
            // 新增
            processMeta.setCreator(currUserUid);
            processMeta.setCreateTime(date);
            processMeta.setUpdateUser(currUserUid);
            processMeta.setLastUpdateTime(date);
            processMeta.setProMetaStatus(DhProcessMeta.STATUS_ON); // 元数据状态
            dhProcessMetaMapper.save(processMeta);
        } else {
            // 更新
            DhProcessMeta metaSelective = new DhProcessMeta();
            BeanUtils.copyProperties(processMeta, metaSelective);
            metaSelective.setProMetaUid(metaInDb.getProMetaUid());
            metaSelective.setUpdateUser(currUserUid);
            metaSelective.setLastUpdateTime(date);
            metaSelective.setProMetaStatus(metaInDb.getProMetaStatus()); // 元数据状态不变
            dhProcessMetaMapper.updateByProMetaUidSelective(metaSelective);
        }
    }


    /**
     * 插入流程定义
     * @param processDefinition 需要导入的流程定义
     */
    private ServerResponse insertProcessDefinitionRecord(DhProcessDefinition processDefinition) {
        // 插入流程定义记录
        processDefinition.setCreateUser(getCurrentUserUid());
        processDefinition.setLastModifiedUser(getCurrentUserUid());
        processDefinition.setLastModifiedDate(new Date());
        processDefinition.setProStatus(DhProcessDefinition.STATUS_SYNCHRONIZED);
        int count = dhProcessDefinitionMapper.save(processDefinition);
        return count == 1 ? ServerResponse.createBySuccess() : ServerResponse.createByErrorMessage("保存失败");
    }

    /**
     * 导入流程分类列表
     * @param transferData 
     */
    private void importCategoryList(DhTransferData transferData) {
        List<DhProcessCategory> categoryList = transferData.getCategoryList();
        if (CollectionUtils.isEmpty(categoryList)) {
            return;
        }
        // data中的分类主键
        List<String> uidListInData = this.getIdentityListOfObjectList(categoryList);
        // DB中的分类主键
        List<DhProcessCategory> categoryListInDb = dhProcessCategoryService.listByCategoryUidList(uidListInData);
        List<String> uidListInDb = this.getIdentityListOfObjectList(categoryListInDb);
        // 去除掉数据库中已有的分类后如果还有，则批量插入
        if (!removeElementByIdentityList(categoryList, uidListInDb).isEmpty()) {
            dhProcessCategoryService.insertBatch(categoryList);
        }
    }

    /**
     * 导入环节
     * @param transferData
     */
    private void importActivityMetaList(DhTransferData transferData) {
        List<BpmActivityMeta> activityMetaList = transferData.getBpmActivityMetaList();
        String currUserUid = getCurrentUserUid();
        Date date = new Date();
        for (BpmActivityMeta activityMeta : activityMetaList) {
            activityMeta.setCreator(currUserUid);
            activityMeta.setCreateTime(date);
            activityMeta.setUpdateBy(currUserUid);
            activityMeta.setUpdateTime(date);
        }
        bpmActivityMetaMapper.saveBatch(activityMetaList);
    }

    /**
     * 导入环节配置
     * @param transferData
     */
    private void importActivityConfList(DhTransferData transferData) {
        List<DhActivityConf> activityConfList = transferData.getActivityConfList();
        String currUserUid = getCurrentUserUid();
        Date date = new Date();
        for (DhActivityConf activityConf : activityConfList) {
            activityConf.setUpdator(currUserUid);
            activityConf.setUpdateTime(date);
        }
        dhActivityConfService.insertBatch(activityConfList);
    }

    /**
     * 导入网关连接线
     * @param transferData
     */
    private void importGatewayLineList(DhTransferData transferData) {
        List<DhGatewayLine> gatewayLineList = transferData.getGatewayLineList();
        if (!CollectionUtils.isEmpty(gatewayLineList)) {
            dhGatewayLineMapper.insertBatch(gatewayLineList);
        }
    }

    /**
     * 导入规则
     * @param transferData
     */
    private void importRuleList(DhTransferData transferData) {
        List<DatRule> ruleList = transferData.getRuleList();
        if (CollectionUtils.isEmpty(ruleList)) {
            return ;
        }
        String dateStr = DateUtil.datetoString(new Date());
        String currUserUid = getCurrentUserUid();
        for (DatRule rule : ruleList) {
            rule.setCreator(currUserUid);
            rule.setCreateTime(dateStr);
            rule.setStartTime(dateStr);
        }
        datRuleMapper.batchInsertDatRule(ruleList);
    }

    /**
     * 导入规则条件
     * @param transferData
     */
    private void importRuleConditionList(DhTransferData transferData) {
        List<DatRuleCondition> ruleConditionList = transferData.getRuleConditionList();
        if (CollectionUtils.isEmpty(ruleConditionList)) {
            return;
        }
        String currUserUid = getCurrentUserUid();
        String dateStr = DateUtil.datetoString(new Date());
        for (DatRuleCondition ruleCondition : ruleConditionList) {
            ruleCondition.setCreator(currUserUid);
            ruleCondition.setCreateTime(dateStr);
        }
        datRuleConditionMapper.inserToDatRuleCondition(ruleConditionList);
    }

    /**
     * 导入步骤
     * @param transferData
     */
    private void importStepList(DhTransferData transferData) {
        List<DhStep> stepList = transferData.getStepList();
        if (CollectionUtils.isEmpty(stepList)) {
            return;
        }
        dhStepService.insertBatch(stepList);
    }

    /**
     * 导入分派记录
     * @param transferData
     */
    private void importActivityAssignList(DhTransferData transferData) {
        List<DhActivityAssign> activityAssignList = transferData.getActivityAssignList();
        if (CollectionUtils.isEmpty(activityAssignList)) {
            return;
        }
        dhActivityAssignService.insertBatch(activityAssignList);
    }

    /**
     * 导入可回退环节配置
     * @param transferData
     */
    private void importActivityRejectList(DhTransferData transferData) {
        List<DhActivityReject> activityRejectList = transferData.getActivityRejectList();
        if (CollectionUtils.isEmpty(activityRejectList)) {
            return;
        }
        dhActivityRejectMapper.insertBatch(activityRejectList);
    }

    /**
     * 导入通知模版
     * @param transferData
     */
    private void importNotifyTemplateList(DhTransferData transferData) {
        List<DhNotifyTemplate> notifyTemplateList = transferData.getNotifyTemplateList();
        if (CollectionUtils.isEmpty(notifyTemplateList)) {
            return;
        }
        List<String> templateUidList = getIdentityListOfObjectList(notifyTemplateList);
        // 查询出引擎中已有的模版
        List<DhNotifyTemplate> dhNotifyTemplatesInDb = dhNotifyTemplateService.listByTemplateUidList(templateUidList);
        if (!CollectionUtils.isEmpty(dhNotifyTemplatesInDb)) {
            // 去除重复
            List<String> templateUidListInDb = getIdentityListOfObjectList(dhNotifyTemplatesInDb);
            this.removeElementByIdentityList(notifyTemplateList, templateUidListInDb);
        }
        // 如果去除已经存在的后不剩了，就结束
        if (CollectionUtils.isEmpty(notifyTemplateList)) {
            return;
        }
        String currUserUid = getCurrentUserUid();
        Date date = new Date();
        for (DhNotifyTemplate notifyTemplate : notifyTemplateList) {
            notifyTemplate.setCreateUserUid(currUserUid);
            notifyTemplate.setCreateTime(date);
            notifyTemplate.setUpdateUserUid(currUserUid);
            notifyTemplate.setUpdateTime(date);
        }
        dhNotifyTemplateService.insertBatch(notifyTemplateList);
    }

    /**
     * 导入权限信息
     */
    private void importObjectPermissionList(DhTransferData transferData) {
        List<DhObjectPermission> objectPermissionList = transferData.getObjectPermissionList();
        if (CollectionUtils.isEmpty(objectPermissionList)) {
            return;
        }
        dhObjectPermissionMapper.saveBatch(objectPermissionList);
    }

    /**
     * 从对象集合中去除 标识符对应的对象
     * @param objectList 对象集合
     * @param identityList 标识符集合
     * @param <T>
     * @return
     */
    private <T> List<T> removeElementByIdentityList(List<T> objectList, List<String> identityList) {
        if (CollectionUtils.isEmpty(objectList) || CollectionUtils.isEmpty(identityList)) {
            return objectList;
        }
        Iterator<T> iterator = objectList.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item instanceof DhProcessCategory) {
                if (identityList.contains(((DhProcessCategory) item).getCategoryUid())) {
                    iterator.remove();
                }
            } else if (item instanceof DhNotifyTemplate) {
                if (identityList.contains(((DhNotifyTemplate) item).getTemplateUid())) {
                    iterator.remove();
                }
            }

        }
        return objectList;
    }

    @Override
    public ServerResponse validateTransferDataForImportProcessDefinition(DhTransferData transferData) {
        // 校验流程定义
        if (CollectionUtils.isEmpty(transferData.getProcessDefinitionList())) {
            return ServerResponse.createByErrorMessage("缺少流程定义信息");
        } else if (transferData.getProcessDefinitionList().size() > 1) {
            return ServerResponse.createByErrorMessage("检测到流程定义大于一条");
        }
        DhProcessDefinition definition = transferData.getProcessDefinitionList().get(0);


        // 校验环节信息
        if (CollectionUtils.isEmpty(transferData.getBpmActivityMetaList())) {
            return ServerResponse.createByErrorMessage("缺少环节信息");
        }
        // 校验环节配置
        if (CollectionUtils.isEmpty(transferData.getActivityConfList())) {
            return ServerResponse.createByErrorMessage("缺少环节配置信息");
        }
        return ServerResponse.createBySuccess();
    }

    private String getCurrentUserUid() {
        return (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
    }


    @Transactional
    @Override
    public ServerResponse removeProcessDefinition(String proAppId, String proUid, String proVerUid) {
        DhProcessDefinition dhProcessDefinition = dhProcessDefinitionService.getDhProcessDefinition(proAppId, proUid, proVerUid);
        if (dhProcessDefinition == null) { // 流程定义不存在直接返回成功
            return ServerResponse.createBySuccess();
        }
        // 判断流程定义是否已启用
        if (DhProcessDefinition.STATUS_ENABLED.equals(dhProcessDefinition.getProStatus())) {
            return ServerResponse.createByErrorMessage("启用中的流程不能删除");
        }
        // 删除流程定义表中记录
        DhProcessDefinition definitionSelective = new DhProcessDefinition(proAppId, proUid, proVerUid);
        int countRow = dhProcessDefinitionMapper.deleteBySelective(definitionSelective);
        if (countRow == 0) {
            return ServerResponse.createByErrorMessage("删除流程定义失败");
        }
        // 删除流程定义相关权限（不含步骤相关）
        dhObjectPermissionService.removeByProAppIdAndProUidAndProVerUid(proAppId, proUid, proVerUid);
        List<BpmActivityMeta> activityMetaList = bpmActivityMetaService.listAllBpmActivityMeta(proAppId, proUid, proVerUid);
        List<String> activityIdList = this.getIdentityListOfObjectList(activityMetaList);
        List<String> sourceUserActivityIdList = this.getSourceUserActivityIdList(activityMetaList); // 所有环节的activity_id集合
        List<String> sourceGatewayActiivtyIdList = this.getSourceGatewayActivityIdList(activityMetaList); // 所有原生网关环节
        // 获得所有的步骤
        List<DhStep> stepList = dhStepService.listAllStepsOfProcessDefinition(proAppId, proUid, proVerUid);
        List<String> stepUidList = this.getIdentityListOfObjectList(stepList);
        // 删除表单字段相关权限
        dhObjectPermissionService.removeByStepUidList(stepUidList);
        // 删除接口触发器有关的参数映射记录
        dhTriggerInterfaceService.removeByStepUidList(stepUidList);
        // 删除步骤
        dhStepService.removeByStepUidList(stepUidList);
        // 删除分配明细
        dhActivityAssignService.removeByActivityIdList(sourceUserActivityIdList);
        // 删除可供驳回的环节记录
        dhActivityRejectMapper.deleteByActivityIds(sourceUserActivityIdList);
        // 删除环节配置信息
        List<DhActivityConf> sourceActivityConfList = this.getSourceActivityConfListByActivityMetaList(activityMetaList);
        List<String> confIdList = this.getIdentityListOfObjectList(sourceActivityConfList);
        dhActivityConfService.removeByActcUidList(confIdList);
        // 获得所有的网关连接线
        List<DhGatewayLine> dhGatewayLines = dhGatewayLineService.listByGatewayActivityIdList(sourceGatewayActiivtyIdList);
        List<String> gatewayLineUidList = this.getIdentityListOfObjectList(dhGatewayLines);
        // 获得所有规则
        List<String> ruleIdList = this.getRuleIdListByDhGatewayLines(dhGatewayLines);
        // 删除规则
        datRuleService.removeByRuleIdList(ruleIdList);
        // 删除条件
        datRuleConditionService.removeByRuleIdList(ruleIdList);
        // 删除网关连接线
        dhGatewayLineService.removeByGatewayLineUidList(gatewayLineUidList);
        // 删除所有环节
        bpmActivityMetaService.removeByActivityIdList(activityIdList);
        // 查找对应的表单
        List<BpmForm> bpmForms = bpmFormManageService.listAllFormsOfProcessDefinition(proUid, proVerUid);
        List<String> formUidList = this.getIdentityListOfObjectList(bpmForms);
        // 删除表单字段
        bpmFormFieldService.removeByFormUidList(formUidList);
        // 删除表单与公共表单关联关系
        if (!CollectionUtils.isEmpty(formUidList)) {
            bpmFormRelePublicFormMapper.removeByFormUidList(formUidList);
        }
        // 删除表单
        bpmFormManageService.removeFormsByFormUidList(formUidList);

        return ServerResponse.createBySuccess();
    }
}