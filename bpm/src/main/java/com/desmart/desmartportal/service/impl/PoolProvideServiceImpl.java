package com.desmart.desmartportal.service.impl;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.ThreadPoolProvideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class PoolProvideServiceImpl implements ThreadPoolProvideService {
    private ExecutorService threadPoolToUpdateRouteResult;
    @Autowired
    private DhRouteService dhRouteService;

    @PostConstruct
    public void init() {
        threadPoolToUpdateRouteResult = Executors.newFixedThreadPool(5);
    }

    @Override
    public ExecutorService getThreadPoolToUpdateRouteResult() {
        return threadPoolToUpdateRouteResult;
    }

    @PreDestroy
    public void destory() {
        threadPoolToUpdateRouteResult.shutdown();
    }

    @Override
    public ServerResponse updateRouteResult(int insId, BpmRoutingData bpmRoutingData) {
        Future<Boolean> future = threadPoolToUpdateRouteResult.submit(new SaveRouteResultCallable(dhRouteService, insId, bpmRoutingData));
        Boolean updateSucess = true;
        try {
            updateSucess = future.get(2, TimeUnit.SECONDS); // 最多等待2秒
        } catch (Exception e) {
            updateSucess = false;
        }
        return updateSucess ? ServerResponse.createBySuccess() : ServerResponse.createByErrorMessage("更新网关决策失败");
    }



}