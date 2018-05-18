package com.desmart.desmartbpm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhStep;

/**
 * 步骤持久层
 */
@Repository
public interface DhStepMapper {
    int deleteByPrimaryKey(String stepUid);

    int insert(DhStep record);

    int insertSelective(DhStep record);

    DhStep selectByPrimaryKey(String stepUid);

    int updateByPrimaryKeySelective(DhStep record);

    int updateByPrimaryKey(DhStep record);
    
    List<DhStep> listBySelective(DhStep selective);

    
    /**
     * 获得最大的步骤序号
     * @param dhStep
     * @return
     */
    int getMaxStepSort(DhStep dhStep);
    
    /**
     * 更新后续环节的序号-1
     * @param dhStep
     * @return
     */
    int updateStepSortOfRelationStep(DhStep dhStep);

    /**
     * 
     * @Title: listByIds  
     * @Description: 通过proUId,proVerUid,proAppId,activityBpdIds查找  
     * @param: @param ids
     * @param: @return  
     * @return: List<DhStep>
     * @throws
     */
    List<DhStep> listByIds(Map<String, Object> ids);
    /**
     * 
     * @Title: deleteByIds  
     * @Description: 拷贝前先删除新旧流程相同节点中新流程节点元素  
     * @param: @param ids
     * @param: @return  
     * @return: int
     * @throws
     */
    int deleteByIds(Map<String, Object> deleteIds);

}