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
     * 同步环节, 生成平台的环节信息
     * @param request  httpRequest
     * @param bpdId  流程图ID
     * @param snapshotId 引擎中快照版本
     * @param processAppId  流程应用id
     *
     */
    void processModel(String bpdId, String snapshotId, String processAppId);

    /**
     * 获得 VisualModel 中的 items数组
     * @param request
     * @param bpdId
     * @param snapshotId
     * @param processAppId
     * @return
     */
    JSONArray processVisualModel(String bpdId, String snapshotId, String processAppId);
    

}