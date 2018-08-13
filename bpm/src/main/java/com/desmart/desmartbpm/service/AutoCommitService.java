package com.desmart.desmartbpm.service;

/**
 * 提交处理人重复处理的任务
 */
public interface AutoCommitService {

    /**
     * 自动提交处理人重复的任务
     */
    void startAutoCommit();
}