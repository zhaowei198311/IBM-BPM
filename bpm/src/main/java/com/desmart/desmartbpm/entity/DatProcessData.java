package com.desmart.desmartbpm.entity;

/**
 * 流程实例insData下的processData数据
 */
public class DatProcessData {
    private String insUid; // 流程实例主键
    private String departNo; // 发起人部门编号
    private String companyNumber; // 发起人公司编码
    private String insTitle; // 流程实例标题
    private String insInitUser; // 流程发起人
    private String nextBusinessKey; // 触发下个流程的关键字

    public DatProcessData() {

    }

    public String getInsUid() {
        return insUid;
    }

    public void setInsUid(String insUid) {
        this.insUid = insUid;
    }

    public String getDepartNo() {
        return departNo;
    }

    public void setDepartNo(String departNo) {
        this.departNo = departNo;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getInsTitle() {
        return insTitle;
    }

    public void setInsTitle(String insTitle) {
        this.insTitle = insTitle;
    }

    public String getInsInitUser() {
        return insInitUser;
    }

    public void setInsInitUser(String insInitUser) {
        this.insInitUser = insInitUser;
    }

    public String getNextBusinessKey() {
        return nextBusinessKey;
    }

    public void setNextBusinessKey(String nextBusinessKey) {
        this.nextBusinessKey = nextBusinessKey;
    }
}