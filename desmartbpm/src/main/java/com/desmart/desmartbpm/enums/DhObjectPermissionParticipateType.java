package com.desmart.desmartbpm.enums;

public enum DhObjectPermissionParticipateType {
    
    USER("USER", "用户"),
    TEAM("TEAM", "角色组"),
    ROLE("ROLE", "角色");

    private final String code;
    private final String value;

    private DhObjectPermissionParticipateType(String code, String value) {
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
    public static DhObjectPermissionParticipateType codeOf(String code) {
        for (DhObjectPermissionParticipateType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}