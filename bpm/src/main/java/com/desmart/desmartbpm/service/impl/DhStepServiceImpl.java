package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTrigger;
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
                || StringUtils.isBlank(dhStep.getStepObjectUid())) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        } 
        // 查看有没有这个配置
        BpmActivityMeta selective = new BpmActivityMeta(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid(), dhStep.getActivityBpdId());
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (list.size() == 0) {
            return ServerResponse.createByErrorMessage("此环节不存在");
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
        dhStep.setStepSort(generateStepSort(dhStep));
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
        PageHelper.orderBy("STEP_BUSINESS_KEY, STEP_SORT");
        DhStep selective = new DhStep(bpmActivityMeta.getProAppId(), bpmActivityMeta.getBpdId(), bpmActivityMeta.getSnapshotId());
        selective.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
        List<DhStep> stepList = dhStepMapper.listBySelective(selective);
        return ServerResponse.createBySuccess(stepList);
    }
    
    
    @Transactional
    public ServerResponse updateStep(DhStep dhStep) {
        if (StringUtils.isBlank(dhStep.getStepUid()) || StringUtils.isBlank(dhStep.getStepObjectUid())) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        } 
        DhStep currentStep = dhStepMapper.selectByPrimaryKey(dhStep.getStepUid());
        if (currentStep == null) {
            return ServerResponse.createByErrorMessage("找不到此步骤");
        }
        
        if (DhStep.TYPE_TRIGGER.equals(currentStep.getStepType())) {
            DhTrigger trigger = dhTriggerMapper.getByPrimaryKey(dhStep.getStepObjectUid());
            if (trigger == null) {
                return ServerResponse.createByErrorMessage("触发器不存在");
            }
            
        } else if (DhStep.TYPE_FORM.equals(currentStep.getStepType())) {
            BpmForm form = bpmFormManageMapper.queryFormByFormUid(dhStep.getStepObjectUid());
            if (form == null) {
                return ServerResponse.createByErrorMessage("表单不存在");
            }
            // 移除当前绑定表单的字段权限
            removeFieldPermissionOfStep(currentStep.getStepUid());
        } else {
            return ServerResponse.createByErrorMessage("修改失败, 步骤类型异常");
        }
        
        // 执行更新
        DhStep updateSelective = new DhStep();
        updateSelective.setStepUid(currentStep.getStepUid());
        updateSelective.setStepObjectUid(dhStep.getStepObjectUid());
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
        dhStepMapper.updateStepSortOfRelationStep(currentStep);
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
    
    @Override
    @Transactional
    public ServerResponse resortStep(String stepUid, String resortType) {
        if (StringUtils.isBlank(stepUid) || (!StringUtils.equals("increase", resortType) && !StringUtils.equals("reduce", resortType))) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        DhStep currStep = dhStepMapper.selectByPrimaryKey(stepUid);
        if (currStep == null) {
            return ServerResponse.createByErrorMessage("此步骤不存在");
        }
        DhStep anotherStep = null;
        if (StringUtils.equals("reduce", resortType)) {
            anotherStep = getPreStepOfCurrStep(currStep);
            if (anotherStep == null) {
                return ServerResponse.createByErrorMessage("此环节已经是第一个环节");
            }
        } else {
            anotherStep = getNextStepOfCurrStep(currStep);
            if (anotherStep == null) {
                return ServerResponse.createByErrorMessage("此环节已经是最后一个环节");
            }
        }
        Integer tempStep = currStep.getStepSort();
        currStep.setStepSort(anotherStep.getStepSort());
        anotherStep.setStepSort(tempStep);
        dhStepMapper.updateByPrimaryKeySelective(currStep);
        dhStepMapper.updateByPrimaryKeySelective(anotherStep);
        return ServerResponse.createBySuccess();
    }
    
    
    public DhStep getPreStepOfCurrStep(DhStep dhStep) {
        DhStep selective = new DhStep();
        selective.setProAppId(dhStep.getProAppId());
        selective.setProUid(dhStep.getProUid());
        selective.setProVerUid(dhStep.getProVerUid());
        selective.setActivityBpdId(dhStep.getActivityBpdId());
        selective.setStepBusinessKey(dhStep.getStepBusinessKey());
        Integer stepSort = dhStep.getStepSort();
        if (stepSort == null || stepSort.intValue() == 1) {
            return null;
        }
        stepSort = stepSort.intValue() - 1;
        selective.setStepSort(stepSort);
        List<DhStep> list = dhStepMapper.listBySelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
    
    public DhStep getNextStepOfCurrStep(DhStep dhStep) {
        DhStep selective = new DhStep();
        selective.setProAppId(dhStep.getProAppId());
        selective.setProUid(dhStep.getProUid());
        selective.setProVerUid(dhStep.getProVerUid());
        selective.setActivityBpdId(dhStep.getActivityBpdId());
        selective.setStepBusinessKey(dhStep.getStepBusinessKey());
        Integer stepSort = dhStep.getStepSort();
        if (stepSort == null) {
            return null;
        }
        stepSort = stepSort.intValue() + 1;
        selective.setStepSort(stepSort);
        List<DhStep> list = dhStepMapper.listBySelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
    
    /**
     * 生成一个StepSort
     * @return
     */
    private int generateStepSort(DhStep dhStep) {
        return dhStepMapper.getMaxStepSort(dhStep) + 1;
    }
    
}
