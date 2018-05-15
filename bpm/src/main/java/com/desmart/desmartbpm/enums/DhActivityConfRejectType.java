package com.desmart.desmartbpm.enums;

/**
 * 采用哪种方式驳回
 */
public enum DhActivityConfRejectType {
    TO_PROCESS_START("toProcessStart", "驳回到发起人"),
    TO_PRE_ACTIVITY("toPreActivity", "驳回到上个环节"),
    TO_ACTIVITIES("toActivities", "驳回到指定环节");

    private final String code;
    private final String value;

    private DhActivityConfRejectType(String code, String value) {
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
    public static DhActivityConfRejectType codeOf(String code) {
        for (DhActivityConfRejectType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}