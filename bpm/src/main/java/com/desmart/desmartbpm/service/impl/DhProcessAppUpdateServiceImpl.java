package com.desmart.desmartbpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.*;
import com.desmart.desmartbpm.entity.DhProcessDefinitionBo;
import com.desmart.desmartbpm.mongo.ModelMongoDao;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartsystem.dao.DhInterfaceMapper;
import com.desmart.desmartsystem.dao.DhInterfaceParameterMapper;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import org.apache.tools.ant.taskdefs.optional.extension.ExtraAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class DhProcessAppUpdateServiceImpl implements DhProcessAppUpdateService {
    private static final Logger logger = LoggerFactory.getLogger(DhProcessAppUpdateServiceImpl.class);

    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
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
    @Autowired
    private ModelMongoDao modelMongoDao;

    @Transactional
    public ServerResponse pullAllProcessActivityMeta(String proAppId, String snapshotId) {
        List<DhProcessDefinitionBo> exposedDefinitionList = dhProcessDefinitionService.getExposedProcessDefinitionByProAppIdAndSnapshotId(proAppId, snapshotId);
        if (CollectionUtils.isEmpty(exposedDefinitionList)) {
            return ServerResponse.createByErrorMessage("没有找到符合条件的流程定义");
        }
        DhProcessDefinitionBo bo = null;

        Queue<DhProcessDefinitionBo> boToPullQueue = new LinkedList<>();
        List<String> sortedBpdIdsEachTime = new ArrayList<>(); // 记录每次遍历后 被排序的bpdId

        // 对所有的流程定义排序拉取
        // 获得所有流程定义的ProcessModel
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfigService.getFirstActConfig());
        Iterator<DhProcessDefinitionBo> iterator = exposedDefinitionList.iterator();
        // 遍历处理每个bo对象，分析有没有外链流程，如果没有外链流程，加入队列
        while (iterator.hasNext()) {
            bo = iterator.next();
            String processModelStr = modelMongoDao.getProcessModel(bo.getProAppId(), bo.getProUid(), bo.getProVerUid());
            if (processModelStr == null) {
                HttpReturnStatus returnStatus = bpmProcessUtil.getProcessModel(bo.getProAppId(),
                        bo.getProUid(), bo.getProVerUid());
                if (HttpReturnStatusUtil.isErrorResult(returnStatus)) {
                    throw new PlatformException("获得ProcessModel失败");
                }
                processModelStr = returnStatus.getMsg();
                modelMongoDao.saveProcessModel(bo.getProAppId(), bo.getProUid(), bo.getProVerUid(), processModelStr);
            }
            // 分析外链的流程
            Set<String> externalIdList = getExternalIdList(processModelStr);
            if (externalIdList.isEmpty()) {
                // 没有外链流程，入队
                boToPullQueue.offer(bo);
                sortedBpdIdsEachTime.add(bo.getProUid());
                iterator.remove();
            }
            bo.setExternalIdList(externalIdList);
        } // 至此第一次分析结束

        // 反复处理知道所有公开的流程被排序
        List<String> sortedBpdIdsTemp = null;
        while (!exposedDefinitionList.isEmpty()) {
            sortedBpdIdsTemp = new ArrayList<>();
            iterator = exposedDefinitionList.iterator();
            while (iterator.hasNext()) {
                bo = iterator.next();
                bo.removeFromExternalIdList(sortedBpdIdsEachTime); // 去除上次被排序的外链流程
                if (bo.canSynchronize()) {
                    boToPullQueue.offer(bo); // 加入已排序队列
                    sortedBpdIdsTemp.add(bo.getProUid());
                    iterator.remove(); // 从未排序集合中移除
                }
            }
            sortedBpdIdsEachTime = sortedBpdIdsTemp;
            if (sortedBpdIdsEachTime.isEmpty()) {
                // 如果经过一次循环，没有任何流程被排序成功，则说明陷入了死循环
                throw new PlatformException("排序失败，陷入死循环");
            }
        }// 至此所有定义排序结束

        while (!boToPullQueue.isEmpty()) {
            bo = boToPullQueue.poll();
            ServerResponse response = dhProcessDefinitionService.createDhProcessDefinition(bo.getProAppId(), bo.getProUid(), bo.getProVerUid());
            if (!response.isSuccess()) {
                throw new PlatformException("创建环节失败");
            }
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 根据ProcessModel信息获得所有的外链环节
     * @param processModelStr
     * @return
     */
    private Set<String> getExternalIdList(String processModelStr) {
        JSONObject obj = JSON.parseObject(processModelStr);
        JSONArray stepArr = obj.getJSONObject("data").getJSONObject("Diagram").getJSONArray("step");
        Set<String> externalIdList = new HashSet<>();
        findExternalIdFromStepJsonArr(externalIdList, stepArr);
        return externalIdList;
    }

    /**
     * 在ProcessModel的step数组中找到外链流程
     * @param externalList
     * @param stepArr
     */
    private void findExternalIdFromStepJsonArr(Set<String> externalList, JSONArray stepArr) {
        if (stepArr == null || stepArr.size() == 0) {
            return;
        }
        for (int i = 0; i < stepArr.size(); i++) {
            JSONObject step = stepArr.getJSONObject(i);
            if ("subBpd".equals(step.getString("activityType")) && step.getString("externalID") != null) {
                externalList.add(step.getString("externalID"));
            } else if ("subProcess".equals(step.getString("activityType"))) {
                JSONObject diagram = step.getJSONObject("diagram");
                if (diagram != null) {
                    JSONArray steps = diagram.getJSONArray("step");
                    if (steps.size() > 0) {
                        findExternalIdFromStepJsonArr(externalList, steps);
                    }
                }
            }

        }
    }



}