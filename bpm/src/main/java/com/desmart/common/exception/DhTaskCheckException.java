package com.desmart.common.exception;

/**
 * 检查任务异常
 */
public class DhTaskCheckException extends DhTaskLifeCycleException {

    public DhTaskCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhTaskCheckException(String message) {
        super(message);
    }
}