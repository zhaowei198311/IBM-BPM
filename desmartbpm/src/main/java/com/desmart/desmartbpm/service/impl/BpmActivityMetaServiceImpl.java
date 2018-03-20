package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class BpmActivityMetaServiceImpl implements BpmActivityMetaService {

    public BpmActivityMeta getBpmActivityMeta(String activityBpdId, String activityName, String snapshotId, String bpdId,
                                              String type, String activityType, String parentActivityBpdId, String activityTo,
                                              String externalID, String loopType, String bpmTaskType, String bpmProcessSnapshotId,
                                              String miOrder, Integer deepLevel) throws Exception {
        BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
        bpmActivityMeta.setActivityBpdId(activityBpdId);
        bpmActivityMeta.setActivityName(activityName);
        bpmActivityMeta.setBpdId(bpdId);
        bpmActivityMeta.setType(type);
        bpmActivityMeta.setActivityType(activityType);
        bpmActivityMeta.setParentActivityBpdId(parentActivityBpdId);
        bpmActivityMeta.setActivityTo(activityTo);
        bpmActivityMeta.setExternalID(externalID);
        bpmActivityMeta.setLoopType(loopType);
        if (StringUtils.isBlank(loopType)) {
            bpmActivityMeta.setHandleSignType("alone");
        } else if ("none".equalsIgnoreCase(loopType)) {
            bpmActivityMeta.setHandleSignType("alone");
        } else if (!"simpleLoop".equalsIgnoreCase(loopType) && !"MultiInstanceLoop".equalsIgnoreCase(loopType)) {
            bpmActivityMeta.setHandleSignType("alone");
        } else {
            bpmActivityMeta.setHandleSignType("count");
        }

        bpmActivityMeta.setBpmTaskType(bpmTaskType);
        bpmActivityMeta.setSnapshotUid(bpmProcessSnapshotId);
        if (StringUtils.isNotBlank(snapshotId)) {
            bpmActivityMeta.setSnapshotId(snapshotId);
        }

        bpmActivityMeta.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        bpmActivityMeta.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String employeeNum = (String) SecurityUtils.getSubject().getSession().getAttribute("_currUserNum");
        bpmActivityMeta.setCreator(employeeNum);
        bpmActivityMeta.setUpdateBy(employeeNum);
        bpmActivityMeta.setActivityId("bpm_actymeta:" + UUID.randomUUID().toString());
        bpmActivityMeta.setMiOrder(miOrder);
        bpmActivityMeta.setDeepLevel(deepLevel);
        return bpmActivityMeta;
    }
}