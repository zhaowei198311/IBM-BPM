package com.desmart.desmartbpm.enums;

public enum DhProcessDefinitionStatus {
    SETTING("SETTING", "配置中"),
    SETTED("SETTED", "已配置");

    private final String code;
    private final String value;

    private DhProcessDefinitionStatus(String code, String value) {
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
    public static DhProcessDefinitionStatus codeOf(String code) {
        for (DhProcessDefinitionStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}