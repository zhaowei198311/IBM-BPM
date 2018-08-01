package com.desmart.desmartbpm.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 流程定义升级需要的信息
 */
public class DhDefinitionCopyData {
    private String proAppId; // 应用库id
    private String proUid;  // 图id
    private String oldProVerUid; // 老版本id
    private String newProVerUid; // 新版本id
    private DhProcessDefinition oldDefintion; // 老的流程定义
    private DhProcessDefinition newDefinition; // 新的流程定义
    private List<BpmActivityMeta> oldActivityMetas;  // 老版本的所有环节
    private List<BpmActivityMeta> newActivityMetas;  // 新版本的所有环节
    private String currUserUid;  // 当前用户信息
    private Map<String, String> oldNewUserNodeActIdMap;   // 记录新老版本能对应上的人工环节映射
    private Map<String, String> oldNewGatewayNodeActIdMap;  // 新老网关环节activityId映射关系
    private Map<String, String> oldNewActivityActIdMap; // 新老版本所有能匹配的节点的主键映射关系
    private Map<String, String> oldNewFormUidMap;  // 新老表单主键映射关系
    private Map<String, String> oldNewFieldUidMap; // 新老字段主键映射关系
    private Map<String, String> oldNewStepUidMap;  // 新老步骤主键映射关系
    private Map<String, String> oldNewConfUidMap;  // 新老版本环节配置的对应关系

    private Map<String, BpmActivityMeta> newSourceActivityActIdMap;    // 主键查找新版节点
    private Map<String, BpmActivityMeta> newSourceActivityActBpdIdMap; // activity_bpd_id查找新版节点
    private Map<String, BpmActivityMeta> oldSourceActivityActIdMap;    // 主键查找老版节点
    private Map<String, BpmActivityMeta> oldSourceActivityActBpdIdMap; // activity_bpd_id查找老版节点
    private List<String> matchedUserNodeActBpdIds;  // 新老版本匹配的人员环节的activity_bpd_id
    private List<String> oldMatchedUserNodeActIds;  // 新老版本匹配的人员环节的activity_id

    private List<String> actcUidSource;
    private List<String> actcUidBeReplace;
    private List<String> interfaceTriggerUids; // 记录数据库中所有接口类型的触发器主键集合
    private List<String> oldTriggerInterfaceStepUids;  // 老版本中是接口触发器类型的步骤

    public DhDefinitionCopyData(){}


    public DhDefinitionCopyData(String proAppId, String proUid, String oldProVerUid, String newProVerUid) {
        this.proAppId = proAppId;
        this.proUid = proUid;
        this.oldProVerUid = oldProVerUid;
        this.newProVerUid = newProVerUid;
    }

