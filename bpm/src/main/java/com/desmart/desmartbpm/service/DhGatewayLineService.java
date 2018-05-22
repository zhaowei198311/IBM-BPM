package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;

public interface DhGatewayLineService {
    
    /**
     * 生成网关连接线
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse generateGatewayLine(String proAppId, String proUid, String proVerUid);
    
    /**
     * 判断流程定义是否需要生成网关连接线
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    boolean needGenerateGatewayLine(String proAppId, String proUid, String proVerUid);
}
