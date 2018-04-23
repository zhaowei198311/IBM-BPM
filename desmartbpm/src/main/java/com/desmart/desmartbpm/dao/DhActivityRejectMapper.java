package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhActivityReject;

@Repository
public interface DhActivityRejectMapper {
    int deleteByPrimaryKey(String actrUid);

    int insert(DhActivityReject record);

    List<DhActivityReject> listByActivityId(String activityId);

    DhActivityReject selectByPrimaryKey(String actrUid);

    int updateByPrimaryKeySelective(DhActivityReject record);

    int updateByPrimaryKey(DhActivityReject record);
}