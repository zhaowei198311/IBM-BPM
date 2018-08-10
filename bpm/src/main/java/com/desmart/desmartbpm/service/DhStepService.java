package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.entity.DhTaskInstance;

public interface DhStepService {
    
    /**
     * 创建步骤
     * @param dhStep
     * @return
     */
    ServerResponse create(DhStep dhStep,String actcUid);
    
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
     * ******* 重要 ****************
     * 根据step关键字，获得指定环节的可用步骤，如果指定关键字没有可用步骤，返回默认关键字的可用步骤，按步骤序号正序排序
     * 如果这个环节这个关键字有form表单，就使用这个这个关键字，如果没有表单，而默认关键字下有表单返回default关键字的步骤
     * @param bpmActivityMeta 环节
     * @param stepBusinessKey  步骤关键字
     * @return
     */
    List<DhStep> getStepsWithFormByBpmActivityMetaAndStepBusinessKey(BpmActivityMeta bpmActivityMeta, String stepBusinessKey);

    /**
     * 根据环节和关键字获得步骤，不关心是否有表单步骤
     * @param bpmActivityMeta
     * @param stepBusinessKey
     * @return
     */
    List<DhStep> getStepsByBpmActivityMetaAndStepBusinessKey(BpmActivityMeta bpmActivityMeta, String stepBusinessKey);

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
     * 从步骤列表中的表单步骤
     * @param stepList
     * @return
     */
    DhStep getFormStepOfStepList(List<DhStep> stepList);

    /**
     * 找到指定环节对应的表单步骤，如果提供的关键字没有对应表单步骤，进一步查询默认关键字有没有表单步骤
     * @param currTaskNode  任务节点
     * @param stepBusinessKey 步骤关键字
     * @return
     */
    DhStep getFormStepOfTaskNode(BpmActivityMeta currTaskNode, String stepBusinessKey);

    /**
     * 执行表单步骤前的步骤<br/>
     * 从这套关键字的第一个步骤开始执行，执行到表单步骤结束
     * @param firstStep
     * @return
     */
    ServerResponse executeStepBeforeFormStep(DhStep firstStep, DhTaskInstance dhTaskInstance);

    /**
     * 获得指定流程定义下的所有步骤
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    List<DhStep> listAllStepsOfProcessDefinition(String proAppId, String proUid, String proVerUid);

    /**
     * 根据步骤id主键集合删除步骤
     * @param stepUidList
     * @return
     */
    int removeByStepUidList(List<String> stepUidList);

    /**
     * 批量插入步骤
     * @param stepList
     * @return
     */
    int insertBatch(List<DhStep> stepList);

}    
