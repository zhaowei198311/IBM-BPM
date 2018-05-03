package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;

@Service
public class BpmActivityMetaServiceImpl implements BpmActivityMetaService {
	private static final Logger LOG = LoggerFactory.getLogger(BpmActivityMetaServiceImpl.class);
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
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
    
   
    public ServerResponse<List<Map<String, Object>>> getActivitiyMetasForConfig(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            
        }
        BpmActivityMeta selective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        List<BpmActivityMeta> allMeta = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (allMeta.size() == 0) {
            return ServerResponse.createByErrorMessage("没有匹配的环节，请先同步环节");
        }
        List<Map<String, Object>> processList = new ArrayList<>();
        Map<String, Object> mainProcess = new HashMap<>();
        mainProcess.put("name", "主流程环节");
        mainProcess.put("id", "main");
        List<Map<String, Object>> children = new ArrayList<>();
        List<BpmActivityMeta> subProcessList = new ArrayList<>();
        
        Iterator<BpmActivityMeta> iterator = allMeta.iterator();
        while (iterator.hasNext()) {
            BpmActivityMeta meta = iterator.next();
            if ("SubProcess".equalsIgnoreCase(meta.getBpmTaskType())) {
                subProcessList.add(meta);
                iterator.remove();
                continue;
            }
            if ("UserTask".equalsIgnoreCase(meta.getBpmTaskType())) {
                if (meta.getDeepLevel() == 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("activityName", meta.getActivityName());
                    map.put("actcUid", meta.getDhActivityConf().getActcUid());
                    map.put("activityId", meta.getActivityId());
                    map.put("activityBpdId", meta.getActivityBpdId());
                    children.add(map);
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }
        mainProcess.put("children", children);
        processList.add(mainProcess); // 主流程环节装配完毕
        
        for (BpmActivityMeta meta : subProcessList) {
            Map<String, Object> subProcess = new HashMap<>();
            children = new ArrayList<>();
            String activityBpdId = meta.getActivityBpdId();
            String activityName = meta.getActivityName();
            subProcess.put("name", activityName);
            subProcess.put("id", meta.getActivityId());
            iterator = allMeta.iterator();
            while (iterator.hasNext()) {
                BpmActivityMeta item = iterator.next();
                if (activityBpdId.equals(item.getParentActivityBpdId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("activityName", item.getActivityName());
                    map.put("actcUid", item.getDhActivityConf().getActcUid());
                    map.put("activityId", item.getActivityId());
                    map.put("activityBpdId", item.getActivityBpdId());
                    children.add(map);
                    iterator.remove();
                }
            }
            subProcess.put("children", children);
            processList.add(subProcess);
        }
        
        return ServerResponse.createBySuccess(processList);
    }
    
    public ServerResponse<List<BpmActivityMeta>> getHumanActivitiesOfDhProcessDefinition(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        BpmActivityMeta selective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        selective.setBpmTaskType("UserTask");
        List<BpmActivityMeta> humanMetaList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        return ServerResponse.createBySuccess(humanMetaList);
    }
    
    public ServerResponse<List<BpmActivityMeta>> getGatewaysOfDhProcessDefinition(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        BpmActivityMeta selective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        selective.setActivityType("gateway");
        List<BpmActivityMeta> getwayMetaList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        return ServerResponse.createBySuccess(getwayMetaList);
    }
    
    
}