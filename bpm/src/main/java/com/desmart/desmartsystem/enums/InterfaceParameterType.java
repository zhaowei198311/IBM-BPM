package com.desmart.desmartsystem.enums;

/**
 * 事项分配对应关系，分配给哪种类型的接收人
 */
public enum InterfaceParameterType {
	STRING("String", "String"),
	INTEGER("Integer", "Integer"),
	DOUBLE("Double", "Double"),
	BOOLEAN("Boolean", "Boolean"),
	DATE("Date", "Date"),
	ARRAY("Array", "Array"),
	INPUT("input", "input"),
	OUTPUT("output", "output");

    private final String code;
    private final String value;

    private InterfaceParameterType(String code, String value) {
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
    public static InterfaceParameterType codeOf(String code) {
        for (InterfaceParameterType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}