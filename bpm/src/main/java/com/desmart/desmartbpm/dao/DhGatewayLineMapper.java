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
     * 根据条件查询网关连接线集合,按照is_delfault、rule_id排序，默认线路放在最后
     * @param dhGatewayLine
     * @return
     */
    List<DhGatewayLine> getGateWayLinesByCondition(DhGatewayLine dhGatewayLine);
    
    /**
     * 
     * @Title: deleteByActivityIds  
     * @Description: 根据activityIds删除  
     * @param @param activityIds
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteByActivityIds(List<String> activityIds);
    
    /**
     * 
     * @Title: listByActivityId  
     * @Description: 根据activityId查询  
     * @param @param activityId
     * @param @return  
     * @return List<DhGatewayLine>  
     * @throws
     */
    List<DhGatewayLine> listByActivityId(String activityId);
    /**
     * 
     * @Title: listByActivityIds  
     * @Description: 根据activityIds批量查询  
     * @param @param activityIds
     * @param @return  
     * @return List<DhGatewayLine>  
     * @throws
     */
    List<DhGatewayLine> listByActivityIds(List<String> activityIds);
}