package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.enetity.BpmGlobalConfig;

/**
 * 全局配置服务
 */
public interface BpmGlobalConfigService {
    /**
     * 获得一个启用的全局配置
     */
    BpmGlobalConfig getFirstActConfig();
}