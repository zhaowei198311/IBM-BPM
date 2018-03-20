package com.desmart.desmartbpm.util;


import com.desmart.desmartbpm.enetity.BpmGlobalConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientConnPoolUtils {
    public HttpClientConnPoolUtils() {
    }

    /**
     * 根据全局配置初始化http连接池
     * @param poolmgr
     * @param bpmcfg
     */
    public static void setHttpPoolCfg(PoolingHttpClientConnectionManager poolmgr, BpmGlobalConfig bpmcfg) {
        Integer maxConnNum = bpmcfg.getHttpMaxConnection();
        maxConnNum = maxConnNum == null ? 200 : Math.abs(maxConnNum);
        poolmgr.setMaxTotal(maxConnNum);
        Integer preRouteConnNum = bpmcfg.getPreRouteMaxConnection();
        preRouteConnNum = preRouteConnNum == null ? 200 : Math.abs(preRouteConnNum);
        poolmgr.setDefaultMaxPerRoute(preRouteConnNum);
        Integer socketTimeout = bpmcfg.getBpmClientTimeout();
        socketTimeout = socketTimeout == null ? '\uea60' : Math.abs(socketTimeout);
        socketTimeout = socketTimeout < 3000 ? 3000 : socketTimeout;
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();
        poolmgr.setDefaultSocketConfig(socketConfig);
    }
}