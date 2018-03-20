package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.enetity.BpmActivityMeta;

public interface BpmActivityMetaService {
    BpmActivityMeta getBpmActivityMeta(String activityBpdId, String activityName, String snapshotId, String bpdId, String type,
                                       String activityType, String parentActivityBpdId, String activityTo, String externalID,
                                       String loopType, String bpmTaskType, String bpmProcessSnapshotId, String miOrder,
                                       Integer deepLevel) throws Exception;


}