package com.desmart.common.exception;

/**
 * 提交任务异常
 */
public class DhTaskCommitException extends DhTaskLifeCycleException {
    private String dataForSubmitTaskStr;


    public DhTaskCommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhTaskCommitException(String message) {
        super(message);
    }

    public DhTaskCommitException(String message, String dataForSubmitTaskStr) {
        super(message);
        this.dataForSubmitTaskStr = dataForSubmitTaskStr;
    }

    public String getDataForSubmitTaskStr() {
        return dataForSubmitTaskStr;
    }


}