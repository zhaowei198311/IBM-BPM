package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhActivityAssign;

@Repository
public interface DhActivityAssignMapper {
    int deleteByPrimaryKey(String actuUid);

    int insert(DhActivityAssign dhActivityAssign);

    DhActivityAssign selectByPrimaryKey(String actuUid);

    int updateByPrimaryKeySelective(DhActivityAssign record);

    int updateByPrimaryKey(DhActivityAssign record);
    
    List<DhActivityAssign> listByDhActivityAssignSelective(DhActivityAssign selective);
    /**
     * 
     * @Title: deleteByActivityIds  
     * @Description: 根据activityIds批量删除  
     * @param @param activityId
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteByActivityIds(List<String> activityIds);
    
    int deleteBySelective(DhActivityAssign dhActivityAssign);
    
    int insertBatch(List<DhActivityAssign> list);
    
    List<DhActivityAssign> listByActivityId(String activityId);
}