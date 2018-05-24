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
    /**
     * 根据条件查询网关连接线集合,按照is_delfault排序，默认线路放在最后
     * @param dhGatewayLine
     * @return
     */
    List<DhGatewayLine> getGateWayLinesByCondition(DhGatewayLine dhGatewayLine);
}