package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhActivityReject;

@Repository
public interface DhActivityRejectMapper {
    int deleteByPrimaryKey(String actrUid);

    int insert(DhActivityReject record);

    /**
     * 列出指定活动环节可以驳回的环节（包含环节名）
     * @param activityId
     * @return
     */
    List<DhActivityReject> listByActivityId(String activityId);

    /**
     * 列出指定的所有活动环节可以驳回的环节
     * @param activityIdList
     * @return
     */
    List<DhActivityReject> listByActivityIdList(List<String> activityIdList);

    DhActivityReject selectByPrimaryKey(String actrUid);

    int updateByPrimaryKeySelective(DhActivityReject record);

    int updateByPrimaryKey(DhActivityReject record);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int insertBatch(List<DhActivityReject> list);
    
    int deleteByActivityId(String activityId);
    /**
     * 
     * @Title: deleteByActivityIds  
     * @Description: 根据activityIds批量删除  
     * @param @param activityIds
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteByActivityIds(List<String> activityIds);


}