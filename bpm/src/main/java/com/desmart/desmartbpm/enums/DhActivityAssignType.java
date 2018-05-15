package com.desmart.desmartbpm.enums;

/**
 * 分配哪种操作的执行人
 */
public enum DhActivityAssignType {
    DEFAULT_HANDLER("defaultHandler", "默认处理人"),
    CHOOSEABLE_HANDLER("chooseableHandler", "可选处理人"),
    OUTTIME_NOTIFY("outtimeNotify", "超时通知人");

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