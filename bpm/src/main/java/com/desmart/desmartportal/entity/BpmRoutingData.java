package com.desmart.desmartportal.entity;

import java.util.*;

import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import org.apache.commons.collections.CollectionUtils;

/**
 * 找寻下个环节信息时记录的信息
 */
public class BpmRoutingData {
    private BpmActivityMeta sourceNode;   // 作为起点的节点
    private List<BpmActivityMeta> normalNodes = new ArrayList<>();  // 记录人工任务节点的集合
    private List<BpmActivityMeta> gatewayNodes = new ArrayList<>(); // 记录遇到的排他网关
    private List<BpmActivityMeta> startProcessNodes = new ArrayList<>(); // 代表需要创建的子流程的节点
    private List<BpmActivityMeta> endProcessNodes = new ArrayList<>(); // 代表需要结束的子流程的节点
    private List<BpmActivityMeta> mainEndNodes = new ArrayList<>(); // 记录结束节点的集合
    private Set<DhGatewayRouteResult> routeResults = new HashSet<>(); // 排他网关的计算结果
    private List<BpmActivityMeta> taskNodesOnSameDeepLevel; // 与起始节点平级的任务节点
    private List<BpmActivityMeta> taskNodesOnOtherDeepLevel; // 与起始节点不平级的任务节点
    private List<BpmActivityMeta> startProcessNodesOnSameDeepLevel;  // 与起始任务平级的代表子流程的节点 (内部维护了第一个任务节点信息)
    private List<BpmActivityMeta> startProcessNodesOnOtherDeepLevel; // 与起始任务不平级的代表子流程的节点(内部维护了第一个任务节点信息)
    private List<BpmActivityMeta> firstTaskNodesOfStartProcessOnSameDeepLevel;  // 属于当前流程子流程的流程的第一个任务节点
    private List<BpmActivityMeta> firstTaskNodesOfStartProcessOnOtherDeepLevel;  // 不属于当前流程子流程的流程的第一个任务节点
    private Map<String, BpmActivityMeta> actIdAndNodeIdentitySubProcessMap; // （代表子流程的节点的activityId） -> 代表子流程的节点


    public void includeAll(BpmRoutingData bpmRouteingData) {
        this.normalNodes.addAll(bpmRouteingData.getNormalNodes());
        this.gatewayNodes.addAll(bpmRouteingData.getGatewayNodes());
        this.startProcessNodes.addAll(bpmRouteingData.getStartProcessNodes());
        this.endProcessNodes.addAll(bpmRouteingData.getEndProcessNodes());
        this.mainEndNodes.addAll(bpmRouteingData.getMainEndNodes());
        this.routeResults.addAll(bpmRouteingData.getRouteResults());
    }

    public void addNormalNode(BpmActivityMeta normlNode) {
        this.normalNodes.add(normlNode);
    }
    public void addGatewayNode(BpmActivityMeta gatewayNode) {
        this.gatewayNodes.add(gatewayNode);
    }
    public void addStartProcessNode(BpmActivityMeta endProcessNode) {
        this.startProcessNodes.add(endProcessNode);
    }
    public void addEndProcessNode(BpmActivityMeta subEndNode) {
        this.endProcessNodes.add(subEndNode);
    }
    public void addMainEndNode(BpmActivityMeta mainEndNode) {
        this.mainEndNodes.add(mainEndNode);
    }
    public void addRouteResult(DhGatewayRouteResult routeResult) {
        this.routeResults.add(routeResult);
    }

    /**
     * 根据activityId获得代表子流程的节点
     * @param activityId
     * @return
     */
    public BpmActivityMeta getNodeIdentitySubProcessByActivityId(String activityId) {
        return actIdAndNodeIdentitySubProcessMap.get(activityId);
    }

    public List<BpmActivityMeta> getStartProcessNodesOnSameDeepLevel() {
        if (startProcessNodesOnSameDeepLevel == null) {
            startProcessNodesOnSameDeepLevel = new ArrayList<>();
        }
        return startProcessNodesOnSameDeepLevel;
    }

    public void setStartProcessNodesOnSameDeepLevel(List<BpmActivityMeta> startProcessNodesOnSameDeepLevel) {
        this.startProcessNodesOnSameDeepLevel = startProcessNodesOnSameDeepLevel;
    }

    public List<BpmActivityMeta> getStartProcessNodesOnOtherDeepLevel() {
        if (startProcessNodesOnOtherDeepLevel == null) {
            startProcessNodesOnOtherDeepLevel = new ArrayList<>();
        }
        return startProcessNodesOnOtherDeepLevel;
    }

    public void setStartProcessNodesOnOtherDeepLevel(List<BpmActivityMeta> startProcessNodesOnOtherDeepLevel) {
        this.startProcessNodesOnOtherDeepLevel = startProcessNodesOnOtherDeepLevel;
    }

    public List<BpmActivityMeta> getTaskNodesOnOtherDeepLevel() {
        if (taskNodesOnOtherDeepLevel == null) {
            taskNodesOnOtherDeepLevel = new ArrayList<>();
        }
        return taskNodesOnOtherDeepLevel;
    }

