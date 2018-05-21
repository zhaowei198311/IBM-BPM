package com.desmart.desmartbpm.entity.engine;

/**
 * 流程引擎中的图
 */
public class LswBpd {
    private String bpdId;
    private String versionId;
    private String name;
    private String data;
    
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
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "LswBpd [bpdId=" + bpdId + ", versionId=" + versionId + ", name="
                + name + ", data=" + data + "]";
    }
    
    
}
