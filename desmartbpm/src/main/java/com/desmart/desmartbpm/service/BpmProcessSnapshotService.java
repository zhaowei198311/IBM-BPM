package com.desmart.desmartbpm.service;

import com.alibaba.fastjson.JSONArray;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 流程快照服务
 */
public interface BpmProcessSnapshotService {

    void startSysncActivityMeta(HttpServletRequest request, List<String> snapshotIds);

    /**
     *
     * @param request  httpRequest
     * @param bpdId  流程图ID
     * @param snapshotId 引擎中快照版本
     * @param processAppId  流程应用id
     * @param bpmProcessSnapshotId  快照版本的id，快照版本的主键
     */
    void processModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId, String bpmProcessSnapshotId);

    /** 获得虚拟节点  */
    JSONArray processVisualModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId);

}