package com.desmart.desmartbpm.enums;

public enum DhActivityConfOuttimeNotifyType {
	USERS("users", "指定人员"),
	HANDLER_USER("handlerUser","处理人"),
	HANDLER_USER_SUPERIOR("handlerUserSuperior","处理人的上级");

    private final String code;
    private final String value;

    private DhActivityConfOuttimeNotifyType(String code, String value) {
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
    public static DhActivityConfOuttimeNotifyType codeOf(String code) {
        for (DhActivityConfOuttimeNotifyType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
