package com.desmart.desmartbpm.entity.engine;

import java.util.Arrays;

/**
 * 流程引擎中的图
 */
public class LswBpd {
    private String bpdId; // 图id
    private String versionId; // 版本id
    private String name; // 名称
    private byte[] data; // 包含路由条件的XML数据
    
    public String getBpdId() {
        return bpdId;
    }
    public void setBpdId(String bpdId) {
        this.bpdId = bpdId;
    }
    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "LswBpd [bpdId=" + bpdId + ", versionId=" + versionId + ", name="
                + name;
    }
    
    
    
    
}
