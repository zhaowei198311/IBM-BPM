package com.desmart.desmartbpm.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程定义升级需要的信息
 */
public class DhDefinitionCopyData {
    private String proAppId;
    private String proUid;
    private String oldProVerUid;
    private String newProVerUid;
    private Map<String, String> oldNewFieldUidMap;
    private Map<String, String> oldNewStepUidMap;
    private Map<String, String> oldNewFormUidMap;
    private Map<String, String> oldNewUserNodeIdMap;
    private Map<String, String> oldNewGatewayNodeIdMap;
    private String currUserUid;
    private DhProcessDefinition oldDefintion;
    private DhProcessDefinition newDefinition;

    public DhDefinitionCopyData(){}


    public DhDefinitionCopyData(String proAppId, String proUid, String oldProVerUid, String newProVerUid) {
        this.proAppId = proAppId;
        this.proUid = proUid;
        this.oldProVerUid = oldProVerUid;
        this.newProVerUid = newProVerUid;
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

    public Map<String, String> getOldNewUserNodeIdMap() {
        return oldNewUserNodeIdMap;
    }

    public void setOldNewUserNodeIdMap(Map<String, String> oldNewUserNodeIdMap) {
        this.oldNewUserNodeIdMap = oldNewUserNodeIdMap;
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

    public Map<String, String> getOldNewGatewayNodeIdMap() {
        return oldNewGatewayNodeIdMap;
    }

    public void setOldNewGatewayNodeIdMap(Map<String, String> oldNewGatewayNodeIdMap) {
        this.oldNewGatewayNodeIdMap = oldNewGatewayNodeIdMap;
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