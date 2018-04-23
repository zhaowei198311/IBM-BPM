package com.desmart.desmartbpm.enums;

/**
 * 分配给哪种类型的接收人
 */
public enum DhActivityAssignAssignType {
    USER("USER", "用户"),
    TEAM("TEAM", "角色组"),
    ROLE("ROLE", "角色");

    private final String code;
    private final String value;

    private DhActivityAssignAssignType(String code, String value) {
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
    public static DhActivityAssignAssignType codeOf(String code) {
        for (DhActivityAssignAssignType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}