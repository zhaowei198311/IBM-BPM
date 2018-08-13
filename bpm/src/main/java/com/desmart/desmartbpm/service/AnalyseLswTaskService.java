package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

import java.util.Map;

public interface AnalyseLswTaskService {


    ServerResponse analyseLswTask(LswTask lswTask, Map<Integer, String> groupInfo, BpmGlobalConfig globalConfig);

}