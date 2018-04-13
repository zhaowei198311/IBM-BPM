package com.desmart.desmartbpm.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;

@Service
public class BpmActivityMetaServiceImpl implements BpmActivityMetaService {
	private static final Logger LOG = LoggerFactory.getLogger(BpmActivityMetaServiceImpl.class);
	
	/**
	 * 根据条件创建一个环节配置
	 */
    public BpmActivityMeta getBpmActivityMeta(String activityBpdId, String activityName, String snapshotId, String bpdId,
                                              String type, String activityType, String parentActivityBpdId, String activityTo,
                                              String externalId, String loopType, String bpmTaskType, String bpmProcessSnapshotId,
                                              String miOrder, Integer deepLevel, String proAppId) throws Exception {
        BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
        bpmActivityMeta.setActivityBpdId(activityBpdId);
        bpmActivityMeta.setActivityName(activityName);
        bpmActivityMeta.setBpdId(bpdId);
        bpmActivityMeta.setType(type);
        bpmActivityMeta.setActivityType(activityType);
        bpmActivityMeta.setParentActivityBpdId(parentActivityBpdId);
        bpmActivityMeta.setActivityTo(activityTo);
        bpmActivityMeta.setExternalId(externalId);
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

        bpmActivityMeta.setUpdateTime(new Date());
        bpmActivityMeta.setCreateTime(new Date());
        String employeeNum = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        bpmActivityMeta.setCreator(employeeNum);
        bpmActivityMeta.setUpdateBy(employeeNum);
        bpmActivityMeta.setActivityId(EntityIdPrefix.BPM_ACTIVITY_META + UUID.randomUUID().toString());
        bpmActivityMeta.setMiOrder(miOrder);
        bpmActivityMeta.setDeepLevel(deepLevel);
        bpmActivityMeta.setProAppId(proAppId);
        return bpmActivityMeta;
    }
}