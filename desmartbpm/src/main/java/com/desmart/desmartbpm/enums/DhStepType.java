package com.desmart.desmartbpm.enums;

/**
 * 步骤类型
 */
public enum DhStepType {
    FORM("form", "表单"),
    SERVICE("trigger", "服务");

    private final String code;
    private final String value;

    private DhStepType(String code, String value) {
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
    public static DhStepType codeOf(String code) {
        for (DhStepType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}