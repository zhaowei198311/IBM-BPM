package com.desmart.desmartportal.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.BpmRoutingData;

import java.util.concurrent.ExecutorService;

/**
 * 提供线程池的接口
 */
public interface ThreadPoolProvideService {

    ExecutorService getThreadPoolToUpdateRouteResult();

    /**
     * 异步更新网关决策表的方法不受事务影响
     * @param insId
     * @param bpmRoutingData
     * @return
     */
    ServerResponse updateRouteResult(int insId, BpmRoutingData bpmRoutingData);

}