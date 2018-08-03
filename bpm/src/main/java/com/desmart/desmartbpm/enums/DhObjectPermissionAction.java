package com.desmart.desmartbpm.enums;

public enum DhObjectPermissionAction {
    EDIT("EDIT", "编辑"),
    VIEW("VIEW", "查看"),
    HIDDEN("HIDDEN", "隐藏"),
    START("START", "发起"),
    UPLOAD("UPLOAD", "上传"),
    DOWNLOAD("DOWNLOAD", "下载"),
    SKIP("SKIP", "跳过必填验证"),
    PRINT("PRINT", "打印");

    private final String code;
    private final String value;

    private DhObjectPermissionAction(String code, String value) {
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
    public static DhObjectPermissionAction codeOf(String code) {
        for (DhObjectPermissionAction type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}