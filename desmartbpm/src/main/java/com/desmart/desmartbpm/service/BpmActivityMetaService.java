package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.entity.BpmActivityMeta;

public interface BpmActivityMetaService {
    BpmActivityMeta getBpmActivityMeta(String activityBpdId, String activityName, String snapshotId, String bpdId, String type,
                                       String activityType, String parentActivityBpdId, String activityTo, String externalID,
                                       String loopType, String bpmTaskType, String bpmProcessSnapshotId, String miOrder,
                                       Integer deepLevel, String proAppId) throws Exception;

    List<BpmActivityMeta> listHumanActivity(String proAppId, String proUid, String proVerUid);
}