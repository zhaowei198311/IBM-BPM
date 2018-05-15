package com.desmart.desmartbpm.exception;

/**
 * 获取连接失败
 */
public class MqNodeFailException extends MqException {

    public MqNodeFailException(String message) {
        super(message);
    }

    public MqNodeFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
