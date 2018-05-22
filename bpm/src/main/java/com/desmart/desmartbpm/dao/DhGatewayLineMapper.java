package com.desmart.desmartbpm.dao;

import java.util.List;

import com.desmart.desmartbpm.entity.DhGatewayLine;

public interface DhGatewayLineMapper {
    int deleteByPrimaryKey(String gatewayLineUid);

    int insert(DhGatewayLine record);

    int insertSelective(DhGatewayLine record);

    DhGatewayLine selectByPrimaryKey(String gatewayLineUid);

    int updateByPrimaryKeySelective(DhGatewayLine record);

    int updateByPrimaryKey(DhGatewayLine record);
    
    int insertBatch(List<DhGatewayLine> list);
    
    /**
     * 列出指定排他网关的连接线
     * @param activityId
     * @return
     */
    int countByActivityId(String activityId);
}