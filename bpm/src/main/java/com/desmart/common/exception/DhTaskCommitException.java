package com.desmart.common.exception;

/**
 * 提交任务异常
 */
public class DhTaskCommitException extends DhTaskLifeCycleException {

    public DhTaskCommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhTaskCommitException(String message) {
        super(message);
    }

}