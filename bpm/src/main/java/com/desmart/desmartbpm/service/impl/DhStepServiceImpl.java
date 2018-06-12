package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
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
import com.desmart.desmartbpm.entity.DhProcessDefinition;
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
        
        //检查该环节配置中步骤关键字是否已经存在
        DhStep selective1 = new DhStep(list.get(0).getProAppId()
        		, list.get(0).getBpdId(), list.get(0).getSnapshotId());
        selective1.setActivityBpdId(list.get(0).getActivityBpdId());
        selective1.setStepBusinessKey(dhStep.getStepBusinessKey());
        selective1.setStepType(dhStep.getStepType());
        if(dhStepMapper.listBySelective(selective1)!=null) {
        	return ServerResponse.createByErrorMessage("关键字已存在，请重新自定义关键字");
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
//    
    
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

    @Override
    public List<DhStep> getStepsOfBpmActivityMetaByStepBusinessKey(BpmActivityMeta bpmActivityMeta, String stepBusinessKey) {
        // 需要查询源环节的步骤配置
        BpmActivityMeta sourceMeta = null;
        if (bpmActivityMeta.getSourceActivityId().equals(bpmActivityMeta.getActivityId())){
            sourceMeta = bpmActivityMeta;
        } else {
            sourceMeta = bpmActivityMetaMapper.queryByPrimaryKey(bpmActivityMeta.getSourceActivityId());
        }
        DhStep stepSelective = new DhStep(sourceMeta.getProAppId(), sourceMeta.getBpdId(), sourceMeta.getSnapshotId());
        stepSelective.setActivityBpdId(sourceMeta.getActivityBpdId());
        stepSelective.setStepBusinessKey(stepBusinessKey);
        PageHelper.orderBy("STEP_SORT");
        return dhStepMapper.listBySelective(stepSelective);
    }

	@Override
	public ServerResponse<List<DhStep>> getStepInfoByCondition(DhStep dhStep) {
		List<DhStep> stepList = dhStepMapper.listBySelective(dhStep);
        return ServerResponse.createBySuccess(stepList);
	}
 

	@Override
	public ServerResponse createStepToAll(DhStep dhStep) {
		if (StringUtils.isBlank(dhStep.getProAppId()) || StringUtils.isBlank(dhStep.getProUid())
                || StringUtils.isBlank(dhStep.getProVerUid()) || StringUtils.isBlank(dhStep.getStepType())
                || StringUtils.isBlank(dhStep.getStepObjectUid())) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
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
        }
		List<BpmActivityMeta> sourceList = bpmActivityMetaMapper.queryByConditionToSource
				(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid());
		List<DhStep> insertList = new ArrayList<>();//用于新增步骤
		List<DhStep> updateList = new ArrayList<>();//用于修改步骤
		for (BpmActivityMeta bpmActivityMeta : sourceList) {
			
		//检查该环节配置中步骤关键字是否已经存在
        DhStep selective1 = new DhStep(bpmActivityMeta.getProAppId()
        		, bpmActivityMeta.getBpdId(), bpmActivityMeta.getSnapshotId());
        selective1.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
        selective1.setStepBusinessKey(dhStep.getStepBusinessKey());
        selective1.setStepType(dhStep.getStepType());
        List<DhStep> list = dhStepMapper.listBySelective(selective1);
        if(list!=null&&list.size()>0) {
        	selective1 = list.get(0);
        	selective1.setStepObjectUid(dhStep.getStepObjectUid());
        	updateList.add(selective1);
		}else {
			selective1.setStepUid(EntityIdPrefix.DH_STEP + UUID.randomUUID().toString());
			selective1.setStepSort(generateStepSort(dhStep));
			selective1.setStepObjectUid(dhStep.getStepObjectUid());
			insertList.add(selective1);
		}
        
		}
		int count = 0;
		if(updateList.size()>0) {
			count += dhStepMapper.updateBatchDhStep(updateList);
		}
		if(insertList.size()>0) {
			count += dhStepMapper.insertBatchDhStep(insertList);
		}
			return ServerResponse.createBySuccess();
	}
    
}
