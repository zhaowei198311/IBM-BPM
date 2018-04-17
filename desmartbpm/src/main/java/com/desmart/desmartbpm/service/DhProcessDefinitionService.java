package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;

public interface DhProcessDefinitionService {

    /**
     * 列出指定的流程元数据相关的所有流程定义
     * @param metaUid
     * @return
     */
    ServerResponse listProcessDefinitionsIncludeUnSynchronized(String metaUid, Integer pageNum, Integer pageSize);

    /**
     * 创建流程定义
     * @param proAppId 流程应用库id
     * @param proUid  流程图id
     * @param proVerUid 流程版本id
     * @param request httpRequest
     * @return
     */
    ServerResponse createDhProcessDefinition(String proAppId, String proUid, String proVerUid, HttpServletRequest request);

    ServerResponse isDhProcessDefinitionExist(String proAppId, String proUid, String proVerUid);
}