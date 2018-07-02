package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface DhGatewayLineService {
    
    /**
     * 生成网关连接线
     * @param dhProcessDefinition
     * @return
     */
    ServerResponse generateGatewayLine(DhProcessDefinition dhProcessDefinition);
    
    /**
     * 判断是否需要为流程定义生成网关连接线记录
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    boolean needGenerateGatewayLine(String proAppId, String proUid, String proVerUid);

    /**
     * 根据条件查询网关连接线集合,按照is_delfault、rule_id排序，默认线路放在最后
     * @param dhGatewayLine
     * @return
     */
    List<DhGatewayLine> getGateWayLinesByCondition(DhGatewayLine dhGatewayLine);
}
