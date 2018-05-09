package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.desmartbpm.entity.DhTaskHandler;

public interface DhTaskHandlerMapper {
    int deleteByPrimaryKey(String handleUid);

    int insert(DhTaskHandler record);

    int insertSelective(DhTaskHandler record);

    DhTaskHandler selectByPrimaryKey(String handleUid);

    int updateByPrimaryKeySelective(DhTaskHandler record);

    int updateByPrimaryKey(DhTaskHandler record);
    
    /**
     * 根据流程实例id和节点id列出所有状态是"on"的记录
     * @param insId 流程实例id
     * @param activityBpdId  元素id
     * @return
     */
    List<DhTaskHandler> listActiveRecordsByInsIdAndActivityBpdId(@Param("insId")Integer insId, @Param("activityBpdId")String activityBpdId);

    int insertBatch(List<DhTaskHandler> list);
}