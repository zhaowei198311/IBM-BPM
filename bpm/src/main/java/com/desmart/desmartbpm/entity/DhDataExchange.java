package com.desmart.desmartbpm.entity;

/**
 * 辅助主流程与子流程间数据传递的
 */
public class DhDataExchange {
    private Integer insId;    // 流程实例编号
    private String identity;  // 标识关键字
    private String insUid;    // 流程实例主键

    public DhDataExchange() {

    }

    public DhDataExchange(Integer insId, String identity, String insUid) {
        this.insId = insId;
        this.identity = identity;
        this.insUid = insUid;
    }

    public Integer getInsId() {
        return insId;
    }

    public void setInsId(Integer insId) {
        this.insId = insId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getInsUid() {
        return insUid;
    }

    public void setInsUid(String insUid) {
        this.insUid = insUid;
    }

    @Override
    public String toString() {
        return "DhDataExchange{" +
                "insId=" + insId +
                ", identity='" + identity + '\'' +
                ", insUid='" + insUid + '\'' +
                '}';
    }
}