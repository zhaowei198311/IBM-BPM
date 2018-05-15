package com.desmart.desmartbpm.common;

public class HttpReturnStatus {
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = -1;
    public static final String OK_STATUS = "200";
    public static final String ERROR_STATUS = "error";

    public HttpReturnStatus() {

    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}