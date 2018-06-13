package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhStep;

public interface DhStepService {
    
    /**
     * 创建步骤
     * @param dhStep
     * @return
     */
    ServerResponse create(DhStep dhStep);
    
    /**
     * 列出DhActivityConf对应的所有步骤
     * @param conf
     * @return
     */
    ServerResponse<List<DhStep>> getStepOfDhActivityConf(DhActivityConf conf);
    
    
    /**
     * 更新步骤
     * @param dhStep
     * @return
     */
    ServerResponse updateStep(DhStep dhStep);
    
    /**
     * 删除步骤
     * @param stepUid
     * @return
     */
    ServerResponse deleteDhStep(String stepUid);
    
    /**
     * 排序步骤
     * @param stepUid
     * @param resortType  increase, reduce
     * @return
     */
    ServerResponse resortStep(String stepUid, String resortType);
    
    /**
     * 找到指定步骤的上一个步骤
     * @param dhStep
     * @return
     */
    DhStep getPreStepOfCurrStep(DhStep dhStep); 
    
    /**
     * 找到指定步骤的下一个步骤
     * @param dhStep
     * @return
     */
    DhStep getNextStepOfCurrStep(DhStep dhStep);
    
    /**
     * 根据step关键字，获得指定环节的步骤，按步骤序号正序排序
     * @param bpmActivityMeta
     * @param stepBusinessKey
     * @return
     */
    List<DhStep> getStepsOfBpmActivityMetaByStepBusinessKey(BpmActivityMeta bpmActivityMeta, String stepBusinessKey);
    /**
     * 为该流程所有环节新增表单步骤，同关键字将修改表单uid
     * @param dhStep
     * @return
     */
	ServerResponse createStepToAll(DhStep dhStep);
	/**
	 * 根据条件查询所有步骤
	 */
	ServerResponse<List<DhStep>> getStepInfoByCondition(DhStep dhStep);

    /**
     * 从步骤列表中找出第一个表单步骤
     * @param stepList
     * @return
     */
    DhStep getFirstFormStepOfStepList(List<DhStep> stepList);

    /**
     * 找到指定环节对应的表单步骤，如果提供的关键字没有对应表单步骤，进一步查询默认关键字有没有表单步骤
     * @param currTaskNode
     * @param stepBusinessKey
     * @return
     */
    DhStep getFormStepOfTaskNode(BpmActivityMeta currTaskNode, String stepBusinessKey);


}    
