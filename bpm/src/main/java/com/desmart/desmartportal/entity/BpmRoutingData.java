package com.desmart.desmartportal.entity;

import com.desmart.desmartbpm.entity.BpmActivityMeta;

import java.util.HashSet;
import java.util.Set;

/**
 * 找寻下个环节信息时记录的信息
 */
public class BpmRoutingData {
    Set<BpmActivityMeta> normalNodes = new HashSet<>();  // 记录人工任务节点的集合
    Set<BpmActivityMeta> gatewayNodes = new HashSet<>(); // 记录遇到的排他网关
    Set<BpmActivityMeta> subProcessNodes = new HashSet<>(); // 代表子流程的节点
    Set<BpmActivityMeta> subEndNodes = new HashSet<>(); // 记录结束节点的集合
    Set<BpmActivityMeta> mainEndNodes = new HashSet<>(); // 记录结束节点的集合
    Set<DhGatewayRouteResult> routeResults = new HashSet<>(); // 排他网关的计算结果

    public void includeAll(BpmRoutingData bpmRouteingData) {
        this.normalNodes.addAll(bpmRouteingData.getNormalNodes());
        this.gatewayNodes.addAll(bpmRouteingData.getGatewayNodes());
        this.subProcessNodes.addAll(bpmRouteingData.getSubEndNodes());
        this.subEndNodes.addAll(bpmRouteingData.getSubEndNodes());
        this.mainEndNodes.addAll(bpmRouteingData.getMainEndNodes());
        this.routeResults.addAll(bpmRouteingData.getRouteResults());
    }

    public void addNormalNode(BpmActivityMeta normlNode) {
        this.normalNodes.add(normlNode);
    }
    public void addGateway(BpmActivityMeta gatewayNode) {
        this.gatewayNodes.add(gatewayNode);
    }
    public void addSubProcessNode(BpmActivityMeta subProcessNode) {
        this.subProcessNodes.add(subProcessNode);
    }
    public void addSubEndNode(BpmActivityMeta subEndNode) {
        this.subEndNodes.add(subEndNode);
    }
    public void addMainEndNode(BpmActivityMeta mainEndNode) {
        this.mainEndNodes.add(mainEndNode);
    }
    public void addRouteResult(DhGatewayRouteResult routeResult) {
        this.routeResults.add(routeResult);
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

    public Set<BpmActivityMeta> getSubProcessNodes() {
        return subProcessNodes;
    }

    public void setSubProcessNodes(Set<BpmActivityMeta> subProcessNodes) {
        this.subProcessNodes = subProcessNodes;
    }

    public Set<BpmActivityMeta> getSubEndNodes() {
        return subEndNodes;
    }

    public void setSubEndNodes(Set<BpmActivityMeta> subEndNodes) {
        this.subEndNodes = subEndNodes;
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