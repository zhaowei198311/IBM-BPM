package com.desmart.desmartportal.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhGatewayRouteResult;

@Repository
public interface DhGatewayRouteResultMapper {
    
    int save(DhGatewayRouteResult dhGatewayRouteResult);
    
    /** 根据流程实例编号和网关元素id更新结果  */
    int updateRouteResultByInsIdAndActivityBpdId(DhGatewayRouteResult dhGatewayRouteResult);
    
    /** 根据流程实例编号和网关元素id查询记录 */
    DhGatewayRouteResult queryByInsIdAndActivityBpdId(@Param("insId")Integer insId, @Param("activityBpdId")String activityBpdId);
    
    /** 根据流程实例编号和网关元素id删除指定记录  */
    int deleteByInsIdAndActivityBpdId(@Param("insId")Integer insId, @Param("activityBpdId")String activityBpdId);
}
