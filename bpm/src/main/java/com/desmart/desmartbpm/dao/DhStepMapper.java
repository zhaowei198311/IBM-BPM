package com.desmart.desmartbpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
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
     * 获得流程定义相关的步骤(带activity_id)
     * @return
     */
    List<DhStep> listStepsOfProcessDefinition(@Param("proAppId") String proAppId,
                                              @Param("proUid") String proUid,
                                              @Param("proVerUid") String proVerUid);
    
    /**
     * 获得特定环节的最大的步骤序号
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
    /**
     * 
     * @Title: deleteBySelective  
     * @Description: 根据对象DH_STEP条件进行删除  
     * @param @param dhStep
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteBySelective(DhStep dhStep);

    /**
     * 批量新增
     * @param insertList
     * @return
     */
    Integer insertBatchDhStep(@Param("insertList")List<DhStep> insertList);
    /**
     * 批量修改
     * @param updateList
     * @return
     */
    Integer updateBatchDhStep(@Param("updateList")List<DhStep> updateList);
    
    /**
     * 
     * @Title: queryDhStepByProUidAndProAppId  
     * @Description: 根据proUid和proAppId查询且去掉stepObjectUid重复字段  
     * @param dhStep
     * @return  
     * @return List<DhStep>
     */
    List<DhStep> queryDhStepByProUidAndProAppId(DhStep dhStep);

    /**
     * 根据步骤id主键，批量删除
     * @param stepUidList
     * @return
     */
    int removeByStepUidList(List<String> stepUidList);
    
    /**
     * 根据表单id获得绑定该表单的所有步骤集合
     * @param formUid
     * @return
     */
    List<DhStep> queryStepListByFormUid(String formUid);
}