    public List<BpmActivityMeta> getTaskNodesOnSameDeepLevel() {
        if (taskNodesOnSameDeepLevel == null) {
            taskNodesOnSameDeepLevel = new ArrayList<>();
        }
        return taskNodesOnSameDeepLevel;
    }

    public void setTaskNodesOnSameDeepLevel(List<BpmActivityMeta> taskNodesOnSameDeepLevel) {
        this.taskNodesOnSameDeepLevel = taskNodesOnSameDeepLevel;
    }

    public List<BpmActivityMeta> getFirstTaskNodesOfStartProcessOnSameDeepLevel() {
        if (firstTaskNodesOfStartProcessOnSameDeepLevel == null) {
            firstTaskNodesOfStartProcessOnSameDeepLevel = new ArrayList<>();
        }
        return firstTaskNodesOfStartProcessOnSameDeepLevel;
    }

    public void setFirstTaskNodesOfStartProcessOnSameDeepLevel(List<BpmActivityMeta> firstTaskNodesOfStartProcessOnSameDeepLevel) {
        this.firstTaskNodesOfStartProcessOnSameDeepLevel = firstTaskNodesOfStartProcessOnSameDeepLevel;
    }

    public List<BpmActivityMeta> getFirstTaskNodesOfStartProcessOnOtherDeepLevel() {
        if (firstTaskNodesOfStartProcessOnOtherDeepLevel == null) {
            firstTaskNodesOfStartProcessOnOtherDeepLevel = new ArrayList<>();
        }
        return firstTaskNodesOfStartProcessOnOtherDeepLevel;
    }

    public void setFirstTaskNodesOfStartProcessOnOtherDeepLevel(List<BpmActivityMeta> firstTaskNodesOfStartProcessOnOtherDeepLevel) {
        this.firstTaskNodesOfStartProcessOnOtherDeepLevel = firstTaskNodesOfStartProcessOnOtherDeepLevel;
    }

    public void setTaskNodesOnOtherDeepLevel(List<BpmActivityMeta> taskNodesOnOtherDeepLevel) {
        this.taskNodesOnOtherDeepLevel = taskNodesOnOtherDeepLevel;
    }

    public BpmActivityMeta getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(BpmActivityMeta sourceNode) {
        this.sourceNode = sourceNode;
    }

    public List<BpmActivityMeta> getNormalNodes() {
        return normalNodes;
    }

    public void setNormalNodes(List<BpmActivityMeta> normalNodes) {
        this.normalNodes = normalNodes;
    }

    public List<BpmActivityMeta> getGatewayNodes() {
        return gatewayNodes;
    }

    public void setGatewayNodes(List<BpmActivityMeta> gatewayNodes) {
        this.gatewayNodes = gatewayNodes;
    }

    public List<BpmActivityMeta> getStartProcessNodes() {
        return startProcessNodes;
    }

    public void setStartProcessNodes(List<BpmActivityMeta> startProcessNodes) {
        this.startProcessNodes = startProcessNodes;
    }

    public Map<String, BpmActivityMeta> getActIdAndNodeIdentitySubProcessMap() {
        return actIdAndNodeIdentitySubProcessMap;
    }

    public void setActIdAndNodeIdentitySubProcessMap(Map<String, BpmActivityMeta> actIdAndNodeIdentitySubProcessMap) {
        this.actIdAndNodeIdentitySubProcessMap = actIdAndNodeIdentitySubProcessMap;
    }



    public List<BpmActivityMeta> getEndProcessNodes() {
        return endProcessNodes;
    }

    public void setEndProcessNodes(List<BpmActivityMeta> endProcessNodes) {
        this.endProcessNodes = endProcessNodes;
    }

    public List<BpmActivityMeta> getMainEndNodes() {
        return mainEndNodes;
    }

    public void setMainEndNodes(List<BpmActivityMeta> mainEndNodes) {
        this.mainEndNodes = mainEndNodes;
    }

    public Set<DhGatewayRouteResult> getRouteResults() {
        return routeResults;
    }

    public void setRouteResults(Set<DhGatewayRouteResult> routeResults) {
        this.routeResults = routeResults;
    }

    /**
     * 去除重复的元素
     */
    public void removeAllDuplicate() {
        if (CollectionUtils.isNotEmpty(normalNodes)) {
            removeDuplicate(normalNodes);
        }
        if (!CollectionUtils.isEmpty(gatewayNodes)) {
            removeDuplicate(gatewayNodes);
        }
        if (!CollectionUtils.isEmpty(startProcessNodes)) {
            removeDuplicate(startProcessNodes);
        }
        if (!CollectionUtils.isEmpty(endProcessNodes)) {
            removeDuplicate(endProcessNodes);
        }
        if (!CollectionUtils.isEmpty(mainEndNodes)) {
            removeDuplicate(mainEndNodes);
        }
    }

    private void removeDuplicate(List<BpmActivityMeta> bpmActivityMetas) {
        Set<String> actvityIds = new HashSet<>();
        Iterator<BpmActivityMeta> iterator = bpmActivityMetas.iterator();
        while (iterator.hasNext()) {
            BpmActivityMeta item = iterator.next();
            if (!actvityIds.add(item.getActivityId())) {
                iterator.remove();
            }
        }
    }


}