package com.desmart.desmartbpm.service;

import com.alibaba.fastjson.JSONArray;
import com.desmart.common.constant.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 流程快照服务
 */
public interface BpmProcessSnapshotService {


    /**
     * 同步环节
     * @param request  httpRequest
     * @param bpdId  流程图ID
     * @param snapshotId 引擎中快照版本
     * @param processAppId  流程应用id
     *
     */
    void processModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId);

    /** 获得虚拟节点  */
    JSONArray processVisualModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId);
    

}