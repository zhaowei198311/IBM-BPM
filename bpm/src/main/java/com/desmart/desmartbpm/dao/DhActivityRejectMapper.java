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

    DhActivityReject selectByPrimaryKey(String actrUid);

    int updateByPrimaryKeySelective(DhActivityReject record);

    int updateByPrimaryKey(DhActivityReject record);
    
    int insertBatch(List<DhActivityReject> list);
    
    int deleteByActivityId(String activityId);
    
}