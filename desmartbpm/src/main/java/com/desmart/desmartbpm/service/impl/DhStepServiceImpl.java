package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.enums.DhObjectPermissionObjType;
import com.desmart.desmartbpm.enums.DhStepType;
import com.desmart.desmartbpm.service.DhStepService;
import com.github.pagehelper.PageHelper;

@Service
public class DhStepServiceImpl implements DhStepService {
    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhStepMapper dhStepMapper;
    @Autowired
    private DhTriggerMapper dhTriggerMapper;
    @Autowired
    private DhObjectPermissionMapper dhObjectPermissionMapper;
    
    @Autowired
    private BpmFormManageMapper bpmFormManageMapper;
    
    @Override
    public ServerResponse create(DhStep dhStep) {
        if (StringUtils.isBlank(dhStep.getProAppId()) || StringUtils.isBlank(dhStep.getProUid())
                || StringUtils.isBlank(dhStep.getProVerUid()) || StringUtils.isBlank(dhStep.getStepType())
                || dhStep.getStepSort() == null || dhStep.getStepSort().intValue() < 0
                || StringUtils.isBlank(dhStep.getStepObjectUid())) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        } 
        // 查看有没有这个配置
        BpmActivityMeta selective = new BpmActivityMeta(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid(), dhStep.getActivityBpdId());
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (list.size() == 0) {
            return ServerResponse.createByErrorMessage("此环节不存在");
        }
        
        // 查看这个step是否已经存在
        if (isStepExists(dhStep)) {
            return ServerResponse.createByErrorMessage("步骤序号：" +dhStep.getStepSort() +"，步骤关键字："+dhStep.getStepBusinessKey()+"已经存在，不能重复配置");
        }
        
        String stepType = dhStep.getStepType();
        DhStepType stepTypeEnum = DhStepType.codeOf(stepType);
        if (stepTypeEnum == null) {
            return ServerResponse.createByErrorMessage("步骤类型不符合要求");
        }
        if (stepTypeEnum == DhStepType.FORM) {
            // form类型
            if (bpmFormManageMapper.queryFormByFormUid(dhStep.getStepObjectUid()) == null) {
                return ServerResponse.createByErrorMessage("表单不存在");
            }
        } else {
            // 触发器类型
            if (dhTriggerMapper.getByPrimaryKey(dhStep.getStepObjectUid()) == null) {
                return ServerResponse.createByErrorMessage("触发器不存在");
            }
        }
        dhStep.setStepUid(EntityIdPrefix.DH_STEP + UUID.randomUUID().toString());
        dhStepMapper.insert(dhStep);
        