    /**
     * 生成新老版本任务环节和网关环节的映射关系
     */
    public void checkRelationShipAboutActivity() {
        Map<String, BpmActivityMeta> newUserTaskMap = new HashMap<>(); // 是源环节且是人工环节 键是activityBpdId
        Map<String, BpmActivityMeta> newGatewayMap = new HashMap<>();  // 是源环节且是网关环节 键是activityBpdId
        oldNewUserNodeActIdMap = new HashMap<>();
        oldNewGatewayNodeActIdMap = new HashMap<>();
        oldNewConfUidMap = new HashMap<>();
        actcUidSource = new ArrayList<>();
        actcUidBeReplace = new ArrayList<>();
        newSourceActivityActBpdIdMap = new HashMap<>();
        newSourceActivityActIdMap = new HashMap<>();
        oldSourceActivityActBpdIdMap = new HashMap<>();
        oldSourceActivityActIdMap = new HashMap<>();
        oldNewActivityActIdMap = new HashMap<>(); // 所有新老版本节点的映射关系
        matchedUserNodeActBpdIds = new ArrayList<>();
        oldMatchedUserNodeActIds = new ArrayList<>();

        // 将新环节装入map方便对比
        for (BpmActivityMeta newActivityMeta : newActivityMetas) {
            if (newActivityMeta.getActivityId().equals(newActivityMeta.getSourceActivityId())) {
                newSourceActivityActBpdIdMap.put(newActivityMeta.getActivityBpdId(), newActivityMeta);
                newSourceActivityActIdMap.put(newActivityMeta.getActivityId(), newActivityMeta);
                if (BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(newActivityMeta.getBpmTaskType())) {
                    // 是源环节 且 是人工环节
                    newUserTaskMap.put(newActivityMeta.getActivityBpdId(), newActivityMeta);
                } else if (BpmActivityMeta.ACTIVITY_TYPE_GATEWAY.equals(newActivityMeta.getActivityType())) {
                    // 是源环节 且 是排他网关
                    newGatewayMap.put(newActivityMeta.getActivityBpdId(), newActivityMeta);
                }
            }
        }
        // 遍历老版本节点与新版本对比
        BpmActivityMeta bpmActivityMeta;
        for (BpmActivityMeta oldActivityMeta : oldActivityMetas) {
            if (oldActivityMeta.getActivityId().equals(oldActivityMeta.getSourceActivityId())) {
                // 是源环节
                oldSourceActivityActBpdIdMap.put(oldActivityMeta.getActivityBpdId(), oldActivityMeta);
                oldSourceActivityActIdMap.put(oldActivityMeta.getActivityId(), oldActivityMeta);
                BpmActivityMeta newActivity = newSourceActivityActBpdIdMap.get(oldActivityMeta.getActivityBpdId());
                if (newActivity != null) {
                    // 如果新环节中有此activity_bpd_id, 加入映射关系
                    oldNewActivityActIdMap.put(oldActivityMeta.getActivityId(), newActivity.getActivityId());
                }
                if (BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(oldActivityMeta.getBpmTaskType())) {
                    // 是源环节 且 是人工环节
                    bpmActivityMeta = newUserTaskMap.get(oldActivityMeta.getActivityBpdId());
                    if (bpmActivityMeta != null) {
                        // 此activity_bpd_id的环节在新版本中也存在，且还是人工环节，对应关系成立
                        oldNewUserNodeActIdMap.put(oldActivityMeta.getActivityId(), bpmActivityMeta.getActivityId()); // 记录映射关系
                        String oldActcUid = oldActivityMeta.getDhActivityConf().getActcUid();
                        String newActcUid = bpmActivityMeta.getDhActivityConf().getActcUid();
                        matchedUserNodeActBpdIds.add(oldActivityMeta.getActivityBpdId());
                        oldMatchedUserNodeActIds.add(oldActivityMeta.getActivityId());
                        actcUidSource.add(oldActcUid);
                        actcUidBeReplace.add(newActcUid);
                        oldNewConfUidMap.put(oldActcUid, newActcUid);
                    }
                } else if (BpmActivityMeta.ACTIVITY_TYPE_GATEWAY.equals(oldActivityMeta.getActivityType())) {
                    // 是源环节 且 是排他网关
                    bpmActivityMeta = newGatewayMap.get(oldActivityMeta.getActivityBpdId());
                    // 排他网关输出线连接的节点actiivty_bpd_id一致，对应关系成立
                    if (bpmActivityMeta != null && StringUtils.equals(oldActivityMeta.getActivityTo(), bpmActivityMeta.getActivityTo())) {
                        oldNewGatewayNodeActIdMap.put(oldActivityMeta.getActivityId(), bpmActivityMeta.getActivityId());
                    }
                }
            }
        }

    }



    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public String getOldProVerUid() {
        return oldProVerUid;
    }

    public void setOldProVerUid(String oldProVerUid) {
        this.oldProVerUid = oldProVerUid;
    }

    public String getNewProVerUid() {
        return newProVerUid;
    }

    public void setNewProVerUid(String newProVerUid) {
        this.newProVerUid = newProVerUid;
    }

    public Map<String, String> getOldNewFieldUidMap() {
        return oldNewFieldUidMap;
    }

    public void setOldNewFieldUidMap(Map<String, String> oldNewFieldUidMap) {
        this.oldNewFieldUidMap = oldNewFieldUidMap;
    }

    public Map<String, String> getOldNewStepUidMap() {
        return oldNewStepUidMap;
    }

    public void setOldNewStepUidMap(Map<String, String> oldNewStepUidMap) {
        this.oldNewStepUidMap = oldNewStepUidMap;
    }

    public Map<String, String> getOldNewFormUidMap() {
        return oldNewFormUidMap;
    }

