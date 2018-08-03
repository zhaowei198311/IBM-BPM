package com.desmart.desmartbpm.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class DhProcessDefinitionBo {
    private String proUid;// 流程id(引擎中流程图id)
    private String proAppId; // 流程应用库id
    private String proVerUid;// 流程版本id（引擎中流程快照id）
    private Set<String> externalIdList;  // 含有的外链流程id
    private String proName; // 流程名

    public DhProcessDefinitionBo(){}

    public DhProcessDefinitionBo(String proAppId, String proUid, String proVerUid) {
        this.proUid = proUid;
        this.proAppId = proAppId;
        this.proVerUid = proVerUid;
    }

    /**
     * 将一个外链编号加入
     * @param externalId
     */
    public void addToCalledProcessList(String externalId) {
        if (this.externalIdList == null) {
            this.externalIdList = new HashSet<>();
        }
        this.externalIdList.add(externalId);
    }

    public boolean canSynchronize() {
        return this.externalIdList.isEmpty();
    }

    public void removeFromExternalIdList(List<String> bpdIdListCanSynchronize) {
        this.externalIdList.removeAll(bpdIdListCanSynchronize);
    }

    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getProVerUid() {
        return proVerUid;
    }

    public void setProVerUid(String proVerUid) {
        this.proVerUid = proVerUid;
    }

    public Set<String> getExternalIdList() {
        return externalIdList;
    }

    public void setExternalIdList(Set<String> externalIdList) {
        this.externalIdList = externalIdList;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}