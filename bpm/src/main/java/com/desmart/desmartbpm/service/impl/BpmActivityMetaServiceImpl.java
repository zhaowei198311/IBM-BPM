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
import org.springframework.util.CollectionUtils;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.github.pagehelper.PageHelper;

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
        bpmActivityMeta.setSourceActivityId(bpmActivityMeta.getActivityId()); // 将源环节主键设置为自己的主键
        bpmActivityMeta.setMiOrder(miOrder);
        bpmActivityMeta.setDeepLevel(deepLevel);
        bpmActivityMeta.setProAppId(proAppId);
        return bpmActivityMeta;
    }
    
   
    public ServerResponse<List<Map<String, Object>>> getActivitiyMetasForConfig(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            
        }
        BpmActivityMeta selective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        PageHelper.orderBy("SORT_NUM");
        List<BpmActivityMeta> allMetaList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        
        List<BpmActivityMeta> basicMetaList = filterBasicActivity(allMetaList);
        
        if (basicMetaList.size() == 0) {
            return ServerResponse.createByErrorMessage("没有匹配的环节，请先同步环节");
        }
        List<Map<String, Object>> processList = new ArrayList<>();
        Map<String, Object> mainProcess = new HashMap<>();
        mainProcess.put("name", "主流程环节");
        mainProcess.put("id", "main");
        mainProcess.put("type", "mainProcess");
        List<Map<String, Object>> children = new ArrayList<>();
        List<BpmActivityMeta> subProcessList = new ArrayList<>();
        List<BpmActivityMeta> calledProcessList = new ArrayList<>();
        
        Iterator<BpmActivityMeta> iterator = basicMetaList.iterator();
        while (iterator.hasNext()) {
            BpmActivityMeta meta = iterator.next();
            if ("SubProcess".equalsIgnoreCase(meta.getBpmTaskType())) { // 如果类型是子流程，单独做折叠栏
                subProcessList.add(meta);
                iterator.remove();
                continue;
            } else if ("CalledProcess".equalsIgnoreCase(meta.getBpmTaskType())) {
                calledProcessList.add(meta);
                iterator.remove();
                continue;
            }else if ("UserTask".equalsIgnoreCase(meta.getBpmTaskType())) {
                if (meta.getDeepLevel() == 0) {
                    // 如果是人工节点，并且是主流程下的人工节点，就加入主流程折叠栏
                    Map<String, Object> map = new HashMap<>();
                    map.put("activityName", meta.getActivityName());
                    map.put("actcUid", meta.getDhActivityConf().getActcUid());
                    map.put("activityId", meta.getActivityId());
                    map.put("activityBpdId", meta.getActivityBpdId());
                    map.put("parentActivityId", meta.getParentActivityId());
                    children.add(map);
                    iterator.remove();
                }
            } else { // 去除非人工节点，非子流程环节的无关元素
                iterator.remove();
            }
        } // 至此集合中还剩余内连子流程下的内容
        
        mainProcess.put("children", children);
        processList.add(mainProcess); // 主流程环节装配完毕
        
        for (BpmActivityMeta calledProcessNode : calledProcessList) {
            Map<String, Object> calledProcess = new HashMap<>();
            calledProcess.put("name", calledProcessNode.getActivityName());
            calledProcess.put("id", calledProcessNode.getActivityId());
            calledProcess.put("type", "calledProcess");
            calledProcess.put("children", new ArrayList());
            processList.add(calledProcess);
        }
        
        for (BpmActivityMeta subProcessNode : subProcessList) {
            Map<String, Object> subProcess = new HashMap<>();
            children = new ArrayList<>();
            String activityBpdId = subProcessNode.getActivityBpdId();
            String activityName = subProcessNode.getActivityName();
            subProcess.put("name", activityName);
            subProcess.put("id", subProcessNode.getActivityId());
            subProcess.put("type", "subProcess");
            iterator = basicMetaList.iterator();
            while (iterator.hasNext()) {
                BpmActivityMeta item = iterator.next();
                if (subProcessNode.getActivityId().equals(item.getParentActivityId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("activityName", item.getActivityName());
                    map.put("actcUid", item.getDhActivityConf().getActcUid());
                    map.put("activityId", item.getActivityId());
                    map.put("activityBpdId", item.getActivityBpdId());
                    map.put("parentActivityId", item.getParentActivityId());
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
        humanMetaList = this.filterBasicActivity(humanMetaList);
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
    
    /**/
     public List<BpmActivityMeta> getNextToActivity(BpmActivityMeta bpmActivityMeta) {
     
        String activityTo = bpmActivityMeta.getActivityTo();
        List<BpmActivityMeta> normal = new ArrayList<>();
        
        if (StringUtils.isBlank(activityTo)) {
            return normal;
        }
        String[] activityBpdIds = activityTo.split(",");
        List<BpmActivityMeta> bpmActivityMetas = new ArrayList<>();
       
        for(int i = 0; i < activityBpdIds.length; ++i) {
            String activityBpdId = activityBpdIds[i];
            if (StringUtils.isNotBlank(activityBpdId)) {
                // 获取当前快照版本的这个元素
                bpmActivityMetas.addAll(getBpmActivityMeta(activityBpdId, bpmActivityMeta.getSnapshotId(), bpmActivityMeta.getBpdId()));
            }
        }
        // 遍历与当前节点直接关联的元素
        Iterator<BpmActivityMeta> iterator = bpmActivityMetas.iterator();
       
        while(iterator.hasNext()) {
            BpmActivityMeta activityMeta = iterator.next();
            String type = activityMeta.getType();
            String activityType = activityMeta.getActivityType();
            String bpmTaskType = activityMeta.getBpmTaskType();
            if ("activity".equals(activityType) && "UserTask".equals(bpmTaskType) && "activity".equals(type)) {
                normal.add(activityMeta);
            } else {
                // 递归调用自身
                normal.addAll(getNextToActivity(activityMeta));
            }
        }

        return normal;
       
    }

    @Override
    public List<BpmActivityMeta> getBpmActivityMeta(String activityBpdId, String snapshotId, String bpdId){
        BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setActivityBpdId(activityBpdId);
        metaSelective.setSnapshotId(snapshotId);
        metaSelective.setBpdId(bpdId);
        return bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
    }

    @Override
    public BpmActivityMeta getByActBpdIdAndParentActIdAndProVerUid(String activityBpdId, String parentActivityId, String proVerUid) {
        BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setActivityBpdId(activityBpdId);
        metaSelective.setParentActivityId(parentActivityId);
        metaSelective.setSnapshotId(proVerUid);
        List<BpmActivityMeta> bpmActivityMetas = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        if (bpmActivityMetas.isEmpty()) {
            return null;
        }
        return bpmActivityMetas.get(0);
    }

    /**
     * 根据代表子流程的元素，获得这个子流程的启动事件元素
     * @return
     */
    @Override
    public BpmActivityMeta getStartMetaOfSubProcess(BpmActivityMeta subProcessNode) {
        BpmActivityMeta selective = new BpmActivityMeta();
        selective.setParentActivityId(subProcessNode.getActivityId());
        selective.setActivityType("start");
        selective.setType("event");
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public BpmActivityMeta getFirstUserTaskMetaOfSubProcess(BpmActivityMeta subProcessNode) {
        BpmActivityMeta startNode = getStartMetaOfSubProcess(subProcessNode);
        return this.getByActBpdIdAndParentActIdAndProVerUid(startNode.getActivityTo(), subProcessNode.getActivityId(),
                startNode.getSnapshotId());
    }


    @Override
    public BpmActivityMeta getStartMetaOfMainProcess(String proAppId, String proUid, String proVerUid) {
        BpmActivityMeta selective = new BpmActivityMeta();
        selective.setProAppId(proAppId);
        selective.setBpdId(proUid);
        selective.setSnapshotId(proVerUid);
        selective.setActivityType("start");
        selective.setType("event");
        selective.setParentActivityId("0");
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public BpmActivityMeta getFirstUserTaskMetaOfMainProcess(String proAppId, String proUid, String proVerUid) {
        BpmActivityMeta startNode = getStartMetaOfMainProcess(proAppId, proUid, proVerUid);
        if (startNode == null) return null;
        String activityBpdId = startNode.getActivityTo();
        BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setParentActivityId("0");
        metaSelective.setSnapshotId(proVerUid);
        metaSelective.setBpdId(proUid);
        metaSelective.setActivityBpdId(activityBpdId);
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }


    /**
     * 获得指定元素的父元素
     * @param bpmActivityMeta
     * @return
     */
    private BpmActivityMeta getParentBpmActivityMeta(BpmActivityMeta bpmActivityMeta) {
        String parentActivityId = bpmActivityMeta.getParentActivityId();
        if ("0".equals(parentActivityId)) {
            return null;
        } else {
            return bpmActivityMetaMapper.queryByPrimaryKey(parentActivityId);
        }
    }


	@Override
	public BpmActivityMeta queryByPrimaryKey(String activityId) {
		// TODO Auto-generated method stub
		return bpmActivityMetaMapper.queryByPrimaryKey(activityId);
	}


	@Override
	public BpmActivityMeta getBpmActivityMeta(String proAppId, String activityBpdId, String snapshotId, String bpdId) {
		// TODO Auto-generated method stub
		BpmActivityMeta metaSelective = new BpmActivityMeta();
		metaSelective.setProAppId(proAppId);
        metaSelective.setActivityBpdId(activityBpdId);
        metaSelective.setSnapshotId(snapshotId);
        metaSelective.setBpdId(bpdId);
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        if(list!=null && list.size()>0) {
        	return list.get(0);
        }
        return null;
	}


	@Override
	public List<BpmActivityMeta> getBpmActivityMetaByActivityType(String proAppId, String snapshotId, String bpdId,
			String activityType) {
		// TODO Auto-generated method stub
		BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setProAppId(proAppId);
        metaSelective.setSnapshotId(snapshotId);
        metaSelective.setBpdId(bpdId);
        metaSelective.setActivityType(activityType);
        return bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
	}
    
	@Override
    public List<BpmActivityMeta> filterBasicActivity(List<BpmActivityMeta> bpmActivityMetaList) {
        if (bpmActivityMetaList == null || bpmActivityMetaList.size() == 0) {
            return new ArrayList<BpmActivityMeta>();
        }  
        Iterator<BpmActivityMeta> it = bpmActivityMetaList.iterator();
        while (it.hasNext()) {
            BpmActivityMeta item = it.next();
            if (!StringUtils.equals(item.getActivityId(), item.getSourceActivityId())) {
                it.remove();
            }
        }
        return bpmActivityMetaList;
    }

    @Override
    public List<BpmActivityMeta> listAllBpmActivityMeta(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return new ArrayList<>();
        }
        BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setProAppId(proAppId);
        metaSelective.setBpdId(proUid);
        metaSelective.setSnapshotId(proVerUid);
        return bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
    }

    
}