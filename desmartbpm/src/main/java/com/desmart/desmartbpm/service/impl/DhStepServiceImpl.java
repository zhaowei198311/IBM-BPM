package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.enums.DhStepType;
import com.desmart.desmartbpm.service.DhStepService;

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
    
    private BpmFormManageMapper bpmFormManageMapper;
    
    @Override
    public ServerResponse create(DhStep dhStep) {
        if (StringUtils.isBlank(dhStep.getProAppId()) || StringUtils.isBlank(dhStep.getProUid())
                || StringUtils.isBlank(dhStep.getProVerUid()) || StringUtils.isBlank(dhStep.getStepType())
                || dhStep.getStepSort() == null || dhStep.getStepSort().intValue() < 0) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        } 
        // 查看有没有这个配置
        BpmActivityMeta selective = new BpmActivityMeta(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid(), dhStep.getActivityBpdId());
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (list.size() == 0) {
            return ServerResponse.createByErrorMessage("此环节不存在");
        }
        
        // 查看这个step是否已经存在
        DhStep stepSelective = new DhStep(dhStep.getProAppId(), dhStep.getProUid(), dhStep.getProVerUid(),
                dhStep.getActivityBpdId(), dhStep.getStepSort(), dhStep.getStepBusinessKey());
        if (dhStepMapper.listBySelective(stepSelective).size() > 0) {
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
    
    

    
}
