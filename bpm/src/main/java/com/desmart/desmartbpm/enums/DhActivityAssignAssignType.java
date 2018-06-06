package com.desmart.desmartbpm.enums;

/**
 * 事项分配对应关系，分配给哪种类型的接收人
 */
public enum DhActivityAssignAssignType {
    USER("user", "用户"),
    TEAM("team", "角色组"),
    ROLE("role", "角色"),
    FIELD("field", "表单字段"),
	TRIGGER("trigger","触发器");

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