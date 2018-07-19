package com.desmart.desmartportal.entity;

import com.desmart.desmartbpm.entity.BpmActivityMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 找寻下个环节信息时记录的信息
 */
public class BpmRoutingData {
    private BpmActivityMeta sourceNode;   // 作为起点的节点
    private Set<BpmActivityMeta> normalNodes = new HashSet<>();  // 记录人工任务节点的集合
    private Set<BpmActivityMeta> gatewayNodes = new HashSet<>(); // 记录遇到的排他网关
    private Set<BpmActivityMeta> startProcessNodes = new HashSet<>(); // 代表需要创建的子流程的节点
    private Set<BpmActivityMeta> endProcessNodes = new HashSet<>(); // 代表需要结束的子流程的节点
    private Set<BpmActivityMeta> mainEndNodes = new HashSet<>(); // 记录结束节点的集合
    private Set<DhGatewayRouteResult> routeResults = new HashSet<>(); // 排他网关的计算结果
    private List<BpmActivityMeta> taskNodesOnSameDeepLevel = new ArrayList<>(); // 与起始节点平级的任务节点
    private List<BpmActivityMeta> firstTaskNodesOfStartProcessOnSameDeepLevel = new ArrayList<>();  // 属于当前流程子流程的流程的第一个任务节点
    private List<BpmActivityMeta> taskNodesOnOtherDeepLevel = new ArrayList<>(); // 与起始节点不平级的任务节点
    private List<BpmActivityMeta> firstTaskNodesOfStartProcessOnOtherDeepLevel = new ArrayList<>();  // 不属于当前流程子流程的流程的第一个任务节点
    private List<BpmActivityMeta> startProcessNodesOnSameDeepLevel = new ArrayList<>();  // 与起始任务平级的代表子流程的节点
    private List<BpmActivityMeta> startProcessNodesOnOtherDeepLevel = new ArrayList<>(); // 与起始任务不平级的代表子流程的节点


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

    public List<BpmActivityMeta> getStartProcessNodesOnSameDeepLevel() {
        return startProcessNodesOnSameDeepLevel;
    }

    public void setStartProcessNodesOnSameDeepLevel(List<BpmActivityMeta> startProcessNodesOnSameDeepLevel) {
        this.startProcessNodesOnSameDeepLevel = startProcessNodesOnSameDeepLevel;
    }

    public List<BpmActivityMeta> getStartProcessNodesOnOtherDeepLevel() {
        return startProcessNodesOnOtherDeepLevel;
    }

    public void setStartProcessNodesOnOtherDeepLevel(List<BpmActivityMeta> startProcessNodesOnOtherDeepLevel) {
        this.startProcessNodesOnOtherDeepLevel = startProcessNodesOnOtherDeepLevel;
    }

    public List<BpmActivityMeta> getTaskNodesOnOtherDeepLevel() {
        return taskNodesOnOtherDeepLevel;
    }

    public List<BpmActivityMeta> getTaskNodesOnSameDeepLevel() {
        return taskNodesOnSameDeepLevel;
    }

    public void setTaskNodesOnSameDeepLevel(List<BpmActivityMeta> taskNodesOnSameDeepLevel) {
        this.taskNodesOnSameDeepLevel = taskNodesOnSameDeepLevel;
    }

    public List<BpmActivityMeta> getFirstTaskNodesOfStartProcessOnSameDeepLevel() {
        return firstTaskNodesOfStartProcessOnSameDeepLevel;
    }

    public void setFirstTaskNodesOfStartProcessOnSameDeepLevel(List<BpmActivityMeta> firstTaskNodesOfStartProcessOnSameDeepLevel) {
        this.firstTaskNodesOfStartProcessOnSameDeepLevel = firstTaskNodesOfStartProcessOnSameDeepLevel;
    }

    public List<BpmActivityMeta> getFirstTaskNodesOfStartProcessOnOtherDeepLevel() {
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

    public Set<BpmActivityMeta> getNormalNodes() {
        return normalNodes;
    }

    public void setNormalNodes(Set<BpmActivityMeta> normalNodes) {
        this.normalNodes = normalNodes;
    }

    public Set<BpmActivityMeta> getGatewayNodes() {
        return gatewayNodes;
    }

    public void setGatewayNodes(Set<BpmActivityMeta> gatewayNodes) {
        this.gatewayNodes = gatewayNodes;
    }

    public Set<BpmActivityMeta> getStartProcessNodes() {
        return startProcessNodes;
    }

    public void setStartProcessNodes(Set<BpmActivityMeta> startProcessNodes) {
        this.startProcessNodes = startProcessNodes;
    }


    public Set<BpmActivityMeta> getEndProcessNodes() {
        return endProcessNodes;
    }

    public void setEndProcessNodes(Set<BpmActivityMeta> endProcessNodes) {
        this.endProcessNodes = endProcessNodes;
    }

    public Set<BpmActivityMeta> getMainEndNodes() {
        return mainEndNodes;
    }

    public void setMainEndNodes(Set<BpmActivityMeta> mainEndNodes) {
        this.mainEndNodes = mainEndNodes;
    }

    public Set<DhGatewayRouteResult> getRouteResults() {
        return routeResults;
    }

    public void setRouteResults(Set<DhGatewayRouteResult> routeResults) {
        this.routeResults = routeResults;
    }
}