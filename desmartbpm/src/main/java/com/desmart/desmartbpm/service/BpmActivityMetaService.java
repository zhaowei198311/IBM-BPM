package com.desmart.desmartbpm.service;

import java.util.List;
import java.util.Map;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;

public interface BpmActivityMetaService {
    BpmActivityMeta getBpmActivityMeta(String activityBpdId, String activityName, String snapshotId, String bpdId, String type,
                                       String activityType, String parentActivityBpdId, String activityTo, String externalID,
                                       String loopType, String bpmTaskType, String bpmProcessSnapshotId, String miOrder,
                                       Integer deepLevel, String proAppId) throws Exception;

    /**
     * 获得用来生产环节配置层级菜单的信息
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse<List<Map<String, Object>>> getActivitiyMetasForConfig(String proAppId, String proUid, String proVerUid);
}