    public void setOldNewFormUidMap(Map<String, String> oldNewFormUidMap) {
        this.oldNewFormUidMap = oldNewFormUidMap;
    }

    public Map<String, String> getOldNewUserNodeActIdMap() {
        return oldNewUserNodeActIdMap;
    }

    public void setOldNewUserNodeActIdMap(Map<String, String> oldNewUserNodeActIdMap) {
        this.oldNewUserNodeActIdMap = oldNewUserNodeActIdMap;
    }

    public String getCurrUserUid() {
        return currUserUid;
    }

    public void setCurrUserUid(String currUserUid) {
        this.currUserUid = currUserUid;
    }

    public DhProcessDefinition getOldDefintion() {
        return oldDefintion;
    }

    public void setOldDefintion(DhProcessDefinition oldDefintion) {
        this.oldDefintion = oldDefintion;
    }

    public DhProcessDefinition getNewDefinition() {
        return newDefinition;
    }

    public void setNewDefinition(DhProcessDefinition newDefinition) {
        this.newDefinition = newDefinition;
    }

    public Map<String, String> getOldNewGatewayNodeActIdMap() {
        return oldNewGatewayNodeActIdMap;
    }

    public void setOldNewGatewayNodeActIdMap(Map<String, String> oldNewGatewayNodeActIdMap) {
        this.oldNewGatewayNodeActIdMap = oldNewGatewayNodeActIdMap;
    }

    public List<BpmActivityMeta> getOldActivityMetas() {
        return oldActivityMetas;
    }

    public void setOldActivityMetas(List<BpmActivityMeta> oldActivityMetas) {
        this.oldActivityMetas = oldActivityMetas;
    }

    public Map<String, String> getOldNewActivityActIdMap() {
        return oldNewActivityActIdMap;
    }

    public void setOldNewActivityActIdMap(Map<String, String> oldNewActivityActIdMap) {
        this.oldNewActivityActIdMap = oldNewActivityActIdMap;
    }

    public Map<String, BpmActivityMeta> getOldSourceActivityActIdMap() {
        return oldSourceActivityActIdMap;
    }

    public void setOldSourceActivityActIdMap(Map<String, BpmActivityMeta> oldSourceActivityActIdMap) {
        this.oldSourceActivityActIdMap = oldSourceActivityActIdMap;
    }

    public Map<String, BpmActivityMeta> getOldSourceActivityActBpdIdMap() {
        return oldSourceActivityActBpdIdMap;
    }

    public void setOldSourceActivityActBpdIdMap(Map<String, BpmActivityMeta> oldSourceActivityActBpdIdMap) {
        this.oldSourceActivityActBpdIdMap = oldSourceActivityActBpdIdMap;
    }

    public List<String> getMatchedUserNodeActBpdIds() {
        return matchedUserNodeActBpdIds;
    }

    public void setMatchedUserNodeActBpdIds(List<String> matchedUserNodeActBpdIds) {
        this.matchedUserNodeActBpdIds = matchedUserNodeActBpdIds;
    }

    public List<String> getOldMatchedUserNodeActIds() {
        return oldMatchedUserNodeActIds;
    }

    public void setOldMatchedUserNodeActIds(List<String> oldMatchedUserNodeActIds) {
        this.oldMatchedUserNodeActIds = oldMatchedUserNodeActIds;
    }

    public List<BpmActivityMeta> getNewActivityMetas() {
        return newActivityMetas;
    }

    public void setNewActivityMetas(List<BpmActivityMeta> newActivityMetas) {
        this.newActivityMetas = newActivityMetas;
    }

    public Map<String, BpmActivityMeta> getNewSourceActivityActBpdIdMap() {
        return newSourceActivityActBpdIdMap;
    }

    public void setNewSourceActivityActBpdIdMap(Map<String, BpmActivityMeta> newSourceActivityActBpdIdMap) {
        this.newSourceActivityActBpdIdMap = newSourceActivityActBpdIdMap;
    }

    public Map<String, BpmActivityMeta> getNewSourceActivityActIdMap() {
        return newSourceActivityActIdMap;
    }

    public void setNewSourceActivityActIdMap(Map<String, BpmActivityMeta> newSourceActivityActIdMap) {
        this.newSourceActivityActIdMap = newSourceActivityActIdMap;
    }

