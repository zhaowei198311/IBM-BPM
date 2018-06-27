package com.desmart.desmartsystem.enums;

/**
 * 事项分配对应关系，分配给哪种类型的接收人
 */
public enum InterfaceType {
	WEBSERVICE("webservice", "webservice"),
	RESTAPI("restapi", "restapi"),
	RPC("rpc", "rpc");

    private final String code;
    private final String value;

    private InterfaceType(String code, String value) {
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
    public static InterfaceType codeOf(String code) {
        for (InterfaceType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}