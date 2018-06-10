package com.desmart.desmartportal.service.impl;

import com.desmart.desmartportal.service.ThreadPoolProvideService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PoolProvideServiceImpl implements ThreadPoolProvideService {
    private ExecutorService threadPoolToUpdateRouteResult;

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
}