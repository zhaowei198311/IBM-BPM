package com.desmart.desmartbpm.service.impl;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.*;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartsystem.dao.DhInterfaceMapper;
import com.desmart.desmartsystem.dao.DhInterfaceParameterMapper;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DhProcessAppUpdateServiceImpl implements DhProcessAppUpdateService {
    private static final Logger logger = LoggerFactory.getLogger(DhProcessAppUpdateServiceImpl.class);

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
    @Autowired
    private BpmFormManageMapper bpmFormManageMapper;
    @Autowired
    private BpmFormFieldMapper bpmFormFieldMapper;
    @Autowired
    private DhTriggerMapper dhTriggerMapper;
    @Autowired
    private DhInterfaceMapper dhInterfaceMapper;
    @Autowired
    private DhInterfaceParameterMapper dhInterfaceParameterMapper;
    @Autowired
    private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
    @Autowired
    private BpmPublicFormMapper bpmPublicFormMapper;
    @Autowired
    private DhNotifyTemplateMapper dhNotifyTemplateMapper;


    public ServerResponse pullAllProcessActivityMeta(String proAppId, String snapshotId) {
        List<DhProcessDefinition> exposedDefinitionList = dhProcessDefinitionService.getExposedProcessDefinitionByProAppIdAndSnapshotId(proAppId, snapshotId);
        if (CollectionUtils.isEmpty(exposedDefinitionList)) {
            return ServerResponse.createByErrorMessage("没有找到符合条件的流程定义");
        }
        // 对所有的流程定义排序拉取
        // 获得所有流程定义的ProcessModel

        return null;


    }





}