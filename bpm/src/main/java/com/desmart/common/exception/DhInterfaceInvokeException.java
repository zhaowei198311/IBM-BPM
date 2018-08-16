package com.desmart.common.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 接口调用异常
 */
public class DhInterfaceInvokeException extends PlatformException {
    private String requestBody;
    private String responseBody;

    public DhInterfaceInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhInterfaceInvokeException(String message) {
        super(message);
    }

    public DhInterfaceInvokeException(String message, Map<String, String> data) {
        super(message);
        this.requestBody = data.get("requestBody");
        this.responseBody = data.get("responseBody");
    }


    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
