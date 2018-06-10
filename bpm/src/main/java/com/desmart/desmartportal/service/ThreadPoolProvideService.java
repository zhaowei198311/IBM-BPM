package com.desmart.desmartportal.service;

import java.util.concurrent.ExecutorService;

/**
 * 提供线程池的接口
 */
public interface ThreadPoolProvideService {

    ExecutorService getThreadPoolToUpdateRouteResult();

}