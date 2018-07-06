package com.desmart.desmartbpm.enums;

public enum DhTriggerType {

    JAVACLASS("javaclass", "java类"),
    SCRIPT("script", "脚本"),
    SQL("sql", "SQL"),
    CHOOSE_USER("chooseUser", "选人触发器"),
    VALIDATE("validate", "校验触发器"),
    INTERFACE("interface", "接口");

    private final String code;
    private final String value;

    private DhTriggerType(String code, String value) {
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
    public static DhTriggerType codeOf(String code) {
        for (DhTriggerType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}