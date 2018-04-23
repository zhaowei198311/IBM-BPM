package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.DhActivityReject;

public interface DhActivityRejectMapper {
    int deleteByPrimaryKey(String actrUid);

    int insert(DhActivityReject record);

    List<DhActivityReject> listByActivityId(String activityId);

    DhActivityReject selectByPrimaryKey(String actrUid);

    int updateByPrimaryKeySelective(DhActivityReject record);

    int updateByPrimaryKey(DhActivityReject record);
}