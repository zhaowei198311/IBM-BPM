package com.desmart.common.exception;

/**
 * 任务处理中的异常
 */
public class DhTaskLifeCycleException extends PlatformException{

    public DhTaskLifeCycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhTaskLifeCycleException(String message) {
        super(message);
    }
}