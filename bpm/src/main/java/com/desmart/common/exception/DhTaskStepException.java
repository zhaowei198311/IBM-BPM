package com.desmart.common.exception;

/**
 * 步骤执行异常
 */
public class DhTaskStepException extends DhTaskLifeCycleException {

    public DhTaskStepException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhTaskStepException(String message) {
        super(message);
    }
}