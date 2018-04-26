package com.desmart.desmartbpm.enums;

/**
 * 人工环节的任务分配规则
 */
public enum DhActivityConfAssignType {
    ROLE_AND_DEPARTMENT("roleAndDepartment", "角色+部门"),
    ROLE_AND_COMPANY("roleAndCompany", "角色+公司编码"),
    TEAM_AND_DEPARTMENT("teamAndDepartment", "角色组+部门"),
    TEAM_AND_COMPANY("teamAndCompany", "角色组+公司编码"),
    LEADER_OF_PRE_ACTIVITY_USER("leaderOfPreActivityUser", "上个环节提交人的上级"),
    USERS("users", "指定人员"),
    PROCESS_CREATOR("processCreator", "流程发起人"),
    BY_FIELD("byField", "根据表单字段");

    private final String code;
    private final String value;

    private DhActivityConfAssignType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    /**
     * 通过code得到枚举类
     * @param code
     * @return
     */
    public static DhActivityConfAssignType codeOf(String code) {
        for (DhActivityConfAssignType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}