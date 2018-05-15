package com.desmart.desmartbpm.exception;

/**
 * 队列异常
 */
public class MqException extends RuntimeException {

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }
}
