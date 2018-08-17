package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

import java.util.List;
import java.util.Map;

/**
 * 分析一个引擎中的任务，将它转换为平台的任务
 */
public interface AnalyseLswTaskService {

    ServerResponse analyseLswTask(LswTask lswTask, Map<Integer, List<String>> groupInfo, BpmGlobalConfig globalConfig);

}