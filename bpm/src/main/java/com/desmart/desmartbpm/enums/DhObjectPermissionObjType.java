package com.desmart.desmartbpm.enums;

public enum DhObjectPermissionObjType {
    FORM("FORM", "表单"),
    PROCESS("PROCESS", "流程"),
    FIELD("FIELD", "字段"),
    ATTACHMENT("ATTACHMENT", "附件"),
    META("META","元数据");

    private final String code;
    private final String value;

    private DhObjectPermissionObjType(String code, String value) {
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
    public static DhObjectPermissionObjType codeOf(String code) {
        for (DhObjectPermissionObjType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}