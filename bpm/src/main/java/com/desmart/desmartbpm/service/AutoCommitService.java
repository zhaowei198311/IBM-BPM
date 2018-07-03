package com.desmart.desmartbpm.service;

/**
 * 支持自动提交功能的服务
 */
public interface AutoCommitService {

    /**
     * 自动提交任务
     */
    void startAutoCommit();
}