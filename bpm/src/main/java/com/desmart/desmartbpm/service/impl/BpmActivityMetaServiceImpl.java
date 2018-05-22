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
import com.desmart.desmartbpm.util.DataListUtils;
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
            if ("SubProcess".equalsIgnoreCase(meta.getBpmTaskType())) { // 如果类型是子流程，单独做折叠栏
                subProcessList.add(meta);
                iterator.remove();
                continue;
            }
            if ("UserTask".equalsIgnoreCase(meta.getBpmTaskType())) { // 如果是人工节点，并且是主流程下的人工节点，就加入主流程折叠栏
                if (meta.getDeepLevel() == 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("activityName", meta.getActivityName());
                    map.put("actcUid", meta.getDhActivityConf().getActcUid());
                    map.put("activityId", meta.getActivityId());
                    map.put("activityBpdId", meta.getActivityBpdId());
                    children.add(map);
                    iterator.remove();
                }
            } else { // 去除非人工节点，非子流程环节的无关元素
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

        return DataListUtils.cloneList(normal, BpmActivityMeta.class);
       
    }
    
    public List<BpmActivityMeta> getBpmActivityMeta(String activityBpdId, String snapshotId, String bpdId){
        BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setActivityBpdId(activityBpdId);
        metaSelective.setSnapshotId(snapshotId);
        metaSelective.setBpdId(bpdId);
        return bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
    }
        
    
    public Map<String, Object> getNextToActivity(BpmActivityMeta sourceActivityMeta, String insUid) {
        Map<String, Object> results = new HashMap();
        String activityTo = sourceActivityMeta.getActivityTo();
        List<BpmActivityMeta> end = new ArrayList();
        List<BpmActivityMeta> normal = new ArrayList();
        List<BpmActivityMeta> gateAndData = new ArrayList();
        if (StringUtils.isNotBlank(activityTo)) {
            String[] toActivityBpdIds = activityTo.split(",");
            List<BpmActivityMeta> bpmActivityMetas = new ArrayList<>(); 
            // 遍历处理source节点的直接连接节点
            for (int i=0; i<toActivityBpdIds.length; i++) {
                String activityBpdId = toActivityBpdIds[i];
                if (StringUtils.isNotBlank(activityBpdId)) {
                    // 获取当前快照版本的这个元素
                    bpmActivityMetas.addAll(getBpmActivityMeta(activityBpdId, sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId()));
                }
            }
            Iterator<BpmActivityMeta> iterator = bpmActivityMetas.iterator();
            // 迭代源节点的后续节点
            while (iterator.hasNext()) {
                BpmActivityMeta activityMeta = iterator.next();
                String type = activityMeta.getType();
                String activityType = activityMeta.getActivityType();
                String bpmTaskType = activityMeta.getBpmTaskType();
                String[] tos;
                List<BpmActivityMeta> gateActivityMetas;
                Map subGateData;
                List<BpmActivityMeta> subNormal;
                List<BpmActivityMeta> subGateAnd;
                List<BpmActivityMeta> subEnd;
                if ("activity".equals(activityType) && "ServiceTask".equals(bpmTaskType) && "activity".equals(type)) {
                    // 系统服务
                    tos = activityMeta.getActivityTo().split(",");
                    for (int i=0; i<tos.length; i++) {
                        gateActivityMetas = getBpmActivityMeta(tos[i], sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId());
                        subGateData = this.getNowActivity((BpmActivityMeta)gateActivityMetas.get(0), insUid);
                        gateAndData.addAll((List)subGateData.get("normal"));
                        gateAndData.addAll((List)subGateData.get("gateAnd"));
                        end.addAll((List)subGateData.get("end"));
                    }
                } else if ("activity".equals(activityType) && "UserTask".equals(bpmTaskType) && "activity".equals(type)) {
                    // 人工环节
                    normal.add(activityMeta);
                } else {
                    String parentActivityBpdId;
                    List subMetas;
                    if ("activity".equals(activityType) && "CalledProcess".equals(bpmTaskType) && "activity".equals(type)) {
                        // 外链流程
                        String externalId = activityMeta.getExternalId(); // 外链的流程的bpdId(流程图id)
                        // 跳过这个节点获得它下个节点的信息, 同Service节点
                        tos = activityMeta.getActivityTo().split(",");
                        for (int i=0; i<tos.length; i++) {
                            gateActivityMetas = getBpmActivityMeta(tos[i], sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId());
                            subGateData = this.getNowActivity((BpmActivityMeta)gateActivityMetas.get(0), insUid);
                            gateAndData.addAll((List)subGateData.get("normal"));
                            gateAndData.addAll((List)subGateData.get("gateAnd"));
                            end.addAll((List)subGateData.get("end"));
                        }
                    } else {
                        if ("activity".equals(activityType) && "SubProcess".equals(bpmTaskType) && "activity".equals(type)) {
                            // 内链子流程
                            // 找到子流程的开始节点
                            BpmActivityMeta startMetaOfSubProcess = getStartMetaOfSubProcess(activityMeta);
                            // 获得开始节点相连的节点id，并处理
                            String[] toArr = startMetaOfSubProcess.getActivityTo().split(",");
                            for (int i=0; i<toArr.length; i++) {
                                String activityBpdId = toArr[i];
                                subGateAnd = getBpmActivityMeta(activityBpdId, sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId());
                                Map<String, Object> subData = getNowActivity((BpmActivityMeta)subGateAnd.get(0), insUid);
                                normal.addAll((List)subData.get("normal"));
                                gateAndData.addAll((List)subData.get("gateAnd"));
                                end.addAll((List)subData.get("end"));
                            }
                            
                        } else if (!"event".equals(type)) {
                            if ("gateway".equals(type)) {
                                if ("gatewayAnd".equals(activityType)) {
                                    // 并行网关
                                    tos = activityMeta.getActivityTo().split(",");
                                    for (int i=0; i<tos.length; i++) {
                                        gateActivityMetas = getBpmActivityMeta(tos[i], sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId());
                                        subGateData = this.getNowActivity((BpmActivityMeta)gateActivityMetas.get(0), insUid);
                                        gateAndData.addAll((List)subGateData.get("normal"));
                                        gateAndData.addAll((List)subGateData.get("gateAnd"));
                                        end.addAll((List)subGateData.get("end"));
                                    }
                                    
                                } else if ("gatewayOr".equals(activityType)) {
                                    // 包容网关
                                    tos = activityMeta.getActivityTo().split(",");
                                    for (int i=0; i<tos.length; i++) {
                                        gateActivityMetas = getBpmActivityMeta(tos[i], sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId());
                                        subGateData = this.getNowActivity((BpmActivityMeta)gateActivityMetas.get(0), insUid);
                                        normal.addAll((List)subGateData.get("normal"));
                                        gateAndData.addAll((List)subGateData.get("gateAnd"));
                                        end.addAll((List)subGateData.get("end"));
                                    }
                                    
                                } else if ("gateway".equals(activityType)) {
                                    // 排他网关
                                    tos = activityMeta.getActivityTo().split(",");
                                    for (int i=0; i<tos.length; i++) {
                                        gateActivityMetas = getBpmActivityMeta(tos[i], sourceActivityMeta.getSnapshotId(), sourceActivityMeta.getBpdId());
                                        subGateData = this.getNowActivity((BpmActivityMeta)gateActivityMetas.get(0), insUid);
                                        normal.addAll((List)subGateData.get("normal"));
                                        gateAndData.addAll((List)subGateData.get("gateAnd"));
                                        end.addAll((List)subGateData.get("end"));
                                    }
                                    
                                }
                            }
                        } else if ("end".equals(activityType) && !activityMeta.getParentActivityBpdId().equals("0")) {
                            // 如果是结束事件，但不是主流程的结束事件
                            // 找到代表子流程的那个元素
                            end.add(activityMeta);
                            BpmActivityMeta nodeIdentifySubProcess = getParentBpmActivityMeta(activityMeta);
                            if (nodeIdentifySubProcess != null) {
                                String actToStr = nodeIdentifySubProcess.getActivityTo();
                                if (StringUtils.isNotBlank(actToStr)) {
                                    String[] actos = actToStr.split(",");
                                    for (String acto : actos) {
                                        List<BpmActivityMeta> metas = getBpmActivityMeta(acto, nodeIdentifySubProcess.getSnapshotId(), nodeIdentifySubProcess.getBpdId());
                                        Map<String, Object> subEndData = this.getNowActivity(metas.get(0), insUid);
                                        normal.addAll((List)subEndData.get("normal"));
                                        gateAndData.addAll((List)subEndData.get("gateAnd"));
                                        end.addAll((List)subEndData.get("end"));
                                    }
                                }
                            }
                            
                        } else if ("end".equals(activityType) && activityMeta.getParentActivityBpdId().equals("0")) {
                            // 如果是主流程的结束事件
                            end.add(activityMeta);
                        }
                    }
                }
                
            }
        
        }
        
//        List<BpmActivityMeta> normal = this.getNextToPerson((List)normal, (Map)documentData, documentId);
//        List<BpmActivityMeta> gateAndData = this.getNextToPerson((List)gateAndData, (Map)documentData, documentId);
        results.put("normal", normal);
        results.put("gateAnd", gateAndData);
        results.put("end", end);
        return results;
    }

    
    // 处理与开始节点相连的某个节点（被循环调用）
    private Map<String, Object> getNowActivity(BpmActivityMeta bpmActivityMeta, String insUid) {
        Map<String, Object> results = new HashMap<>();
        List<BpmActivityMeta> normal = new ArrayList<>();
        List<BpmActivityMeta> gateAndData = new ArrayList<>();
        List<BpmActivityMeta> end = new ArrayList<>();
        
        String type = bpmActivityMeta.getType();
        String activityType = bpmActivityMeta.getActivityType();
        String bpmTaskType = bpmActivityMeta.getBpmTaskType();
        if ("activity".equals(activityType) && "UserTask".equals(bpmTaskType) && "activity".equals(type)) {
            // 如果此环节是人工任务
            normal.add(bpmActivityMeta);
        } else {
            Map unNormal;
            List<BpmActivityMeta> subNormal;
            List<BpmActivityMeta> subActivityMeta;
            if ("gateway".equals(type) && "gatewayAnd".equals(activityType)) {
                // 如果此环节是并行网关
                unNormal = this.getNextToActivity(bpmActivityMeta, insUid);
                subNormal = (List)unNormal.get("normal");
                gateAndData.addAll(subNormal);
                subActivityMeta = (List)unNormal.get("gateAnd");
                gateAndData.addAll(subActivityMeta);
            } else {
                List subGateEnd;
                String parentActivityBpdId;
                String subStartsql;
                if ("activity".equals(activityType) && "SubProcess".equals(bpmTaskType) && "activity".equals(type)) {
                    // 如果此环节是子流程
                    BpmActivityMeta startMetaOfSubProcess = getStartMetaOfSubProcess(bpmActivityMeta);
                    String activityTos = startMetaOfSubProcess.getActivityTo();
                    if (StringUtils.isNotBlank(activityTos)) {
                        String[] tos = activityTos.split(",");
                        for (int i=0; i<tos.length; i++) {
                            String acrivityBpdId = tos[i];
                            List<BpmActivityMeta> subActivityMetas = getBpmActivityMeta(acrivityBpdId, startMetaOfSubProcess.getSnapshotId(), startMetaOfSubProcess.getBpdId());
                            Map<String, Object> subData = this.getNowActivity(subActivityMetas.get(0), insUid);
                            normal.addAll((List)subData.get("normal"));
                            gateAndData.addAll((List)subData.get("gateAnd"));
                            end.addAll((List)subData.get("end"));
                        }
                    }
                } else {
                    if ("event".equals(type) && "end".equals(activityType) && !bpmActivityMeta.getParentActivityBpdId().equals("0")) {
                        // 如果此环节是结束事件，但不是主流程的结束事件
                        parentActivityBpdId = bpmActivityMeta.getParentActivityBpdId();
                        end.add(bpmActivityMeta);
                        // 找到此元素的父元素,即在上一层中代表这个子流程的元素
                        BpmActivityMeta parentBpmActivityMeta = getParentBpmActivityMeta(bpmActivityMeta);
                        if (parentBpmActivityMeta != null) {
                            String[] tos = parentBpmActivityMeta.getActivityTo().split(",");
                            for (int i=0; i<tos.length; i++) {
                                String activityBpdId = tos[i];
                                subNormal = this.getBpmActivityMeta(activityBpdId, bpmActivityMeta.getSnapshotId(), bpmActivityMeta.getBpdId());
                                Map<String, Object> subEndData = getNowActivity((BpmActivityMeta)subNormal.get(0), insUid);
                                normal.addAll((List)subEndData.get("normal"));
                                gateAndData.addAll((List)subEndData.get("gateAnd"));
                                end.addAll((List)subEndData.get("end"));
                            }
                        }
                    } else {
                        unNormal = this.getNextToActivity(bpmActivityMeta, insUid);
                        subNormal = (List)unNormal.get("normal");
                        normal.addAll(subNormal);
                        subActivityMeta = (List)unNormal.get("gateAnd");
                        gateAndData.addAll(subActivityMeta);
                        subGateEnd = (List)unNormal.get("end");
                        end.addAll(subGateEnd);
                    }
                    
                }
            }
            
        }
        results.put("normal", normal);
        results.put("end", end);
        results.put("gateAnd", gateAndData);
        return results;
    }
    
    /**
     * 根据代表内连子流程的元素，获得这个子流程的启动事件元素
     * @return
     */
    private BpmActivityMeta getStartMetaOfSubProcess(BpmActivityMeta bpmActivityMeta) {
        BpmActivityMeta selective = new BpmActivityMeta();
        selective.setParentActivityBpdId(bpmActivityMeta.getActivityBpdId());
        selective.setProAppId(bpmActivityMeta.getProAppId());
        selective.setBpdId(bpmActivityMeta.getBpdId());
        selective.setSnapshotId(bpmActivityMeta.getSnapshotId());
        selective.setActivityType("start");
        selective.setType("event");
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
    
    /**
     * 获得指定元素的父元素，例子：主流程上有个元素A代表一个子流程，通过这个子流程中的一个元素找到A
     * @param bpmActivityMeta
     * @return
     */
    private BpmActivityMeta getParentBpmActivityMeta(BpmActivityMeta bpmActivityMeta) {
        BpmActivityMeta selective = new BpmActivityMeta();
        selective.setActivityBpdId(bpmActivityMeta.getParentActivityBpdId());
        selective.setProAppId(bpmActivityMeta.getProAppId());
        selective.setBpdId(bpmActivityMeta.getBpdId());
        selective.setSnapshotId(bpmActivityMeta.getSnapshotId());
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
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
	public List<BpmActivityMeta> getBpmActivityMetaByActivityType(String activityBpdId, String snapshotId, String bpdId,
			String activityType) {
		// TODO Auto-generated method stub
		BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setActivityBpdId(activityBpdId);
        metaSelective.setSnapshotId(snapshotId);
        metaSelective.setBpdId(bpdId);
        metaSelective.setActivityType(activityType);
        return bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
	}
    
    

    
}