    public Map<String, String> getOldNewConfUidMap() {
        return oldNewConfUidMap;
    }

    public void setOldNewConfUidMap(Map<String, String> oldNewConfUidMap) {
        this.oldNewConfUidMap = oldNewConfUidMap;
    }

    public List<String> getActcUidSource() {
        return actcUidSource;
    }

    public void setActcUidSource(List<String> actcUidSource) {
        this.actcUidSource = actcUidSource;
    }

    public List<String> getActcUidBeReplace() {
        return actcUidBeReplace;
    }

    public void setActcUidBeReplace(List<String> actcUidBeReplace) {
        this.actcUidBeReplace = actcUidBeReplace;
    }

    /**
     * 根据老板环节的actvityId获得新版中此环节的activityId
     * @param oldActivityId
     * @return
     */
    public String getNewActIdByOldActId(String oldActivityId) {
        return oldNewActivityActIdMap.get(oldActivityId);
    }

    public BpmActivityMeta getOldActivityMetaByActId(String oldActivityId) {
        return oldSourceActivityActIdMap.get(oldActivityId);
    }

    public BpmActivityMeta getNewActivityMetaByActId(String newActivityId) {
        return newSourceActivityActIdMap.get(newActivityId);
    }

    /**
     * 根据activityId集合获得 老版本的源节点
     * @param actIds
     * @return
     */
    public List<BpmActivityMeta> getOldActivityMetaByActIds(Collection<String> actIds) {
        List<BpmActivityMeta> results = new ArrayList<>();
        for (String actId : actIds) {
            BpmActivityMeta bpmActivityMeta = oldSourceActivityActIdMap.get(actId);
            if (bpmActivityMeta != null) {
                results.add(bpmActivityMeta);
            }
        }
        return results;
    }

    /**
     * 根据activityId集合获得 新版本的源节点
     * @param actIds
     * @return
     */
    public List<BpmActivityMeta> getNewActivityMetaByActIds(Collection<String> actIds) {
        List<BpmActivityMeta> results = new ArrayList<>();
        for (String actId : actIds) {
            BpmActivityMeta bpmActivityMeta = newSourceActivityActIdMap.get(actId);
            if (bpmActivityMeta != null) {
                results.add(bpmActivityMeta);
            }
        }
        return results;
    }

    /**
     * 根据activity_bpd_id查询新版中的节点
     * @return
     */
    public BpmActivityMeta getNewActivityMetaByActBpdId(String newActivityBpdId) {
        return newSourceActivityActBpdIdMap.get(newActivityBpdId);
    }

    /**
     * 根据老的字段id获得对应的新字段id
     * @param oldFieldUid
     * @return
     */
    public String getNewFieldUidByOldFieldUid(String oldFieldUid) {
        return oldNewFieldUidMap.get(oldFieldUid);
    }

    /**
     * 根据老的表单id获得对应的新表单id
     * @param oldFormUid
     * @return
     */
    public String getNewFormUidByOldFormUid(String oldFormUid) {
        return oldNewFormUidMap.get(oldFormUid);
    }

    /**
     * 根据老版的步骤id获取新版本的步骤id
     * @param oldStepUid
     * @return
     */
    public String getNewStepUidByOldStepUid(String oldStepUid) {
        return oldNewStepUidMap.get(oldStepUid);
    }

    public List<String> getInterfaceTriggerUids() {
        return interfaceTriggerUids;
    }

    public void setInterfaceTriggerUids(List<String> interfaceTriggerUids) {
        this.interfaceTriggerUids = interfaceTriggerUids;
    }

    public List<String> getOldTriggerInterfaceStepUids() {
        return oldTriggerInterfaceStepUids;
    }

    public void setOldTriggerInterfaceStepUids(List<String> oldTriggerInterfaceStepUids) {
        this.oldTriggerInterfaceStepUids = oldTriggerInterfaceStepUids;
    }

    @Override
    public String toString() {
        return "DhDefinitionCopyData{" +
                "proAppId='" + proAppId + '\'' +
                ", proUid='" + proUid + '\'' +
                ", oldProVerUid='" + oldProVerUid + '\'' +
                ", newProVerUid='" + newProVerUid + '\'' + "流程名：" + oldDefintion.getProName() +
                '}';
    }
}