package com.desmart.desmartbpm.enums;

/**
 * 分配哪种操作的执行人
 */
public enum DhActivityAssignType {
    PROCESS("process", "处理人"),
    TIMEOUT_NOTIFY("timeoutNotify", "超时通知人");

    private final String code;
    private final String value;

    private DhActivityAssignType(String code, String value) {
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
    public static DhActivityAssignType codeOf(String code) {
        for (DhActivityAssignType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}