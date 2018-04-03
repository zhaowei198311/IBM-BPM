package com.desmart.desmartbpm.exception;

/**
 * 所有节点尝试连接失败
 */
public class MqAllNodesFailException extends MqException {

    public MqAllNodesFailException(String message) {
        super(message);
    }

    public MqAllNodesFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