        return ServerResponse.createBySuccess();
    }
    
    public ServerResponse<List<DhStep>> getStepOfDhActivityConf(DhActivityConf conf) {
        String activityId = conf.getActivityId();
        if (StringUtils.isBlank(activityId)) {
            return ServerResponse.createByErrorMessage("缺少参数");
        }
        BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
        if (bpmActivityMeta == null) {
            return ServerResponse.createByErrorMessage("找不到环节");
        }
        PageHelper.orderBy("STEP_SORT");
        DhStep selective = new DhStep(bpmActivityMeta.getProAppId(), bpmActivityMeta.getBpdId(), bpmActivityMeta.getSnapshotId());
        selective.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
        List<DhStep> stepList = dhStepMapper.listBySelective(selective);
        return ServerResponse.createBySuccess(stepList);
    }
    
    
    @Transactional
    public ServerResponse updateStep(DhStep dhStep) {
        if (StringUtils.isBlank(dhStep.getStepUid()) || StringUtils.isBlank(dhStep.getStepBusinessKey()) || StringUtils.isBlank(dhStep.getStepType())
                || dhStep.getStepSort() == null || dhStep.getStepSort().intValue() < 0 || StringUtils.isBlank(dhStep.getStepObjectUid())) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        } 
        DhStep currentStep = dhStepMapper.selectByPrimaryKey(dhStep.getStepUid());
        if (currentStep == null) {
            return ServerResponse.createByErrorMessage("找不到此步骤");
        }
        dhStep.setProAppId(currentStep.getProAppId());
        dhStep.setProUid(currentStep.getProUid());
        dhStep.setProVerUid(currentStep.getProVerUid());
        dhStep.setActivityBpdId(currentStep.getActivityBpdId());
        // 步骤序号变更的话看是否已经有这个序号同关键字的步骤
        if (!currentStep.getStepSort().equals(dhStep.getStepSort()) && isStepExists(dhStep)) {
            return ServerResponse.createByErrorMessage("修改失败，这个步骤序号已存在相同步骤关键字的步骤");
        }
        
        if (DhStepType.TRIGGER.getCode().equals(currentStep.getStepType()) && DhStepType.TRIGGER.getCode().equals(dhStep.getStepType())) {
            if (dhTriggerMapper.getByPrimaryKey(dhStep.getStepObjectUid()) == null) {
                return ServerResponse.createByErrorMessage("触发器不存在");
            }
        } else if (DhStepType.TRIGGER.getCode().equals(currentStep.getStepType()) && DhStepType.FORM.getCode().equals(dhStep.getStepType())) {
            if (bpmFormManageMapper.queryFormByFormUid(dhStep.getStepObjectUid()) == null) {
                return ServerResponse.createByErrorMessage("表单不存在");
            }
        } else if (DhStepType.FORM.getCode().equals(currentStep.getStepType()) && DhStepType.TRIGGER.getCode().equals(dhStep.getStepType())) {
            if (dhTriggerMapper.getByPrimaryKey(dhStep.getStepObjectUid()) == null) {
                return ServerResponse.createByErrorMessage("触发器不存在");
            }
            removeFieldPermissionOfStep(dhStep.getStepUid());
        } else if (DhStepType.FORM.getCode().equals(currentStep.getStepType()) && DhStepType.FORM.getCode().equals(dhStep.getStepType())) {
            if (bpmFormManageMapper.queryFormByFormUid(dhStep.getStepObjectUid()) == null) {
                return ServerResponse.createByErrorMessage("表单不存在");
            }
            removeFieldPermissionOfStep(dhStep.getStepUid());
        } else {
            return ServerResponse.createByErrorMessage("修改失败, 步骤类型异常");
        }
        // 执行更新
        DhStep updateSelective = new DhStep();
        updateSelective.setStepUid(currentStep.getStepUid());
        updateSelective.setStepSort(dhStep.getStepSort());
        updateSelective.setStepObjectUid(dhStep.getStepObjectUid());
        updateSelective.setStepType(dhStep.getStepType());
        updateSelective.setStepBusinessKey(dhStep.getStepBusinessKey());
        dhStepMapper.updateByPrimaryKeySelective(updateSelective);
        return ServerResponse.createBySuccess();
    }
    
    @Transactional
    public ServerResponse deleteDhStep(String stepUid) {
        if (StringUtils.isBlank(stepUid)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        DhStep currentStep = dhStepMapper.selectByPrimaryKey(stepUid);
        if (currentStep == null) {
            return ServerResponse.createByErrorMessage("找不到此步骤");
        }
        if (DhStepType.FORM.getCode().equals(currentStep.getStepType())) {
            // 如果是表单类型，清除权限
            removeFieldPermissionOfStep(stepUid);
        }
        dhStepMapper.deleteByPrimaryKey(stepUid);
        return ServerResponse.createBySuccess();
    }
    
    /**
     * 删除步骤对应的字段权限
     * @param stepUid
     */
    private void removeFieldPermissionOfStep(String stepUid) {
        DhObjectPermission deleteSelective = new DhObjectPermission();
        deleteSelective.setStepUid(stepUid);
        deleteSelective.setOpObjType(DhObjectPermissionObjType.FIELD.getCode());
        dhObjectPermissionMapper.delectByDhObjectPermissionSelective(deleteSelective);
    }
    
    /**
     * 根据 应用库id， 图id， 版本id， 流程图元素id， 步骤序号， 步骤关键字 查看步骤是否存在
     * @param dhStep
     * @return
     */
    private boolean isStepExists(DhStep dhStep) {
        DhStep stepSelective = new DhStep(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid(),
                dhStep.getActivityBpdId(), dhStep.getStepSort(), dhStep.getStepBusinessKey());
        if (dhStepMapper.listBySelective(stepSelective).size() > 0) {
            return true;
        } 
        return false;
    }
    
}
