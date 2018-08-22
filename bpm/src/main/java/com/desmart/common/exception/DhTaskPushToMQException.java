package com.desmart.common.exception;

/**
 * 检查任务异常
 */
public class DhTaskPushToMQException extends DhTaskLifeCycleException {
    private String dataForSubmitTaskStr;


    public DhTaskPushToMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhTaskPushToMQException(String message) {
        super(message);
    }

    public DhTaskPushToMQException(String message, String dataForSubmitTaskStr) {
        super(message);
        this.dataForSubmitTaskStr = dataForSubmitTaskStr;
    }

    public String getDataForSubmitTaskStr() {
        return dataForSubmitTaskStr;
    }
}